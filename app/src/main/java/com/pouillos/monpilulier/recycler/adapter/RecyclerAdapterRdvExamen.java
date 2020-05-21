package com.pouillos.monpilulier.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.RdvExamen;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderRdvExamen;

import java.util.List;

public class RecyclerAdapterRdvExamen extends RecyclerView.Adapter<RecyclerViewHolderRdvExamen> {

        private List<RdvExamen> listRdvExamen;

    public interface Listener {
        void onClickRdvExamenButton(int position);
    }

    private final Listener callback;

        public RecyclerAdapterRdvExamen(List<RdvExamen> listRdvExamen, Listener callback) {
            this.listRdvExamen = listRdvExamen;
            this.callback = callback;
        }

        @Override
        public RecyclerViewHolderRdvExamen onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_rdv_examen, parent, false);

            return new RecyclerViewHolderRdvExamen(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolderRdvExamen viewHolder, int position) {
                viewHolder.updateWithRdvExamen(this.listRdvExamen.get(position), this.callback);
        }

        @Override
        public int getItemCount() {
            return this.listRdvExamen.size();
        }

    public RdvExamen getRdvExamen(int position){
        return this.listRdvExamen.get(position);
    }

}
