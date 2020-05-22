package com.example.laba_41;

import android.os.Bundle;
import android.view.Window;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public class MyFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_fragment_activity);
        DialogFragment dFragment = new DatePickerFragment();
        dFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
