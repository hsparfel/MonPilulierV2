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
import com.pouillos.monpilulier.activities.listallx.ListAllOrdoPrescriptionActivity;
import com.pouillos.monpilulier.activities.newx.NewOrdoPrescriptionActivity;
import com.pouillos.monpilulier.entities.OrdoPrescription;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.views.viewholder.ListAllOrdoPrescriptionViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllOrdoPrescriptionAdapter extends RecyclerView.Adapter<ListAllOrdoPrescriptionViewHolder>{

        // FOR DATA
        private List<OrdoPrescription> listAllOrdoPrescription;
        private Ordonnance ordonnance;

        // CONSTRUCTOR
        public ListAllOrdoPrescriptionAdapter(Ordonnance ordonnance ) {
            this.ordonnance = ordonnance;

            this.listAllOrdoPrescription = OrdoPrescription.find(OrdoPrescription.class, "ordonnance = ?", ordonnance.getId().toString());
            Collections.sort(this.listAllOrdoPrescription);
        }

        @Override
        public int getItemCount() {
            return listAllOrdoPrescription.size();
        }

        @Override
        public ListAllOrdoPrescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllOrdoPrescriptionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllOrdoPrescriptionViewHolder holder, int position) {
            OrdoPrescription ordoPrescription = listAllOrdoPrescription.get(position);
            holder.display(ordoPrescription);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newOrdoPrescriptionActivity = new Intent(v.getContext(), NewOrdoPrescriptionActivity.class);
                            newOrdoPrescriptionActivity.putExtra("activitySource", ListAllOrdoPrescriptionActivity.class);
                            newOrdoPrescriptionActivity.putExtra("ordoPrescriptionAModifId", ordoPrescription.getId());
                            v.getContext().startActivity(newOrdoPrescriptionActivity);
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
                                    .setTitle("Suppression OrdoPrescription")
                                    .setMessage("Etes vous sûr de vouloir supprimer le ordoPrescription "+ordoPrescription.afficherTitre()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ordoPrescription.delete();
                                            listAllOrdoPrescription = OrdoPrescription.find(OrdoPrescription.class, "ordonnance = ?", new String[]{ordonnance.getId().toString()});
                                            Collections.sort(listAllOrdoPrescription);
                                          //  Collections.sort(listAllOrdoPrescription);
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
                            .setTitle(ordoPrescription.afficherTitre())
                            .setMessage(ordoPrescription.afficherDetail())
                            .show();
                }
            });
        }
    }











