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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Dose;

import java.util.List;

public class NewDoseActivity extends AppCompatActivity {
    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textDescription;
    private Class<?> activitySource;
    private Dose doseAModif;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dose);

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
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("doseAModifId")) {
            Long doseAModifId = intent.getLongExtra("doseAModifId",0);
            doseAModif = Dose.findById(Dose.class,doseAModifId);

            textNom.setText(doseAModif.getName());
            textDescription.setText(doseAModif.getDetail());
        }
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewDoseActivity.this, activitySource);
        nextActivity.putExtra("activitySource", NewDoseActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Dose> listAllDose = Dose.listAll(Dose.class);
        for (Dose dose : listAllDose) {
            if (doseAModif == null) {
                if (textView.getText().toString().equals(dose.getName())) {
                    textView.requestFocus();
                    textView.setError("la dose existe déjà");
                    reponse = true;
                }
            } else {
                if (!doseAModif.getName().equals(dose.getName()) && textView.getText().toString().equals(dose.getName())) {
                    textView.requestFocus();
                    textView.setError("la dose existe déjà");
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
        if (doseAModif ==null) {
            Dose dose = new Dose(textNom.getText().toString(), textDescription.getText().toString());
            dose.save();
        } else {
            Dose dose = (Dose.find(Dose.class,"id = ?", doseAModif.getId().toString())).get(0);
            dose.setName(textNom.getText().toString());
            dose.setDetail(textDescription.getText().toString());
            dose.save();
        }
    }
}
