package com.pouillos.monpilulier.recycler.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.RdvContact;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvContact;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewHolderRdvContact extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.detail)
    TextView detail;

    private WeakReference<RecyclerAdapterRdvContact.Listener> callbackWeakRef;

    public RecyclerViewHolderRdvContact(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //ButterKnife.bind(this);
    }

    public void updateWithRdvContact(RdvContact rdvContact, RecyclerAdapterRdvContact.Listener callback){
        this.detail.setText(rdvContact.toString());
        this.detail.setOnClickListener(this);
        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<RecyclerAdapterRdvContact.Listener>(callback);
    }


    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        RecyclerAdapterRdvContact.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickRdvContactButton(getAdapterPosition());
    }
}
