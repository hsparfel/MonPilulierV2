package com.pouillos.monpilulier.activities.afficher;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.add.AddExamenActivity;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AfficherExamenActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;
    @State
    Examen examenSelected;

    TimePickerDialog picker;

    List<Examen> listExamenBD;
    ArrayAdapter adapter;

    @BindView(R.id.selectExamen)
    AutoCompleteTextView selectedExamen;
    @BindView(R.id.listExamen)
    TextInputLayout listExamen;

    @BindView(R.id.layoutName)
    TextInputLayout layoutName;
    @BindView(R.id.textName)
    TextInputEditText textName;

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

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_examen);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        hideAllFields();
        displayFabs();

        AfficherExamenActivity.AsyncTaskRunner runner = new AfficherExamenActivity.AsyncTaskRunner();
        runner.execute();

        setTitle("Examens");

        selectedExamen.setOnItemClickListener(this);
    }

    public class AsyncTaskRunner extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            //activeUser = findActiveUser();
            publishProgress(50);
            listExamenBD = Examen.listAll(Examen.class);
            //listExamenBD = Examen.find(Examen.class,"utilisateur = ?",""+activeUser.getId());
            Collections.sort(listExamenBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            if (listExamenBD.size() == 0) {
                Toast.makeText(AfficherExamenActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                listExamen.setVisibility(View.GONE);
            } else {
                buildDropdownMenu(listExamenBD, AfficherExamenActivity.this,selectedExamen);

            }
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabAdd)
    public void fabAddClick() {
        ouvrirActiviteSuivante(AfficherExamenActivity.this, AddExamenActivity.class);
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {

        examenSelected.setName(textName.getText().toString());

        examenSelected.save();
        //AfficherExamenActivity.AsyncTaskRunner runner = new AfficherExamenActivity.AsyncTaskRunner();
        //runner.execute();
        ouvrirActiviteSuivante(AfficherExamenActivity.this,AfficherExamenActivity.class);
        enableFields(false);
        displayAllFields(false);
        displayFabs();
        Toast.makeText(AfficherExamenActivity.this, R.string.modification_saved, Toast.LENGTH_LONG).show();
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
        deleteItem(AfficherExamenActivity.this, examenSelected, AfficherExamenActivity.class);
    }

    private void resizeAllFields(boolean bool) {
        if (bool) {
            textName.setMinWidth(getResources().getDimensionPixelOffset(R.dimen.field_min_width));
        } else {
            textName.setMinWidth(0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        examenSelected = listExamenBD.get(position);
        enableFields(false);
        displayFabs();
        fillAllFields();
        displayAllFields(false);
    }

    private void clearAllFields() {
        textName.setText(null);
    }

    @Override
    public void displayFabs() {
        fabSave.hide();
        fabCancel.hide();
        if (examenSelected == null) {
            fabEdit.hide();
            fabDelete.hide();
        } else {
            fabEdit.show();
            fabDelete.show();
            fabAdd.show();
        }
    }

    private void fillAllFields() {
        textName.setText(examenSelected.getName().toString());
    }

    private void enableFields(boolean bool) {
        layoutName.setEnabled(bool);
    }

    private void displayAllFields(boolean bool) {
        layoutName.setVisibility(View.VISIBLE);
    }

    private void hideAllFields() {
        layoutName.setVisibility(View.GONE);
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

