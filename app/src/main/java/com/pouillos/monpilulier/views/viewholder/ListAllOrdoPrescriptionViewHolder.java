package com.pouillos.monpilulier.views.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.OrdoPrescription;

public class ListAllOrdoPrescriptionViewHolder extends RecyclerView.ViewHolder{

    private final TextView name;
    private OrdoPrescription currentOrdoPrescription;

    public ListAllOrdoPrescriptionViewHolder(final View itemView) {
        super(itemView);
        name = ((TextView) itemView.findViewById(R.id.name));
    }

    public void display(OrdoPrescription ordoPrescription) {
        currentOrdoPrescription = ordoPrescription;
        name.setText(currentOrdoPrescription.afficherTitre());
    }
}