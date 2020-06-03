package com.pouillos.monpilulier.activities.add;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Medicament;
import com.pouillos.monpilulier.entities.MedicamentLight;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.entities.Rappel;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.Duree;
import com.pouillos.monpilulier.enumeration.Frequence;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterPrescription;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRappel;
import com.pouillos.monpilulier.utils.ItemClickSupport;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import icepick.Icepick;
import icepick.State;

public class AddPrescriptionActivity extends NavDrawerActivity implements Serializable, BasicUtils, RecyclerAdapterRappel.Listener {
    @State
    Utilisateur activeUser;
    @State
    Date date;
    @State
    Medicament medicamentSelected;
    @State
    Ordonnance ordonnance;
    @State
    Prescription prescription;

    //pr eviter erreur compile ene tattendan de creer le champ
    //EditText textDate;

    @BindView(R.id.textMedicament)
    AutoCompleteTextView selectedMedicament;
    @BindView(R.id.layoutMedicament)
    TextInputLayout listMedicament;

    @BindView(R.id.layoutFrequence)
    TextInputLayout layoutFrequence;
    @BindView(R.id.textFrequence)
    TextInputEditText textFrequence;

    @BindView(R.id.layoutDuree)
    TextInputLayout layoutDuree;
    @BindView(R.id.textDuree)
    TextInputEditText textDuree;

    List<MedicamentLight> listMedicamentLightBD;
    private List<Rappel> listRappelBD;

    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    @BindView(R.id.textDate)
    TextInputEditText textDate;


    public Frequence frequence;
    public Duree duree;

