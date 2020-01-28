package com.pouillos.monpilulier.activities.newx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.Dose;
import com.pouillos.monpilulier.entities.Duree;
import com.pouillos.monpilulier.entities.Medicament;

import com.pouillos.monpilulier.entities.OrdoPrescription;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrdoPrescriptionActivity extends AppCompatActivity implements BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private Spinner spinnerMedicament;
    private ImageButton buttonAddMedicament;
    private Spinner spinnerNbDose;
    private Spinner spinnerDose;
    private ImageButton buttonAddDose;
    private Spinner spinnerNbFrequence;
    private Spinner spinnerFrequence;
    private ImageButton buttonAddFrequence;

    private CheckBox checkBoxMatin;
    private CheckBox checkBoxMidi;
    private CheckBox checkBoxSoir;

    private RadioGroup radioGroupRepas;
    private RadioButton radioButtonRepasAvant;
    private RadioButton radioButtonRepasPendant;
    private RadioButton radioButtonRepasApres;

    private Spinner spinnerNbDuree;
    private Spinner spinnerDuree;
    private ImageButton buttonAddDuree;
    private TextView textDate;
    private Date date;

    private TextView textDescription;


    private Class<?> activitySource;
    private OrdoPrescription ordoPrescriptionAModif;
    private OrdoPrescription ordoPrescription;
    private Intent intent;
    private Medicament medicament;
    private Ordonnance ordonnanceSauvegarde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ordo_prescription);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        spinnerMedicament = (Spinner) findViewById(R.id.spinnerMedicament);
        buttonAddMedicament = (ImageButton) findViewById(R.id.buttonAddMedicament);
        spinnerNbDose = (Spinner) findViewById(R.id.spinnerNbDose);
        spinnerDose = (Spinner) findViewById(R.id.spinnerDose);

        buttonAddDose = (ImageButton) findViewById(R.id.buttonAddDose);
        spinnerNbFrequence = (Spinner) findViewById(R.id.spinnerNbFrequence);
        spinnerFrequence = (Spinner) findViewById(R.id.spinnerFrequence);
        buttonAddFrequence = (ImageButton) findViewById(R.id.buttonAddFrequence);

        checkBoxMatin = (CheckBox) findViewById(R.id.checkBoxMatin);
        checkBoxMidi = (CheckBox) findViewById(R.id.checkBoxMidi);
        checkBoxSoir = (CheckBox) findViewById(R.id.checkBoxSoir);
        radioGroupRepas = (RadioGroup) findViewById(R.id.radioGroupRepas);
        radioButtonRepasAvant = (RadioButton) findViewById(R.id.radioButtonRepasAvant);
        radioButtonRepasPendant = (RadioButton) findViewById(R.id.radioButtonRepasPendant);
        radioButtonRepasApres = (RadioButton) findViewById(R.id.radioButtonRepasApres);
        spinnerNbDuree = (Spinner) findViewById(R.id.spinnerNbDuree);
        spinnerDuree = (Spinner) findViewById(R.id.spinnerDuree);
        buttonAddDuree = (ImageButton) findViewById(R.id.buttonAddDuree);
        textDate = findViewById(R.id.textDate);

        textDescription = findViewById(R.id.textDescription);


        createSpinners();

        traiterIntent();

        buttonAddMedicament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityAddMedicament();
            }
        });

        buttonAddDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityAddDose();
            }
        });

        buttonAddFrequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityAddFrequence();
            }
        });

        buttonAddDuree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityAddDuree();
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

                //TODO rajouter obligation description

                //enregistrer en bdd
                saveToDb(textDescription);

                //retour
                retourPagePrecedente();
            }
        });
    }



    @Override
    public void traiterIntent() {
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("ordonnanceSauvegardeId")) {
            Long ordonnanceSauvegardeId = intent.getLongExtra("ordonnanceSauvegardeId", 0);
            ordonnanceSauvegarde = Ordonnance.findById(Ordonnance.class, ordonnanceSauvegardeId);
            textDate.setText(DateUtils.ecrireDate(ordonnanceSauvegarde.getDate()));
            date = ordonnanceSauvegarde.getDate();
        }
        if (intent.hasExtra("ordoPrescriptionAModifId")) {
            Long ordoPrescriptionAModifId = intent.getLongExtra("ordoPrescriptionAModifId", 0);
            ordoPrescriptionAModif = OrdoPrescription.findById(OrdoPrescription.class, ordoPrescriptionAModifId);
            ordonnanceSauvegarde = Ordonnance.findById(Ordonnance.class, ordoPrescriptionAModif.getOrdonnance().getId());
            textDescription.setText(ordoPrescriptionAModif.getDetail());

            spinnerMedicament.setSelection(getIndex(spinnerMedicament, ordoPrescriptionAModif.getMedicament().getName()));
        }
    }

    @Override
    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewOrdoPrescriptionActivity.this, NewOrdonnanceActivity.class);
        nextActivity.putExtra("activitySource", NewOrdoPrescriptionActivity.class);
        nextActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void saveToDb(TextView... args) {
        medicament = (Medicament) Medicament.find(Medicament.class,"name = ?", spinnerMedicament.getSelectedItem().toString()).get(0);

        if (ordoPrescriptionAModif != null) {

            ordoPrescriptionAModif.setMedicament(medicament);
            ordoPrescriptionAModif.setDetail(textDescription.getText().toString());
            ordoPrescriptionAModif.save();
        } else {
            ordoPrescription = new OrdoPrescription();

            ordoPrescription.setMedicament(medicament);
            ordoPrescription.setDetail(textDescription.getText().toString());
            ordoPrescription.setOrdonnance(ordonnanceSauvegarde);
            ordoPrescription.setId(ordoPrescription.save());
        }
    }



    @Override
    public void createSpinners() {
        //Medicament
        List<Medicament> listAllMedicament = Medicament.listAll(Medicament.class,"name");
        List<String> listMedicamentName = new ArrayList<String>();
        listMedicamentName.add("sélectionner");
        for (Medicament medicament : listAllMedicament) {
            listMedicamentName.add(medicament.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMedicamentName) {
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
        spinnerMedicament.setAdapter(adapter);

        //Nb Dose
        //List<Medicament> listAllMedicament = Medicament.listAll(Medicament.class,"name");
        List<String> listNbDoseName = new ArrayList<String>();
        listNbDoseName.add("sélectionner");
        listNbDoseName.add("0,5");
        listNbDoseName.add("1");
        listNbDoseName.add("1,5");
        listNbDoseName.add("2");
        listNbDoseName.add("2,5");
        listNbDoseName.add("3");
        listNbDoseName.add("3,5");
        listNbDoseName.add("4");
        listNbDoseName.add("4,5");
        listNbDoseName.add("5");
        listNbDoseName.add("5,5");
        listNbDoseName.add("6");
        listNbDoseName.add("6,5");
        listNbDoseName.add("7");
        listNbDoseName.add("7,5");
        listNbDoseName.add("8");
        listNbDoseName.add("8,5");
        listNbDoseName.add("9");
        listNbDoseName.add("9,5");
        listNbDoseName.add("10");

        //for (Medicament medicament : listAllMedicament) {
          //  listMedicamentName.add(medicament.getName());
        //}
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listNbDoseName) {
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
        spinnerNbDose.setAdapter(adapter);

        //Dose
        List<Dose> listAllDose = Dose.listAll(Dose.class,"name");
        List<String> listDoseName = new ArrayList<String>();
        listDoseName.add("sélectionner");
        for (Dose dose : listAllDose) {
            listDoseName.add(dose.getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDoseName) {
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
        spinnerDose.setAdapter(adapter);

        //Nb Frequence
        //List<Medicament> listAllMedicament = Medicament.listAll(Medicament.class,"name");
        List<String> listNbFrequenceName = new ArrayList<String>();
        listNbFrequenceName.add("sélectionner");
        listNbFrequenceName.add("1");
        listNbFrequenceName.add("2");
        listNbFrequenceName.add("3");
        listNbFrequenceName.add("4");
        listNbFrequenceName.add("5");
        listNbFrequenceName.add("6");
        listNbFrequenceName.add("7");
        listNbFrequenceName.add("8");
        listNbFrequenceName.add("9");
        listNbFrequenceName.add("10");

        //for (Medicament medicament : listAllMedicament) {
          //  listMedicamentName.add(medicament.getName());
        //}
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listNbFrequenceName) {
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
        spinnerNbFrequence.setAdapter(adapter);

        //Frequence
        List<Duree> listAllFrequence = Duree.listAll(Duree.class);
        List<String> listFrequenceName = new ArrayList<String>();
        listFrequenceName.add("sélectionner");
        for (Duree frequence : listAllFrequence) {
            listFrequenceName.add(frequence.getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFrequenceName) {
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
        spinnerFrequence.setAdapter(adapter);

        //Nb Duree
        //List<Medicament> listAllMedicament = Medicament.listAll(Medicament.class,"name");
        List<String> listNbDureeName = new ArrayList<String>();
        listNbDureeName.add("sélectionner");
        listNbDureeName.add("1");
        listNbDureeName.add("2");
        listNbDureeName.add("3");
        listNbDureeName.add("4");
        listNbDureeName.add("5");
        listNbDureeName.add("6");
        listNbDureeName.add("7");
        listNbDureeName.add("8");
        listNbDureeName.add("9");
        listNbDureeName.add("10");
        //for (Medicament medicament : listAllMedicament) {
         //   listMedicamentName.add(medicament.getName());
        //}
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listNbDureeName) {
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
        spinnerNbDuree.setAdapter(adapter);

        //Duree
        List<Duree> listAllDuree = Duree.listAll(Duree.class);
        List<String> listDureeName = new ArrayList<String>();
        listDureeName.add("sélectionner");
        for (Duree duree : listAllDuree) {
            listDureeName.add(duree.getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDureeName) {
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
        spinnerDuree.setAdapter(adapter);




    }

    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMinDate(date.getTime());
                TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                tv1.setText("date: "+dateString);
                textDate.setError(null);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    date = df.parse(dateString);
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
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
}
