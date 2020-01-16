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
import com.pouillos.monpilulier.activities.listallx.ListAllCabinetActivity;
import com.pouillos.monpilulier.activities.newx.NewCabinetActivity;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.views.viewholder.ListAllCabinetViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllCabinetAdapter extends RecyclerView.Adapter<ListAllCabinetViewHolder>{

        // FOR DATA
        private List<Cabinet> listAllCabinet;

        // CONSTRUCTOR
        public ListAllCabinetAdapter( ) {
            this.listAllCabinet = Cabinet.listAll(Cabinet.class);
            Collections.sort(this.listAllCabinet);
        }

        @Override
        public int getItemCount() {
            return listAllCabinet.size();
        }

        @Override
        public ListAllCabinetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllCabinetViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllCabinetViewHolder holder, int position) {
            Cabinet cabinet = listAllCabinet.get(position);
            holder.display(cabinet);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newCabinetActivity = new Intent(v.getContext(), NewCabinetActivity.class);
                            newCabinetActivity.putExtra("activitySource", ListAllCabinetActivity.class);
                            newCabinetActivity.putExtra("cabinetAModifId", cabinet.getId());
                            v.getContext().startActivity(newCabinetActivity);
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
                                    .setTitle("Suppression Cabinet")
                                    .setMessage("Etes vous sûr de vouloir supprimer le cabinet "+cabinet.afficherTitre()+" ?"
                                            +"\nAttention, cette suppression entrainera la suppression d'autres elements liés")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            cabinet.delete();
                                            OrdoAnalyse.deleteAll(OrdoAnalyse.class,"cabinet = ?",cabinet.getId().toString());


                                            listAllCabinet = Cabinet.listAll(Cabinet.class,"name");
                                          //  Collections.sort(listAllCabinet);
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
                            .setTitle(cabinet.afficherTitre())
                            .setMessage(cabinet.afficherDetail())
                            .show();
                }
            });
        }
    }











