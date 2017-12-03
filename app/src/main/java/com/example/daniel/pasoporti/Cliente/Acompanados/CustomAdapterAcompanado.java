package com.example.daniel.pasoporti.Cliente.Acompanados;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.R;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 9/27/2017.
 */


public class CustomAdapterAcompanado extends FirebaseAcompanadoRecyclerAdapter<CustomAdapterAcompanado.ViewHolder, Acompanado> {

    static OnItemClickListener clickListener;

    public CustomAdapterAcompanado(Query query, Class<Acompanado> acompanadoClass, List<Acompanado> acompanadosList, ArrayList<String> mAdapterKeys) {
        super(query);
    }

    public CustomAdapterAcompanado(Query query, @Nullable ArrayList<Acompanado> items, @Nullable ArrayList<String> keys) {
        super(query, items, keys);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View acompanadoView = inflater.inflate(R.layout.rowacompanados, parent, false);

        ViewHolder viewHolder = new ViewHolder(acompanadoView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Acompanado acompanadoList = getItem(position);
        ViewHolder mViewHolderAcom = viewHolder;

        mViewHolderAcom.tvAcompanadoNombre.setText(acompanadoList.getNombre());
        mViewHolderAcom.tvAcompanadoTipoId.setText(acompanadoList.getTipoId());
        mViewHolderAcom.tvAcompanadoIdentificacion.setText(acompanadoList.getId());

        mViewHolderAcom.tvAcompanadoNombre.setTag(position);
        mViewHolderAcom.tvAcompanadoTipoId.setTag(position);
        mViewHolderAcom.tvAcompanadoIdentificacion.setTag(position);


    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvAcompanadoNombre;
        public TextView tvAcompanadoTipoId;
        public TextView tvAcompanadoIdentificacion;


        public ViewHolder(View itemView) {
            super(itemView);
            tvAcompanadoNombre = (TextView) itemView.findViewById(R.id.textViewNombre);
            tvAcompanadoTipoId = (TextView) itemView.findViewById(R.id.textViewTipoID);
            tvAcompanadoIdentificacion = (TextView) itemView.findViewById(R.id.textViewIdentificacion);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override protected void itemAdded(Acompanado item, String key, int position) {
        Log.d("CustomAdapterAcompanado", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(Acompanado oldItem, Acompanado newItem, String key, int position) {
        Log.d("CustomAdapterAcompanado", "Changed an item.");
    }

    @Override protected void itemRemoved(Acompanado item, String key, int position) {
        Log.d("CustomAdapterAcompanado", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(Acompanado item, String key, int oldPosition, int newPosition) {
        Log.d("CustomAdapterAcompanado", "Moved an item.");
    }

}