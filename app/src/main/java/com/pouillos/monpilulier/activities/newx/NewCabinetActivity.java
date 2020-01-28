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
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.Date;
import java.util.List;

public class NewCabinetActivity extends AppCompatActivity implements BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textDescription;
    private TextView textAdresse;
    private TextView textCP;
    private TextView textVille;
    private Class<?> activitySource;
    private Cabinet cabinetAModif;
    private Intent intent;
    private Rdv rdvEnCours;

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

    @Override
    public void traiterIntent() {
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("cabinetAModifId")) {
            Long cabinetAModifId = intent.getLongExtra("cabinetAModifId",0);
            cabinetAModif = Cabinet.findById(Cabinet.class,cabinetAModifId);

            textNom.setText(cabinetAModif.getName());
            textDescription.setText(cabinetAModif.getDetail());
            textAdresse.setText(cabinetAModif.getAdresse());
            textCP.setText(cabinetAModif.getCp());
            textVille.setText(cabinetAModif.getVille());
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
        if (cabinetAModif ==null) {
            Cabinet cabinet = new Cabinet(args[0].getText().toString(), args[1].getText().toString(), args[2].getText().toString(), args[3].getText().toString(), args[4].getText().toString());
            cabinet.save();
        } else {
            Cabinet cabinet = (Cabinet.find(Cabinet.class,"id = ?", cabinetAModif.getId().toString())).get(0);
            cabinet.setName(args[0].getText().toString());
            cabinet.setDetail(args[1].getText().toString());
            cabinet.setAdresse(args[2].getText().toString());
            cabinet.setCp(args[3].getText().toString());
            cabinet.setVille(args[4].getText().toString());
            cabinet.save();
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
        Intent nextActivity = new Intent(NewCabinetActivity.this,activitySource);
        nextActivity.putExtra("activitySource", NewCabinetActivity.class);
        nextActivity.putExtra("rdvEnCours", rdvEnCours);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Cabinet> listAllCabinet = Cabinet.listAll(Cabinet.class);
        for (Cabinet cabinet : listAllCabinet) {
            if (cabinetAModif == null) {
                if (textView.getText().toString().equals(cabinet.getName())) {
                    textView.requestFocus();
                    textView.setError("le cabinet existe déjà");
                    reponse = true;
                }
            } else {
                if (!cabinetAModif.getName().equals(cabinet.getName()) && textView.getText().toString().equals(cabinet.getName())) {
                    textView.requestFocus();
                    textView.setError("le cabinet existe déjà");
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
        if (textView.getText().length() <5) {
            textView.requestFocus();
            textView.setError("Saisie Non Valide  (10 chiffres)");
            return false;
        } else {
            return true;
        }
    }

}
