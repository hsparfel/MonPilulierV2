package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Ordonnance;

public class ListAllOrdonnanceViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Ordonnance currentOrdonnance;

    public ListAllOrdonnanceViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Ordonnance ordonnance) {
        currentOrdonnance = ordonnance;
        name.setText(currentOrdonnance.afficherTitre());
    }
}