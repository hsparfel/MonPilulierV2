package com.pouillos.monpilulier.activities.newx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.Date;
import java.util.List;

public class NewAnalyseActivity extends AppCompatActivity implements BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textDescription;
    private Class<?> activitySource;
    private Analyse analyseAModif;
    private Intent intent;
    private Rdv rdvEnCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_analyse);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        textNom = findViewById(R.id.textNom);
        textDescription = findViewById(R.id.textDescription);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);

        traiterIntent();

        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourPagePrecedente();
            }
        });

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //rediger les verifs de remplissage des champs
                if (isExistant(textNom)) {
                    return;
                }
                if (!isRempli(textNom)) {
                    return;
                }

                //enregistrer en bdd
                saveToDb(textNom, textDescription);

                //retour
                retourPagePrecedente();
            }
        });
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
    public void traiterIntent() {
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("analyseAModifId")) {
            Long analyseAModifId = intent.getLongExtra("analyseAModifId",0);
            analyseAModif = Analyse.findById(Analyse.class,analyseAModifId);
            textNom.setText(analyseAModif.getName());
            textDescription.setText(analyseAModif.getDetail());
        }
        if (intent.hasExtra("rdvEnCours")) {
            rdvEnCours = (Rdv) intent.getSerializableExtra("rdvEnCours");
        }
    }

    @Override
    public void showDatePickerDialog(View v) {
    }

    @Override
    public void alertOnSpinners() {
    }

    @Override
    public void alertOffSpinners() {
    }

    @Override
    public void saveToDb(TextView... args) {
        if (analyseAModif ==null) {
            Analyse analyse = new Analyse(args[0].getText().toString(), args[1].getText().toString());
            analyse.setId(analyse.save());
        } else {
            analyseAModif.setName(args[0].getText().toString());
            analyseAModif.setDetail(args[1].getText().toString());
            analyseAModif.save();
        }
    }

    @Override
    public void saveToDb(TextView textNom, Date date, String sexe) {
    }

    @Override
    public void createSpinners() {
    }

    @Override
    public void retourPagePrecedente(Intent intent) {
    }

    @Override
    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewAnalyseActivity.this,activitySource);
        nextActivity.putExtra("activitySource", NewAnalyseActivity.class);
        nextActivity.putExtra("rdvEnCours", rdvEnCours);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Analyse> listAllAnalyse = Analyse.listAll(Analyse.class);
        for (Analyse analyse : listAllAnalyse) {
            if (analyseAModif == null) {
                if (textView.getText().toString().equals(analyse.getName())) {
                    textView.requestFocus();
                    textView.setError("l'analyse existe déjà");
                    reponse = true;
                }
            } else {
                if (!analyseAModif.getName().equals(analyse.getName()) && textView.getText().toString().equals(analyse.getName())) {
                    textView.requestFocus();
                    textView.setError("l'analyse existe déjà");
                    reponse = true;
                }
            }
        }
        return reponse;
    }

    @Override
    public boolean isRempli(TextView textView) {
        if (TextUtils.isEmpty(textView.getText())) {
            textView.requestFocus();
            textView.setError("Saisie Obligatoire");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isRempli(TextView textView, Date date) {
        return false;
    }

    @Override
    public boolean isRempli(TextView textView, String string) {
        return false;
    }

    @Override
    public boolean isRempli(Spinner... args) {
        return false;
    }

    @Override
    public boolean isValid(TextView textView) {
        return false;
    }

}
