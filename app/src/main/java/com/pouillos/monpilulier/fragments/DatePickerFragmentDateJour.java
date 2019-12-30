package com.pouillos.monpilulier.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragmentDateJour extends DialogFragment {

        private onDateClickListener onDateClickListener;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            //int year = 1980;
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    onDateClickListener.onDateSet(datePicker, i, i1, i2);
                }
            }, year, month, day);
            //dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;



            // Create a new instance of DatePickerDialog and return it
           /* return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    onDateClickListener.onDateSet(datePicker, i, i1, i2);
                }
            }, year, month, day);*/
        }

        public void setOnDateClickListener(onDateClickListener onDateClickListener) {
            if (onDateClickListener != null) {
                this.onDateClickListener = onDateClickListener;
            }
        }


        //}

        public interface onDateClickListener {
            void onDateSet(DatePicker datePicker, int i, int i1, int i2);
        }
    }


