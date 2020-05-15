package com.pouillos.monpilulier.activities.add;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.AccueilActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherAnalyseActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherExamenActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Photo;
import com.pouillos.monpilulier.entities.RdvAnalyse;
import com.pouillos.monpilulier.entities.RdvContact;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.TypePhoto;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AddOrdonnanceActivity extends NavDrawerActivity implements Serializable, BasicUtils {
    @State
    Utilisateur activeUser;
    @State
    Date date;
    @State
    Contact contactSelected;
    @State
    RdvContact rdvContactSelected;
    @State
    Ordonnance ordonnance;

    TimePickerDialog picker;

    @BindView(R.id.selectContact)
    AutoCompleteTextView selectedContact;
    @BindView(R.id.listContact)
    TextInputLayout listContact;

    @BindView(R.id.selectRdvContact)
    AutoCompleteTextView selectedRdvContact;
    @BindView(R.id.listRdvContact)
    TextInputLayout listRdvContact;

    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    @BindView(R.id.textDate)
    TextInputEditText textDate;

    @BindView(R.id.chipWithRdv)
    Chip chipWithRdv;
    @BindView(R.id.chipWithoutRdv)
    Chip chipWithoutRdv;

    boolean withRdv;
    boolean withoutRdv;
    private List<Contact> listContactBD;
    private List<RdvContact> listRdvContactBD;

    @BindView(R.id.fabAddPrescription)
    FloatingActionButton fabAddPrescription;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_ordonnance);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        AddOrdonnanceActivity.AsyncTaskRunnerBDContact runnerBDContact = new AddOrdonnanceActivity.AsyncTaskRunnerBDContact();
        runnerBDContact.execute();

        updateDisplay();

        setTitle("+ Ordonnance");

        hideAll(true);

        //selectedContact.setOnItemClickListener(this);
        selectedContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                hideAll(true);
                contactSelected = listContactBD.get(position);
                selectedRdvContact.setText(null);
                rdvContactSelected = null;
                withoutRdv = false;
                withRdv = false;
                chipWithoutRdv.setEnabled(true);
                chipWithRdv.setEnabled(true);
                chipWithoutRdv.setChecked(false);
                chipWithRdv.setChecked(false);

                updateDisplay();
            }
        });

        //selectedRdvContact.setOnItemClickListener(this);
        selectedRdvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                rdvContactSelected = listRdvContactBD.get(position);
                updateDisplay();
            }
        });

        //layoutDate.setEnabled(false);
      //  layoutHeure.setEnabled(false);
    }

    private void hideAll(boolean bool){
        if (bool) {
            chipWithRdv.setVisibility(View.GONE);
            chipWithoutRdv.setVisibility(View.GONE);
            listRdvContact.setVisibility(View.GONE);
            layoutDate.setVisibility(View.GONE);
        }

        fabAddPrescription.hide();
    }

    public void updateDisplay() {
        if (contactSelected != null) {
            chipWithoutRdv.setVisibility(View.VISIBLE);
            chipWithRdv.setVisibility(View.VISIBLE);
        }
        if (chipWithRdv.isChecked()) {
            listRdvContact.setVisibility(View.VISIBLE);
            layoutDate.setVisibility(View.GONE);
        } else if (chipWithoutRdv.isChecked()) {
            listRdvContact.setVisibility(View.GONE);
            layoutDate.setVisibility(View.VISIBLE);
        }
        if (chipWithRdv.isChecked() && rdvContactSelected != null) {
            fabAddPrescription.show();
        } else if (chipWithoutRdv.isChecked() && textDate.getText() != null) {
            fabAddPrescription.show();
        } else {
            fabAddPrescription.hide();
        }
    }



    @OnClick(R.id.chipWithRdv)
    public void chipWithRdvClick() {
        progressBar.setVisibility(View.VISIBLE);
        AddOrdonnanceActivity.AsyncTaskRunnerBDRdvContact runnerBDRdvContact = new AddOrdonnanceActivity.AsyncTaskRunnerBDRdvContact();
        runnerBDRdvContact.execute();

        chipWithRdv.setEnabled(false);
        chipWithoutRdv.setEnabled(true);

        withRdv = true;
        withoutRdv = false;

        date = null;
        textDate.setText(null);
        selectedRdvContact.setText(null);
        rdvContactSelected = null;
        updateDisplay();

    }
    @OnClick(R.id.chipWithoutRdv)
    public void chipWithoutRdvClick() {

        chipWithRdv.setEnabled(true);
        chipWithoutRdv.setEnabled(false);

        withoutRdv = true;
        withRdv = false;

        date = new Date();
        textDate.setText(DateUtils.ecrireDate(date));
        selectedRdvContact.setText(null);
        rdvContactSelected = null;
        updateDisplay();
    }



    public class AsyncTaskRunnerBDContact extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(50);
            String requete = "";
            requete += "SELECT c.* FROM CONTACT AS c LEFT JOIN ASSOCIATION_UTILISATEUR_CONTACT as auc ";
            requete += "ON c.ID = auc.CONTACT " ;
            requete += "WHERE auc.UTILISATEUR = "+activeUser.getId();

            listContactBD = Contact.findWithQuery(Contact.class, requete);
            Collections.sort(listContactBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
                buildDropdownMenu(listContactBD, AddOrdonnanceActivity.this,selectedContact);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    public class AsyncTaskRunnerBDRdvContact extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);

            publishProgress(50);
            listRdvContactBD = RdvContact.find(RdvContact.class,"utilisateur = ? and contact = ?",activeUser.getId().toString(),contactSelected.getId().toString());
            Collections.sort(listRdvContactBD);
            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);

            if (listRdvContactBD.size() == 0) {
                Toast.makeText(AddOrdonnanceActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                listRdvContact.setVisibility(View.GONE);
            } else {
                List<String> listString = new ArrayList<>();
                String[] listDeroulante;
                listDeroulante = new String[listRdvContactBD.size()];
                for (RdvContact current : listRdvContactBD) {
                    listString.add(DateUtils.ecrireDateHeure(current.getDate()));
                }
                listString.toArray(listDeroulante);
                ArrayAdapter adapter = new ArrayAdapter(AddOrdonnanceActivity.this, R.layout.list_item, listDeroulante);
                selectedRdvContact.setAdapter(adapter);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabAddPrescription)
    public void fabAddPrescriptionClick() {
        //Toast.makeText(AddOrdonnanceActivity.this, "a faire", Toast.LENGTH_LONG).show();
        //todo enregistrer ordo en base
        saveToDb();
        ouvrirActiviteSuivante(AddOrdonnanceActivity.this,AddPrescriptionActivity.class,"ordonnanceId",ordonnance.getId(),false);
        //passer l'id ordo ne extra
    }

    @Override
    public void saveToDb() {
        ordonnance = new Ordonnance();
        ordonnance.setContact(contactSelected);
        ordonnance.setDate(date);
        ordonnance.setRdvContact(rdvContactSelected);
        ordonnance.setUtilisateur(activeUser);
        ordonnance.setId(ordonnance.save());
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
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        //newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.show(getSupportFragmentManager(), "editTexteDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMaxDate(new Date().getTime());
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
