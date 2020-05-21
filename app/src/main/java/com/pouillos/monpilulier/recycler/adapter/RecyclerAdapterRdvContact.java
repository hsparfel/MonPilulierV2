package com.pouillos.monpilulier.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.RdvContact;
import com.pouillos.monpilulier.recycler.holder.RecyclerViewHolderRdvContact;

import java.util.List;

public class RecyclerAdapterRdvContact extends RecyclerView.Adapter<RecyclerViewHolderRdvContact> {

        private List<RdvContact> listRdvContact;

    public interface Listener {
        void onClickRdvContactButton(int position);
    }

    private final Listener callback;

        public RecyclerAdapterRdvContact(List<RdvContact> listRdvContact, Listener callback) {
            this.listRdvContact = listRdvContact;
            this.callback = callback;
        }

        @Override
        public RecyclerViewHolderRdvContact onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_rdv_contact, parent, false);

            return new RecyclerViewHolderRdvContact(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolderRdvContact viewHolder, int position) {
                viewHolder.updateWithRdvContact(this.listRdvContact.get(position), this.callback);
        }

        @Override
        public int getItemCount() {
            return this.listRdvContact.size();
        }

    public RdvContact getRdvContact(int position){
        return this.listRdvContact.get(position);
    }

}
