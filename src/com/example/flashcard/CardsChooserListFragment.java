package com.example.flashcard;

import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CardsChooserListFragment extends ListFragment {

	public static final String EXTRA_FILENAME = "com.example.flashcard.filename";
	public static final String NUM_CHECKED_ITEMS = "com.example.flashcard.num_checked_items";
	static final int REQUEST_CONFIRM_DELETE = 0;
	static final int REQUEST_FILENAME = 1;
	private String[] mFileNames;
	private boolean mConfirmDelete = false;
	private ActionMode mActionMode;
	public static final String TAG = "CHOOSE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		getActivity().setTitle(R.string.app_name);
		Log.d(TAG, "onCreate");

		mFileNames = getActivity().fileList();
		List<String> fileList = new ArrayList<String>(Arrays.asList(mFileNames));
		
		for (String fileName: fileList) {
			Log.d(TAG, fileName);
		}

		MyStringAdapter mAdapter = new MyStringAdapter(getActivity(),
				R.layout.file_list_item, fileList);
		setListAdapter(mAdapter);
	}

	private class MyStringAdapter extends ArrayAdapter<String> {

		public MyStringAdapter(Context context, int resource,
				List<String> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String emptyString = "";

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.file_list_item, null);
			}
			String fileString = getItem(position).toString();
			TextView fileName = (TextView) convertView
					.findViewById(R.id.fileName);
			fileName.setText(fileString);
			
			TextView dateCreated = (TextView) convertView.findViewById(R.id.dateCreated);
			File file = new File(getActivity().getFilesDir(),fileString);
			
			Date lastModDate = new Date(file.lastModified());
			String formattedDate = DateFormat.format("EEEE, MMM dd, yyyy", lastModDate).toString();
			
			dateCreated.setText(formattedDate);
			
			
			TextView numOfCards = (TextView) convertView.findViewById(R.id.numOfCards);
			FlashCardDatabase cardsDatabase = FlashCardDatabase.getDatabase(getActivity(), fileString);
			int databaseSize = cardsDatabase.getArrayList().size();
			if (databaseSize < 10) {
				emptyString = "0";
			}
			numOfCards.setText(emptyString + cardsDatabase.getArrayList().size());
			

			return convertView;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.cards_chooser_list_view, container,
				false);
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.file_list_item_context, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				if (item.getItemId() == R.id.deleteFileMenuItem) {

					int checkedItemCount = getListView().getCheckedItemCount();
					Bundle args = new Bundle();
					args.putInt(NUM_CHECKED_ITEMS, checkedItemCount);

					mActionMode = mode;
					DeleteFilesDialogFragment newFragment = new DeleteFilesDialogFragment();
					newFragment.setArguments(args);
					newFragment.setTargetFragment(
							CardsChooserListFragment.this,
							REQUEST_CONFIRM_DELETE);
					newFragment.show(getFragmentManager(), "deletefile");

					return true;
				}
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
			}
		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String fileName = (String) getListView().getItemAtPosition(position);
		Intent i = new Intent(getActivity(), ShowCardPagerActivity.class);
		i.putExtra(EXTRA_FILENAME, fileName);
		startActivity(i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
//		Log.d(TAG, "resultcode: " + resultCode);
//		Log.d(TAG, "requestCode: " + requestCode);


		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		if (requestCode == REQUEST_FILENAME) {
			String fileName = data
					.getStringExtra(CreateCardsPagerActivity.EXTRA_FILENAME);
			
			ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
			adapter.add(fileName);
			adapter.notifyDataSetChanged();
		}

		if (requestCode == REQUEST_CONFIRM_DELETE) {
			mConfirmDelete = (Boolean) data
					.getSerializableExtra(DeleteFilesDialogFragment.EXTRA_CONFIRM);
			ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
			if (mConfirmDelete) {
				for (int i = adapter.getCount() - 1; i >= 0; i--) {
					if (getListView().isItemChecked(i)) {
						getActivity().deleteFile(adapter.getItem(i));
						adapter.remove(adapter.getItem(i));
					}
				}
			}
			adapter.notifyDataSetChanged();
			mActionMode.finish();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.cards_chooser_options, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_create_cards:
			Intent i = new Intent(getActivity(), CreateCardsPagerActivity.class);
			startActivityForResult(i, REQUEST_FILENAME);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
