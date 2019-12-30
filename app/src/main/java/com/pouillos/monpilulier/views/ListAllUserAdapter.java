package com.pouillos.monpilulier.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.stetho.common.Util;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.ListAllAnalyseActivity;
import com.pouillos.monpilulier.activities.ListAllUserActivity;
import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.activities.NewUserActivity;
import com.pouillos.monpilulier.entities.Utilisateur;

import java.util.Collections;
import java.util.List;

public class ListAllUserAdapter extends RecyclerView.Adapter<ListAllUserViewHolder>{

        private List<Utilisateur> listAllUser;

        public ListAllUserAdapter( ) {
            this.listAllUser = Utilisateur.listAll(Utilisateur.class);
            Collections.sort(this.listAllUser);
        }

        @Override
        public int getItemCount() {
            return listAllUser.size();
        }

        @Override
        public ListAllUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_user_item, parent, false);
            return new ListAllUserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllUserViewHolder holder, int position) {
            Utilisateur utilisateur = listAllUser.get(position);
            holder.display(utilisateur);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utilisateur.setActif(true);
                    utilisateur.save();
                    List<Utilisateur> listUserADesactiver = (List<Utilisateur>) Utilisateur.find(Utilisateur.class, "id <> ?", utilisateur.getId().toString());
                    for (Utilisateur userToDesactivate : listUserADesactiver) {
                        userToDesactivate.setActif(false);
                        userToDesactivate.save();
                    }
                    goToHomePage(holder.itemView);
                }
            });
        }

        public void goToHomePage(View view) {
            Intent nextActivity = new Intent(view.getContext(), MainActivity.class);
            nextActivity.putExtra("precedent", ListAllUserActivity.class);
            view.getContext().startActivity(nextActivity);
            ((Activity)view.getContext()).finish();
        }
    }