  //  @BindView(R.id.fragment_list_frequence)
   // Fragment fragment_list_frequence;
    Fragment fragmentListFrequence;
    Fragment fragmentListDuree;

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;
    @BindView(R.id.fabAddRappel)
    ExtendedFloatingActionButton fabAddRappel;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.rbWhenNeeded)
    RadioButton rbWhenNeeded;
    @BindView(R.id.rbEveryDay)
    RadioButton rbEveryDay;
    @BindView(R.id.rbEveryDayByHour)
    RadioButton rbEveryDayByHour;
    @BindView(R.id.rbEveryXDays)
    RadioButton rbEveryXDays;
    @BindView(R.id.rbChosenDays)
    RadioButton rbChosenDays;


    @BindView(R.id.rbNoEnding)
    RadioButton rbNoEnding;

    @BindView(R.id.rbUntilDate)
    RadioButton rbUntilDate;

    @BindView(R.id.rbDuringDays)
    RadioButton rbDuringDays;

    @BindView(R.id.chipLundi)
    Chip chipLundi;
    @BindView(R.id.chipMardi)
    Chip chipMardi;
    @BindView(R.id.chipMercredi)
    Chip chipMercredi;
    @BindView(R.id.chipJeudi)
    Chip chipJeudi;
    @BindView(R.id.chipVendredi)
    Chip chipVendredi;
    @BindView(R.id.chipSamedi)
    Chip chipSamedi;
    @BindView(R.id.chipDimanche)
    Chip chipDimanche;
    @BindView(R.id.chipGroupJour)
    ChipGroup chipGroupJour;

    @BindView(R.id.numberPickerFrequence)
    com.shawnlin.numberpicker.NumberPicker numberPickerFrequence;

    @BindView(R.id.numberPickerDuree)
    com.shawnlin.numberpicker.NumberPicker numberPickerDuree;

    @BindView(R.id.preFreq)
    TextView preFreq;
    @BindView(R.id.preDuree)
    TextView preDuree;
    @BindView(R.id.postFreq)
    TextView postFreq;
    @BindView(R.id.postDuree)
    TextView postDuree;

    @BindView(R.id.layoutFrequenceOption)
    LinearLayout layoutFrequenceOption;
    @BindView(R.id.layoutDureeOption)
    LinearLayout layoutDureeOption;

    @BindView(R.id.listRappel)
    RecyclerView listRappel;

    private RecyclerAdapterRappel adapter;

    @Override
    protected void onResume() {
        super.onResume();
        //SharedPreferences preferences=getSharedPreferences("prescription",MODE_PRIVATE);
        //int prescriptionId = preferences.getInt("registration_id", 0);
        if (prescription != null) {
            AddPrescriptionActivity.AsyncTaskRunnerBDRappel runnerBDRappel = new AddPrescriptionActivity.AsyncTaskRunnerBDRappel();
            runnerBDRappel.execute();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);
        fragmentListFrequence = (Fragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_list_frequence);
        fragmentListDuree = (Fragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_list_duree);
        fragmentListFrequence.getView().setBackgroundColor(Color.LTGRAY);
        fragmentListDuree.getView().setBackgroundColor(Color.LTGRAY);

        progressBar.setVisibility(View.VISIBLE);

        AddPrescriptionActivity.AsyncTaskRunnerBDMedicament runnerBDMedicament = new AddPrescriptionActivity.AsyncTaskRunnerBDMedicament();
        runnerBDMedicament.execute();
        fabAddRappel.hide();
        hideAll(true);
        updateDisplay();

        setTitle("+ Prescription");
        listMedicament.setEnabled(false);
     //   hideAll(true);
        traiterIntent();
        //selectedContact.setOnItemClickListener(this);
        selectedMedicament.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selection = (String) adapterView.getItemAtPosition(position);
                List<MedicamentLight> listMedicamentLight = MedicamentLight.find(MedicamentLight.class,"denomination = ?", selection);
                MedicamentLight medicamentLight = null;
                if (listMedicamentLight.size() > 0) {
                    medicamentLight = listMedicamentLight.get(0);
                }
                medicamentSelected = Medicament.findById(Medicament.class,medicamentLight.getId());
                hideAll(true);
                updateDisplay();
            }
        });
        numberPickerFrequence.setOnValueChangedListener(onValueChangeListenerFrequence);
        numberPickerDuree.setOnValueChangedListener(onValueChangeListenerDuree);
    }

    public void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("ordonnanceId")) {
            Long ordonnanceId = intent.getLongExtra("ordonnanceId", 0);
            ordonnance = Ordonnance.findById(Ordonnance.class, ordonnanceId);
        }
    }

    com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener onValueChangeListenerFrequence =
            new 	com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddPrescriptionActivity.this,"selected number freq"+numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            };

    com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener onValueChangeListenerDuree =
            new 	com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddPrescriptionActivity.this,"selected number duree"+numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                    date = DateUtils.ajouterJour(ordonnance.getDate(),numberPicker.getValue());
                }
            };


    public void onRGFrequenceClick(View view){

        if (rbWhenNeeded.isChecked()) {
            frequence = Frequence.WhenNeeded;
            preFreq.setText("");
            postFreq.setText("");
        } else if (rbEveryDay.isChecked()) {
            frequence = Frequence.EveryDay;
            preFreq.setText("");
            postFreq.setText("");
        } else if (rbEveryDayByHour.isChecked()) {
            frequence = Frequence.EveryDayByHour;
            preFreq.setText("toutes les");
            postFreq.setText("heures");
        } else if (rbEveryXDays.isChecked()) {
            frequence = Frequence.EveryXDays;
            preFreq.setText("tous les");
            postFreq.setText("jours");
        } else if (rbChosenDays.isChecked()) {
            frequence = Frequence.ChosenDays;
            preFreq.setText("");
            preFreq.setText("");
        }
        textFrequence.setText(frequence.toString());
        fragmentListFrequence.getView().setVisibility(View.GONE);
        //updateDisplay();
    }

    public void onRGDureeClick(View view){
        date = null;
        textDate.setText("");
        if (rbNoEnding.isChecked()) {
            duree = Duree.NoEnding;
            preDuree.setText("");
            postDuree.setText("");
        } else if (rbUntilDate.isChecked()) {
            duree = Duree.UntilDate;
            preDuree.setText("");
            postDuree.setText("");
        } else if (rbDuringDays.isChecked()) {
            duree = Duree.DuringDays;
            preDuree.setText("pendant");
            postDuree.setText("jours");
        }
        textDuree.setText(duree.toString());
        fragmentListDuree.getView().setVisibility(View.GONE);
        //updateDisplay();
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {
        saveToDb();
        //revenirActivitePrecedente("prescription","id",prescription.getId());
        finish();
    }

    @OnClick(R.id.fabAddRappel)
    public void fabAddRappelClick() {
        saveToDb();
        //revenirActivitePrecedente("prescription","id",prescription.getId());
        ouvrirActiviteSuivante(AddPrescriptionActivity.this,AddRappelActivity.class,"prescriptionId", prescription.getId(),false);
    }

    @OnClick(R.id.textFrequence)
    public void textFrequenceClick() {
        fragmentListFrequence.getView().setVisibility(View.VISIBLE);
    }

    @OnTextChanged(R.id.textFrequence)
    public void textFrequenceChanged() {
        hideAll(true);
        updateDisplay();
    }

    @OnClick(R.id.textDuree)
    public void textDureeClick() {
        fragmentListDuree.getView().setVisibility(View.VISIBLE);
    }

    @OnTextChanged(R.id.textDuree)
    public void textDureeChanged() {
        hideAll(true);
        updateDisplay();
    }


    private void hideAll(boolean bool){
        //fabAddRappel.hide();
        fabSave.hide();
        layoutFrequence.setVisibility(View.GONE);
        fragmentListFrequence.getView().setVisibility(View.GONE);
        chipGroupJour.setVisibility(View.GONE);
        layoutFrequenceOption.setVisibility(View.GONE);
        layoutDuree.setVisibility(View.GONE);
        fragmentListDuree.getView().setVisibility(View.GONE);
        layoutDate.setVisibility(View.GONE);
        layoutDureeOption.setVisibility(View.GONE);


    }

    public void updateDisplay() {
       // hideAll(true);
        if (medicamentSelected != null) {
            layoutFrequence.setVisibility(View.VISIBLE);
            if (frequence != null) {
                if (frequence == Frequence.WhenNeeded) {
                    fabSave.show();
                } else {
                    layoutDuree.setVisibility(View.VISIBLE);
                }
                if (duree != null) {
                    if (duree == Duree.NoEnding) {
                        date = DateUtils.ajouterAnnee(ordonnance.getDate(),3);
                    } else if (duree == Duree.UntilDate) {
                        layoutDate.setVisibility(View.VISIBLE);
                    } else if (duree == Duree.DuringDays) {
                        layoutDureeOption.setVisibility(View.VISIBLE);

                        date = DateUtils.ajouterJour(ordonnance.getDate(),1);
                    }
                    if (date != null) {
                        fabAddRappel.show();
                        if (adapter != null && adapter.getItemCount() > 0) {
                            fabSave.show();
                        }
                    } else {
                        fabAddRappel.hide();
                    }

                }

            }
        }
       /* if (frequence != null && frequence != Frequence.WhenNeeded && frequence != Frequence.EveryDayByHour) {
            if (duree != null && duree == Duree.NoEnding) {
                fabAddRappel.show();
                layoutDate.setVisibility(View.GONE);
                layoutDureeOption.setVisibility(View.GONE);
            } else {
                fabAddRappel.hide();
                if (duree == Duree.UntilDate) {
                    layoutDate.setVisibility(View.VISIBLE);
                    layoutDureeOption.setVisibility(View.GONE);
                    if (textDate.getText() != null && !textDate.getText().toString().equalsIgnoreCase("")) {
                        fabAddRappel.show();
                    }
                } else if (duree == Duree.DuringDays) {
                    layoutDate.setVisibility(View.GONE);
                    layoutDureeOption.setVisibility(View.VISIBLE);
                    fabAddRappel.show();
                } else {
                    layoutDate.setVisibility(View.GONE);
                    layoutDureeOption.setVisibility(View.GONE);
                }
            }
        } else {
            fabAddRappel.hide();
        }
        if (frequence == Frequence.EveryDayByHour || frequence == Frequence.EveryXDays) {
            layoutFrequenceOption.setVisibility(View.VISIBLE);
        } else {
            layoutFrequenceOption.setVisibility(View.GONE);
        }
        if (frequence == Frequence.ChosenDays) {
            chipGroupJour.setVisibility(View.VISIBLE);
        } else {
            chipGroupJour.setVisibility(View.GONE);
        }
        if (adapter != null && adapter.getItemCount() > 0) {
            fabSave.show();
        }*/
    }

    @Override
    public void onClickDeleteButton(int position) {
        Rappel rappel = adapter.getRappel(position);
        Toast.makeText(AddPrescriptionActivity.this, "You are trying to delete user : "+ rappel.getHeure()+" - "+rappel.getQuantiteDose()+ " " + rappel.getDose().getName(), Toast.LENGTH_SHORT).show();
        rappel.delete();


        listRappelBD.remove(position);
        adapter.notifyItemRemoved(position);
       // adapter.notifyDataSetChanged();
    }

    public class AsyncTaskRunnerBDMedicament extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(50);

            listMedicamentLightBD = MedicamentLight.listAll(MedicamentLight.class);

            Collections.sort(listMedicamentLightBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            buildDropdownMenu(listMedicamentLightBD, AddPrescriptionActivity.this,selectedMedicament);
            listMedicament.setEnabled(true);
            if (prescription != null) {
                AddPrescriptionActivity.AsyncTaskRunnerBDRappel runnerBDRappel = new AddPrescriptionActivity.AsyncTaskRunnerBDRappel();
                runnerBDRappel.execute();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @Override
    public void saveToDb() {
        if (prescription == null) {
            prescription = new Prescription();
        }

        prescription.setMedicament(medicamentSelected);
        prescription.setDuree(duree);
        prescription.setFrequence(frequence);
        prescription.setOrdonnance(ordonnance);
        if (frequence != null && frequence != Frequence.WhenNeeded) {
            prescription.setFrequenceOption(numberPickerFrequence.getValue());
        }
        if (duree != null) {
            prescription.setDureeOption(numberPickerDuree.getValue());
        }
        prescription.setDateFin(date);


        prescription.setId(prescription.save());
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
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        //newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.show(getSupportFragmentManager(), "editTexteDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMinDate(new Date().getTime());
               // TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                Calendar c1 = Calendar.getInstance();
                // set Month
                // MONTH starts with 0 i.e. ( 0 - Jan)
                c1.set(Calendar.MONTH, datePicker.getMonth());
                // set Date
                c1.set(Calendar.DATE, datePicker.getDayOfMonth());
                // set Year
                c1.set(Calendar.YEAR, datePicker.getYear());
                // creating a date object with specified time.
                date = c1.getTime();

                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                //tv1.setText("date: "+dateString);
                textDate.setText(dateString);
                textDate.setError(null);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    date = df.parse(dateString);
                    hideAll(true);
                    updateDisplay();
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
            }
        });
    }

    public class AsyncTaskRunnerBDRappel extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            //activeUser = findActiveUser();
            publishProgress(50);
            listRappelBD = Rappel.find(Rappel.class,"prescription = ?", prescription.getId().toString());
            //listContactBD = Contact.findWithQuery(Contact.class, requete);
            Collections.sort(listRappelBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            //buildDropdownMenu(listContactBD, AddOrdonnanceActivity.this,selectedContact);
            //todo alimenter la listview
            //this.githubUsers = new ArrayList<>();
            // 3.2 - Create adapter passing the list of users
            configureRecyclerView();
            configureOnClickRecyclerView();
            hideAll(true);
            updateDisplay();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(listRappel, R.layout.recycler_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                    }
                });
    }

    public void configureRecyclerView() {
        adapter = new RecyclerAdapterRappel(listRappelBD, this);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        listRappel.setAdapter(adapter);
        // 3.4 - Set layout manager to position the items
        //this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listRappel.setLayoutManager(new LinearLayoutManager(this));
    }
}
