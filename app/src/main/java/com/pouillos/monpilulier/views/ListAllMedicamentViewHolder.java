package com.pouillos.monpilulier.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Medicament;

public class ListAllMedicamentViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Medicament currentMedicament;

    public ListAllMedicamentViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Medicament medicament) {
        currentMedicament = medicament;
        name.setText(currentMedicament.getName());
    }
}