package com.pouillos.monpilulier.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderPrescription;

import java.util.List;

public class RecyclerAdapterPrescription extends RecyclerView.Adapter<RecyclerViewHolderPrescription> {

        private List<Prescription> listPrescription;
        private boolean activate;

    public interface Listener {
        void onClickDeleteButton(int position);
    }

    private final Listener callback;

        public RecyclerAdapterPrescription(List<Prescription> listPrescription, Listener callback) {
            this.listPrescription = listPrescription;
            this.callback = callback;
        }

        @Override
        public RecyclerViewHolderPrescription onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_prescription, parent, false);

            return new RecyclerViewHolderPrescription(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolderPrescription viewHolder, int position) {
                viewHolder.updateWithPrescription(this.listPrescription.get(position), this.callback);
            if (activate) {
                viewHolder.buttonDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.buttonDelete.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return this.listPrescription.size();
        }

    public Prescription getPrescription(int position){
        return this.listPrescription.get(position);
    }

    public void displayButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }
}
