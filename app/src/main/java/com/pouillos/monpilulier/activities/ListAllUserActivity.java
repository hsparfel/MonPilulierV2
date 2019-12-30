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
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.ListAllUserAdapter;

import java.io.Serializable;
import java.util.List;

public class ListAllUserActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_user);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_user_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllUserAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newUserActivity = new Intent(ListAllUserActivity.this, NewUserActivity.class);
                        newUserActivity.putExtra("precedent",ListAllUserActivity.class);
                        startActivity(newUserActivity);
                        finish();
                    }
                }
        );
    }
}
