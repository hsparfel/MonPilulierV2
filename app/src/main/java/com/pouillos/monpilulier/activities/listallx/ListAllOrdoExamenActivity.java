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
import com.pouillos.monpilulier.activities.newx.NewOrdoExamenActivity;
import com.pouillos.monpilulier.entities.OrdoExamen;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.views.adapter.ListAllOrdoExamenAdapter;

import java.io.Serializable;

public class ListAllOrdoExamenActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;
    private Intent intent;
    private Class<?> activitySource;
    private Ordonnance ordonnanceSauvegarde;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_ordo_examen);

        traiterIntent();

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_ordoExamen_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllOrdoExamenAdapter(ordonnanceSauvegarde));

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newOrdoExamenActivity = new Intent(ListAllOrdoExamenActivity.this, NewOrdoExamenActivity.class);
                        newOrdoExamenActivity.putExtra("activitySource", ListAllOrdoExamenActivity.class);
                        newOrdoExamenActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
                        startActivity(newOrdoExamenActivity);
                        finish();
                    }
                }
        );
    }

    public void traiterIntent(){
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("ordonnanceSauvegardeId")) {
            Long Id = intent.getLongExtra("ordonnanceSauvegardeId",0);
            ordonnanceSauvegarde = Ordonnance.findById(Ordonnance.class, Id);
        }
    }
}
