package com.example.daniel.pasoporti;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daniel.pasoporti.Clases.Usuario;
import com.example.daniel.pasoporti.Cliente.Acompanados.AcompanadosActivity;
import com.example.daniel.pasoporti.Cliente.ClienteWelcomeActivity;
import com.example.daniel.pasoporti.Cliente.Historial.HistorialActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserReference;

    private Usuario usuario;

    private MaterialEditText Nombre, Identificacion,Direccion,Telefono,Email;
    private MaterialBetterSpinner TipoId;
    private LinearLayout LNombre, LIdentificacion,LDireccion,LTelefono,LTipoId,LOpciones;
    private Button Editar,Guardar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Intent i= getIntent();
        final boolean opts=i.getBooleanExtra("opciones",true);

        LNombre = (LinearLayout) findViewById(R.id.layout_PerfilNombre);
        LIdentificacion = (LinearLayout) findViewById(R.id.layout_PerfilIdentificacion);
        LDireccion = (LinearLayout) findViewById(R.id.layout_PerfilDireccion);
        LTelefono = (LinearLayout) findViewById(R.id.layout_PerfilTelefono);
        LTipoId = (LinearLayout) findViewById(R.id.layout_PerfilTipoDoc);
        LOpciones = (LinearLayout) findViewById(R.id.layout_Cliente);

        Nombre = (MaterialEditText) findViewById(R.id.Perfil_Nombre);
        Identificacion = (MaterialEditText) findViewById(R.id.Perfil_identificacion);
        Direccion = (MaterialEditText) findViewById(R.id.Perfil_Direccion);
        Telefono = (MaterialEditText) findViewById(R.id.Perfil_Telefono);
        Email = (MaterialEditText) findViewById(R.id.Perfil_Email);
        TipoId = (MaterialBetterSpinner) findViewById(R.id.spinner_Perfil_TipoDoc);

        Editar=(Button) findViewById(R.id.buttonEdit);
        Guardar=(Button) findViewById(R.id.buttonGuardar);

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
                    startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
                }

            }
        };
        mUserReference=mDatabase.getReference("Usuarios").child(mAuth.getCurrentUser().getUid());
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario=dataSnapshot.getValue(Usuario.class);
                Nombre.setText(usuario.getNombre());
                Identificacion.setText(usuario.getId());
                TipoId.setText(usuario.getTipoId());
                Direccion.setText(usuario.getDireccion());
                Telefono.setText(usuario.getTelefono());
                Email.setText(usuario.getEmail());
                if( !opts){
                    LOpciones.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClick_Inicio(View view) {
        Intent i= new Intent(PerfilActivity.this, ClienteWelcomeActivity.class);
        startActivity(i);
    }

    public void onClick_LogOut(View view) {
        mAuth.signOut();
        Intent i = new Intent(PerfilActivity.this,LoginActivity.class);
        startActivity(i);
    }

    public void onClick_Editar(View view) {
        Nombre.setFocusable(true);
        Nombre.setFocusableInTouchMode(true);
        Identificacion.setFocusable(true);
        Identificacion.setFocusableInTouchMode(true);
        Direccion.setFocusable(true);
        Direccion.setFocusableInTouchMode(true);
        Telefono.setFocusable(true);
        Telefono.setFocusableInTouchMode(true);
        TipoId.setFocusable(true);

        String [] tiposId=getResources().getStringArray(R.array.tiposDoc);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PerfilActivity.this, android.R.layout.simple_dropdown_item_1line, tiposId);
        TipoId.setAdapter(adapter);

        LNombre.setBackgroundResource(R.drawable.rounded);
        LIdentificacion.setBackgroundResource(R.drawable.rounded);
        LDireccion.setBackgroundResource(R.drawable.rounded);
        LTelefono.setBackgroundResource(R.drawable.rounded);
        LTipoId.setBackgroundResource(R.drawable.rounded);

        Editar.setVisibility(View.GONE);
        Guardar.setVisibility(View.VISIBLE);


    }

    public void onClick_Guardar(View view) {
        if (!CheckEmptyFields()){
            Map<String,Object> UsuarioMap = new HashMap<>();
            UsuarioMap.put("nombre", Nombre.getText().toString().trim());
            UsuarioMap.put("direccion", Direccion.getText().toString().trim());
            UsuarioMap.put("tipoId", TipoId.getText().toString().trim());
            UsuarioMap.put("id", Identificacion.getText().toString().trim());
            UsuarioMap.put("telefono", Telefono.getText().toString().trim());
            mUserReference.updateChildren(UsuarioMap);

            Toast.makeText(this, "Usuario Actualizado Exitosamente", Toast.LENGTH_SHORT).show();

            Editar.setVisibility(View.VISIBLE);
            Guardar.setVisibility(View.GONE);
            LNombre.setBackgroundResource(R.drawable.rounded_disabled);
            LIdentificacion.setBackgroundResource(R.drawable.rounded_disabled);
            LDireccion.setBackgroundResource(R.drawable.rounded_disabled);
            LTelefono.setBackgroundResource(R.drawable.rounded_disabled);
            LTipoId.setBackgroundResource(R.drawable.rounded_disabled);
            Nombre.setFocusable(false);
            Nombre.setFocusableInTouchMode(false);
            Identificacion.setFocusable(false);
            Identificacion.setFocusableInTouchMode(false);
            Direccion.setFocusable(false);
            Direccion.setFocusableInTouchMode(false);
            Telefono.setFocusable(false);
            Telefono.setFocusableInTouchMode(false);
            TipoId.setFocusable(false);
            TipoId.setDropDownHeight(0);
        }
    }

    private boolean CheckEmptyFields() {
        boolean sw=false;
        if(TextUtils.isEmpty(Nombre.getText().toString())){
            Nombre.setError("Nombre no puede estar en blanco");
            sw=true;
        }
        if(TextUtils.isEmpty(Identificacion.getText().toString())){
            Identificacion.setError("Identificación no puede estar en blanco");
            sw=true;
        }
        if(TextUtils.isEmpty(Direccion.getText().toString())){
            Direccion.setError("Dirección no puede estar en blanco");
            sw=true;
        }
        if(TextUtils.isEmpty(Email.getText().toString())){
            Email.setError("Email no puede estar en blanco");
            sw=true;
        }else {
            if(!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                sw=true;
            }
        }
        if(TextUtils.isEmpty(Telefono.getText().toString())){
            Telefono.setError("Teléfono no puede estar en blanco");
            sw=true;
        }
        return sw;

    }


    public void onClick_Cambiar(View view) {

        LayoutInflater li= LayoutInflater.from(this);
        View prompt= li.inflate(R.layout.change_email_pass,null);
        @SuppressLint("RestrictedApi") AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.Theme_AppCompat_Light_Dialog));

        alertDialogBuilder.setView(prompt);


        final MaterialBetterSpinner selTipo = (MaterialBetterSpinner) prompt.findViewById(R.id.spinner_Tipo_Cambio);
        final MaterialEditText Email = (MaterialEditText) prompt.findViewById(R.id.Perfil_Current_Email);
        final MaterialEditText Password = (MaterialEditText) prompt.findViewById(R.id.Perfil_Current_Password);
        final MaterialEditText Cambio = (MaterialEditText) prompt.findViewById(R.id.Perfil_Change);

        final String[] valores={"Email","Contraseña"};

        selTipo.setText(valores[0]);
        Cambio.setHint("Nuevo Email");
        Cambio.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PerfilActivity.this, android.R.layout.simple_dropdown_item_1line, valores);
        selTipo.setAdapter(adapter);

        selTipo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(selTipo.getText().toString().trim().equals("Email")){
                    Cambio.setHint("Nuevo Email");
                    Cambio.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }else{
                    Cambio.setHint("Nueva Contraseña");
                    Cambio.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Cambio.setMinCharacters(6);
                }
            }
        });

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Guardar", null) //Set to null. We override the onclick
                .setNegativeButton("Cancelar", null);

        final AlertDialog alertDialog=alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(alertDialog.getContext(), R.color.colorPrimary));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(alertDialog.getContext(), R.color.colorPrimary));

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(TextUtils.isEmpty(Email.getText().toString())){
                           Email.setError("Email no debe estar en blanco");
                        }
                        if(!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()){
                            Cambio.setError("Digíte Email Correcto");
                        }

                        if(TextUtils.isEmpty(Password.getText().toString())){
                            Password.setError("Contraseña no puede estar en blanco");
                        }

                        if(Password.getText().toString().length()<6){
                            Password.setError("Contraseña debe tener mas de 6 dígitos");
                        }

                        if(TextUtils.isEmpty(Cambio.getText().toString())){
                            Cambio.setError("Digíte Email Nuevo");
                        }

                        if(selTipo.getText().toString().trim().equals("Email")){
                            if(!Patterns.EMAIL_ADDRESS.matcher(Cambio.getText().toString()).matches()){
                                Cambio.setError("Digíte Email Nuevo Valido");
                            }
                        }else{
                            if(Cambio.getText().toString().length()<6){
                                Cambio.setError("Contraseña debe ser mayor de 6 Dígitos");
                            }
                        }

                        if (!TextUtils.isEmpty(Email.getText().toString())&& Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches() && !TextUtils.isEmpty(Password.getText().toString()) && Password.getText().toString().length()>=6 && !TextUtils.isEmpty(Cambio.getText().toString())){
                            AuthCredential credential= EmailAuthProvider.getCredential(Email.getText().toString(),Password.getText().toString());
                            mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(selTipo.getText().toString().trim().equals("Email")){
                                        if(Patterns.EMAIL_ADDRESS.matcher(Cambio.getText().toString()).matches()){
                                            mAuth.getCurrentUser().updateEmail(Cambio.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Map<String,Object> UsuarioMap = new HashMap<>();
                                                    UsuarioMap.put("email", Cambio.getText().toString().trim());
                                                    Toast.makeText(PerfilActivity.this,"Email Actualizado", Toast.LENGTH_SHORT);
                                                    mUserReference.updateChildren(UsuarioMap);

                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    }else{
                                        if(Cambio.getText().toString().length()<6){
                                            mAuth.getCurrentUser().updatePassword(Cambio.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(PerfilActivity.this,"Contraseña Actualizada", Toast.LENGTH_SHORT);
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    }

                                }
                            });
                            Toast.makeText(PerfilActivity.this,"Verificar Email y Contraseña", Toast.LENGTH_SHORT);

                        }

                    }
                });

            }
        });

        alertDialog.show();

    }

    public void onClick_Acompanados(View view) {
        Intent i=new Intent(PerfilActivity.this, AcompanadosActivity.class);
        startActivity(i);
    }

    public void onClick_Historial(View view) {
        Intent i = new Intent(PerfilActivity.this, HistorialActivity.class);
        startActivity(i);
    }
}
