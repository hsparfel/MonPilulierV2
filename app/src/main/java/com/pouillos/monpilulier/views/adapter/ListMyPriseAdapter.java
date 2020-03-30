package com.pouillos.monpilulier.views.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Prise;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.viewholder.ListAllPriseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ListMyPriseAdapter extends RecyclerView.Adapter<ListAllPriseViewHolder>{

        // FOR DATA
        private List<Prise> listMyPrise = new ArrayList<>();
        private Utilisateur utilisateur;
       // private List<Association> listAllAssociation;


        // CONSTRUCTOR
        public ListMyPriseAdapter( ) {
            this.utilisateur  = (new Utilisateur()).findActifUser();
            //TODO revoir requete personnalisé à la place avec order by date et date > aujourd'hui
            this.listMyPrise = Prise.findWithQuery(Prise.class, "Select * from Prise where prescription in (" +
                    "select id from Ordo_Prescription where ordonnance in (" +
                    "select id from Ordonnance where utilisateur = ?))", utilisateur.getId().toString());
            //this.listMyPrise = Prise.find(Prise.class,"utilisateur = ?",utilisateur.getId().toString());
        }

        @Override
        public int getItemCount() {
            return listMyPrise.size();
        }

        @Override
        public ListAllPriseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item_no_modif, parent, false);
            return new ListAllPriseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllPriseViewHolder holder, int position) {
            Prise prise = listMyPrise.get(position);
            holder.display(prise);



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle(prise.afficherTitre())
                            .setMessage(prise.afficherDetail())
                            .show();
                }
            });
        }
    }











