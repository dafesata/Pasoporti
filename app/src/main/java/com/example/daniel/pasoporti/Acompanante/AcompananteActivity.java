package com.example.daniel.pasoporti.Acompanante;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.daniel.pasoporti.Clases.Usuario;
import com.example.daniel.pasoporti.Historial.HistorialActivity;
import com.example.daniel.pasoporti.LoginActivity;
import com.example.daniel.pasoporti.PerfilActivity;
import com.example.daniel.pasoporti.R;
import com.example.daniel.pasoporti.ServicioActivo.ServicioActivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcompananteActivity extends AppCompatActivity {

    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserReference;

    private Usuario usuario;
    private TextView labelUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanante);

        mDatabase=FirebaseDatabase.getInstance();
        mUserReference=mDatabase.getReference("Usuarios");
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
                    startActivity(new Intent(AcompananteActivity.this, LoginActivity.class));
                }
                // ...
            }
        };
        labelUsuario=(TextView) findViewById(R.id.textViewUsuario);

        mUserReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario=dataSnapshot.getValue(Usuario.class);
                labelUsuario.setText(usuario.getNombre().trim());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClick_Inicio(View view) {
        finish();
    }

    public void onClick_MisServicios(View view) {
        Intent i = new Intent(AcompananteActivity.this, HistorialActivity.class);
        i.putExtra("Tipo","Acompanante");
        startActivity(i);
    }

    public void onClick_Perfil(View view) {
        Intent i = new Intent(AcompananteActivity.this, PerfilActivity.class);
        i.putExtra("opciones",false);
        startActivity(i);
    }

    public void onClick_ServiciosActivos(View view) {
        Intent i=new Intent(AcompananteActivity.this, ServicioActivo.class);
        i.putExtra("Tipo","Acompanante");
        startActivity(i);
    }

    public void onClick_LogOut(View view) {
        mAuth.signOut();
        Intent i = new Intent(AcompananteActivity.this,LoginActivity.class);
        startActivity(i);
    }
}
