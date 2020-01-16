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
import com.pouillos.monpilulier.activities.listallx.ListAllRdvActivity;
import com.pouillos.monpilulier.activities.newx.NewRdvActivity;
import com.pouillos.monpilulier.entities.Association;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.viewholder.ListAllRdvViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllRdvAdapter extends RecyclerView.Adapter<ListAllRdvViewHolder>{

        // FOR DATA
        private List<Rdv> listAllRdv;
        private Utilisateur utilisateur;

        // CONSTRUCTOR
        public ListAllRdvAdapter( ) {
            this.utilisateur  = (new Utilisateur()).findActifUser();
            this.listAllRdv = Rdv.listAll(Rdv.class,"date");
            Collections.sort(this.listAllRdv);
        }

        @Override
        public int getItemCount() {
            return listAllRdv.size();
        }

        @Override
        public ListAllRdvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllRdvViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllRdvViewHolder holder, int position) {
            Rdv rdv = listAllRdv.get(position);
            holder.display(rdv);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newRdvActivity = new Intent(v.getContext(), NewRdvActivity.class);
                            newRdvActivity.putExtra("activitySource", ListAllRdvActivity.class);
                            newRdvActivity.putExtra("rdvAModifId", rdv.getId());

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
                                            listAllRdv = Rdv.listAll(Rdv.class,"date");
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











