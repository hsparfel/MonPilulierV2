package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.OrdoAnalyse;

public class ListAllOrdoAnalyseViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private OrdoAnalyse currentOrdoAnalyse;

    public ListAllOrdoAnalyseViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(OrdoAnalyse ordoAnalyse) {
        currentOrdoAnalyse = ordoAnalyse;
        name.setText(currentOrdoAnalyse.afficherTitre());
    }
}