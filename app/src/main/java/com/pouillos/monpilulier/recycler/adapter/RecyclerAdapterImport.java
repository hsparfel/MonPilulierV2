package com.pouillos.monpilulier.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.ImportContact;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderImport;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderPrescription;

import java.util.List;

public class RecyclerAdapterImport extends RecyclerView.Adapter<RecyclerViewHolderImport> {

        private List<ImportContact> listImport;

    public interface Listener {
        void onClickDeleteButton(int position);
    }

    private final Listener callback;

        public RecyclerAdapterImport(List<ImportContact> listImportContact, Listener callback) {
            this.listImport = listImportContact;
            this.callback = callback;
        }

        @Override
        public RecyclerViewHolderImport onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_item, parent, false);

            return new RecyclerViewHolderImport(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolderImport viewHolder, int position) {
                viewHolder.updateWithImport(this.listImport.get(position), this.callback);
        }

        @Override
        public int getItemCount() {
            return this.listImport.size();
        }

    public ImportContact getImportContact(int position){
        return this.listImport.get(position);
    }

}
