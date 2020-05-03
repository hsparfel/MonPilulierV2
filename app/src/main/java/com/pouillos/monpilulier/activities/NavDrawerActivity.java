package com.pouillos.monpilulier.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.afficher.AfficherContactActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherContactActivity;
import com.pouillos.monpilulier.interfaces.BasicUtils;

public class NavDrawerActivity extends AppCompatActivity implements BasicUtils, NavigationView.OnNavigationItemSelectedListener {
    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //a redefinit à chq fois
        super.onCreate(savedInstanceState);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_profile:
                Toast.makeText(this, "à implementer 1", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_account:
                Toast.makeText(this, "à implementer 1b", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_treatments :
                Toast.makeText(this, "à implementer 2", Toast.LENGTH_LONG).show();
                break;

            case R.id.activity_main_drawer_appointments :
                //Toast.makeText(this, "à implementer 3", Toast.LENGTH_LONG).show();
                ouvrirActiviteSuivante(NavDrawerActivity.this,AfficherRdvActivity.class);
                break;
            case R.id.activity_main_drawer_contacts :
                Intent intent = new Intent(NavDrawerActivity.this, AfficherContactActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.menu_activity_main_params:
                Toast.makeText(this, "Il n'y a rien à paramétrer ici, passez votre chemin...", Toast.LENGTH_LONG).show();








                return true;
            case R.id.menu_activity_main_search:
                //Toast.makeText(this, "Recherche indisponible, demandez plutôt l'avis de Google, c'est mieux et plus rapide.", Toast.LENGTH_LONG).show();

                Intent myProfilActivity = new Intent(NavDrawerActivity.this, ChercherContactActivity.class);
                startActivity(myProfilActivity);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    public void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        //ActionBar ab = getSupportActionBar();
        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);
    }

    // 2 - Configure Drawer Layout
    public void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    public void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
//TODO à remplacr par methode generique


    @Override
    public void ouvrirActiviteSuivante(Context context, Class classe){
        Intent intent = new Intent(context, classe);
        startActivity(intent);
        finish();
    }

    @Override
    public void ouvrirActiviteSuivante(Context context, Class classe, String nomExtra, Long objetIdExtra ) {
        Intent intent = new Intent(context, classe);
        intent.putExtra(nomExtra, objetIdExtra);
        startActivity(intent);
        finish();
    }
}
