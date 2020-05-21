package com.pouillos.monpilulier.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.RdvAnalyse;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderRdvAnalyse;

import java.util.List;

public class RecyclerAdapterRdvAnalyse extends RecyclerView.Adapter<RecyclerViewHolderRdvAnalyse> {

        private List<RdvAnalyse> listRdvAnalyse;

    public interface Listener {
        void onClickRdvAnalyseButton(int position);
    }

    private final Listener callback;

        public RecyclerAdapterRdvAnalyse(List<RdvAnalyse> listRdvAnalyse, Listener callback) {
            this.listRdvAnalyse = listRdvAnalyse;
            this.callback = callback;
        }

        @Override
        public RecyclerViewHolderRdvAnalyse onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_rdv_analyse, parent, false);

            return new RecyclerViewHolderRdvAnalyse(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolderRdvAnalyse viewHolder, int position) {
                viewHolder.updateWithRdvAnalyse(this.listRdvAnalyse.get(position), this.callback);
        }

        @Override
        public int getItemCount() {
            return this.listRdvAnalyse.size();
        }

    public RdvAnalyse getRdvAnalyse(int position){
        return this.listRdvAnalyse.get(position);
    }

}
