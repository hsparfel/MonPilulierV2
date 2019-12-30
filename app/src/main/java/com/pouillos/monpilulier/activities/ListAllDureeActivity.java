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
import com.pouillos.monpilulier.views.ListAllDureeAdapter;

import java.io.Serializable;

public class ListAllDureeActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_duree);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_duree_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllDureeAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newDureeActivity = new Intent(ListAllDureeActivity.this, NewDureeActivity.class);
                        newDureeActivity.putExtra("precedent", ListAllDureeActivity.class);
                        startActivity(newDureeActivity);
                        finish();
                    }
                }
        );
    }
}
