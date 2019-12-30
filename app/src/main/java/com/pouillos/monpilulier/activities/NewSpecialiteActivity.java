package com.pouillos.monpilulier.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Specialite;

import java.util.Date;
import java.util.List;

public class NewSpecialiteActivity extends AppCompatActivity {
    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textDescription;
    private Class<?> pagePrecedente;
    private Specialite specialiteToUpdate;
    private  Medecin medecinToUpdate;
    private Intent intent;

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
                retourPagePrecedenteAnnuler(intent);
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
                retourPagePrecedenteValider(intent);
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
        pagePrecedente = (Class<?>) intent.getSerializableExtra("precedent");
        if (intent.hasExtra("specialiteToUpdate")) {
            specialiteToUpdate = (Specialite) intent.getSerializableExtra("specialiteToUpdate");
            textNom.setText(specialiteToUpdate.getName());
            textDescription.setText(specialiteToUpdate.getDetail());
        }

        if (intent.hasExtra("medecinToUpdate")) {
            medecinToUpdate = (Medecin) intent.getSerializableExtra("medecinToUpdate");
        }
    }

    public void retourPagePrecedenteAnnuler(Intent intent) {
        Intent nextActivity = new Intent(NewSpecialiteActivity.this,pagePrecedente);
        if (intent.hasExtra("medecinToUpdate")) {
            medecinToUpdate = (Medecin) intent.getSerializableExtra("medecinToUpdate");
            nextActivity.putExtra("medecinToUpdate", medecinToUpdate);
        } else {
            nextActivity.putExtra("precedent", NewSpecialiteActivity.class);
        }
        startActivity(nextActivity);
        finish();
    }

    public void retourPagePrecedenteValider(Intent intent) {
        Intent nextActivity = new Intent(NewSpecialiteActivity.this,pagePrecedente);
        if (intent.hasExtra("medecinToUpdate")) {
            medecinToUpdate = (Medecin) intent.getSerializableExtra("medecinToUpdate");
            Specialite specialite = (Specialite) Specialite.find(Specialite.class,"name = ?", textNom.getText().toString()).get(0);
            medecinToUpdate.setSpecialite(specialite);
            nextActivity.putExtra("medecinToUpdate", medecinToUpdate);
        } else {
            nextActivity.putExtra("precedent", NewSpecialiteActivity.class);
        }
        startActivity(nextActivity);
        finish();
    }

    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Specialite> listAllSpecialite = Specialite.listAll(Specialite.class);
        for (Specialite specialite : listAllSpecialite) {
            if (specialiteToUpdate == null) {
                if (textView.getText().toString().equals(specialite.getName())) {
                    textView.requestFocus();
                    textView.setError("la specialite existe déjà");
                    reponse = true;
                }
            } else {
                if (!specialiteToUpdate.getName().equals(specialite.getName()) && textView.getText().toString().equals(specialite.getName())) {
                    textView.requestFocus();
                    textView.setError("la specialite existe déjà");
                    reponse = true;
                }
            }
        }
        return reponse;
    }

    public boolean isRempli(TextView textView) {
        if (TextUtils.isEmpty(textView.getText())) {
            textView.requestFocus();
            textView.setError("Saisie Obligatoire");
            return false;
        } else {
            return true;
        }
    }

    public void saveToDb(TextView textNom, TextView textDescription) {
        if (specialiteToUpdate==null) {
            Specialite specialite = new Specialite(textNom.getText().toString(), textDescription.getText().toString());
            specialite.setCreationDate(new Date());
            specialite.save();
        } else {
            Specialite specialite = (Specialite.find(Specialite.class,"id = ?",specialiteToUpdate.getId().toString())).get(0);
            specialite.setName(textNom.getText().toString());
            specialite.setDetail(textDescription.getText().toString());
            specialite.save();
        }
    }
}
