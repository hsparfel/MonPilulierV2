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
import com.pouillos.monpilulier.activities.ListAllOrdoAnalyseActivity;
import com.pouillos.monpilulier.activities.NewOrdoAnalyseActivity;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.entities.Ordonnance;

import java.util.Collections;
import java.util.List;

public class ListAllOrdoAnalyseAdapter extends RecyclerView.Adapter<ListAllOrdoAnalyseViewHolder>{

        // FOR DATA
        private List<OrdoAnalyse> listAllOrdoAnalyse;
        private Ordonnance ordonnance;

        // CONSTRUCTOR
        public ListAllOrdoAnalyseAdapter(Ordonnance ordonnance ) {
            this.ordonnance = ordonnance;
            this.listAllOrdoAnalyse = OrdoAnalyse.find(OrdoAnalyse.class, "ordonnance = ?", new String[]{ordonnance.getId().toString()});
            Collections.sort(this.listAllOrdoAnalyse);
        }

        @Override
        public int getItemCount() {
            return listAllOrdoAnalyse.size();
        }

        @Override
        public ListAllOrdoAnalyseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllOrdoAnalyseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllOrdoAnalyseViewHolder holder, int position) {
            OrdoAnalyse ordoAnalyse = listAllOrdoAnalyse.get(position);
            holder.display(ordoAnalyse);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newOrdoAnalyseActivity = new Intent(v.getContext(), NewOrdoAnalyseActivity.class);
                            newOrdoAnalyseActivity.putExtra("precedent", ListAllOrdoAnalyseActivity.class);
                            newOrdoAnalyseActivity.putExtra("ordoAnalyseToUpdate", ordoAnalyse);
                            v.getContext().startActivity(newOrdoAnalyseActivity);
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
                                    .setTitle("Suppression OrdoAnalyse")
                                    .setMessage("Etes vous sûr de vouloir supprimer le ordoAnalyse "+ordoAnalyse.getName()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ordoAnalyse.delete();
                                            listAllOrdoAnalyse = OrdoAnalyse.find(OrdoAnalyse.class, "ordonnance = ?", new String[]{ordonnance.getId().toString()});
                                            Collections.sort(listAllOrdoAnalyse);
                                          //  Collections.sort(listAllOrdoAnalyse);
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
                            .setTitle(ordoAnalyse.getName())
                            .setMessage(ordoAnalyse.toString())
                            .show();
                }
            });
        }
    }











