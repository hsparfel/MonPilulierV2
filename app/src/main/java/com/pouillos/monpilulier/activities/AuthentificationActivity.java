package com.pouillos.monpilulier.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.add.AddUserActivity;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Utilisateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AuthentificationActivity extends AppCompatActivity implements Serializable, AdapterView.OnItemClickListener {

    @State
    Utilisateur activeUser;

    @BindView(R.id.selectUser)
    AutoCompleteTextView selectedUser;
    @BindView(R.id.listUser)
    TextInputLayout listUser;
    @BindView(R.id.floating_action_button)
    FloatingActionButton fab;

    private List<Utilisateur> listUserBD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_authentification_test);
        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            activeUser = listUserActif.get(0);
        }
        AuthentificationActivity.AsyncTaskRunnerUser runnerUser = new AuthentificationActivity.AsyncTaskRunnerUser();
        runnerUser.execute();
        ButterKnife.bind(this);
        selectedUser.setOnItemClickListener(this);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public class AsyncTaskRunnerUser extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            listUserBD = Utilisateur.listAll(Utilisateur.class);
            return null;
        }

        protected void onPostExecute(Void result) {
            if (listUserBD.size() == 0) {
                listUser.setVisibility(View.INVISIBLE);
            } else {
                List<String> listUserString = new ArrayList<>();
                String[] listDeroulanteUser;
                if (activeUser != null) {
                    listDeroulanteUser = new String[listUserBD.size()-1];
                } else {
                    listDeroulanteUser = new String[listUserBD.size()];
                }
                for (Utilisateur user : listUserBD) {
                    if (activeUser== null || !user.getName().equalsIgnoreCase(activeUser.getName())){
                        listUserString.add(user.getName());
                    }
                }
                listUserString.toArray(listDeroulanteUser);
                ArrayAdapter adapter = new ArrayAdapter(AuthentificationActivity.this, R.layout.list_item, listDeroulanteUser);
                selectedUser.setAdapter(adapter);
                listUser.setVisibility(View.VISIBLE);
                }
            }
        }

    @OnClick(R.id.floating_action_button)
    public void fabClick() {
        Intent myProfilActivity = new Intent(AuthentificationActivity.this, AddUserActivity.class);
        startActivity(myProfilActivity);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (activeUser != null) {
            activeUser.setActif(false);
            activeUser.save();
        }
        List<Utilisateur> listUserSelected = Utilisateur.find(Utilisateur.class, "name = ?", item);
        if (listUserSelected.size() !=0){
            activeUser = listUserSelected.get(0);
            activeUser.setActif(true);
            activeUser.save();
        }
        Intent myProfilActivity = new Intent(AuthentificationActivity.this, MainActivity.class);
        startActivity(myProfilActivity);
        finish();
    }
}