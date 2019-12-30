package com.pouillos.monpilulier.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Dose;

public class ListAllDoseViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Dose currentDose;

    public ListAllDoseViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Dose dose) {
        currentDose = dose;
        name.setText(currentDose.getName());
    }
}