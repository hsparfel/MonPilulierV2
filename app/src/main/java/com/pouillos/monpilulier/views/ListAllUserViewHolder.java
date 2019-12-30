package com.pouillos.monpilulier.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NewUserActivity;
import com.pouillos.monpilulier.entities.Utilisateur;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAllUserViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Utilisateur currentUtilisateur;

    public ListAllUserViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Utilisateur utilisateur) {
        currentUtilisateur = utilisateur;
        name.setText(currentUtilisateur.getName());
    }
}