package com.pouillos.monpilulier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.ListAllAnalyseAdapter;

import java.io.Serializable;
import java.util.List;

public class ListAllAnalyseActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_analyse);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_analyse_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllAnalyseAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newAnalyseActivity = new Intent(ListAllAnalyseActivity.this, NewAnalyseActivity.class);
                        newAnalyseActivity.putExtra("precedent", ListAllAnalyseActivity.class);
                        startActivity(newAnalyseActivity);
                        finish();
                    }
                }
        );
    }
}
