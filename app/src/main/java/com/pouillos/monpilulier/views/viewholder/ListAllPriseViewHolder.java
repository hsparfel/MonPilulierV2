package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Prise;

public class ListAllPriseViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Prise currentPrise;

    public ListAllPriseViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Prise prise) {
        currentPrise = prise;
        name.setText(currentPrise.afficherTitre());
    }
}