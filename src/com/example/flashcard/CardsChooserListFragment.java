package com.example.flashcard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

		mFileNames = getActivity().fileList();
		List<String> fileList = new ArrayList<String>(Arrays.asList(mFileNames));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.file_list_item, fileList);
		setListAdapter(adapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ListView listView = getListView();
		listView.setDivider(getResources().getDrawable(android.R.color.darker_gray));
		listView.setDividerHeight(10);
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
		
		
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		
		if (requestCode == REQUEST_FILENAME) {
			String fileName = data.getStringExtra(CreateCardsPagerActivity.EXTRA_FILENAME);
			
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
