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
import com.pouillos.monpilulier.entities.Medicament;

import java.util.Date;
import java.util.List;

public class NewCabinetActivity extends AppCompatActivity {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textDescription;
    private TextView textAdresse;
    private TextView textCP;
    private TextView textVille;
    private Class<?> pagePrecedente;
    private Cabinet cabinetToUpdate;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cabinet);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        textNom = findViewById(R.id.textNom);
        textDescription = findViewById(R.id.textDescription);
        textAdresse = findViewById(R.id.textAdresse);
        textCP = findViewById(R.id.textCP);
        textVille = findViewById(R.id.textVille);
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
                if (!isRempli(textAdresse)) {
                    return;
                }
                if (!isRempli(textCP)) {
                    return;
                }
                if (!isValid(textCP)) {
                    return;
                }
                if (!isRempli(textVille)) {
                    return;
                }

                //enregistrer en bdd
                saveToDb(textNom, textDescription, textAdresse, textCP, textVille);

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
        if (intent.hasExtra("cabinetToUpdate")) {
            cabinetToUpdate = (Cabinet) intent.getSerializableExtra("cabinetToUpdate");
            textNom.setText(cabinetToUpdate.getName());
            textDescription.setText(cabinetToUpdate.getDetail());
            textAdresse.setText(cabinetToUpdate.getAdresse());
            textCP.setText(cabinetToUpdate.getCp());
            textVille.setText(cabinetToUpdate.getVille());
        }
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewCabinetActivity.this,pagePrecedente);
        nextActivity.putExtra("precedent", NewCabinetActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Cabinet> listAllCabinet = Cabinet.listAll(Cabinet.class);
        for (Cabinet cabinet : listAllCabinet) {
            if (cabinetToUpdate == null) {
                if (textView.getText().toString().equals(cabinet.getName())) {
                    textView.requestFocus();
                    textView.setError("le cabinet existe déjà");
                    reponse = true;
                }
            } else {
                if (!cabinetToUpdate.getName().equals(cabinet.getName()) && textView.getText().toString().equals(cabinet.getName())) {
                    textView.requestFocus();
                    textView.setError("le cabinet existe déjà");
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

    public boolean isValid(TextView textView) {
        if (textView.getText().length() <5) {
            textView.requestFocus();
            textView.setError("Saisie Non Valide  (10 chiffres)");
            return false;
        } else {
            return true;
        }
    }

    public void saveToDb(TextView textNom, TextView textDescription, TextView textAdresse, TextView textCP, TextView textVille) {
        if (cabinetToUpdate==null) {
            Cabinet cabinet = new Cabinet(textNom.getText().toString(), textDescription.getText().toString(), textAdresse.getText().toString(), textCP.getText().toString(), textVille.getText().toString());
            cabinet.setCreationDate(new Date());
            cabinet.save();
        } else {
            Cabinet cabinet = (Cabinet.find(Cabinet.class,"id = ?",cabinetToUpdate.getId().toString())).get(0);
            cabinet.setName(textNom.getText().toString());
            cabinet.setDetail(textDescription.getText().toString());
            cabinet.setAdresse(textAdresse.getText().toString());
            cabinet.setCp(textCP.getText().toString());
            cabinet.setVille(textVille.getText().toString());
            cabinet.save();
        }
    }
}
