package com.pouillos.monpilulier.activities.newx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdonnanceActivity;
import com.pouillos.monpilulier.entities.Association;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Specialite;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewMedecinActivity extends AppCompatActivity implements BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddSpecialite;
    private TextView textNom;
    private TextView textDescription;
    private Spinner spinnerSpecialite;
    private TextView textTelephone;
    private TextView textEmail;
    private Class<?> activitySource;
    private Medecin medecinAModif;
    private Intent intent;
    private Medecin medecin;
    private CheckBox checkBoxAssocier;
    private Utilisateur utilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medecin);

        utilisateur = (new Utilisateur()).findActifUser();
        buttonAddSpecialite = (ImageButton) findViewById(R.id.buttonAddSpecialite);
        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        textNom = findViewById(R.id.textNom);
        textDescription = findViewById(R.id.textDescription);
        spinnerSpecialite = (Spinner) findViewById(R.id.spinnerSpecialite);
        textTelephone = findViewById(R.id.textTelephone);
        textEmail = findViewById(R.id.textEmail);
        checkBoxAssocier = (CheckBox) findViewById(R.id.checkBoxAssocier);

        createSpinners();

        traiterIntent();

        spinnerSpecialite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alertOffSpinners();
                return false;
            }
        });

        buttonAddSpecialite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAdd();
            }
        });

        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourPagePrecedente(intent);
            }
        });

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rediger les verifs de remplissage des champs
                if (medecinAModif ==null && isExistant(textNom)) {
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
                if (!isRempli(spinnerSpecialite)){
                    return;
                }

                //enregistrer en bdd
                saveToDb(textNom, textDescription, textTelephone, textEmail);
                if (checkBoxAssocier.isChecked()) {
                    saveToDbAssocier();
                } else {
                    deleteToDbAssocier();
                }
                //retour
                retourPagePrecedente(intent);
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
        if (intent.hasExtra("medecinAModifId")) {
            Long medecinAModifId = intent.getLongExtra("medecinAModifId",0);
            medecinAModif = Medecin.findById(Medecin.class,medecinAModifId);
            textNom.setText(medecinAModif.getName());
            textDescription.setText(medecinAModif.getDetail());
            textTelephone.setText(medecinAModif.getTelephone());
            textEmail.setText(medecinAModif.getEmail());
            spinnerSpecialite.setSelection(getIndex(spinnerSpecialite, medecinAModif.getSpecialite().getName()));
        } else {
            medecinAModif = new Medecin();}
        if (intent.hasExtra("associe")) {
            checkBoxAssocier.setChecked(intent.getBooleanExtra("associe",false));
        }
    }

    @Override
    public void showDatePickerDialog(View v) {
    }

    @Override
    public void retourPagePrecedente(Intent intent) {
        Intent nextActivity = new Intent(NewMedecinActivity.this,activitySource);
        nextActivity.putExtra("activitySource", NewMedecinActivity.class);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void retourPagePrecedente() {

    }

    public void ouvrirActivityAdd() {
    }

    @Override
    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Medecin> listAllMedecin = Medecin.listAll(Medecin.class);
        for (Medecin medecin : listAllMedecin) {
            if (medecinAModif.getId() == null) {
                if (textView.getText().toString().equals(medecin.getName())) {
                    textView.requestFocus();
                    textView.setError("le medecin existe déjà");
                    reponse = true;
                }
            } else {
                if (!medecinAModif.getName().equals(medecin.getName()) && textView.getText().toString().equals(medecin.getName())) {
                    textView.requestFocus();
                    textView.setError("le medecin existe déjà");
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
        if (args[0].getSelectedItem().toString().equals("sélectionner")) {
            alertOnSpinners();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isValid(TextView textView) {
        return false;
    }

    public void saveToDbAssocier() {
        if (medecinAModif.getId()!=null) {
            medecin = (Medecin.find(Medecin.class, "name = ?", medecinAModif.getName())).get(0);
        }
        List<Association> listAssociation = Association.find(Association.class,"utilisateur = ? and medecin = ?", utilisateur.getId().toString(), medecin.getId().toString());
        if (listAssociation.size() == 0) {
            Association association = new Association(utilisateur.getId(), medecin.getId());
            association.save();
        }
    }

    public void deleteToDbAssocier() {
        List<Association> listAssociation = Association.find(Association.class,"utilisateur = ? and medecin = ?", utilisateur.getId().toString(), medecin.getId().toString());
        if (listAssociation.size() != 0) {
            listAssociation.get(0).delete();
        }
    }

    @Override
    public void alertOnSpinners() {
        TextView errorText = (TextView)spinnerSpecialite.getSelectedView();
        errorText.setTextColor(Color.RED);
    }

    @Override
    public void alertOffSpinners() {
        TextView errorText = (TextView)spinnerSpecialite.getSelectedView();
        errorText.setTextColor(Color.BLACK);
    }

    @Override
    public void saveToDb(TextView... args) {
        Specialite specialite = (Specialite) Specialite.find(Specialite.class,"name = ?", spinnerSpecialite.getSelectedItem().toString()).get(0);
        if (medecinAModif ==null) {
            medecin = new Medecin(args[0].getText().toString(), args[1].getText().toString(),specialite, args[2].getText().toString(), args[3].getText().toString());
            medecin.setId(medecin.save());
        } else {
            if (medecinAModif.getId()!=null) {
                medecin = (Medecin.find(Medecin.class, "id = ?", medecinAModif.getId().toString())).get(0);
            } else {
                medecin = new Medecin();
            }
            medecin.setName(args[0].getText().toString());
            medecin.setDetail(args[1].getText().toString());
            medecin.setTelephone(args[2].getText().toString());
            medecin.setEmail(args[3].getText().toString());
            medecin.setSpecialite(specialite);
            medecin.setId(medecin.save());
        }
    }

    @Override
    public void saveToDb(TextView textNom, Date date, String sexe) {
    }

    @Override
    public void createSpinners() {
        List<Specialite> listAllSpecialite = Specialite.listAll(Specialite.class,"name");
        List<String> listSpecialiteName = new ArrayList<String>();
        listSpecialiteName.add("sélectionner");
        for (Specialite specialite : listAllSpecialite) {
            listSpecialiteName.add(specialite.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpecialiteName) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0) {
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialite.setAdapter(adapter);
    }
}
