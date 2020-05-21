package com.pouillos.monpilulier.recycler.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.RdvAnalyse;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvAnalyse;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewHolderRdvAnalyse extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.detail)
    TextView detail;

    private WeakReference<RecyclerAdapterRdvAnalyse.Listener> callbackWeakRef;

    public RecyclerViewHolderRdvAnalyse(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //ButterKnife.bind(this);
    }

    public void updateWithRdvAnalyse(RdvAnalyse rdvAnalyse, RecyclerAdapterRdvAnalyse.Listener callback){
        this.detail.setText(rdvAnalyse.toString());
        this.detail.setOnClickListener(this);
        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<RecyclerAdapterRdvAnalyse.Listener>(callback);
    }


    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        RecyclerAdapterRdvAnalyse.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickRdvAnalyseButton(getAdapterPosition());
    }
}
