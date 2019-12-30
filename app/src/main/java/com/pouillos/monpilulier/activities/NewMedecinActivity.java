package com.pouillos.monpilulier.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Specialite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewMedecinActivity extends AppCompatActivity {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddSpecialite;
    private TextView textNom;
    private TextView textDescription;
    private Spinner spinnerSpecialite;
    private TextView textTelephone;
    private TextView textEmail;
    private Class<?> pagePrecedente;
    private Medecin medecinToUpdate;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medecin);

        buttonAddSpecialite = (ImageButton) findViewById(R.id.buttonAddSpecialite);
        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        textNom = findViewById(R.id.textNom);
        textDescription = findViewById(R.id.textDescription);
        spinnerSpecialite = (Spinner) findViewById(R.id.spinnerSpecialite);
        textTelephone = findViewById(R.id.textTelephone);
        textEmail = findViewById(R.id.textEmail);

        createSpinnerSpecialite();

        traiterIntent();

        buttonAddSpecialite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAdd();

            }
        });

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
                if (!isValidTel(textTelephone)) {
                    return;
                }
                if (!isValidEmail(textEmail)) {
                    return;
                }

                //enregistrer en bdd
                saveToDb(textNom, textDescription, textTelephone, textEmail);

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
        if (intent.hasExtra("medecinToUpdate")) {
            medecinToUpdate = (Medecin) intent.getSerializableExtra("medecinToUpdate");
            textNom.setText(medecinToUpdate.getName());
            textDescription.setText(medecinToUpdate.getDetail());
            textTelephone.setText(medecinToUpdate.getTelephone());
            textEmail.setText(medecinToUpdate.getEmail());
            spinnerSpecialite.setSelection(getIndex(spinnerSpecialite, medecinToUpdate.getSpecialite().getName()));
        } else {medecinToUpdate = new Medecin();}
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewMedecinActivity.this,pagePrecedente);
        nextActivity.putExtra("precedent", NewMedecinActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityAdd() {
        Intent nextActivity = new Intent(NewMedecinActivity.this,NewSpecialiteActivity.class);
        nextActivity.putExtra("precedent", NewMedecinActivity.class);
        medecinToUpdate.setName(textNom.getText().toString());
        medecinToUpdate.setDetail(textDescription.getText().toString());
        medecinToUpdate.setTelephone(textTelephone.getText().toString());
        medecinToUpdate.setEmail(textEmail.getText().toString());
        nextActivity.putExtra("medecinToUpdate", medecinToUpdate);
        startActivity(nextActivity);
        finish();
    }

    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Medecin> listAllMedecin = Medecin.listAll(Medecin.class);
        for (Medecin medecin : listAllMedecin) {
            if (medecinToUpdate.getId() == null) {
                if (textView.getText().toString().equals(medecin.getName())) {
                    textView.requestFocus();
                    textView.setError("le medecin existe déjà");
                    reponse = true;
                }
            } else {
                if (!medecinToUpdate.getName().equals(medecin.getName()) && textView.getText().toString().equals(medecin.getName())) {
                    textView.requestFocus();
                    textView.setError("le medecin existe déjà");
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

    public boolean isValidTel(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && textView.getText().length() <10) {
            textView.requestFocus();
            textView.setError("Saisie Non Valide  (10 chiffres)");
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidEmail(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && !isEmailAdress(textView.getText().toString())) {
            textView.requestFocus();
            textView.setError("Saisie Non Valide (email)");
            return false;
        } else {
            return true;
        }
    }

    public void saveToDb(TextView textNom, TextView textDescription, TextView textTelephone, TextView textEmail) {
        Specialite specialite = (Specialite) Specialite.find(Specialite.class,"name = ?", spinnerSpecialite.getSelectedItem().toString()).get(0);
        if (medecinToUpdate==null) {
            Medecin medecin = new Medecin(textNom.getText().toString(), textDescription.getText().toString(),specialite, textTelephone.getText().toString(), textEmail.getText().toString());
            medecin.setCreationDate(new Date());
            medecin.save();
        } else {
            Medecin medecin;
            if (medecinToUpdate.getId()!=null) {
                medecin = (Medecin.find(Medecin.class, "id = ?", medecinToUpdate.getId().toString())).get(0);
            } else {
                medecin = new Medecin();
            }
            medecin.setName(textNom.getText().toString());
            medecin.setDetail(textDescription.getText().toString());
            medecin.setTelephone(textTelephone.getText().toString());
            medecin.setEmail(textEmail.getText().toString());
            medecin.setSpecialite(specialite);
            medecin.save();
        }
    }

    public static boolean isEmailAdress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    public void createSpinnerSpecialite() {
        List<Specialite> listAllSpecialite = Specialite.listAll(Specialite.class,"name");
        List<String> listSpecialiteName = new ArrayList<String>();
        for (Specialite specialite : listAllSpecialite) {
            listSpecialiteName.add(specialite.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpecialiteName);
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialite.setAdapter(adapter);
    }
}
