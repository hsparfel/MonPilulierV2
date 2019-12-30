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
import com.pouillos.monpilulier.views.ListAllMedecinAdapter;

import java.io.Serializable;

public class ListAllMedecinActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_medecin);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_medecin_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllMedecinAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newMedecinActivity = new Intent(ListAllMedecinActivity.this, NewMedecinActivity.class);
                        newMedecinActivity.putExtra("precedent", ListAllMedecinActivity.class);
                        startActivity(newMedecinActivity);
                        finish();
                    }
                }
        );
    }
}
