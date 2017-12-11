package com.example.daniel.pasoporti.Cliente.Acompanados;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.Historial.HistorialActivity;
import com.example.daniel.pasoporti.PerfilActivity;
import com.example.daniel.pasoporti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcompanadosActivity extends AppCompatActivity {

    private CoordinatorLayout layoutRoot;

    private String TAG= this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserReference,mAcompanadosReference,mServiciosReference;
    private Acompanado acompanado;

    private RecyclerView list;

    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    private Query mQuery;
    private CustomAdapterAcompanado mCustomAdapterAcompanado;
    private List<Acompanado> acompanadosList;
    private ArrayList<String> mAdapterKeys;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanados);
        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        mDatabase=FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();
        mUserReference=mDatabase.getReference("Usuarios").child(mAuth.getCurrentUser().getUid());
        mAcompanadosReference=mDatabase.getReference("Acompanados");
        mServiciosReference=mDatabase.getReference("Servicios");

        mQuery = mAcompanadosReference.orderByChild("Usuario/"+mAuth.getCurrentUser().getUid()).equalTo(true);
        acompanadosList=new ArrayList<>();
        list = (RecyclerView)findViewById(R.id.RecyclerView);
        mCustomAdapterAcompanado = new CustomAdapterAcompanado(mQuery,Acompanado.class, acompanadosList,mAdapterKeys);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mCustomAdapterAcompanado);


        handleInstanceState(savedInstanceState);



        mCustomAdapterAcompanado.SetOnItemClickListener(new CustomAdapterAcompanado.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                acompanado=mCustomAdapterAcompanado.getItem(position);
                AnadirAcompanado(false);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnadirAcompanado(true);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Acompanado acompanadoD= mCustomAdapterAcompanado.getItem(position);
                final List<Servicio> ServiciosList=new ArrayList<>();

                @SuppressLint("RestrictedApi") AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(AcompanadosActivity.this,R.style.Theme_AppCompat_Light_Dialog));
                alertDialogBuilder.setMessage("Al eliminar el acompañado se elminaran todos los servicios asociados a este, ¿desea continuar?")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", null)
                        .setNegativeButton("Cancelar",null);
                final AlertDialog alertDialog=alertDialogBuilder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(alertDialog.getContext(), R.color.colorPrimary));
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(alertDialog.getContext(), R.color.colorPrimary));

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        Button cancelar= ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mUserReference.child("Acompanados").child(acompanadoD.getUID()).removeValue();
                                mAcompanadosReference.child(acompanadoD.getUID()).removeValue();
                                mServiciosReference.orderByChild("Acompanado/"+acompanadoD.getUID()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snap: dataSnapshot.getChildren()){
                                            ServiciosList.add(getConvertedObject(snap));
                                            snap.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                dialog.dismiss();

                                Snackbar.make(layoutRoot, "Acompañado Eliminado", Snackbar.LENGTH_SHORT)
                                        .setAction("DESHACER", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final String key=acompanadoD.getUID();
                                                Map<String,Object> AcompanadoValues= acompanadoD.toMap();

                                                Map<String,Object> childUpdates= new HashMap<>();
                                                childUpdates.put("/Usuarios/"+mAuth.getCurrentUser().getUid()+"/Acompanados/"+key,true);
                                                childUpdates.put("/Acompanados/"+key,AcompanadoValues);

                                                for(Servicio serv:ServiciosList){
                                                    childUpdates.put("/Servicios/"+serv.getUID(),serv.toMap());
                                                    childUpdates.put("/Servicios/"+serv.getUID()+"/Usuario/"+mAuth.getCurrentUser().getUid(),true);
                                                    childUpdates.put("/Servicios/"+serv.getUID()+"/Acompanado/"+key,true);
                                                }

                                                mDatabase.getReference().updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDatabase.getReference("Acompanados").child(key).child("Usuario").child(mAuth.getCurrentUser().getUid()).setValue(true);

                                                    }
                                                });


                                            }
                                        })
                                        .show();

                            }
                        });
                    }
                });

                alertDialog.show();

            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(list);


    }



    public void onClick_Inicio(View view) {
        Intent i= new Intent(AcompanadosActivity.this, PerfilActivity.class);
        startActivity(i);
    }

    private void AnadirAcompanado(final boolean Crear) {
        LayoutInflater li= LayoutInflater.from(this);
        View prompt= li.inflate(R.layout.addacompanados,null);
        @SuppressLint("RestrictedApi") AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.Theme_AppCompat_Light_Dialog));

        alertDialogBuilder.setView(prompt);



        final MaterialEditText Nombre =(MaterialEditText) prompt.findViewById(R.id.Acompanado_Nombre);
        final MaterialBetterSpinner TipoId =(MaterialBetterSpinner) prompt.findViewById(R.id.spinner_Acompanado_TipoDoc);
        final MaterialEditText Identificacion=(MaterialEditText) prompt.findViewById(R.id.Acompanado_identificacion);
        final MaterialEditText Direccion=(MaterialEditText) prompt.findViewById(R.id.Acompanado_Direccion);
        final MaterialEditText Telefono=(MaterialEditText) prompt.findViewById(R.id.Acompanado_Telefono);
        final MaterialEditText EPS=(MaterialEditText) prompt.findViewById(R.id.Acompanado_EPS);
        final MaterialEditText Email=(MaterialEditText) prompt.findViewById(R.id.Acompanado_Email);
        final MaterialBetterSpinner Parentesco =(MaterialBetterSpinner) prompt.findViewById(R.id.spinner_Acompanado_Parentesco);
        final LinearLayout layoutServicios= (LinearLayout) prompt.findViewById(R.id.layout_acompanado_servicios);
        final ImageButton AcompanadoHisto=(ImageButton) prompt.findViewById(R.id.imageButtonHistorial);

        final String[] tiposDoc= getResources().getStringArray(R.array.tiposDoc);
        ArrayAdapter<String> adapterDocs = new ArrayAdapter<>(AcompanadosActivity.this, android.R.layout.simple_dropdown_item_1line, tiposDoc);
        TipoId.setAdapter(adapterDocs);

        final String[] tipoParentescos = getResources().getStringArray(R.array.tiposParentescos);
        ArrayAdapter<String> adapterParentescos = new ArrayAdapter<>(AcompanadosActivity.this, android.R.layout.simple_dropdown_item_1line, tipoParentescos);
        Parentesco.setAdapter(adapterParentescos);


        if (!Crear){
            layoutServicios.setVisibility(View.VISIBLE);
            mAcompanadosReference.child(acompanado.getUID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Acompanado profile= dataSnapshot.getValue(Acompanado.class);
                    if(profile!=null) {
                        Nombre.setText(profile.getNombre());
                        TipoId.setText(profile.getTipoId());
                        Identificacion.setText(profile.getId());
                        Direccion.setText(profile.getDireccion());
                        Telefono.setText(profile.getTelefono());
                        EPS.setText(profile.getEPS());
                        Email.setText(profile.getEmail());
                        Parentesco.setText(profile.getParentesco());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        AcompanadoHisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AcompanadosActivity.this, HistorialActivity.class);
                i.putExtra("Tipo","Cliente");
                i.putExtra("AcompanadoUID",acompanado.getUID());
                startActivity(i);
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
                        if(TextUtils.isEmpty(Nombre.getText().toString())){
                            Nombre.setError("Digíte Nombre");
                        }
                        if(TextUtils.isEmpty(TipoId.getText().toString())){
                            TipoId.setError("Seleccione Tipo ID");
                        }

                        if(TextUtils.isEmpty(Identificacion.getText().toString())){
                            Identificacion.setError("Digíte Identificacion");
                        }
                        if(TextUtils.isEmpty(Direccion.getText().toString())){
                            Direccion.setError("Digíte Direccion");
                        }
                        if(TextUtils.isEmpty(Telefono.getText().toString())){
                            Telefono.setError("Digíte Telefono");
                        }
                        if(TextUtils.isEmpty(EPS.getText().toString())){
                            EPS.setError("Digíte EPS");
                        }
                        if(TextUtils.isEmpty(Parentesco.getText().toString())){
                            Parentesco.setError("Seleccione Parentesco");
                        }
                        if(TextUtils.isEmpty(Email.getText().toString())){
                            Email.setError("Digíte Email");
                        }else{
                            if(!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()){
                                Email.setError("Digíte Email Válido");
                            }
                        }

                        if (!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !TextUtils.isEmpty(EPS.getText().toString()) && !TextUtils.isEmpty(Email.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches() && !TextUtils.isEmpty(Parentesco.getText().toString())){


                            final String key= mAcompanadosReference.push().getKey();
                            Acompanado NewAcompanado = new Acompanado(Nombre.getText().toString().trim(),Email.getText().toString().trim(),Direccion.getText().toString().trim(),TipoId.getText().toString(),key,Identificacion.getText().toString().trim(),Telefono.getText().toString().trim(),EPS.getText().toString().trim(),Parentesco.getText().toString().trim());
                            HashMap<String,Object> UpdateAcompanado = new HashMap<String, Object>();;

                            if(!Crear){

                                NewAcompanado.setUID(acompanado.getUID());
                                UpdateAcompanado.put("nombre",Nombre.getText().toString().trim());
                                UpdateAcompanado.put("tipoId",TipoId.getText().toString().trim());
                                UpdateAcompanado.put("id",Identificacion.getText().toString().trim());
                                UpdateAcompanado.put("direccion",Direccion.getText().toString().trim());
                                UpdateAcompanado.put("telefono",Telefono.getText().toString().trim());
                                UpdateAcompanado.put("parentesco",Parentesco.getText().toString().trim());
                                UpdateAcompanado.put("email",Email.getText().toString().trim());
                                UpdateAcompanado.put("eps",EPS.getText().toString().trim());
                            }

                            Map<String,Object> AcompanadoValues= NewAcompanado.toMap();

                            if(Crear) {
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/Usuarios/" + mAuth.getCurrentUser().getUid() + "/Acompanados/" + key, true);
                                childUpdates.put("/Acompanados/" + key, AcompanadoValues);

                                mDatabase.getReference().updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mDatabase.getReference("Acompanados").child(key).child("Usuario").child(mAuth.getCurrentUser().getUid()).setValue(true);
                                        Toast.makeText(AcompanadosActivity.this, "Acompañado Creado", Toast.LENGTH_SHORT);
                                        dialog.dismiss();
                                    }
                                });
                            }else{
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/Usuarios/" + mAuth.getCurrentUser().getUid() + "/Acompanados/" + acompanado.getUID(), true);
                                childUpdates.put("/Acompanados/"+acompanado.getUID(),UpdateAcompanado);

                                mDatabase.getReference("Acompanados").child(acompanado.getUID()).updateChildren(UpdateAcompanado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(AcompanadosActivity.this, "Acompañado Guardado", Toast.LENGTH_SHORT);
                                        dialog.dismiss();
                                    }
                                });

                            }


                        }

                    }
                });
            }
        });
        alertDialog.show();
    }

    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(SAVED_ADAPTER_ITEMS) &&
                savedInstanceState.containsKey(SAVED_ADAPTER_KEYS)) {
            acompanadosList = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_ADAPTER_ITEMS));
            mAdapterKeys = savedInstanceState.getStringArrayList(SAVED_ADAPTER_KEYS);
        } else {
            acompanadosList = new ArrayList<>();
            mAdapterKeys = new ArrayList<>();
        }
    }

    protected Servicio getConvertedObject(DataSnapshot snapshot) {
        String UID= (String)snapshot.child("uid").getValue();
        String Estado= (String)snapshot.child("estado").getValue();
        String TipoServicio= (String)snapshot.child("tipoServicio").getValue();
        String Ciudad= (String)snapshot.child("ciudad").getValue();
        String dirRecogida= (String)snapshot.child("dirRecogida").getValue();
        String dirLlevar= (String)snapshot.child("dirLlevar").getValue();
        String dirRegreso= (String)snapshot.child("dirRegreso").getValue();
        String Observaciones= (String)snapshot.child("observaciones").getValue();
        Long Fecha =(Long) snapshot.child("fecha").getValue();
        Long Id=(Long) snapshot.child("id").getValue();


        return new Servicio(UID,Estado,TipoServicio,Ciudad,dirRecogida,dirLlevar,dirRegreso,Observaciones,Fecha,Id);
    }


}
