package com.pouillos.monpilulier.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolder;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        // FOR DATA
        private List<Prescription> listPrescription;

        // CONSTRUCTOR
        public RecyclerAdapter(List<Prescription> listPrescription) {
            this.listPrescription = listPrescription;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_item, parent, false);

            return new RecyclerViewHolder(view);
        }

        // UPDATE VIEW HOLDER WITH A GITHUBUSER
        @Override
        public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
            viewHolder.updateWithPrescription(this.listPrescription.get(position));
        }

        // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
        @Override
        public int getItemCount() {
            return this.listPrescription.size();
        }


}
