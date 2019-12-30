package com.pouillos.monpilulier.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Duree;

public class ListAllDureeViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Duree currentDuree;

    public ListAllDureeViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Duree duree) {
        currentDuree = duree;
        name.setText(currentDuree.getName());
        }
}