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
import com.pouillos.monpilulier.entities.Dose;
import com.pouillos.monpilulier.entities.Examen;

import java.util.Date;
import java.util.List;

public class NewExamenActivity extends AppCompatActivity {
    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textDescription;
    private Class<?> pagePrecedente;
    private Examen examenToUpdate;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_examen);

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

    public void traiterIntent() {
        intent = getIntent();
        pagePrecedente = (Class<?>) intent.getSerializableExtra("precedent");
        if (intent.hasExtra("examenToUpdate")) {
            examenToUpdate = (Examen) intent.getSerializableExtra("examenToUpdate");
            textNom.setText(examenToUpdate.getName());
            textDescription.setText(examenToUpdate.getDetail());
        }
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewExamenActivity.this,pagePrecedente);
        nextActivity.putExtra("precedent", NewExamenActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Examen> listAllExamen = Examen.listAll(Examen.class);
        for (Examen examen : listAllExamen) {
            if (examenToUpdate == null) {
                if (textView.getText().toString().equals(examen.getName())) {
                    textView.requestFocus();
                    textView.setError("l'examen existe déjà");
                    reponse = true;
                }
            } else {
                if (!examenToUpdate.getName().equals(examen.getName()) && textView.getText().toString().equals(examen.getName())) {
                    textView.requestFocus();
                    textView.setError("l'examen existe déjà");
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
        if (examenToUpdate==null) {
            Examen examen = new Examen(textNom.getText().toString(), textDescription.getText().toString());
            examen.setCreationDate(new Date());
            examen.save();
        } else {
            Examen examen = (Examen.find(Examen.class,"id = ?",examenToUpdate.getId().toString())).get(0);
            examen.setName(textNom.getText().toString());
            examen.setDetail(textDescription.getText().toString());
            examen.save();
        }
    }
}
