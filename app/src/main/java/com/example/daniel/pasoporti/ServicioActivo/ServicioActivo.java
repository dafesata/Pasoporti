package com.example.daniel.pasoporti.ServicioActivo;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class ServicioActivo extends AppCompatActivity {

    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mAcompanadosReference,mServiciosReference,mUserReference;
    private Query query;
    private Acompanado acompanado;
    private ViewPager mViewPager;
    private ViewPagerAdapterFather pagerAdapter;


    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_activo);

        final List<Servicio>[] serviciosList = new ArrayList[1];
        serviciosList[0]=new ArrayList<>();
        tabs=(TabLayout) findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager=(ViewPager) findViewById(R.id.view_pager_parent);

        mDatabase= FirebaseDatabase.getInstance();
        mAcompanadosReference = mDatabase.getReference("Acompanados");
        mServiciosReference=mDatabase.getReference("Servicios");
        mAcompanadosReference=mDatabase.getReference("Acompanados");
        mAuth= FirebaseAuth.getInstance();


        mUserReference=mDatabase.getReference("Usuarios").child(mAuth.getCurrentUser().getUid());

        query = mServiciosReference.orderByChild("Usuario/"+mAuth.getCurrentUser().getUid()).equalTo(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Servicio servicio= new Servicio().getConvertedObject(data);
                    if (servicio.getEstado().equals("Iniciado") || servicio.getEstado().equals("En Cita") || servicio.getEstado().equals("Retorno")){
                        serviciosList[0].add(servicio);
                        Query Aquery;
                        Aquery=mAcompanadosReference.orderByChild("Servicios/"+servicio.getUID()).equalTo(true);
                        Aquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snap:dataSnapshot.getChildren()) {
                                    acompanado = new Acompanado().getConvertedObject(snap);
                                    tabs.addTab(tabs.newTab().setText(acompanado.getNombre()));
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

    }

    public void onClick_Inicio(View view) {
        finish();
    }
}
