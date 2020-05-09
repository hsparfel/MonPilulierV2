package com.pouillos.monpilulier.activities.afficher;

import android.app.TimePickerDialog;
import android.content.Context;
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
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Utilisateur;
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

public class AfficherProfilActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;
    @State
    Profil profilSelected;
    @State
    Date date;

    List<Profil> listProfilBD;
    ArrayAdapter adapter;

    @BindView(R.id.selectProfil)
    AutoCompleteTextView selectedProfil;
    @BindView(R.id.listProfil)
    TextInputLayout listProfil;

    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    @BindView(R.id.textDate)
    TextInputEditText textDate;
    @BindView(R.id.layoutTaille)
    TextInputLayout layoutTaille;
    @BindView(R.id.textTaille)
    TextInputEditText textTaille;
    @BindView(R.id.layoutPoids)
    TextInputLayout layoutPoids;
    @BindView(R.id.textPoids)
    TextInputEditText textPoids;

    @BindView(R.id.fabDelete)
    FloatingActionButton fabDelete;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_profil);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        hideAllFields();
        displayFabs();

        AfficherProfilActivity.AsyncTaskRunner runner = new AfficherProfilActivity.AsyncTaskRunner();
        runner.execute();

        setTitle("Mes Profils");

        selectedProfil.setOnItemClickListener(this);
    }

    public class AsyncTaskRunner extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(50);
            listProfilBD = Profil.find(Profil.class,"utilisateur = ?",""+activeUser.getId());
            Collections.sort(listProfilBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            if (listProfilBD.size() == 0) {
                Toast.makeText(AfficherProfilActivity.this, "Aucune correspondance, modifier puis appliquer filtre", Toast.LENGTH_LONG).show();
                listProfil.setVisibility(View.GONE);
            } else {
                buildDropdownMenu(listProfilBD, AfficherProfilActivity.this,selectedProfil);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabDelete)
    public void fabDeleteClick() {
        deleteItem(AfficherProfilActivity.this, profilSelected, AfficherProfilActivity.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        profilSelected = listProfilBD.get(position);
        enableFields(false);
        displayFabs();
        fillAllFields();
        displayAllFields(false);
   }

    private void clearAllFields() {
        textDate.setText(null);
        textTaille.setText(null);
        textPoids.setText(null);
    }



    @Override
    public void displayFabs() {
        if (profilSelected == null) {
            fabDelete.hide();
        } else {
            fabDelete.show();
        }
    }

    private void fillAllFields() {
        textDate.setText(DateUtils.ecrireDate(profilSelected.getDate()));
        textTaille.setText(""+profilSelected.getTaille()+" cm");
        textPoids.setText(""+profilSelected.getPoids()+" kgs");
    }

    private void enableFields(boolean bool) {
        layoutDate.setEnabled(false);
        layoutTaille.setEnabled(false);
        layoutPoids.setEnabled(false);
    }

    private void displayAllFields(boolean bool) {
        layoutDate.setVisibility(View.VISIBLE);
        layoutTaille.setVisibility(View.VISIBLE);
        layoutPoids.setVisibility(View.VISIBLE);
    }

    private void hideAllFields() {
        layoutDate.setVisibility(View.GONE);
        layoutTaille.setVisibility(View.GONE);
        layoutPoids.setVisibility(View.GONE);
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

