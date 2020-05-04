package com.pouillos.monpilulier.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.add.AddUserActivity;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AuthentificationActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.selectUser)
    AutoCompleteTextView selectedUser;
    @BindView(R.id.listUser)
    TextInputLayout listUser;
    @BindView(R.id.authFab)
    FloatingActionButton authFab;

    private List<Utilisateur> listUserBD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_authentification);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        activeUser=findActiveUser();

        if (activeUser == null) {
            getSupportActionBar().hide();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        setTitle(getResources().getString(R.string.title_connexion));

        AuthentificationActivity.AsyncTaskRunnerUser runnerUser = new AuthentificationActivity.AsyncTaskRunnerUser();
        runnerUser.execute();

        selectedUser.setOnItemClickListener(this);
    }

    public class AsyncTaskRunnerUser extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            listUserBD = Utilisateur.listAll(Utilisateur.class);
            if (activeUser != null) {
              for (Utilisateur user : listUserBD) {
                  if (user.getId()==activeUser.getId()){
                      listUserBD.remove(user);
                      break;
                  }
              }
            }
            Collections.sort(listUserBD);
            return null;
        }

        protected void onPostExecute(Void result) {
            if (listUserBD.size() == 0) {
                listUser.setVisibility(View.GONE);
            } else {
                buildDropdownMenu(listUserBD,AuthentificationActivity.this,selectedUser);
                listUser.setVisibility(View.VISIBLE);
                }
            }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
        }

    @OnClick(R.id.authFab)
    public void fabClick() {
        ouvrirActiviteSuivante(AuthentificationActivity.this, AddUserActivity.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (activeUser != null) {
            activeUser.setActif(false);
            activeUser.save();
        }
        activeUser = listUserBD.get(position);
        activeUser.setActif(true);
        activeUser.save();
//////////////////////
       /*
        List<Utilisateur> listUserSelected = Utilisateur.find(Utilisateur.class, "name = ?", item);
        if (listUserSelected.size() !=0){
            activeUser = listUserSelected.get(0);
            activeUser.setActif(true);
            activeUser.save();
        }*/
        /////////////////
        ouvrirActiviteSuivante(AuthentificationActivity.this, AccueilActivity.class);
    }


}