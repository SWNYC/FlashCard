package com.example.flashcard;

import android.support.v4.app.Fragment;

public class CreateCardsActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CreateCardsFragment();
	}

}
