package com.pouillos.monpilulier.activities.afficher;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.ortiz.touchview.TouchImageView;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherContactActivity;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Photo;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.RdvAnalyse;
import com.pouillos.monpilulier.entities.RdvContact;
import com.pouillos.monpilulier.entities.RdvExamen;
import com.pouillos.monpilulier.entities.Region;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.TypePhoto;
import com.pouillos.monpilulier.interfaces.BasicUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AfficherPhotoActivity  extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {
//todo rajouter fab delete + modifier fab actuel pour partage divers
    @State
    Utilisateur activeUser;

    List<?> listItemBD;
    ArrayAdapter adapter;

    @BindView(R.id.selectItem)
    AutoCompleteTextView selectedItem;
    @BindView(R.id.listItem)
    TextInputLayout listItem;

    @BindView(R.id.fabShare)
    FloatingActionButton fabShare;
    @BindView(R.id.fabDelete)
    FloatingActionButton fabDelete;

    @State
    boolean booleanOrdonnance = false;
    @State
    boolean booleanAnalyse = false;
    @State
    boolean booleanExamen = false;

    @BindView(R.id.chipOrdonnance)
    Chip chipOrdonnance;
    @BindView(R.id.chipAnalyse)
    Chip chipAnalyse;
    @BindView(R.id.chipExamen)
    Chip chipExamen;
    @BindView(R.id.chipGroupType)
    ChipGroup chipGroupType;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.imageView)
    TouchImageView imageView;
    @State
    Photo photoSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_photo);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

      //  progressBar.setVisibility(View.GONE);

        selectedItem.setOnItemClickListener(this);
        setTitle("Mes Photos");

        fabShare.hide();
        fabDelete.hide();
    }

    @OnClick(R.id.fabShare)
    public void fabShareClick() {
        Toast.makeText(AfficherPhotoActivity.this, "a faire1", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fabDelete)
    public void fabDeleteClick() {
     //   Toast.makeText(AfficherPhotoActivity.this, "a faire2", Toast.LENGTH_LONG).show();
        deleteItem(AfficherPhotoActivity.this, photoSelected, AfficherPhotoActivity.class, true);

    }


    @OnClick(R.id.chipOrdonnance)
    public void chipOrdonnanceClick() {
        progressBar.setVisibility(View.VISIBLE);
        booleanOrdonnance = !booleanOrdonnance;
        booleanAnalyse = false;
        booleanExamen = false;
        chipOrdonnance.setEnabled(false);
        chipAnalyse.setEnabled(true);
        chipExamen.setEnabled(true);
        AfficherPhotoActivity.AsyncTaskRunnerBD runnerBD = new AfficherPhotoActivity.AsyncTaskRunnerBD();
        runnerBD.execute();
    }

    @OnClick(R.id.chipAnalyse)
    public void chipAnalyseClick() {
        progressBar.setVisibility(View.VISIBLE);
        booleanAnalyse = !booleanAnalyse;
        booleanOrdonnance = false;
        booleanExamen = false;
        chipOrdonnance.setEnabled(true);
        chipAnalyse.setEnabled(false);
        chipExamen.setEnabled(true);
        AfficherPhotoActivity.AsyncTaskRunnerBD runnerBD = new AfficherPhotoActivity.AsyncTaskRunnerBD();
        runnerBD.execute();
    }

    @OnClick(R.id.chipExamen)
    public void chipExamenClick() {
        progressBar.setVisibility(View.VISIBLE);
        booleanExamen = !booleanExamen;
        booleanAnalyse = false;
        booleanOrdonnance = false;
        chipOrdonnance.setEnabled(true);
        chipAnalyse.setEnabled(true);
        chipExamen.setEnabled(false);
        AfficherPhotoActivity.AsyncTaskRunnerBD runnerBD = new AfficherPhotoActivity.AsyncTaskRunnerBD();
        runnerBD.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(AfficherPhotoActivity.this, "a faire", Toast.LENGTH_LONG).show();
        imageView.setImageBitmap(null);
        imageView.setVisibility(View.VISIBLE);
        photoSelected = (Photo) listItemBD.get(position);
        fabShare.hide();
        fabDelete.hide();
        afficherPhoto(photoSelected);

    }

    private void afficherPhoto(Photo photo) {

        String filename = photo.getPath();
        //String imagePath = getExternalFilesDir() + "/" + filename;
        //String imagePath = Environment
             //   .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/MonPilulierApp"+filename;
        File file = new File(filename);
        if (file.isFile()) {
            Picasso.with(AfficherPhotoActivity.this)
                    .load(new File(filename))
                    .rotate(90)
                    .into(imageView);
            fabShare.show();
            fabDelete.show();
        } else {
            Toast.makeText(AfficherPhotoActivity.this, "ficher image introuvable", Toast.LENGTH_LONG).show();
            fabDelete.show();
        }
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

    public class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(50);
            String requete = "";
            requete += "SELECT p.* FROM PHOTO AS p LEFT JOIN ";
            if (booleanOrdonnance) {
                requete += "RDV_CONTACT AS item ON item.ID = p.ITEM_ID ";
            } else if (booleanAnalyse) {
                requete += "RDV_ANALYSE AS item ON item.ID = p.ITEM_ID ";
            }else if (booleanExamen) {
                requete += "RDV_EXAMEN AS item ON item.ID = p.ITEM_ID ";
            }
            requete += "WHERE item.UTILISATEUR = " + activeUser.getId().toString();
            requete += " AND p.TYPE LIKE ";
            if (booleanOrdonnance) {

                //todo
                // requete += "\"Contact\"";
            } else if (booleanAnalyse) {
                //requete += "\""+ "Analyses" +"\"";
                //requete += "\""+ TypePhoto.RdvAnalyse +"\"";
               requete += "\""+ TypePhoto.Analyse.toString() +"\"";
            } else if (booleanExamen) {
                requete += "\""+ TypePhoto.Examen.toString() +"\"";
            }
            listItemBD = Photo.findWithQuery(Photo.class, requete);
            //Collections.sort(listItemBD);

            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            if (listItemBD.size() == 0) {
                Toast.makeText(AfficherPhotoActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                //textRechercheIntervenant.setVisibility(View.GONE);
                listItem.setVisibility(View.GONE);

            } else {
                listItem.setVisibility(View.VISIBLE);
            }

            buildDropdownMenu(listItemBD, AfficherPhotoActivity.this,selectedItem);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }
}