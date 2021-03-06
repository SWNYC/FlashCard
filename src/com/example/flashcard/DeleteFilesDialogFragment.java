package com.example.flashcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteFilesDialogFragment extends DialogFragment {
	public static final String EXTRA_CONFIRM = "com.example.flashcard.deletefile";
	private boolean mConfirmDelete;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		Bundle args = getArguments();
		int checkedItemCount = args.getInt(CardsChooserListFragment.NUM_CHECKED_ITEMS);
		Resources res = getResources();
		
		String title = res.getQuantityString(R.plurals.delete_dialog_text, checkedItemCount, checkedItemCount);

		builder.setMessage(title)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mConfirmDelete = true;
								sendResult(Activity.RESULT_OK);
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sendResult(Activity.RESULT_OK);
							}
						});

		return builder.create();
	}
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra(EXTRA_CONFIRM, mConfirmDelete);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	
}

