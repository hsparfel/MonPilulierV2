package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.OrdoExamen;

public class ListAllOrdoExamenViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private OrdoExamen currentOrdoExamen;

    public ListAllOrdoExamenViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(OrdoExamen ordoExamen) {
        currentOrdoExamen = ordoExamen;
        name.setText(currentOrdoExamen.afficherTitre());
    }
}