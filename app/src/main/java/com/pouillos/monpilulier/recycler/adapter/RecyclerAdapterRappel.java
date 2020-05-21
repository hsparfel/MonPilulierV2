package com.pouillos.monpilulier.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Rappel;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderPrescription;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderRappel;

import java.util.List;

public class RecyclerAdapterRappel extends RecyclerView.Adapter<RecyclerViewHolderRappel> {

        private List<Rappel> listRappel;

    public interface Listener {
        void onClickDeleteButton(int position);
    }

    private final Listener callback;

    public RecyclerAdapterRappel(List<Rappel> listRappel, Listener callback) {
        this.listRappel = listRappel;
        this.callback = callback;
    }

        @Override
        public RecyclerViewHolderRappel onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_item, parent, false);

            return new RecyclerViewHolderRappel(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolderRappel viewHolder, int position) {

                viewHolder.updateWithRappel(this.listRappel.get(position), this.callback);
        }

        @Override
        public int getItemCount() {
            return this.listRappel.size();
        }

    public Rappel getRappel(int position){
        return this.listRappel.get(position);
    }

}
