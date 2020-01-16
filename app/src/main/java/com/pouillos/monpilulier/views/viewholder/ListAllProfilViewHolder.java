package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Profil;

public class ListAllProfilViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Profil currentProfil;

    public ListAllProfilViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Profil profil) {
        currentProfil = profil;
        name.setText(currentProfil.afficherTitre());
    }
}