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
import com.pouillos.monpilulier.activities.listallx.ListAllMedecinActivity;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Specialite;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.Date;
import java.util.List;

public class NewSpecialiteActivity extends AppCompatActivity implements BasicUtils {
    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textDescription;
    private Class<?> activitySource;
    private Specialite specialiteAModif;
    private  Medecin medecinToUpdate;
    private Intent intent;
    private boolean associe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_specialite);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        textNom = findViewById(R.id.textNom);
        textDescription = findViewById(R.id.textDescription);
        
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

    public void traiterIntent() {
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("specialiteAModifId")) {

            Long specialiteAModifId = intent.getLongExtra("specialiteAModifId",0);
            specialiteAModif = Specialite.findById(Specialite.class,specialiteAModifId);



            textNom.setText(specialiteAModif.getName());
            textDescription.setText(specialiteAModif.getDetail());
        }

        if (intent.hasExtra("medecinToUpdate")) {
            medecinToUpdate = (Medecin) intent.getSerializableExtra("medecinToUpdate");
        }
        if (intent.hasExtra("associe")) {
            associe = intent.getBooleanExtra("associe",false);
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
        if (specialiteAModif ==null) {
            Specialite specialite = new Specialite(args[0].getText().toString(), args[1].getText().toString());
            specialite.save();
        } else {
            Specialite specialite = (Specialite.find(Specialite.class,"id = ?", specialiteAModif.getId().toString())).get(0);
            specialite.setName(args[0].getText().toString());
            specialite.setDetail(args[1].getText().toString());
            specialite.save();
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
        Intent nextActivity = new Intent(NewSpecialiteActivity.this, activitySource);
        nextActivity.putExtra("activitySource", NewSpecialiteActivity.class);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Specialite> listAllSpecialite = Specialite.listAll(Specialite.class);
        for (Specialite specialite : listAllSpecialite) {
            if (specialiteAModif == null) {
                if (textView.getText().toString().equals(specialite.getName())) {
                    textView.requestFocus();
                    textView.setError("la specialite existe déjà");
                    reponse = true;
                }
            } else {
                if (!specialiteAModif.getName().equals(specialite.getName()) && textView.getText().toString().equals(specialite.getName())) {
                    textView.requestFocus();
                    textView.setError("la specialite existe déjà");
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
