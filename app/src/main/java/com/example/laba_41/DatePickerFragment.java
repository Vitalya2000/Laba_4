package com.example.laba_41;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener{
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_TEXT_DATE = "widget_text_date_";
    SimpleDateFormat dateFormat;
    Calendar dateAndTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        dateAndTime = Calendar.getInstance();
        int year = dateAndTime.get(Calendar.YEAR);
        int month = dateAndTime.get(Calendar.MONTH);
        int day = dateAndTime.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, month);
        dateAndTime.set(Calendar.DAY_OF_MONTH, day);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor sp = sharedPreferences.edit();
        sp.putString(DatePickerFragment.WIDGET_TEXT_DATE + widgetID, dateFormat.format(dateAndTime.getTime()));
        sp.commit();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity().getApplicationContext());
        MainActivity.updateWidget(getActivity().getApplicationContext(), appWidgetManager, sharedPreferences, widgetID);
        getActivity().finish();

    }
    public void onCancel(DialogInterface dialog){
        Toast.makeText(getActivity(),"Data ne vybrana", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }
}







