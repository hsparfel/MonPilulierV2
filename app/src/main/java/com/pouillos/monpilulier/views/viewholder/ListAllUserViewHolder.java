package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Utilisateur;

public class ListAllUserViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Utilisateur currentUtilisateur;

    public ListAllUserViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Utilisateur utilisateur) {
        currentUtilisateur = utilisateur;
        name.setText(currentUtilisateur.afficherTitre());
    }
}