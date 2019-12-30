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
import com.pouillos.monpilulier.views.ListAllOrdonnanceAdapter;

import java.io.Serializable;

public class ListAllOrdonnanceActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_ordonnance);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_ordonnance_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllOrdonnanceAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newOrdonnanceActivity = new Intent(ListAllOrdonnanceActivity.this, NewOrdonnanceActivity.class);
                        newOrdonnanceActivity.putExtra("precedent", ListAllOrdonnanceActivity.class);
                        startActivity(newOrdonnanceActivity);
                        finish();
                    }
                }
        );
    }
}
