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
import com.pouillos.monpilulier.activities.newx.NewOrdoPrescriptionActivity;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.views.adapter.ListAllOrdoPrescriptionAdapter;

import java.io.Serializable;

public class ListAllOrdoPrescriptionActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;
    private Intent intent;
    private Class<?> activitySource;
    private Ordonnance ordonnanceSauvegarde;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_ordo_prescription);

        traiterIntent();

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_ordoPrescription_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllOrdoPrescriptionAdapter(ordonnanceSauvegarde));

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newOrdoPrescriptionActivity = new Intent(ListAllOrdoPrescriptionActivity.this, NewOrdoPrescriptionActivity.class);
                        newOrdoPrescriptionActivity.putExtra("activitySource", ListAllOrdoPrescriptionActivity.class);
                        newOrdoPrescriptionActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
                        startActivity(newOrdoPrescriptionActivity);
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
