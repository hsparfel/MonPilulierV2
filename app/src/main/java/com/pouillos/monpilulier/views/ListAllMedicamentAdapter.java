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
import com.pouillos.monpilulier.activities.ListAllMedicamentActivity;
import com.pouillos.monpilulier.activities.NewMedicamentActivity;
import com.pouillos.monpilulier.entities.Medicament;

import java.util.Collections;
import java.util.List;

public class ListAllMedicamentAdapter extends RecyclerView.Adapter<ListAllMedicamentViewHolder>{

        // FOR DATA
        private List<Medicament> listAllMedicament;

        // CONSTRUCTOR
        public ListAllMedicamentAdapter( ) {
            this.listAllMedicament = Medicament.listAll(Medicament.class);
            Collections.sort(this.listAllMedicament);
        }

        @Override
        public int getItemCount() {
            return listAllMedicament.size();
        }

        @Override
        public ListAllMedicamentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllMedicamentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllMedicamentViewHolder holder, int position) {
            Medicament medicament = listAllMedicament.get(position);
            holder.display(medicament);
            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newMedicamentActivity = new Intent(v.getContext(), NewMedicamentActivity.class);
                            newMedicamentActivity.putExtra("precedent", ListAllMedicamentActivity.class);
                            newMedicamentActivity.putExtra("medicamentToUpdate", medicament);
                            v.getContext().startActivity(newMedicamentActivity);
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
                                    .setTitle("Suppression Medicament")
                                    .setMessage("Etes vous sûr de vouloir supprimer le medicament "+medicament.getName()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            medicament.delete();
                                            listAllMedicament = Medicament.listAll(Medicament.class,"name");
                                           // Collections.sort(listAllMedicament);
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
                            .setTitle(medicament.getName())
                            .setMessage(medicament.toString())
                            .show();
                }
            });
        }
    }











