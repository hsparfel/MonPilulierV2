package com.pouillos.monpilulier.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Medecin;

public class ListAllMedecinViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Medecin currentMedecin;

    public ListAllMedecinViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Medecin medecin) {
        currentMedecin = medecin;
        name.setText(currentMedecin.getName());
    }
}