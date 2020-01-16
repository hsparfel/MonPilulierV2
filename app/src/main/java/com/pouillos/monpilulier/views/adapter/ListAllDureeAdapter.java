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
import com.pouillos.monpilulier.activities.listallx.ListAllDureeActivity;
import com.pouillos.monpilulier.activities.newx.NewDureeActivity;
import com.pouillos.monpilulier.entities.Duree;
import com.pouillos.monpilulier.views.viewholder.ListAllDureeViewHolder;

import java.util.Collections;
import java.util.List;

public class ListAllDureeAdapter extends RecyclerView.Adapter<ListAllDureeViewHolder>{

        // FOR DATA
        private List<Duree> listAllDuree;

        // CONSTRUCTOR
        public ListAllDureeAdapter( ) {
            this.listAllDuree = Duree.listAll(Duree.class);
            Collections.sort(this.listAllDuree);
        }

        @Override
        public int getItemCount() {
            return listAllDuree.size();
        }

        @Override
        public ListAllDureeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllDureeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllDureeViewHolder holder, int position) {
            Duree duree = listAllDuree.get(position);
            holder.display(duree);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newDureeActivity = new Intent(v.getContext(), NewDureeActivity.class);
                            newDureeActivity.putExtra("activitySource", ListAllDureeActivity.class);
                            newDureeActivity.putExtra("dureeAModifId", duree.getId());
                            v.getContext().startActivity(newDureeActivity);
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
                                    .setTitle("Suppression Duree")
                                    .setMessage("Etes vous sûr de vouloir supprimer le duree "+duree.afficherTitre()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            duree.delete();
                                            listAllDuree = Duree.listAll(Duree.class);
                                            Collections.sort(listAllDuree);
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("Non", null)
                                    .show();
                        }
                    }
            );


        }
    }











