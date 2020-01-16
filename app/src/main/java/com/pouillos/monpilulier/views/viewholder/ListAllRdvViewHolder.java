package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Rdv;

public class ListAllRdvViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Rdv currentRdv;

    public ListAllRdvViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Rdv rdv) {
        currentRdv = rdv;
        name.setText(currentRdv.afficherTitre());
    }
}