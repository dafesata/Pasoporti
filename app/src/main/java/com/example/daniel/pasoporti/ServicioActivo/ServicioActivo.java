package com.example.daniel.pasoporti.ServicioActivo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicioActivo extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener,DetailsFragment.OnFragmentInteractionListener{

    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mAcompanadosReference,mServiciosReference,mUserReference;
    private Query query;
    private Acompanado acompanado;

    private String servicioSelected;
    private String Tipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_activo);

        final FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabhost);

        Intent i=getIntent();
        Tipo=i.getStringExtra("Tipo");

        tabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

        mDatabase= FirebaseDatabase.getInstance();
        mAcompanadosReference = mDatabase.getReference("Acompanados");
        mServiciosReference=mDatabase.getReference("Servicios");
        mAcompanadosReference=mDatabase.getReference("Acompanados");
        mAuth= FirebaseAuth.getInstance();


        mUserReference=mDatabase.getReference("Usuarios").child(mAuth.getCurrentUser().getUid());

        if(Tipo.equals("Cliente")) {
            query = mServiciosReference.orderByChild("Usuario/" + mAuth.getCurrentUser().getUid()).equalTo(true);
        }else{
            query = mServiciosReference.orderByChild("Acompanante/" + mAuth.getCurrentUser().getUid()).equalTo(true);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int[] index = {0};
                if (dataSnapshot.getChildrenCount()==0){
                    Toast.makeText(ServicioActivo.this,"No hay Servicios",Toast.LENGTH_LONG).show();
                }
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    final Servicio servicio= new Servicio().getConvertedObject(data);
                    if (servicio.getEstado().equals("Iniciado") || servicio.getEstado().equals("En Cita") || servicio.getEstado().equals("Retorno")){
                        Query Aquery;
                        Aquery=mAcompanadosReference.orderByChild("Servicios/"+servicio.getUID()).equalTo(true);
                        Aquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot snap:dataSnapshot.getChildren()) {
                                    acompanado = new Acompanado().getConvertedObject(snap);
                                    tabHost.addTab(tabHost.newTabSpec(String.valueOf(servicio.getUID())).setIndicator(acompanado.getNombre()),
                                            ParentViewPagerFragment.class, null);
                                    index[0]++;

                                }
                                if (index[0]==0){
                                    Toast.makeText(ServicioActivo.this,"No hay Servicios Activos",Toast.LENGTH_LONG).show();
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                servicioSelected=tabHost.getCurrentTabTag();

            }
        });


    }


    @Override
    public void onFragmentInteraction(String text) {

    }

    @Override
    public String getServicio() {
        return servicioSelected;
    }

    @Override
    public String getTipo() {
        return Tipo;
    }

    public void onClick_Inicio(View view) {
        finish();
    }
}
