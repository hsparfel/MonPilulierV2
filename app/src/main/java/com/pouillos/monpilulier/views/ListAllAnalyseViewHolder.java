package com.pouillos.monpilulier.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Analyse;

public class ListAllAnalyseViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Analyse currentAnalyse;

    public ListAllAnalyseViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Analyse analyse) {
        currentAnalyse = analyse;
        name.setText(currentAnalyse.getName());
    }
}