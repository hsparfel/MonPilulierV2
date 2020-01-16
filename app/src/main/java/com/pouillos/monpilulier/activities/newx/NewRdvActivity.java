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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdonnanceActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewRdvActivity extends AppCompatActivity {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddMedecin;
    private ImageButton buttonAddCabinet;
    private ImageButton buttonAddAnalyse;
    private ImageButton buttonAddExamen;
    private TextView textDescription;
    private Spinner spinnerMedecin;
    private Spinner spinnerCabinet;
    private Spinner spinnerAnalyse;
    private Spinner spinnerExamen;
    private Medecin medecin;
    private Cabinet cabinet;
    private Analyse analyse;
    private Examen examen;
    private TextView textDate;
    private Date date;
    private Class<?> activitySource;
    private Rdv rdvAModif;
    private Intent intent;
    private Rdv rdv;
    private Utilisateur utilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rdv);

        utilisateur = (new Utilisateur()).findActifUser();
        buttonAddMedecin = (ImageButton) findViewById(R.id.buttonAddMedecin);
        buttonAddCabinet = (ImageButton) findViewById(R.id.buttonAddCabinet);
        buttonAddAnalyse = (ImageButton) findViewById(R.id.buttonAddAnalyse);
        buttonAddExamen = (ImageButton) findViewById(R.id.buttonAddExamen);
        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);

        textDescription = findViewById(R.id.textDescription);
        spinnerMedecin = (Spinner) findViewById(R.id.spinnerMedecin);
        spinnerCabinet = (Spinner) findViewById(R.id.spinnerCabinet);
        spinnerAnalyse = (Spinner) findViewById(R.id.spinnerAnalyse);
        spinnerExamen = (Spinner) findViewById(R.id.spinnerExamen);

        textDate = findViewById(R.id.textDate);

        createSpinnerMedecin();
        createSpinnerCabinet();
        createSpinnerAnalyse();
        createSpinnerExamen();
        traiterIntent();

        //spinnerMedecin.setOnItemClickListener(new View.OnClickListener());

        spinnerMedecin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alertOffSpinners();
                return false;
            }
        });

        spinnerCabinet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alertOffSpinners();
                return false;
            }
        });

        spinnerAnalyse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alertOffSpinners();
                return false;
            }
        });

        spinnerExamen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alertOffSpinners();
                return false;
            }
        });


        buttonAddMedecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAddMedecin();
            }
        });

        buttonAddCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAddCabinet();
            }
        });

        buttonAddAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAddAnalyse();
            }
        });

        buttonAddExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAddExamen();
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
                if (!isRempli(textDate, date)) {
                    return;
                }
                if (!isRempli(spinnerMedecin, spinnerCabinet, spinnerAnalyse, spinnerExamen)){
                    return;
                }

                //enregistrer en bdd
                saveToDb(textDescription, textDate);

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
        if (intent.hasExtra("rdvAModifId")) {

            Long rdvAModifId = intent.getLongExtra("rdvAModifId",0);
            rdvAModif = Rdv.findById(Rdv.class,rdvAModifId);

            textDescription.setText(rdvAModif.getDetail());
            textDate.setText(DateUtils.ecrireDate(rdvAModif.getDate()));
            date = rdvAModif.getDate();
            if (rdvAModif.getMedecin()!=null) {
                spinnerMedecin.setSelection(getIndex(spinnerMedecin, rdvAModif.getMedecin().getName()));
            }
            if (rdvAModif.getCabinet()!=null) {
            spinnerCabinet.setSelection(getIndex(spinnerCabinet, rdvAModif.getCabinet().getName()));
            }
            if (rdvAModif.getAnalyse()!=null) {
            spinnerAnalyse.setSelection(getIndex(spinnerAnalyse, rdvAModif.getAnalyse().getName()));
            }
            if (rdvAModif.getExamen()!=null) {
            spinnerExamen.setSelection(getIndex(spinnerExamen, rdvAModif.getExamen().getName()));
            }
        } else {
            rdvAModif = new Rdv();}
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewRdvActivity.this, activitySource);
        nextActivity.putExtra("activitySource", NewRdvActivity.class);
        //nextActivity.putExtra("rdvEnCours", rdvEnCours);
        startActivity(nextActivity);
        finish();
    }

    /*public void retourPagePrecedenteAnnuler(Intent intent) {
        Intent nextActivity = new Intent(NewRdvActivity.this,activitySource);
        nextActivity.putExtra("rdvAModif", rdv);
        if (intent.hasExtra("ordonnanceToUpdate")) {
         //   ordonnanceToUpdate = (Ordonnance) intent.getSerializableExtra("ordonnanceToUpdate");
          //  nextActivity.putExtra("ordonnanceToUpdate", ordonnanceToUpdate);
            nextActivity.putExtra("activitySource", ListAllOrdonnanceActivity.class);
        } else {
            nextActivity.putExtra("activitySource", NewRdvActivity.class);
        }
        startActivity(nextActivity);
        finish();
    }

    public void retourPagePrecedenteValider(Intent intent) {
        Intent nextActivity = new Intent(NewRdvActivity.this,activitySource);
        nextActivity.putExtra("rdvAModif", rdv);

        startActivity(nextActivity);
        finish();
    }*/

    public void ouvrirActivityAddMedecin() {

    }

    public void ouvrirActivityAddCabinet() {

    }

    public void ouvrirActivityAddAnalyse() {

    }

    public void ouvrirActivityAddExamen() {

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

    public boolean isRempli(TextView textView, Date date) {
        if (date == null) {
            textView.setError("Sélection Obligatoire");
            return false;
        } else {
            return true;
        }
    }

    public boolean isRempli(Spinner spinnerMedecin, Spinner spinnerCabinet, Spinner spinnerAnalyse, Spinner spinnerExamen) {
        if (spinnerMedecin.getSelectedItem().toString().equals("sélectionner")
                && spinnerCabinet.getSelectedItem().toString().equals("sélectionner")
                && spinnerAnalyse.getSelectedItem().toString().equals("sélectionner")
                && spinnerExamen.getSelectedItem().toString().equals("sélectionner")) {

            alertOnSpinners();
            return false;
        } else {
            return true;
        }
    }

    public void saveToDb(TextView textDescription, TextView textDate) {
        List<Medecin> listMedecin = Medecin.find(Medecin.class, "name = ?", spinnerMedecin.getSelectedItem().toString());
        if (listMedecin.size() !=0) {
            medecin = listMedecin.get(0);
        }
        List<Cabinet> listCabinet = Cabinet.find(Cabinet.class, "name = ?", spinnerCabinet.getSelectedItem().toString());
        if (listCabinet.size() !=0) {
            cabinet = listCabinet.get(0);
        }

        List<Analyse> listAnalyse = Analyse.find(Analyse.class, "name = ?", spinnerAnalyse.getSelectedItem().toString());
        if (listAnalyse.size() !=0) {
            analyse = listAnalyse.get(0);
        }

        List<Examen> listExamen = Examen.find(Examen.class, "name = ?", spinnerExamen.getSelectedItem().toString());
        if (listExamen.size() !=0) {
            examen = listExamen.get(0);
        }

        if (rdvAModif ==null) {
            rdv = new Rdv(textDescription.getText().toString(),utilisateur, medecin, analyse, examen, cabinet, date);
            rdv.setId(rdv.save());
        } else {
            if (rdvAModif.getId()!=null) {
                rdv = (Rdv.find(Rdv.class, "id = ?", rdvAModif.getId().toString())).get(0);
            } else {
                rdv = new Rdv();
            }
            rdv.setDetail(textDescription.getText().toString());
            rdv.setMedecin(medecin);
            rdv.setDate(date);
            rdv.setCabinet(cabinet);
            rdv.setExamen(examen);
            rdv.setAnalyse(analyse);
            rdv.setUtilisateur(utilisateur);
            rdv.setId(rdv.save());
        }
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

    public void createSpinnerMedecin() {
        List<Medecin> listAllMedecin = Medecin.listAll(Medecin.class,"name");
        List<String> listMedecinName = new ArrayList<String>();
        listMedecinName.add("sélectionner");
        for (Medecin medecin : listAllMedecin) {
            listMedecinName.add(medecin.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMedecinName) {
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
        spinnerMedecin.setAdapter(adapter);
    }

    public void createSpinnerCabinet() {
        List<Cabinet> listAllCabinet = Cabinet.listAll(Cabinet.class,"name");
        List<String> listCabinetName = new ArrayList<String>();
        listCabinetName.add("sélectionner");
        for (Cabinet cabinet : listAllCabinet) {
            listCabinetName.add(cabinet.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCabinetName) {
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
        spinnerCabinet.setAdapter(adapter);
    }

    public void createSpinnerAnalyse() {
        List<Analyse> listAllAnalyse = Analyse.listAll(Analyse.class,"name");
        List<String> listAnalyseName = new ArrayList<String>();
        listAnalyseName.add("sélectionner");
        for (Analyse analyse : listAllAnalyse) {
            listAnalyseName.add(analyse.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAnalyseName) {
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
        spinnerAnalyse.setAdapter(adapter);
    }

    public void createSpinnerExamen() {
        List<Examen> listAllExamen = Examen.listAll(Examen.class,"name");
        List<String> listExamenName = new ArrayList<String>();
        listExamenName.add("sélectionner");
        for (Examen examen : listAllExamen) {
            listExamenName.add(examen.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listExamenName) {
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
        spinnerExamen.setAdapter(adapter);
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMinDate(new Date().getTime());
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

    private void alertOnSpinners() {
        TextView errorText = (TextView)spinnerMedecin.getSelectedView();
        errorText.setTextColor(Color.RED);
        errorText = (TextView)spinnerCabinet.getSelectedView();
        errorText.setTextColor(Color.RED);
        errorText = (TextView)spinnerAnalyse.getSelectedView();
        errorText.setTextColor(Color.RED);
        errorText = (TextView)spinnerExamen.getSelectedView();
        errorText.setTextColor(Color.RED);
    }

    private void alertOffSpinners() {
        TextView errorText = (TextView)spinnerMedecin.getSelectedView();
        errorText.setTextColor(Color.BLACK);
        errorText = (TextView)spinnerCabinet.getSelectedView();
        errorText.setTextColor(Color.BLACK);
        errorText = (TextView)spinnerAnalyse.getSelectedView();
        errorText.setTextColor(Color.BLACK);
        errorText = (TextView)spinnerExamen.getSelectedView();
        errorText.setTextColor(Color.BLACK);
    }

}
