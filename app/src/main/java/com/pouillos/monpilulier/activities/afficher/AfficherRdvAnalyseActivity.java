package com.pouillos.monpilulier.activities.afficher;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.add.AddRdvAnalyseActivity;

import com.pouillos.monpilulier.activities.photo.MakePhotoActivity;

import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.RdvAnalyse;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.TypePhoto;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AfficherRdvAnalyseActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;
    @State
    RdvAnalyse rdvAnalyseSelected;
    @State
    Date date;

    TimePickerDialog picker;

    List<RdvAnalyse> listRdvBD;
    ArrayAdapter adapter;

    @BindView(R.id.selectRdv)
    AutoCompleteTextView selectedRdv;
    @BindView(R.id.listRdv)
    TextInputLayout listRdv;

    @BindView(R.id.layoutAnalyse)
    TextInputLayout layoutAnalyse;
    @BindView(R.id.textAnalyse)
    TextInputEditText textAnalyse;
    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    @BindView(R.id.textDate)
    TextInputEditText textDate;
    @BindView(R.id.layoutHeure)
    TextInputLayout layoutHeure;
    @BindView(R.id.textHeure)
    TextInputEditText textHeure;
    @BindView(R.id.layoutNote)
    TextInputLayout layoutNote;
    @BindView(R.id.textNote)
    TextInputEditText textNote;

    @BindView(R.id.fabDelete)
    FloatingActionButton fabDelete;
    @BindView(R.id.fabEdit)
    FloatingActionButton fabEdit;
    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;
    @BindView(R.id.fabCancel)
    FloatingActionButton fabCancel;
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;
    @BindView(R.id.fabPhoto)
    FloatingActionButton fabPhoto;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_rdv_analyse);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        hideAllFields();
        displayFabs();

        AfficherRdvAnalyseActivity.AsyncTaskRunner runner = new AfficherRdvAnalyseActivity.AsyncTaskRunner();
        runner.execute();

        setTitle("Mes Analyses");
        traiterIntent();
        selectedRdv.setOnItemClickListener(this);
    }

    @Override
    public void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("rdvAnalyseId")) {
            Long rdvAnalyseId = intent.getLongExtra("rdvAnalyseId", 0);
            rdvAnalyseSelected = RdvAnalyse.findById(RdvAnalyse.class, rdvAnalyseId);
            selectedRdv.setText(rdvAnalyseSelected.toString());
            // rdvContactSelected = listRdvContactBD.get(position);
            enableFields(false);
            displayFabs();
            fillAllFields();
            displayAllFields(false);
        }
    }

    public class AsyncTaskRunner extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(50);
            listRdvBD = RdvAnalyse.find(RdvAnalyse.class,"utilisateur = ?",""+activeUser.getId());
            Collections.sort(listRdvBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            if (listRdvBD.size() == 0) {
                Toast.makeText(AfficherRdvAnalyseActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                listRdv.setVisibility(View.GONE);
            } else {
                buildDropdownMenu(listRdvBD, AfficherRdvAnalyseActivity.this,selectedRdv);

            }
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabAdd)
    public void fabAddClick() {
        ouvrirActiviteSuivante(AfficherRdvAnalyseActivity.this, AddRdvAnalyseActivity.class,true);
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {
        deleteItem(AfficherRdvAnalyseActivity.this, rdvAnalyseSelected, AfficherRdvAnalyseActivity.class,false);
        rdvAnalyseSelected.setDate(date);
        if (textNote.getText() != null && !textNote.getText().toString().equalsIgnoreCase(rdvAnalyseSelected.getNote())) {
            rdvAnalyseSelected.setNote(textNote.getText().toString());
        }
        rdvAnalyseSelected.save();
        //enregistrer la/les notification(s)
        activerNotification(rdvAnalyseSelected,AfficherRdvAnalyseActivity.this);
        enableFields(false);
        displayAllFields(false);
        displayFabs();
        Toast.makeText(AfficherRdvAnalyseActivity.this, R.string.modification_saved, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fabCancel)
    public void fabCancelClick() {
        clearAllFields();
        resizeAllFields(false);
        enableFields(false);
        displayFabs();
        fillAllFields();
        displayAllFields(false);
    }

    @OnClick(R.id.fabEdit)
    public void fabEditClick() {
        enableFields(true);
        displayAllFields(true);
        resizeAllFields(true);


        fabEdit.hide();
        fabDelete.hide();
        fabAdd.hide();
        fabSave.show();
        fabCancel.show();
    }

    @OnClick(R.id.fabDelete)
    public void fabDeleteClick() {
        deleteItem(AfficherRdvAnalyseActivity.this, rdvAnalyseSelected, AfficherRdvAnalyseActivity.class,true);
    }

    @OnClick(R.id.fabPhoto)
    public void fabPhotoClick() {
        ouvrirActiviteSuivante(AfficherRdvAnalyseActivity.this, MakePhotoActivity.class,"type", TypePhoto.Analyse.toString(),"itemId", rdvAnalyseSelected.getId(),true);
    }

    private void resizeAllFields(boolean bool) {
        if (bool) {
            textNote.setMinWidth(getResources().getDimensionPixelOffset(R.dimen.field_min_width));
        } else {
            textNote.setMinWidth(0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        rdvAnalyseSelected = listRdvBD.get(position);
        enableFields(false);
        displayFabs();
        fillAllFields();
        displayAllFields(false);
    }

    private void clearAllFields() {
        textAnalyse.setText(null);
        textDate.setText(null);
        textHeure.setText(null);
        textNote.setText(null);
    }

    @Override
    public void displayFabs() {
        fabSave.hide();
        fabCancel.hide();
        if (rdvAnalyseSelected == null) {
            fabEdit.hide();
            fabDelete.hide();
            fabPhoto.hide();
        } else {
            fabEdit.show();
            fabDelete.show();
            fabAdd.show();
            fabPhoto.show();
        }
    }

    private void fillAllFields() {
        textAnalyse.setText(rdvAnalyseSelected.getAnalyse().toString());
        textDate.setText(DateUtils.ecrireDate(rdvAnalyseSelected.getDate()));
        textHeure.setText(DateUtils.ecrireHeure(rdvAnalyseSelected.getDate()));
        textNote.setText(rdvAnalyseSelected.getNote());
    }

    private void enableFields(boolean bool) {
        layoutAnalyse.setEnabled(false);
        layoutDate.setEnabled(bool);
        layoutHeure.setEnabled(bool);
        layoutNote.setEnabled(bool);
    }

    private void displayAllFields(boolean bool) {
        layoutAnalyse.setVisibility(View.VISIBLE);
        layoutDate.setVisibility(View.VISIBLE);
        layoutHeure.setVisibility(View.VISIBLE);
        if (bool || !TextUtils.isEmpty(rdvAnalyseSelected.getNote())) {
            layoutNote.setVisibility(View.VISIBLE);
        } else {
            layoutNote.setVisibility(View.GONE);
        }

    }

    private void hideAllFields() {
        layoutAnalyse.setVisibility(View.GONE);
        layoutDate.setVisibility(View.GONE);
        layoutHeure.setVisibility(View.GONE);
        layoutNote.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void showTimePickerDialog(View v) {
        final Calendar cldr = Calendar.getInstance();
        int hour = 8;
        int minutes = 0;
        // time picker dialog
        picker = new TimePickerDialog(AfficherRdvAnalyseActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        String hour = "";
                        String minute = "";
                        if (sHour<10){
                            hour+="0";
                        }
                        if (sMinute<10){
                            minute+="0";
                        }
                        hour+=sHour;
                        minute+=sMinute;

                        textHeure.setText(hour + ":" + minute);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.HOUR_OF_DAY, sHour);
                        calendar.add(Calendar.MINUTE, sMinute);
                        date = calendar.getTime();
                    }
                }, hour, minutes, true);
        picker.show();
    }

    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        //newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.show(getSupportFragmentManager(), "editTexteDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMinDate(new Date().getTime());
                // TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                Calendar c1 = Calendar.getInstance();
                // set Month
                // MONTH starts with 0 i.e. ( 0 - Jan)
                c1.set(Calendar.MONTH, datePicker.getMonth());
                // set Date
                c1.set(Calendar.DATE, datePicker.getDayOfMonth());
                // set Year
                c1.set(Calendar.YEAR, datePicker.getYear());
                // creating a date object with specified time.
                date = c1.getTime();

                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                textDate.setText(dateString);
                textDate.setError(null);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    date = df.parse(dateString);
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
            }
        });
    }
}

