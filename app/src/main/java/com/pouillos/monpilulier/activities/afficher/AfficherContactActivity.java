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
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

import static java.lang.Math.round;

public class AfficherContactActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;
    @State
    Contact contactSelected;

    List<Contact> listContactsBD;
    ArrayAdapter adapter;

    @BindView(R.id.selectContact)
    AutoCompleteTextView selectedContact;
    @BindView(R.id.listContacts)
    TextInputLayout listContacts;

    @BindView(R.id.layoutName)
    TextInputLayout layoutName;
    @BindView(R.id.textName)
    TextInputEditText textName;

    @BindView(R.id.layoutJob)
    TextInputLayout layoutJob;
    @BindView(R.id.textJob)
    TextInputEditText textJob;

    @BindView(R.id.layoutPlace)
    TextInputLayout layoutPlace;
    @BindView(R.id.textPlace)
    TextInputEditText textPlace;

    @BindView(R.id.layoutComplement)
    TextInputLayout layoutComplement;
    @BindView(R.id.textComplement)
    TextInputEditText textComplement;

    @BindView(R.id.layoutNumStreet)
    TextInputLayout layoutNumStreet;
    @BindView(R.id.textNumStreet)
    TextInputEditText textNumStreet;

    @BindView(R.id.layoutZipTown)
    TextInputLayout layoutZipTown;
    @BindView(R.id.textZipTown)
    TextInputEditText textZipTown;

    @BindView(R.id.layoutPhone)
    TextInputLayout layoutPhone;
    @BindView(R.id.textPhone)
    TextInputEditText textPhone;

    @BindView(R.id.layoutFax)
    TextInputLayout layoutFax;
    @BindView(R.id.textFax)
    TextInputEditText textFax;

    @BindView(R.id.layoutEmail)
    TextInputLayout layoutEmail;
    @BindView(R.id.textEmail)
    TextInputEditText textEmail;

    @BindView(R.id.fabRdv)
    FloatingActionButton fabRdv;
    @BindView(R.id.fabPrescription)
    FloatingActionButton fabPrescription;
    @BindView(R.id.fabGoogleMap)
    FloatingActionButton fabGoogleMap;
    @BindView(R.id.fabWaze)
    FloatingActionButton fabWaze;
    @BindView(R.id.fabEdit)
    FloatingActionButton fabEdit;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_contact);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);
