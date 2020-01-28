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
import com.pouillos.monpilulier.activities.listallx.ListAllOrdoExamenActivity;
import com.pouillos.monpilulier.activities.newx.NewOrdoExamenActivity;
import com.pouillos.monpilulier.entities.OrdoExamen;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.views.viewholder.ListAllOrdoExamenViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllOrdoExamenAdapter extends RecyclerView.Adapter<ListAllOrdoExamenViewHolder>{

        // FOR DATA
        private List<OrdoExamen> listAllOrdoExamen;
        private Ordonnance ordonnance;

        // CONSTRUCTOR
        public ListAllOrdoExamenAdapter(Ordonnance ordonnance ) {
            this.ordonnance = ordonnance;

            this.listAllOrdoExamen = OrdoExamen.find(OrdoExamen.class, "ordonnance = ?", ordonnance.getId().toString());
            Collections.sort(this.listAllOrdoExamen);
        }

        @Override
        public int getItemCount() {
            return listAllOrdoExamen.size();
        }

        @Override
        public ListAllOrdoExamenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllOrdoExamenViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllOrdoExamenViewHolder holder, int position) {
            OrdoExamen ordoExamen = listAllOrdoExamen.get(position);
            holder.display(ordoExamen);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newOrdoExamenActivity = new Intent(v.getContext(), NewOrdoExamenActivity.class);
                            newOrdoExamenActivity.putExtra("activitySource", ListAllOrdoExamenActivity.class);
                            newOrdoExamenActivity.putExtra("ordoExamenAModifId", ordoExamen.getId());
                            v.getContext().startActivity(newOrdoExamenActivity);
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
                                    .setTitle("Suppression OrdoExamen")
                                    .setMessage("Etes vous sûr de vouloir supprimer le ordoExamen "+ordoExamen.afficherTitre()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ordoExamen.delete();
                                            listAllOrdoExamen = OrdoExamen.find(OrdoExamen.class, "ordonnance = ?", new String[]{ordonnance.getId().toString()});
                                            Collections.sort(listAllOrdoExamen);
                                          //  Collections.sort(listAllOrdoExamen);
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
                            .setTitle(ordoExamen.afficherTitre())
                            .setMessage(ordoExamen.afficherDetail())
                            .show();
                }
            });
        }
    }











