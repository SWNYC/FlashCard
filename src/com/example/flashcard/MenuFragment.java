package com.example.flashcard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {
	
	private Button mOpenCardsButton;
	private Button mCreateCardsButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {
		
		View v = inflator.inflate(R.layout.menu_fragment, parent, false);
		
		mOpenCardsButton = (Button)v.findViewById(R.id.open_cards_Button);
		mOpenCardsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CardsChooserActivity.class);
				startActivity(i);
			}
		});
		
		mCreateCardsButton = (Button)v.findViewById(R.id.create_cards_Button);
		mCreateCardsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CreateCardsPagerActivity.class);
				startActivity(i);
			}
		});
		
		return v;
	}

}