package com.example.daniel.pasoporti.Cliente.Historial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.Clases.Usuario;
import com.example.daniel.pasoporti.LoginActivity;
import com.example.daniel.pasoporti.PerfilActivity;
import com.example.daniel.pasoporti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class HistorialDetails extends AppCompatActivity {
    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mAcompanadosReference,mServiciosReference;
    private DatabaseReference mUserReference;
    private Usuario usuario;
    private Acompanado acompanado;
    private Servicio servicio;
    private Query query;

    private MaterialEditText IdServicio, Nombre,TipoId,Identificacion,DirRecogida,Informe;
    private MaterialEditText DirCita,DirFinal,Acompanante,TipoServicio,CAcompanante,CConductor,CVehiculo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_details);
        Intent j=getIntent();
        String UID=j.getStringExtra("ServicioId");

        mDatabase= FirebaseDatabase.getInstance();
        mAcompanadosReference = mDatabase.getReference("Acompanados");
        mServiciosReference=mDatabase.getReference("Servicios");
        mAuth= FirebaseAuth.getInstance();

        IdServicio=(MaterialEditText) findViewById(R.id.historial_idserv);
        Nombre=(MaterialEditText) findViewById(R.id.historial_nombre);
        TipoId=(MaterialEditText) findViewById(R.id.historial_tipoDoc);
        Identificacion=(MaterialEditText) findViewById(R.id.historial_numidentificacion);
        DirRecogida=(MaterialEditText) findViewById(R.id.historial_direcogida);
        DirCita=(MaterialEditText) findViewById(R.id.historial_direcita);
        DirFinal=(MaterialEditText) findViewById(R.id.historial_direfinalserv);
        Informe=(MaterialEditText) findViewById(R.id.historial_informe);
        Acompanante=(MaterialEditText) findViewById(R.id.historial_acompanante);
        CAcompanante=(MaterialEditText) findViewById(R.id.historial_califacomp);
        CConductor=(MaterialEditText) findViewById(R.id.historial_califcond);
        CVehiculo=(MaterialEditText) findViewById(R.id.historial_califveh);
        TipoServicio=(MaterialEditText) findViewById(R.id.historial_tiposerv);

        mDatabase=FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                    startActivity(new Intent(HistorialDetails.this, LoginActivity.class));
                }

            }
        };
        mUserReference=mDatabase.getReference("Usuarios").child(mAuth.getCurrentUser().getUid());

        mServiciosReference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicio=(Servicio) dataSnapshot.getValue();

                mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuario=dataSnapshot.getValue(Usuario.class);
                        IdServicio.setText(servicio.getId().toString());
                        Nombre.setText(usuario.getNombre());
                        TipoId.setText(usuario.getTipoId());
                        Identificacion.setText(usuario.getId());
                        DirRecogida.setText(servicio.getDirRecogida());
                        DirCita.setText(servicio.getDirLlevar());
                        DirFinal.setText(servicio.getDirRegreso());
                        /*
                            Falta Informacion Acompañante
                         */
                        TipoServicio.setText(servicio.getTipoServicio());
                        TipoServicio.setText(servicio.getInforme());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onClick_Inicio(View view) {
    }
}
