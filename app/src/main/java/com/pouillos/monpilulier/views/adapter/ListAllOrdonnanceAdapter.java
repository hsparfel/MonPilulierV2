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
import com.pouillos.monpilulier.activities.listallx.ListAllOrdonnanceActivity;
import com.pouillos.monpilulier.activities.newx.NewOrdonnanceActivity;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.entities.OrdoExamen;
import com.pouillos.monpilulier.entities.OrdoPrescription;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.views.viewholder.ListAllOrdonnanceViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllOrdonnanceAdapter extends RecyclerView.Adapter<ListAllOrdonnanceViewHolder>{

        // FOR DATA
        private List<Ordonnance> listAllOrdonnance;

        // CONSTRUCTOR
        public ListAllOrdonnanceAdapter( ) {
            this.listAllOrdonnance = Ordonnance.listAll(Ordonnance.class,"date");
            Collections.sort(this.listAllOrdonnance);
        }

        @Override
        public int getItemCount() {
            return listAllOrdonnance.size();
        }

        @Override
        public ListAllOrdonnanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllOrdonnanceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllOrdonnanceViewHolder holder, int position) {
            Ordonnance ordonnance = listAllOrdonnance.get(position);
            holder.display(ordonnance);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newOrdonnanceActivity = new Intent(v.getContext(), NewOrdonnanceActivity.class);
                            newOrdonnanceActivity.putExtra("activitySource", ListAllOrdonnanceActivity.class);
                            newOrdonnanceActivity.putExtra("ordonnanceAModifId", ordonnance.getId());
                            v.getContext().startActivity(newOrdonnanceActivity);
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
                                    .setTitle("Suppression Ordonnance")
                                    .setMessage("Etes vous sûr de vouloir supprimer le ordonnance "+ordonnance.afficherTitre()+" ?"
                                            +"\nAttention, cette suppression entrainera la suppression d'autres elements liés")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ordonnance.delete();
                                            OrdoAnalyse.deleteAll(OrdoAnalyse.class,"ordonnance = ?",ordonnance.getId().toString());
                                            OrdoExamen.deleteAll(OrdoExamen.class,"ordonnance = ?",ordonnance.getId().toString());
                                            OrdoPrescription.deleteAll(OrdoPrescription.class,"ordonnance = ?",ordonnance.getId().toString());

                                            listAllOrdonnance = Ordonnance.listAll(Ordonnance.class,"date desc");
                                          //  Collections.sort(listAllOrdonnance);
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
                            .setTitle(ordonnance.afficherTitre())
                            .setMessage(ordonnance.afficherDetail())
                            .show();
                }
            });
        }
    }











