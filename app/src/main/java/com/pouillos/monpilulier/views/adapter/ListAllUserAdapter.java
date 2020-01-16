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
import com.pouillos.monpilulier.activities.listallx.ListAllSpecialiteActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllUserActivity;
import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.activities.newx.NewSpecialiteActivity;
import com.pouillos.monpilulier.activities.newx.NewUserActivity;
import com.pouillos.monpilulier.entities.Association;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Specialite;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.viewholder.ListAllUserViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllUserAdapter extends RecyclerView.Adapter<ListAllUserViewHolder>{

        private List<Utilisateur> listAllUser;

        public ListAllUserAdapter( ) {
            this.listAllUser = Utilisateur.listAll(Utilisateur.class);
            Collections.sort(this.listAllUser);
        }

        @Override
        public int getItemCount() {
            return listAllUser.size();
        }

        @Override
        public ListAllUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllUserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllUserViewHolder holder, int position) {
            Utilisateur utilisateur = listAllUser.get(position);
            holder.display(utilisateur);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newUserActivity = new Intent(v.getContext(), NewUserActivity.class);
                            newUserActivity.putExtra("activitySource", ListAllUserActivity.class);
                            newUserActivity.putExtra("userAModifId", utilisateur.getId());
                            v.getContext().startActivity(newUserActivity);
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
                                    .setTitle("Suppression Utilisateur")
                                    .setMessage("Etes vous sûr de vouloir supprimer l'utilisateur "+utilisateur.afficherTitre()+" ?"
                                            +"\nAttention, cette suppression entrainera la suppression d'autres elements liés")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            utilisateur.delete();
                                            //Medecin.deleteAll(Medecin.class,"specialite = ?",specialite.getId().toString());
                                            Association.deleteAll(Association.class,"utilisateur = ?",utilisateur.getId().toString());
                                            Ordonnance.deleteAll(Ordonnance.class,"utilisateur = ?",utilisateur.getId().toString());
                                            Profil.deleteAll(Profil.class,"utilisateur = ?",utilisateur.getId().toString());
                                            Rdv.deleteAll(Rdv.class,"utilisateur = ?",utilisateur.getId().toString());


                                            listAllUser = Utilisateur.listAll(Utilisateur.class,"name");
                                            //Collections.sort(listAllSpecialite);
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
                    utilisateur.setActif(true);
                    utilisateur.save();
                    List<Utilisateur> listUserADesactiver = (List<Utilisateur>) Utilisateur.find(Utilisateur.class, "id <> ?", utilisateur.getId().toString());
                    for (Utilisateur userToDesactivate : listUserADesactiver) {
                        userToDesactivate.setActif(false);
                        userToDesactivate.save();
                    }
                    goToHomePage(holder.itemView);
                }
            });
        }

        public void goToHomePage(View view) {
            Intent nextActivity = new Intent(view.getContext(), MainActivity.class);
            nextActivity.putExtra("activitySource", ListAllUserActivity.class);
            view.getContext().startActivity(nextActivity);
            ((Activity)view.getContext()).finish();
        }
    }











