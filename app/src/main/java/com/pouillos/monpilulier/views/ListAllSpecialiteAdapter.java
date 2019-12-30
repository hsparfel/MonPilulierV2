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
import com.pouillos.monpilulier.activities.ListAllSpecialiteActivity;
import com.pouillos.monpilulier.activities.NewSpecialiteActivity;
import com.pouillos.monpilulier.entities.Specialite;

import java.util.Collections;
import java.util.List;

public class ListAllSpecialiteAdapter extends RecyclerView.Adapter<ListAllSpecialiteViewHolder>{

        // FOR DATA
        private List<Specialite> listAllSpecialite;

        // CONSTRUCTOR
        public ListAllSpecialiteAdapter( ) {
            this.listAllSpecialite = Specialite.listAll(Specialite.class);
            Collections.sort(this.listAllSpecialite);
        }

        @Override
        public int getItemCount() {
            return listAllSpecialite.size();
        }

        @Override
        public ListAllSpecialiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllSpecialiteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllSpecialiteViewHolder holder, int position) {
            Specialite specialite = listAllSpecialite.get(position);
            holder.display(specialite);
            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newSpecialiteActivity = new Intent(v.getContext(), NewSpecialiteActivity.class);
                            newSpecialiteActivity.putExtra("precedent", ListAllSpecialiteActivity.class);
                            newSpecialiteActivity.putExtra("specialiteToUpdate", specialite);
                            v.getContext().startActivity(newSpecialiteActivity);
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
                                    .setTitle("Suppression Specialite")
                                    .setMessage("Etes vous sûr de vouloir supprimer le specialite "+specialite.getName()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            specialite.delete();
                                            listAllSpecialite = Specialite.listAll(Specialite.class,"name");
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
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle(specialite.getName())
                            .setMessage(specialite.toString())
                            .show();
                }
            });
        }
    }











