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
import com.pouillos.monpilulier.activities.ListAllAnalyseActivity;
import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.activities.NewAnalyseActivity;
import com.pouillos.monpilulier.activities.NewUserActivity;
import com.pouillos.monpilulier.entities.Analyse;

import java.util.Collections;
import java.util.List;

public class ListAllAnalyseAdapter extends RecyclerView.Adapter<ListAllAnalyseViewHolder>{

        // FOR DATA
        private List<Analyse> listAllAnalyse;

        // CONSTRUCTOR
        public ListAllAnalyseAdapter( ) {
            this.listAllAnalyse = Analyse.listAll(Analyse.class);
            Collections.sort(this.listAllAnalyse);
        }

        @Override
        public int getItemCount() {
            return listAllAnalyse.size();
        }

        @Override
        public ListAllAnalyseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllAnalyseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllAnalyseViewHolder holder, int position) {
            Analyse analyse = listAllAnalyse.get(position);
            holder.display(analyse);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newAnalyseActivity = new Intent(v.getContext(), NewAnalyseActivity.class);
                            newAnalyseActivity.putExtra("precedent", ListAllAnalyseActivity.class);
                            newAnalyseActivity.putExtra("analyseToUpdate", analyse);
                            v.getContext().startActivity(newAnalyseActivity);
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
                                    .setTitle("Suppression Analyse")
                                    .setMessage("Etes vous sûr de vouloir supprimer le analyse "+analyse.getName()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            analyse.delete();
                                            listAllAnalyse = Analyse.listAll(Analyse.class,"name");
                                            //Collections.sort(listAllAnalyse);
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
                            .setTitle(analyse.getName())
                            .setMessage(analyse.toString())
                            .show();
                }
            });
        }
    }











