package com.example.flashcard;

import android.support.v4.app.Fragment;

public class ShowCardsActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new ShowCardsFragment();
	}

}
