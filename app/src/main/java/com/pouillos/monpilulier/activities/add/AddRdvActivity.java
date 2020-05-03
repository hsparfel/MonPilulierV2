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
    @BindView(R.id.fabEdit)
    FloatingActionButton fabEdit;

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

        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            activeUser = listUserActif.get(0);
        }

        traiterIntent();
        displayFabs();
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
            String contactString = "";
            if (!contact.getCodeCivilite().equalsIgnoreCase("")) {
                contactString += contact.getCodeCivilite()+" ";
            }
            contactString += contact.getNom()+" "+contact.getPrenom();
            textContact.setText(contactString);
        }
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {
        if (checkFields()) {
            if (!isExistant()) {
                saveToDb();
                ouvrirActiviteSuivante(AddRdvActivity.this, AccueilActivity.class);
            } else {
                Toast toast = Toast.makeText(AddRdvActivity.this, "Rdv déjà existant", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(AddRdvActivity.this, "Saisie non valide", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @OnClick(R.id.fabEdit)
    public void fabEditClick() {
        Toast.makeText(AddRdvActivity.this, "à implementer 2", Toast.LENGTH_LONG).show();
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



}
