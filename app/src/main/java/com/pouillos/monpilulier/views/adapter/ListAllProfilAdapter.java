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
import com.pouillos.monpilulier.activities.listallx.ListAllProfilActivity;
import com.pouillos.monpilulier.activities.MyProfilActivity;

import com.pouillos.monpilulier.entities.Association;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.viewholder.ListAllProfilViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllProfilAdapter extends RecyclerView.Adapter<ListAllProfilViewHolder>{

        // FOR DATA
        private List<Profil> listAllProfil;
        private Utilisateur utilisateur;

        // CONSTRUCTOR
        public ListAllProfilAdapter( ) {
            //this.utilisateur  = (new Utilisateur()).findActifUser();
            this.listAllProfil = Profil.listAll(Profil.class,"date");
            Collections.sort(this.listAllProfil);
        }

        @Override
        public int getItemCount() {
            return listAllProfil.size();
        }

        @Override
        public ListAllProfilViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllProfilViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllProfilViewHolder holder, int position) {
            Profil profil = listAllProfil.get(position);
            holder.display(profil);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newProfilActivity = new Intent(v.getContext(), MyProfilActivity.class);
                            newProfilActivity.putExtra("activitySource", ListAllProfilActivity.class);
                            newProfilActivity.putExtra("profilAModifId", profil.getId());

                            List<Association> listAssociation = Association.find(Association.class,"utilisateur = ? and profil = ?", utilisateur.getId().toString(), profil.getId().toString());
                            if (listAssociation.size() >0) {
                                newProfilActivity.putExtra("associe", true);
                            } else {
                                newProfilActivity.putExtra("associe", false);
                            }



                            v.getContext().startActivity(newProfilActivity);
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
                                    .setTitle("Suppression Profil")
                                    .setMessage("Etes vous sûr de vouloir supprimer le profil "+profil.afficherTitre()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            profil.delete();
                                            listAllProfil = Profil.listAll(Profil.class,"date");
                                          //  Collections.sort(listAllProfil);
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
                            .setTitle(profil.afficherTitre())
                            .setMessage(profil.afficherDetail())
                            .show();
                }
            });
        }
    }











