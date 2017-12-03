package com.example.daniel.pasoporti.Cliente.Historial;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mirlenys on 2/10/2017.
 */

public class CustomAdapterHistorial extends FirebaseHistorialRecyclerAdapter<CustomAdapterHistorial.ViewHolder,Servicio> {
    static OnItemClickListener clickListener;


    public CustomAdapterHistorial(Query query, Class<Servicio> servicioClass, List<Servicio> serviciosList, ArrayList<String> mAdapterKeys,Acompanado acompanado) {
        super(query);
    }

    public CustomAdapterHistorial(Query query, @Nullable ArrayList<Servicio> items, @Nullable ArrayList<String> keys) {
        super(query, items, keys);
    }

    @Override
    public CustomAdapterHistorial.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View historialView = inflater.inflate(R.layout.rowhistorial, parent, false);

        ViewHolder viewHolder = new ViewHolder(historialView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Servicio servicioList = getItem(position);
        Query query= FirebaseDatabase.getInstance().getReference("Acompanados").orderByChild("Servicios/"+servicioList.getUID()).equalTo(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Acompanado acompanado=getAcompanado(dataSnapshot);
                Log.d("CustomAdapterHistorial", "onDataChange: "+acompanado.getNombre());
                ViewHolder mViewHolderHist = viewHolder;

                mViewHolderHist.tvHistorialNombre.setText(acompanado.getNombre());
                mViewHolderHist.tvHistorialFecha.setText(servicioList.getFecha());
                mViewHolderHist.tvHistorialHora.setText(servicioList.getHora());
                mViewHolderHist.tvHistorialEstado.setText(servicioList.getEstado());


                mViewHolderHist.tvHistorialNombre.setTag(position);
                mViewHolderHist.tvHistorialFecha.setTag(position);
                mViewHolderHist.tvHistorialHora.setTag(position);
                mViewHolderHist.tvHistorialEstado.setTag(position);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvHistorialNombre;
        public TextView tvHistorialFecha;
        public TextView tvHistorialHora;
        public TextView tvHistorialEstado;


        public ViewHolder(View itemView) {
            super(itemView);
            tvHistorialNombre = (TextView) itemView.findViewById(R.id.textViewNombre);
            tvHistorialFecha = (TextView) itemView.findViewById(R.id.textViewFecha);
            tvHistorialHora = (TextView) itemView.findViewById(R.id.textViewHora);
            tvHistorialEstado = (TextView) itemView.findViewById(R.id.textViewEstado);
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

    public void SetOnItemClickListener(final CustomAdapterHistorial.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public Acompanado getAcompanado(DataSnapshot snapshot) {
        String Nombre= (String)snapshot.child("nombre").getValue();
        String TipoId= (String)snapshot.child("tipoId").getValue();
        String Identificacion= (String)snapshot.child("id").getValue();
        String Direccion= (String)snapshot.child("direccion").getValue();
        String Telefono= (String)snapshot.child("telefono").getValue();
        String Email= (String)snapshot.child("email").getValue();
        String EPS= (String)snapshot.child("eps").getValue();
        String UID= (String)snapshot.child("uid").getValue();
        String Parentesco=(String)snapshot.child("parentesco").getValue();
        return new Acompanado(Nombre,Email,Direccion,TipoId,UID,Identificacion,Telefono,EPS,Parentesco);
    }



}
