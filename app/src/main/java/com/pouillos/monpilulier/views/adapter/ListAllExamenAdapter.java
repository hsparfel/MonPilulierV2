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
import com.pouillos.monpilulier.activities.listallx.ListAllExamenActivity;
import com.pouillos.monpilulier.activities.newx.NewExamenActivity;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.entities.OrdoExamen;
import com.pouillos.monpilulier.views.viewholder.ListAllExamenViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllExamenAdapter extends RecyclerView.Adapter<ListAllExamenViewHolder>{

        // FOR DATA
        private List<Examen> listAllExamen;

        // CONSTRUCTOR
        public ListAllExamenAdapter( ) {
            this.listAllExamen = Examen.listAll(Examen.class);
            Collections.sort(this.listAllExamen);
        }

        @Override
        public int getItemCount() {
            return listAllExamen.size();
        }

        @Override
        public ListAllExamenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllExamenViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllExamenViewHolder holder, int position) {
            Examen examen = listAllExamen.get(position);
            holder.display(examen);
            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newExamenActivity = new Intent(v.getContext(), NewExamenActivity.class);
                            newExamenActivity.putExtra("activitySource", ListAllExamenActivity.class);
                            newExamenActivity.putExtra("examenAModifId", examen.getId());
                            v.getContext().startActivity(newExamenActivity);
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
                                    .setTitle("Suppression Examen")
                                    .setMessage("Etes vous sûr de vouloir supprimer le examen "+examen.afficherTitre()+" ?"
                                            +"\nAttention, cette suppression entrainera la suppression d'autres elements liés")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            examen.delete();
                                            OrdoExamen.deleteAll(OrdoExamen.class,"examen = ?",examen.getId().toString());

                                            listAllExamen = Examen.listAll(Examen.class,"name");
                                           // Collections.sort(listAllExamen);
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
                            .setTitle(examen.afficherTitre())
                            .setMessage(examen.afficherDetail())
                            .show();
                }
            });
        }
    }











