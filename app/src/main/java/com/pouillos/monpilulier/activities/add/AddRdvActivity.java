package com.pouillos.monpilulier.activities.add;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.AccueilActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.tools.RdvNotificationBroadcastReceiver;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AddRdvActivity extends NavDrawerActivity implements Serializable, BasicUtils {
//TODO sur les 3 classes de RDV pb si selection de l'heure avant la date metre enable(false ) puis enable true pour palier à ça
 //TODO implementer les rappels / notif pour les 3 aussi


    @State
    Utilisateur activeUser;
    @State
    Contact contact;
    @State
    Date date = new Date();
    @State
    Rdv rdvToCreate;

    TimePickerDialog picker;

    @BindView(R.id.layoutContact)
    TextInputLayout layoutContact;
    @BindView(R.id.textContact)
    TextInputEditText textContact;
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

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_rdv);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        activeUser = findActiveUser();

        traiterIntent();
        displayFabs();
        layoutContact.setEnabled(false);
        setTitle(getString(R.string.my_meeting));
    }

    @Override
    public void displayFabs() {
            fabSave.show();
    }

    @Override
    public boolean isExistant() {
        boolean bool;
        bool = false;
        List<Rdv> listRdv = Rdv.find(Rdv.class,"utilisateur = ? and contact = ? and date = ?",""+activeUser.getId(),""+contact.getId(),""+date.getTime());
        if (listRdv.size() != 0) {
            bool = true;
        }
        return bool;
    }

    @Override
    public boolean checkFields(){
        boolean bool;
        if (!isFilled(textDate)){
            layoutDate.setError("Obligatoire");
        } else {
            layoutDate.setError(null);
        }
        if (!isFilled(textHeure)){
            layoutHeure.setError("Obligatoire");
        } else {
            layoutHeure.setError(null);
        }

        bool = isFilled(date) && isFilled(textHeure);
        return bool;
    }

    @Override
    public void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("contactId")) {
            Long contactId = intent.getLongExtra("contactId", 0);
            contact = Contact.findById(Contact.class, contactId);
            textContact.setText(contact.toString());
            rdvToCreate = new Rdv();
            rdvToCreate.setContact(contact);
        }
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {
        if (checkFields()) {
            if (!isExistant()) {
                saveToDb();
                ouvrirActiviteSuivante(AddRdvActivity.this, AccueilActivity.class);
            } else {
                Toast.makeText(AddRdvActivity.this, "Rdv déjà existant", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(AddRdvActivity.this, "Saisie non valide", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void saveToDb() {
        Rdv rdv = new Rdv();
        rdv.setNote(textNote.getText().toString());
        rdv.setContact(contact);
        rdv.setUtilisateur(activeUser);
        rdv.setDate(date);
        rdv.save();

        Toast.makeText(AddRdvActivity.this, "Rdv Enregistré", Toast.LENGTH_LONG).show();
        //enregistrer la/les notification(s)
        activerNotification(RdvNotificationBroadcastReceiver.class,rdv.getDate(), rdv.getContact(),AddRdvActivity.this);
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
        picker = new TimePickerDialog(AddRdvActivity.this,
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
                //tv1.setText("date: "+dateString);
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
