package com.example.daniel.pasoporti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.example.daniel.pasoporti.Acompanante.AcompananteWelcomeActivity;
import com.example.daniel.pasoporti.Clases.FontChangeCrawler;
import com.example.daniel.pasoporti.Clases.Usuario;
import com.example.daniel.pasoporti.Cliente.ClienteWelcomeActivity;
import com.example.daniel.pasoporti.Registro.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG= this.getClass().getName();

    private MaterialEditText email,password;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserReference;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=(MaterialEditText) findViewById(R.id.login_email);
        password=(MaterialEditText) findViewById(R.id.login_password);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        mDatabase=FirebaseDatabase.getInstance();
        mUserReference=mDatabase.getReference("Usuarios");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    mUserReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario= dataSnapshot.getValue(Usuario.class);
                            if(usuario.getRol().equals("Cliente")){
                                Intent i=new Intent(LoginActivity.this, ClienteWelcomeActivity.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onClick_Register(View view) {
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);

    }

    public void onClick_Login(View view) {

        if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {


            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            spinner.setVisibility(View.VISIBLE);
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                spinner.setVisibility(View.GONE);
                                if (password.getText().toString().length() < 6) {
                                    Toast.makeText(LoginActivity.this, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();

                                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                                    Toast.makeText(LoginActivity.this, "Email no valido", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Email o contraseña incorrecta! Verifíquelo e intente de nuevo", Toast.LENGTH_SHORT).show();

                                }
                                Log.w(TAG, "signInWithEmail:failed", task.getException());

                            } else {
                                FirebaseUser usuario = mAuth.getCurrentUser();

                                mUserReference.child(usuario.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        spinner.setVisibility(View.GONE);
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                        if (usuario.getRol().equals("Cliente")) {
                                            Intent i = new Intent(LoginActivity.this, ClienteWelcomeActivity.class);
                                            startActivity(i);
                                        }else{
                                            Intent i= new Intent(LoginActivity.this, AcompananteWelcomeActivity.class);
                                            startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            // ...
                        }
                    });
        }else{
            if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                email.setError("Digíte Email Correcto");
            }
            if(TextUtils.isEmpty(email.getText().toString()) && TextUtils.isEmpty(password.getText().toString())) {
                email.setError("Digíte Email");
                password.setError("Digíte Contraseña");
            }else {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.setError("Digíte Email");
                } else {
                    password.setError("Digíte Contraseña");
                }
            }
        }
    }
/*
    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "segoeui.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
    }*/
}
