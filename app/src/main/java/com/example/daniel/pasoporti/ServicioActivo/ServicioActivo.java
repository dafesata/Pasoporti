package com.example.daniel.pasoporti.ServicioActivo;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ServicioActivo extends AppCompatActivity {

    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mAcompanadosReference,mServiciosReference,mUserReference;
    private Acompanado acompanado;

    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_activo);

        final List<Servicio>[] serviciosList = new ArrayList[1];

        mDatabase= FirebaseDatabase.getInstance();
        mAcompanadosReference = mDatabase.getReference("Acompanados");
        mServiciosReference=mDatabase.getReference("Servicios");
        mAuth= FirebaseAuth.getInstance();

        tabs=(TabLayout) findViewById(R.id.tabs);
    }

    public void onClick_Inicio(View view) {
        finish();
    }
}
