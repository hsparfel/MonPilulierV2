package com.pouillos.monpilulier.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Specialite;

public class ListAllSpecialiteViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Specialite currentSpecialite;

    public ListAllSpecialiteViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Specialite specialite) {
        currentSpecialite = specialite;
        name.setText(currentSpecialite.getName());
    }
}