//todo methode masquer les boutons optionnels



        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            activeUser = listUserActif.get(0);
        }

        displayfabs();

        AfficherContactActivity.AsyncTaskRunner runner = new AfficherContactActivity.AsyncTaskRunner();
        runner.execute();

       // ButterKnife.bind(this);
        layoutName.setVisibility(View.GONE);
        layoutJob.setVisibility(View.GONE);
        layoutPlace.setVisibility(View.GONE);
        layoutComplement.setVisibility(View.GONE);
        layoutNumStreet.setVisibility(View.GONE);
        layoutZipTown.setVisibility(View.GONE);
        layoutPhone.setVisibility(View.GONE);
        layoutFax.setVisibility(View.GONE);
        layoutEmail.setVisibility(View.GONE);

        //traiterIntent();
        setTitle("Mes Contacts");
       // toolbar.setTitle("Mes Contacts");
       // getSupportActionBar().setTitle("Mes Contacts");


        selectedContact.setOnItemClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void traiterIntent() {
        Intent intent = getIntent();
        //  activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("contactId")) {

            Long contactId = intent.getLongExtra("contactId", 0);
            //rdvAModif = Rdv.findById(Rdv.class,rdvAModifId);
            contactSelected = Contact.findById(Contact.class, contactId);
            //int position = listContactsBD.indexOf(contactSelected);
            int position = 0;
            //int position2 = 0;
            for (Contact contact : listContactsBD) {
                if (contact.getId().longValue() != contactId) {
                    position ++;
                //}
                //if (contact.getId().longValue() != contactSelected.getId().longValue()) {
                //    position ++;
                } else {
                    break;
                }
                //if (contact.getId().longValue() != contactId) {
                //    position2 ++;
                //}
            }

            //selectedContact.setSelection(position);
            //selectedContact.setListSelection(position);
            //selectedContact.setSelection(position);
            selectedContact.setText(selectedContact.getAdapter().getItem(position).toString(), false);
            //selectedContact.get
            //selectedContact.setSelection(position);
            //selectedContact.setListSelection(position);

            String stringName ="";
            if (!contactSelected.getCodeCivilite().equalsIgnoreCase("")) {
                stringName += contactSelected.getCodeCivilite() + " ";
            }
            if (!contactSelected.getNom().equalsIgnoreCase("")) {
                stringName += contactSelected.getNom() + " ";
            }
            if (!contactSelected.getPrenom().equalsIgnoreCase("")) {
                stringName += contactSelected.getPrenom();
            }
            if (stringName.length()>0) {
                textName.setText(stringName);
                layoutName.setVisibility(View.VISIBLE);
            }

            if (contactSelected.getProfession() != null) {
                textJob.setText(contactSelected.getProfession().getName());
                layoutJob.setVisibility(View.VISIBLE);
            }
            if (contactSelected.getSavoirFaire() != null) {
                textJob.setText(contactSelected.getSavoirFaire().getName());
                layoutJob.setVisibility(View.VISIBLE);
            }

            if (contactSelected.getRaisonSocial() != null) {
                textPlace.setText(contactSelected.getRaisonSocial());
                layoutPlace.setVisibility(View.VISIBLE);
            }

            if (!contactSelected.getComplement().equalsIgnoreCase("")) {
                textComplement.setText(contactSelected.getComplement());
                layoutComplement.setVisibility(View.VISIBLE);
            }

            if (contactSelected.getAdresse() != null) {
                textNumStreet.setText(contactSelected.getAdresse());
                layoutNumStreet.setVisibility(View.VISIBLE);
            }

            String stringCpVille = "";
            if (!contactSelected.getCp().equalsIgnoreCase("")) {
                stringCpVille += contactSelected.getCp() + " ";
            }
            if (!contactSelected.getVille().equalsIgnoreCase("")) {
                stringCpVille += contactSelected.getVille();
            }
            if (stringCpVille.length()>0) {
                textZipTown.setText(stringCpVille);
                layoutZipTown.setVisibility(View.VISIBLE);
            }


            if (contactSelected.getTelephone() != null) {
                textPhone.setText(contactSelected.getTelephone());
                layoutPhone.setVisibility(View.VISIBLE);
            }

            if (contactSelected.getFax() != null) {
                textFax.setText(contactSelected.getFax());
                layoutFax.setVisibility(View.VISIBLE);
            }

            if (contactSelected.getEmail() != null) {
                textEmail.setText(contactSelected.getEmail());
                layoutEmail.setVisibility(View.VISIBLE);
            }
        //todo creer la mehode aff/masquer boutons selon medecin ou adresse
        //todo la dupliquer sur le click item
            displayfabs();
        }
    }

    private void displayfabs() {
        if (contactSelected == null) {
            fabEdit.hide();
            fabGoogleMap.hide();
            fabPrescription.hide();
            fabRdv.hide();
            fabWaze.hide();
        } else {
            fabRdv.show();
            fabEdit.show();
            if (contactSelected.getSavoirFaire() != null) {
                fabPrescription.show();
            } else {
                fabPrescription.hide();
            }
            if (contactSelected.getAdresse() != null && contactSelected.getCp() != null && contactSelected.getVille() != null) {
                fabWaze.show();
                fabGoogleMap.show();
            } else {
                fabWaze.hide();
                fabGoogleMap.hide();
            }
        }
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public class AsyncTaskRunner extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {


            String requete = "";

            listContactsBD = new ArrayList<>();

            requete += "SELECT C.* FROM CONTACT AS C INNER JOIN ASSOCIATION_UTILISATEUR_CONTACT AS AUC ON C.ID = AUC.CONTACT WHERE AUC.UTILISATEUR = "+activeUser.getId().toString();
            requete += " ORDER BY NOM";

            listContactsBD = Contact.findWithQuery(Contact.class, requete);



            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            if (listContactsBD.size() == 0) {
                Toast toast = Toast.makeText(AfficherContactActivity.this, "Aucune correspondance, modifier puis appliquer filtre", Toast.LENGTH_LONG);
                toast.show();
                //listMesIntervenants.setVisibility(View.GONE);
            } else {

                List<String> listContactString = new ArrayList<>();

                String[] listDeroulanteContact = new String[listContactsBD.size()];
                for (Contact contact : listContactsBD) {
                    String affichageContact = "";
                    affichageContact += contact.getNom() + ", " + contact.getPrenom() + " (";

                    if (contact.getSavoirFaire() != null) {
                        affichageContact += contact.getSavoirFaire().getName() + ") * ";
                    } else {
                        affichageContact += contact.getProfession().getName() + ") * ";
                    }
                    affichageContact += contact.getVille();
                    listContactString.add(affichageContact);
                }
                listContactString.toArray(listDeroulanteContact);
                //selectionMetier.setText("Infirmier", false);
                //selectionMesIntervenants.setText(listDeroulanteMedecinOfficiel[0], false);
                adapter = new ArrayAdapter(AfficherContactActivity.this, R.layout.list_item, listDeroulanteContact);
                selectedContact.setAdapter(adapter);
                //textRechercheIntervenant.setOnItemClickListener(this);
                //textRechercheIntervenant.setOnItemClickListener((AdapterView.OnItemClickListener) this);
                // Set the minimum number of characters, to show suggestions
                //textRechercheIntervenant.setThreshold(3);

                traiterIntent();
            }
        }
    }

    @OnClick(R.id.fabRdv)
    public void fabRdvClick() {
        //Toast.makeText(AfficherContactsActivity.this, "à implementer 1", Toast.LENGTH_LONG).show();
        ouvrirActiviteSuivante(AfficherContactActivity.this, AddRdvActivity.class,"contactId",contactSelected.getId());
    }

    @OnClick(R.id.fabPrescription)
    public void fabPrescriptionClick() {
        Toast.makeText(AfficherContactActivity.this, "à implementer 2", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fabGoogleMap)
    public void fabGoogleMapClick() {

        try
        {
            // Launch GoogleMap to look for Hawaii:

            //String url = "https://www.google.com/maps/search/?api=1&query=";
            String url = "geo:";
            String addr = "";
            if (contactSelected.getLatitude() != 0 && contactSelected.getLongitude() != 0) {
                url += contactSelected.getLatitude()+","+contactSelected.getLongitude();
            } else if (contactSelected.getAdresse() != null && contactSelected.getCp() != null && contactSelected.getVille() != null) {
                url += "0,0?q=";
                addr += Uri.parse(contactSelected.getAdresse()+", "+contactSelected.getCp()+", "+contactSelected.getVille()+", FRANCE");
                url += addr;
            }
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        catch ( ActivityNotFoundException ex  )
        {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            startActivity(intent);
        }
    }

    @OnClick(R.id.fabWaze)
    public void fabWazeClick() {

        try
        {
            // Launch Waze to look for Hawaii:

            String url = "https://waze.com/ul?q=";
            url += contactSelected.getAdresse()+"%20"+contactSelected.getCp()+"%20"+contactSelected.getVille();

            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            startActivity( intent );
        }
        catch ( ActivityNotFoundException ex  )
        {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            startActivity(intent);
        }
    }

    @OnClick(R.id.fabEdit)
    public void fabEditClick() {
        Toast.makeText(AfficherContactActivity.this, "à implementer 5", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        int positionVirgule = selectedContact.getText().toString().indexOf(",");
        int positionParentheseOuverte = selectedContact.getText().toString().indexOf("(");
        int positionParentheseFermee = selectedContact.getText().toString().indexOf(")");
        int positionEtoile = selectedContact.getText().toString().indexOf("*");

        String nom = selectedContact.getText().toString().substring(0, positionVirgule);
        String prenom = selectedContact.getText().toString().substring(positionVirgule + 2, positionParentheseOuverte - 1);
        String metier = selectedContact.getText().toString().substring(positionParentheseOuverte + 1, positionParentheseFermee);
        String ville = selectedContact.getText().toString().substring(positionEtoile + 2);


        List<Profession> listProfession = Profession.find(Profession.class, "name = ?", metier);
        List<SavoirFaire> listSavoirFaire = SavoirFaire.find(SavoirFaire.class, "name = ?", metier);
        if (listProfession.size() != 0) {
            Profession profession = listProfession.get(0);
            contactSelected = Contact.find(Contact.class, "nom = ? and prenom = ? and profession = ? and ville = ?", nom, prenom, profession.getId().toString(), ville).get(0);

        }
        if (listSavoirFaire.size() != 0) {
            SavoirFaire savoirFaire = listSavoirFaire.get(0);
            contactSelected = Contact.find(Contact.class, "nom = ? and prenom = ? and savoir_faire = ? and ville = ?", nom, prenom, savoirFaire.getId().toString(), ville).get(0);

        }
        Toast.makeText(AfficherContactActivity.this, "Selected Item is: \t" + contactSelected.getPrenom() + contactSelected.getNom(), Toast.LENGTH_LONG).show();

        String stringName ="";
        if (!contactSelected.getCodeCivilite().equalsIgnoreCase("")) {
            stringName += contactSelected.getCodeCivilite() + " ";
        }
        if (!contactSelected.getNom().equalsIgnoreCase("")) {
            stringName += contactSelected.getNom() + " ";
        }
        if (!contactSelected.getPrenom().equalsIgnoreCase("")) {
            stringName += contactSelected.getPrenom();
        }
        if (stringName.length()>0) {
            textName.setText(stringName);
            layoutName.setVisibility(View.VISIBLE);
        }

        if (contactSelected.getProfession() != null) {
            textJob.setText(contactSelected.getProfession().getName());
            layoutJob.setVisibility(View.VISIBLE);
        }
        if (contactSelected.getSavoirFaire() != null) {
            textJob.setText(contactSelected.getSavoirFaire().getName());
            layoutJob.setVisibility(View.VISIBLE);
        }

        if (contactSelected.getRaisonSocial() != null) {
            textPlace.setText(contactSelected.getRaisonSocial());
            layoutPlace.setVisibility(View.VISIBLE);
        }

        if (!contactSelected.getComplement().equalsIgnoreCase("")) {
            textComplement.setText(contactSelected.getComplement());
            layoutComplement.setVisibility(View.VISIBLE);
        }

        if (contactSelected.getAdresse() != null) {
            textNumStreet.setText(contactSelected.getAdresse());
            layoutNumStreet.setVisibility(View.VISIBLE);
        }

        String stringCpVille = "";
        if (!contactSelected.getCp().equalsIgnoreCase("")) {
            stringCpVille += contactSelected.getCp() + " ";
        }
        if (!contactSelected.getVille().equalsIgnoreCase("")) {
            stringCpVille += contactSelected.getVille();
        }
        if (stringCpVille.length()>0) {
            textZipTown.setText(stringCpVille);
            layoutZipTown.setVisibility(View.VISIBLE);
        }


        if (contactSelected.getTelephone() != null) {
            textPhone.setText(contactSelected.getTelephone());
            layoutPhone.setVisibility(View.VISIBLE);
        }

        if (contactSelected.getFax() != null) {
            textFax.setText(contactSelected.getFax());
            layoutFax.setVisibility(View.VISIBLE);
        }

        if (contactSelected.getEmail() != null) {
            textEmail.setText(contactSelected.getEmail());
            layoutEmail.setVisibility(View.VISIBLE);
        }
        displayfabs();
    }


}

