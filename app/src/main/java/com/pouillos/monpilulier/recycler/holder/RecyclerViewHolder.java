package com.pouillos.monpilulier.recycler.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Prescription;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.detail)
    TextView detail;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithPrescription(Prescription prescription){
        this.detail.setText(prescription.getMedicament().toString());
    }
}
