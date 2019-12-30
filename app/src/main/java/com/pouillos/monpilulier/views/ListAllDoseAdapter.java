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
import com.pouillos.monpilulier.activities.ListAllDoseActivity;
import com.pouillos.monpilulier.activities.NewDoseActivity;
import com.pouillos.monpilulier.entities.Dose;

import java.util.Collections;
import java.util.List;

public class ListAllDoseAdapter extends RecyclerView.Adapter<ListAllDoseViewHolder>{

        // FOR DATA
        private List<Dose> listAllDose;

        // CONSTRUCTOR
        public ListAllDoseAdapter( ) {
            this.listAllDose = Dose.listAll(Dose.class);
            Collections.sort(this.listAllDose);
        }

        @Override
        public int getItemCount() {
            return listAllDose.size();
        }

        @Override
        public ListAllDoseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllDoseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllDoseViewHolder holder, int position) {
            Dose dose = listAllDose.get(position);
            holder.display(dose);
            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newDoseActivity = new Intent(v.getContext(), NewDoseActivity.class);
                            newDoseActivity.putExtra("precedent", ListAllDoseActivity.class);
                            newDoseActivity.putExtra("doseToUpdate", dose);
                            v.getContext().startActivity(newDoseActivity);
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
                                    .setTitle("Suppression Dose")
                                    .setMessage("Etes vous sûr de vouloir supprimer le dose "+dose.getName()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dose.delete();
                                            listAllDose = Dose.listAll(Dose.class,"name");
                                          //  Collections.sort(listAllDose);
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
                            .setTitle(dose.getName())
                            .setMessage(dose.toString())
                            .show();
                }
            });
        }
    }











