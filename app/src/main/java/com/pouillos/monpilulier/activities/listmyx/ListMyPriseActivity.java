package com.pouillos.monpilulier.activities.listmyx;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.views.adapter.ListMyPriseAdapter;

import java.io.Serializable;

public class ListMyPriseActivity extends AppCompatActivity implements Serializable{



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_my_prise);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_my_prise_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListMyPriseAdapter());




    }
}
