package com.example.daniel.pasoporti.Cliente.Historial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.Cliente.ProgramarServicios.ProgramarServicioActivity;
import com.example.daniel.pasoporti.Cliente.ProgramarServicios.SpinnerAdapterAcompanado;
import com.example.daniel.pasoporti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private CoordinatorLayout layoutRoot;

    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mAcompanadosReference,mServiciosReference;
    private Acompanado acompanado;

    private Query query;
    private SpinnerAdapterAcompanado adapterAcompanado;
    private MaterialBetterSpinner AcompanadoSpinner;
    private ArrayList<Acompanado> acompanadoList;
    private ArrayList<String> keys;

    private Query rQuery,AQuery;
    private RecyclerView list;
    private ArrayList<String> mAdapterKeys;
    Acompanado Hacompanado;

    private LinearLayout layoutIdentificacion,layoutTipoId;
    private MaterialEditText Historial_TipoId,Historial_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Intent j=getIntent();
        String UID=j.getStringExtra("AcompanadoUID");

        final CustomAdapterHistorial[] mCustomAdapterHistorial = new CustomAdapterHistorial[1];
        final List<Servicio>[] serviciosList = new ArrayList[1];

        mDatabase= FirebaseDatabase.getInstance();
        mAcompanadosReference = mDatabase.getReference("Acompanados");
        mServiciosReference=mDatabase.getReference("Servicios");
        mAuth= FirebaseAuth.getInstance();

        layoutIdentificacion= (LinearLayout) findViewById(R.id.layout_Historial_Identificacion);
        layoutTipoId = (LinearLayout) findViewById(R.id.layout_HistorialTipoDoc);
        Historial_Id=(MaterialEditText) findViewById(R.id.Historial_identificacion);
        Historial_TipoId= (MaterialEditText) findViewById(R.id.Historial_TipoDoc);

        Historial_TipoId.setFocusable(false);
        Historial_TipoId.setFocusableInTouchMode(false);
        Historial_Id.setFocusable(false);
        Historial_Id.setFocusableInTouchMode(false);

        AcompanadoSpinner= (MaterialBetterSpinner) findViewById(R.id.spinner_Historial_Nombre);
        acompanadoList=new ArrayList<Acompanado>();
        acompanadoList.add(new Acompanado("Todos los Acompañados","1"));
        keys=new ArrayList<String>();
        keys.add("1");
        query = mAcompanadosReference.orderByChild("Usuario/"+mAuth.getCurrentUser().getUid()).equalTo(true);
        adapterAcompanado= new SpinnerAdapterAcompanado(query,acompanadoList,keys, HistorialActivity.this,android.R.layout.simple_dropdown_item_1line);


        list=(RecyclerView) findViewById(R.id.RecyclerView);


        AcompanadoSpinner.setAdapter(adapterAcompanado);
        AcompanadoSpinner.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(TextUtils.equals(AcompanadoSpinner.getText().toString(),"Todos los Acompañados")){
                    layoutTipoId.setBackgroundResource(R.drawable.rounded_disabled);
                    layoutIdentificacion.setBackgroundResource(R.drawable.rounded_disabled);
                    Historial_Id.setText(null);
                    Historial_TipoId.setText(null);

                    rQuery=mServiciosReference.orderByChild("Usuario/"+mAuth.getCurrentUser().getUid()).equalTo(true);

                }else{
                    layoutTipoId.setBackgroundResource(R.drawable.rounded);
                    layoutIdentificacion.setBackgroundResource(R.drawable.rounded);

                    Hacompanado=adapterAcompanado.getItem(AcompanadoSpinner.getText().toString());
                    Historial_Id.setText(Hacompanado.getId());
                    Historial_TipoId.setText(Hacompanado.getTipoId());
                    rQuery=mServiciosReference.orderByChild("Acompanado/"+Hacompanado.getUID()).equalTo(true);
                }
                serviciosList[0] =new ArrayList<>();
                mCustomAdapterHistorial[0] =new CustomAdapterHistorial(rQuery,Servicio.class, serviciosList[0],mAdapterKeys,Hacompanado);

                list.setLayoutManager(new LinearLayoutManager(HistorialActivity.this));
                list.setAdapter(mCustomAdapterHistorial[0]);

                mCustomAdapterHistorial[0].SetOnItemClickListener(new CustomAdapterHistorial.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "onItemClick: "+position);
                        Intent i= new Intent(HistorialActivity.this,HistorialDetails.class);
                        Servicio servicio=mCustomAdapterHistorial[0].getItem(position);
                        i.putExtra("ServicioId",servicio.getUID());
                        startActivity(i);


                    }
                });

            }
        });


        if(!TextUtils.isEmpty(UID)){
            mAcompanadosReference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Acompanado PAcompanado = getConvertedObject(dataSnapshot);
                    AcompanadoSpinner.setText(PAcompanado.getNombre());
                    AcompanadoSpinner.setFocusable(false);
                    AcompanadoSpinner.setFocusableInTouchMode(false);
                    AcompanadoSpinner.setDropDownHeight(0);
                    Historial_TipoId.setText(PAcompanado.getTipoId());
                    Historial_Id.setText(PAcompanado.getId());

                    rQuery=mServiciosReference.orderByChild("Acompanado/"+Hacompanado.getUID()).equalTo(true);
                    serviciosList[0] =new ArrayList<>();
                    mCustomAdapterHistorial[0] =new CustomAdapterHistorial(rQuery,Servicio.class, serviciosList[0],mAdapterKeys,Hacompanado);

                    list.setLayoutManager(new LinearLayoutManager(HistorialActivity.this));
                    list.setAdapter(mCustomAdapterHistorial[0]);

                    mCustomAdapterHistorial[0].SetOnItemClickListener(new CustomAdapterHistorial.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Log.d(TAG, "onItemClick: "+position);
                            Intent i= new Intent(HistorialActivity.this,HistorialDetails.class);
                            startActivity(i);
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }





    }

    public void onClick_Inicio(View view) {
        finish();
    }

    private Acompanado getConvertedObject(DataSnapshot snapshot) {
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
