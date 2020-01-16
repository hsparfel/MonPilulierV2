package com.pouillos.monpilulier.views.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listmyx.ListMyRdvActivity;
import com.pouillos.monpilulier.activities.newx.NewRdvActivity;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.viewholder.ListAllRdvViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ListMyRdvAdapter extends RecyclerView.Adapter<ListAllRdvViewHolder>{

        // FOR DATA
        private List<Rdv> listMyRdv = new ArrayList<>();
        private Utilisateur utilisateur;
       // private List<Association> listAllAssociation;


        // CONSTRUCTOR
        public ListMyRdvAdapter( ) {
            this.utilisateur  = (new Utilisateur()).findActifUser();
         //   listAllAssociation = Association.find(Association.class,"utilisateur = ?", utilisateur.getId().toString());
         //   for (Association association : listAllAssociation) {
           //     if (association.getUtilisateur().getId() == utilisateur.getId()) {
                    this.listMyRdv = Rdv.find(Rdv.class,"utilisateur = ?",utilisateur.getId().toString(),"date");
           ///     }
          //  }
          //  Collections.sort(this.listMyRdv);
        }

        @Override
        public int getItemCount() {
            return listMyRdv.size();
        }

        @Override
        public ListAllRdvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllRdvViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllRdvViewHolder holder, int position) {
            Rdv rdv = listMyRdv.get(position);
            holder.display(rdv);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newRdvActivity = new Intent(v.getContext(), NewRdvActivity.class);
                            newRdvActivity.putExtra("activitySource", ListMyRdvActivity.class);
                            newRdvActivity.putExtra("rdvAModifId", rdv.getId());
                         //   newRdvActivity.putExtra("associe", true);

                            v.getContext().startActivity(newRdvActivity);
                            ((Activity)v.getContext()).finish();
                        }
                    }
            );

            holder.itemView.findViewById(R.id.buttonDelete).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(v.getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Suppression Rdv")
                                    .setMessage("Etes vous sûr de vouloir supprimer le rdv "+rdv.afficherTitre()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            rdv.delete();
                                            listMyRdv = Rdv.listAll(Rdv.class,"date");
                                          //  Collections.sort(listAllRdv);
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("Non", null)
                                    .show();
                        }
                    }
            );

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle(rdv.afficherTitre())
                            .setMessage(rdv.afficherDetail())
                            .show();
                }
            });
        }
    }











