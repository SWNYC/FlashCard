package com.example.flashcard;

import android.support.v4.app.Fragment;

public class CardsChooserActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CardsChooserListFragment();
	}

}
