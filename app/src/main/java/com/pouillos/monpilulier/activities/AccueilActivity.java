package com.pouillos.monpilulier.activities;



import android.app.FragmentTransaction;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.ui.listAllAnalyse.ListAllAnalyseFragment;
import com.pouillos.monpilulier.activities.ui.newAnalyse.NewAnalyseFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class AccueilActivity extends AppCompatActivity implements ListAllAnalyseFragment.OnButtonClickedListener{

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_list_all_examen, R.id.nav_list_all_analyse)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    // --------------
    // CallBack
    // --------------

    @Override
    public void onButtonClicked(View view) {
        Log.e(getClass().getSimpleName(),"Button AddAnalyse clicked !");
        //Fragment fragment = null;
        //fragment = new NewAnalyseFragment();
        //replaceFragment(fragment);




        FragmentManager fm = getSupportFragmentManager();
        //fm.(R.id.container, new NewAnalyseFragment());
        //ft.commit();
       // Fragment f = Fragment.instantiate(this, "NewAnalyseFragment", NewAnalyseFragment);

        //fm.beginTransaction().replace(R.id.container, new NewAnalyseFragment()).commit();
        fm.beginTransaction().replace(R.id.container, NewAnalyseFragment.newInstance()).commit();
       // fm.beginTransaction().replace(R.id.container, new NewAnalyseFragment()).commit();

        /*parentFragmentManager.commit {
            setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            )
            replace(R.id.fragment, BaseFragment)
            addToBackStack(null)
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accueil, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

  //  public void replaceFragment(Fragment someFragment) {
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();*/

        // create a FragmentManager
       // FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the
        //Fragment
       // FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
       // fragmentTransaction.replace(R.id.container, someFragment);
       // fragmentTransaction.addToBackStack(null);
       // fragmentTransaction.commit(); // save the changes
  //  }
}
