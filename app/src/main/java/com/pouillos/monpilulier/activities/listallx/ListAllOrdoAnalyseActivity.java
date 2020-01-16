package com.pouillos.monpilulier.activities.listallx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.newx.NewOrdoAnalyseActivity;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.views.adapter.ListAllOrdoAnalyseAdapter;

import java.io.Serializable;

public class ListAllOrdoAnalyseActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;
    private Intent intent;
    private Class<?> pagePrecedente;
    private OrdoAnalyse ordoAnalyseToUpdate;
    private Ordonnance ordonnanceToUpdate;
    private Ordonnance ordonnanceSauvegarde;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_ordo_analyse);

        traiterIntent();

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_ordoAnalyse_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllOrdoAnalyseAdapter(ordonnanceToUpdate));

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newOrdoAnalyseActivity = new Intent(ListAllOrdoAnalyseActivity.this, NewOrdoAnalyseActivity.class);
                        newOrdoAnalyseActivity.putExtra("activitySource", ListAllOrdoAnalyseActivity.class);
                        newOrdoAnalyseActivity.putExtra("ordonnanceToUpdate", ordonnanceToUpdate);
                        startActivity(newOrdoAnalyseActivity);
                        finish();
                    }
                }
        );
    }

    public void traiterIntent(){
        intent = getIntent();
        pagePrecedente = (Class<?>) intent.getSerializableExtra("precedent");
        if (intent.hasExtra("ordoAnalyseToUpdate")) {
            ordoAnalyseToUpdate = (OrdoAnalyse) intent.getSerializableExtra("ordoAnalyseToUpdate");
            //textDescription.setText(ordoAnalyseToUpdate.getDetail());
            //spinnerAnalyse.setSelection(getIndex(spinnerAnalyse, ordoAnalyseToUpdate.getAnalyse().getName()));
        } else {ordoAnalyseToUpdate = new OrdoAnalyse();}
        if (intent.hasExtra("ordonnanceSauvegardeId")) {
            Long Id = intent.getLongExtra("ordonnanceSauvegardeId",0);
            ordonnanceToUpdate = Ordonnance.findById(Ordonnance.class, Id);
        } else {ordonnanceToUpdate = new Ordonnance();}
    }
}
