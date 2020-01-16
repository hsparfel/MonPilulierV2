package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Examen;

public class ListAllExamenViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Examen currentExamen;

    public ListAllExamenViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Examen examen) {
        currentExamen = examen;
        name.setText(currentExamen.afficherTitre());
    }
}