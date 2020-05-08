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
import com.pouillos.monpilulier.activities.add.AddAnalyseActivity;
import com.pouillos.monpilulier.entities.Analyse;
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

public class AfficherAnalyseActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;
    @State
    Analyse analyseSelected;

    TimePickerDialog picker;

    List<Analyse> listAnalyseBD;
    ArrayAdapter adapter;

    @BindView(R.id.selectAnalyse)
    AutoCompleteTextView selectedAnalyse;
    @BindView(R.id.listAnalyse)
    TextInputLayout listAnalyse;

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
        setContentView(R.layout.activity_afficher_analyse);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        hideAllFields();
        displayFabs();

        AfficherAnalyseActivity.AsyncTaskRunner runner = new AfficherAnalyseActivity.AsyncTaskRunner();
        runner.execute();

        setTitle("Analyses");

        selectedAnalyse.setOnItemClickListener(this);
    }

    public class AsyncTaskRunner extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            //activeUser = findActiveUser();
            publishProgress(50);
            listAnalyseBD = Analyse.listAll(Analyse.class);
            //listAnalyseBD = Analyse.find(Analyse.class,"utilisateur = ?",""+activeUser.getId());
            Collections.sort(listAnalyseBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            if (listAnalyseBD.size() == 0) {
                Toast.makeText(AfficherAnalyseActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                listAnalyse.setVisibility(View.GONE);
            } else {
                buildDropdownMenu(listAnalyseBD, AfficherAnalyseActivity.this,selectedAnalyse);

            }
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabAdd)
    public void fabAddClick() {
        ouvrirActiviteSuivante(AfficherAnalyseActivity.this, AddAnalyseActivity.class);
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {

        analyseSelected.setName(textName.getText().toString());

        analyseSelected.save();
       // AfficherAnalyseActivity.AsyncTaskRunner runner = new AfficherAnalyseActivity.AsyncTaskRunner();
      //  runner.execute();
        ouvrirActiviteSuivante(AfficherAnalyseActivity.this,AfficherAnalyseActivity.class);
        enableFields(false);
        displayAllFields(false);
        displayFabs();
        Toast.makeText(AfficherAnalyseActivity.this, R.string.modification_saved, Toast.LENGTH_LONG).show();
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
        deleteItem(AfficherAnalyseActivity.this, analyseSelected, AfficherAnalyseActivity.class);
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
        analyseSelected = listAnalyseBD.get(position);
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
        if (analyseSelected == null) {
            fabEdit.hide();
            fabDelete.hide();
        } else {
            fabEdit.show();
            fabDelete.show();
            fabAdd.show();
        }
    }

    private void fillAllFields() {
        textName.setText(analyseSelected.getName().toString());
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

