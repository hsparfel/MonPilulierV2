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
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.Date;
import java.util.List;

public class NewExamenActivity extends AppCompatActivity implements BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_examen);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        textNom = findViewById(R.id.textNom);
       // textDescription = findViewById(R.id.textDescription);
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

                //enregistrer en bdd
                saveToDb(textNom);

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

    }

    @Override
    public void showDatePickerDialog(View v) {
    }

    @Override
    public void saveToDb(TextView... args) {

            Examen examen = new Examen(args[0].getText().toString());
            examen.save();

    }

    @Override
    public void saveToDb(TextView textNom, Date date, String sexe) {
    }
}
