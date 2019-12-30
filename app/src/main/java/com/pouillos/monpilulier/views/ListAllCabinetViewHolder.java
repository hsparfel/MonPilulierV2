package com.pouillos.monpilulier.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Cabinet;

public class ListAllCabinetViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private Cabinet currentCabinet;

    public ListAllCabinetViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(Cabinet cabinet) {
        currentCabinet = cabinet;
        name.setText(currentCabinet.getName());
    }
}