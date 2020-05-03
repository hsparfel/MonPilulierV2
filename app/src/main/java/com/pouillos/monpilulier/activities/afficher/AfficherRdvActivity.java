package com.pouillos.monpilulier.activities.afficher;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.add.AddRdvActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AfficherRdvActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;
    @State
    Rdv rdvSelected;

    List<Rdv> listRdvBD;
    ArrayAdapter adapter;

    @BindView(R.id.selectRdv)
    AutoCompleteTextView selectedRdv;
    @BindView(R.id.listRdv)
    TextInputLayout listRdv;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_rdv);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            activeUser = listUserActif.get(0);
        }

        AfficherRdvActivity.AsyncTaskRunner runner = new AfficherRdvActivity.AsyncTaskRunner();
        runner.execute();

        setTitle("Mes Rdv");

        selectedRdv.setOnItemClickListener(this);
    }





    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public class AsyncTaskRunner extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {


            String requete = "";

            listRdvBD = Rdv.find(Rdv.class,"utilisateur = ?",""+activeUser.getId());



            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            if (listRdvBD.size() == 0) {
                Toast toast = Toast.makeText(AfficherRdvActivity.this, "Aucune correspondance, modifier puis appliquer filtre", Toast.LENGTH_LONG);
                toast.show();
            } else {
                List<String> listRdvString = new ArrayList<>();
                String[] listDeroulanteRdv = new String[listRdvBD.size()];
                for (Rdv rdv : listRdvBD) {
                    String rdvString = DateUtils.ecrireDateHeure(rdv.getDate()) + " - ";
                    if (!rdv.getContact().getCodeCivilite().equalsIgnoreCase("")) {
                        rdvString += rdv.getContact().getCodeCivilite() + " ";
                    }
                    rdvString += rdv.getContact().getNom();
                    listRdvString.add(rdvString);
                }
                listRdvString.toArray(listDeroulanteRdv);
                adapter = new ArrayAdapter(AfficherRdvActivity.this, R.layout.list_item, listDeroulanteRdv);
                selectedRdv.setAdapter(adapter);

                traiterIntent();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        Date date = new Date();
        String dateRdv = selectedRdv.getText().toString().substring(0, 16);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        try{
            date = df.parse(dateRdv);
        }catch(ParseException e){
            System.out.println("ERROR");
        }
        rdvSelected = Rdv.find(Rdv.class, "date = ?", "" + date.getTime()).get(0);


        Toast.makeText(AfficherRdvActivity.this, "Selected Item is: \t" + DateUtils.ecrireDateHeure(rdvSelected.getDate()) + " - "+ rdvSelected.getContact(), Toast.LENGTH_LONG).show();
        //lancer l'activite avec xtra
        //ouvrirActiviteSuivante(AddRdvActivity.class,"rdvId",rdvSelected.getId());
        ouvrirActiviteSuivante(AfficherRdvActivity.this, AddRdvActivity.class,"rdvId",rdvSelected.getId());
    }

    /*@Override
    public void ouvrirActiviteSuivante(Class classe){
        Intent intent = new Intent(AfficherRdvActivity.this, classe);
        startActivity(intent);
        finish();
    }

    @Override
    public void ouvrirActiviteSuivante(Class classe, String nomExtra, Long objetIdExtra ) {
        Intent intent = new Intent(AfficherRdvActivity.this, classe);
        intent.putExtra(nomExtra, objetIdExtra);
        startActivity(intent);
        finish();
    }*/
}

