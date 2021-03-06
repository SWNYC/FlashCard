package com.example.flashcard;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaveFileDialogFragment extends DialogFragment {
	public static final String EXTRA_FILENAME = "com.example.flashcard.filename";
	private String mFileName;
	private EditText mFileNameEditText;

	public interface SaveFileDialogListener {
		public void onDialogPositiveClick(String fileName);
	}

	SaveFileDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (SaveFileDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SaveFileDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.save_file_dialog, null);

		mFileNameEditText = (EditText) view.findViewById(R.id.fileNameEditText);

		builder.setView(view)
				.setMessage(R.string.save_dialog)
				.setPositiveButton(R.string.save_button,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						})
				.setNegativeButton(R.string.cancel_dialog_button,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SaveFileDialogFragment.this.getDialog()
										.cancel();
							}
						});
		
		return builder.create();
	}
	
	@Override
	public void onStart()
	{
	    super.onStart();   
	    AlertDialog dialog = (AlertDialog)getDialog();
	    if(dialog != null)
	    {
	        Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
	        positiveButton.setOnClickListener(new View.OnClickListener()
	                {
	                    @Override
	                    public void onClick(View v)
	                    {
	                        Boolean goodFileName = false;
	                        
	                        mFileName = mFileNameEditText.getText()
									.toString();

							String[] fileNames = getActivity().fileList();
							ArrayList<String> fileList = new ArrayList<String>(
									Arrays.asList(fileNames));

							if (fileList.contains(mFileName)) {
								Toast.makeText(getActivity().getApplicationContext(),
										R.string.duplicate_file_name,
										Toast.LENGTH_SHORT).show();
							} else {
								goodFileName = true;
							}
	                        
	                        if(goodFileName) {
	                        	mListener.onDialogPositiveClick(mFileName);
	                            dismiss();
	                        }
	                    }
	                });
	    }
	}

}
