package com.pouillos.monpilulier.activities.listmyx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.MyProfilActivity;
import com.pouillos.monpilulier.views.adapter.ListMyProfilAdapter;

import java.io.Serializable;

public class ListMyProfilActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_my_profil);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_my_profil_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListMyProfilAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newProfilActivity = new Intent(ListMyProfilActivity.this, MyProfilActivity.class);
                        newProfilActivity.putExtra("activitySource", ListMyProfilActivity.class);
                        startActivity(newProfilActivity);
                        finish();
                    }
                }
        );
    }
}
