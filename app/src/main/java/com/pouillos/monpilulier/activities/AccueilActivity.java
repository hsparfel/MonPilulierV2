package com.pouillos.monpilulier.activities;

import android.content.Intent;
import android.os.Bundle;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.List;

import icepick.Icepick;
import icepick.State;

public class AccueilActivity extends NavDrawerActivity implements BasicUtils {

    @State
    Utilisateur activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        setTitle("Bienvenue");


        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            activeUser = listUserActif.get(0);
        } else {
            ouvrirActiviteSuivante(AccueilActivity.this, AuthentificationActivity.class);
        }
    }

}
