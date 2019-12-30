package com.pouillos.monpilulier.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.ListAllExamenActivity;
import com.pouillos.monpilulier.activities.NewExamenActivity;
import com.pouillos.monpilulier.entities.Examen;

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
                            newExamenActivity.putExtra("precedent", ListAllExamenActivity.class);
                            newExamenActivity.putExtra("examenToUpdate", examen);
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
                                    .setMessage("Etes vous sûr de vouloir supprimer le examen "+examen.getName()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            examen.delete();
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
                            .setTitle(examen.getName())
                            .setMessage(examen.toString())
                            .show();
                }
            });
        }
    }











