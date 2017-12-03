package com.example.daniel.pasoporti.Cliente.ProgramarServicios;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.Cliente.ClienteWelcomeActivity;
import com.example.daniel.pasoporti.NonSwipeableViewPager;
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
import com.layer_net.stepindicator.StepIndicator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProgramarServicioActivity extends AppCompatActivity implements  ProgSFechaTipoCiudadFragment.OnFragmentInteractionListener, ProgSAcompananteFragment.OnFragmentInteractionListener, ProgSDireccionFragment.OnFragmentInteractionListener,ProgSObservacionesFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private final String TAG = this.getClass().getName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NonSwipeableViewPager mViewPagerServicio;



    private ProgSFechaTipoCiudadFragment fragmentInicialServicio;
    private ProgSAcompananteFragment fragmentAcompanadoServicio;
    private ProgSDireccionFragment fragmentDireccionServicio;
    private ProgSObservacionesFragment fragmentObservacionesServicio;

    private ImageButton leftNavServicio,rightNavServicio;

    private String Fecha,Hora,TipoServicio,Ciudad,DirRecogida,DirLlevar,DirRegreso,Observaciones;
    private Acompanado SAcompanado;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mAcompanadosReference,mServiciosReference;
    private FirebaseAuth mAuth;

    private Query query;
    private SpinnerAdapterAcompanado adapterAcompanado;
    private MaterialBetterSpinner AcompanadoSpinner;
    private ArrayList<Acompanado> acompanadoList;
    private ArrayList<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programar_servicio);

        mDatabase= FirebaseDatabase.getInstance();
        mAcompanadosReference = mDatabase.getReference("Acompanados");
        mServiciosReference=mDatabase.getReference("Servicios");
        mAuth= FirebaseAuth.getInstance();




        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPagerServicio = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPagerServicio.setAdapter(mSectionsPagerAdapter);
        StepIndicator stepIndicator = (StepIndicator) findViewById(R.id.step_indicator);
        stepIndicator.setupWithViewPager(mViewPagerServicio);
        stepIndicator.setClickable(false);
        //
        mViewPagerServicio.setPagingEnabled(false);

        fragmentInicialServicio= ProgSFechaTipoCiudadFragment.newInstance(1,"1/4");
        fragmentAcompanadoServicio= ProgSAcompananteFragment.newInstance(2,"2/4");
        fragmentDireccionServicio= ProgSDireccionFragment.newInstance(3,"3/4");
        fragmentObservacionesServicio=ProgSObservacionesFragment.newInstance(4,"4/4");

        leftNavServicio=(ImageButton) findViewById(R.id.left_nav_servicio);
        rightNavServicio=(ImageButton) findViewById(R.id.right_nav_servicio);

        leftNavServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPagerServicio.getPagingEnabled()) {
                    mViewPagerServicio.arrowScroll(View.FOCUS_LEFT);
                }else{
                   ErrorMsgNav();
                }

            }
        });

        rightNavServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+mViewPagerServicio.getPagingEnabled());
                if(mViewPagerServicio.getPagingEnabled()) {
                    mViewPagerServicio.arrowScroll(View.FOCUS_RIGHT);
                }else{
                   ErrorMsgNav();
                }

            }
        });

        mViewPagerServicio.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPagerServicio.setPagingEnabled(false);
                switch(position){
                    case 0:
                        validateFechaTipoCiudad();
                        break;
                    case 1:
                        AcompanadoSpinner= (MaterialBetterSpinner) findViewById(R.id.spinner_Servicio_Acompanado);
                        acompanadoList=new ArrayList<Acompanado>();
                        keys=new ArrayList<String>();
                        query = mAcompanadosReference.orderByChild("Usuario/"+mAuth.getCurrentUser().getUid()).equalTo(true);
                        adapterAcompanado= new SpinnerAdapterAcompanado(query,acompanadoList,keys, ProgramarServicioActivity.this,android.R.layout.simple_dropdown_item_1line);
                        AcompanadoSpinner.setAdapter(adapterAcompanado);
                        validateAcompanado();

                        break;
                    case 2:

                        validateDireccion();

                        break;
                    case 3:
                        validateObservaciones();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }



    private void ErrorMsgNav() {
        int position=mViewPagerServicio.getCurrentItem();
        switch (position){
            case 0:
                MaterialEditText Fecha=(MaterialEditText) findViewById(R.id.servicio_fecha);
                MaterialEditText Hora=(MaterialEditText) findViewById(R.id.servicio_hora);
                MaterialBetterSpinner TipoServicio = (MaterialBetterSpinner) findViewById(R.id.spinner_Servicio_TipoServ);
                MaterialBetterSpinner Ciudad = (MaterialBetterSpinner) findViewById(R.id.spinner_Servicio_Ciudad);
                if(TextUtils.isEmpty(Fecha.getText().toString())){
                    Fecha.setError("Seleccione Fecha");
                }
                if(TextUtils.isEmpty(Hora.getText().toString())){
                    Hora.setError("Seleccione Hora");
                }
                if(TextUtils.isEmpty(TipoServicio.getText().toString())){
                    TipoServicio.setError("Seleccione Tipo de Servicio");
                }
                if(TextUtils.isEmpty(Ciudad.getText().toString())){
                    Ciudad.setError("Seleccione Ciudad");
                }
                break;
            case 1:
                MaterialBetterSpinner Acompanado=(MaterialBetterSpinner) findViewById(R.id.spinner_Servicio_Acompanado);
                if(TextUtils.isEmpty(Acompanado.getText().toString())){
                    Acompanado.setError("Seleccione Acompañado");
                }
                break;
            case 2:
                MaterialEditText dirRecogida=(MaterialEditText) findViewById(R.id.servicio_direccion_recogida);
                MaterialEditText dirLlevar=(MaterialEditText) findViewById(R.id.servicio_direccion_destino);
                MaterialEditText dirRegreso= (MaterialEditText) findViewById(R.id.servicio_direccion_regreso);
                CheckBox checkRegreso=(CheckBox) findViewById(R.id.checkBox_Servicio_direccion);
                if(TextUtils.isEmpty(dirRecogida.getText().toString())){
                    dirRecogida.setError("Digíte Dirección de Recogida");
                }
                if(TextUtils.isEmpty(dirLlevar.getText().toString())){
                    dirLlevar.setError("Digíte Dirección donde LLevar");
                }
                if(!checkRegreso.isChecked()){
                    if(TextUtils.isEmpty(dirRegreso.getText().toString())){
                        dirRegreso.setError("Digíte Dirección de Regreso");
                    }
                }

                break;
            case 3:
                MaterialEditText Observacion = (MaterialEditText) findViewById(R.id.servicio_observaciones);
                if (TextUtils.isEmpty(Observacion.getText().toString())){
                    Observacion.setError("Digíte Observaciones");
                }
                break;
        }
    }

    private void validateFechaTipoCiudad() {
        final MaterialEditText Fecha=(MaterialEditText) findViewById(R.id.servicio_fecha);
        final MaterialEditText Hora=(MaterialEditText) findViewById(R.id.servicio_hora);
        final MaterialBetterSpinner TipoServicio=(MaterialBetterSpinner) findViewById(R.id.spinner_Servicio_TipoServ);
        final MaterialBetterSpinner Ciudad=(MaterialBetterSpinner) findViewById(R.id.spinner_Servicio_Ciudad);
        if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
            mViewPagerServicio.setPagingEnabled(false);
        }else{
            mViewPagerServicio.setPagingEnabled(true);
        }

        Fecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }
        });

        Hora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }
        });

        TipoServicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }
        });

        Ciudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }
        });
    }

    private void validateAcompanado() {
        final MaterialBetterSpinner Acompanado=(MaterialBetterSpinner) findViewById(R.id.spinner_Servicio_Acompanado);
        if(TextUtils.isEmpty(Acompanado.getText().toString())){
            mViewPagerServicio.setPagingEnabled(false);
        }else{
            mViewPagerServicio.setPagingEnabled(true);
        }

        Acompanado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Acompanado.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Acompanado.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Acompanado.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }
        });
    }

    private void validateDireccion(){
        final MaterialEditText Recogida = (MaterialEditText) findViewById(R.id.servicio_direccion_recogida);
        final MaterialEditText Llevar = (MaterialEditText) findViewById(R.id.servicio_direccion_destino);
        final MaterialEditText Regreso = (MaterialEditText) findViewById(R.id.servicio_direccion_regreso);
        final CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox_Servicio_direccion);
        if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
            if (checkbox.isChecked()){
                mViewPagerServicio.setPagingEnabled(true);
            }else{
                if(TextUtils.isEmpty(Regreso.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }
        }else{
            mViewPagerServicio.setPagingEnabled(false);
        }

        Recogida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }
        });

        Llevar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }
        });

        Regreso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkbox.isChecked()){
                        mViewPagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            mViewPagerServicio.setPagingEnabled(false);
                        }else{
                            mViewPagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    mViewPagerServicio.setPagingEnabled(false);
                }
            }
        });

    }

    private void validateObservaciones(){
        final MaterialEditText observacion= (MaterialEditText) findViewById(R.id.servicio_observaciones);
        if(TextUtils.isEmpty(observacion.getText().toString())){
            mViewPagerServicio.setPagingEnabled(false);
        }else{
            mViewPagerServicio.setPagingEnabled(true);
        }
        observacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(observacion.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(observacion.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(observacion.getText().toString())){
                    mViewPagerServicio.setPagingEnabled(false);
                }else{
                    mViewPagerServicio.setPagingEnabled(true);
                }
            }
        });
    }

    @Override
    public String getFecha() {
        return Fecha;
    }

    @Override
    public String getHora() {
        return Hora;
    }

    @Override
    public String getTipoServivio() {
        return TipoServicio;
    }

    @Override
    public String getCiudad() {
        return Ciudad;
    }

    @Override
    public void setFecha(String fecha) {
        this.Fecha= fecha;
    }

    @Override
    public void setHora(String Hora) {
        this.Hora=Hora;
    }

    @Override
    public void setTipoServicio(String Tiposervicio) {
        this.TipoServicio=Tiposervicio;
    }

    @Override
    public void setCiudad(String Ciudad) {
        this.Ciudad=Ciudad;
    }


    @Override
    public Acompanado getAcompanado() {
        return SAcompanado;
    }

    @Override
    public void setAcompanado(String acompanado) {
        this.SAcompanado= adapterAcompanado.getItem(acompanado) ;
    }

    @Override
    public String getDirRecogida() {
        return DirRecogida;
    }

    @Override
    public String getDirLlevar() {
        return DirLlevar;
    }

    @Override
    public String getDirRegreso() {
        return DirRegreso;
    }

    @Override
    public void setDirRecogida(String dirRecogida) {
        this.DirRecogida=dirRecogida;
    }

    @Override
    public void setDirLlevar(String dirLlevar) {
        this.DirLlevar=dirLlevar;
    }

    @Override
    public void setDirRegreso(String dirRegreso) {
        this.DirRegreso=dirRegreso;
    }


    @Override
    public String getObservaciones() {
        return Observaciones;
    }

    @Override
    public void setObservaciones(String observaciones) {
        this.Observaciones=observaciones;

    }


    public void onClick_Inicio(View view) {
        Intent i= new Intent(ProgramarServicioActivity.this, ClienteWelcomeActivity.class);
        startActivity(i);
    }

    public void onClick_AddAcompanado(View view) {
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

        final String[] tiposDoc= getResources().getStringArray(R.array.tiposDoc);
        ArrayAdapter<String> adapterDocs = new ArrayAdapter<>(ProgramarServicioActivity.this, android.R.layout.simple_dropdown_item_1line, tiposDoc);
        TipoId.setAdapter(adapterDocs);

        final String[] tipoParentescos = getResources().getStringArray(R.array.tiposParentescos);
        ArrayAdapter<String> adapterParentescos = new ArrayAdapter<>(ProgramarServicioActivity.this, android.R.layout.simple_dropdown_item_1line, tipoParentescos);
        Parentesco.setAdapter(adapterParentescos);



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
                            Map<String,Object> AcompanadoValues= NewAcompanado.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/Usuarios/" + mAuth.getCurrentUser().getUid() + "/Acompanados/" + key, true);
                            childUpdates.put("/Acompanados/" + key, AcompanadoValues);

                            mDatabase.getReference().updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDatabase.getReference("Acompanados").child(key).child("Usuario").child(mAuth.getCurrentUser().getUid()).setValue(true);
                                    Toast.makeText(ProgramarServicioActivity.this, "Acompañado Creado", Toast.LENGTH_SHORT);
                                    dialog.dismiss();
                                }
                            });

                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void onClick_Agendar(View view) {
        final MaterialEditText Observaciones= (MaterialEditText) findViewById(R.id.servicio_observaciones);
        if(TextUtils.isEmpty(Observaciones.getText().toString())){
            Observaciones.setError("Digite Observaciones");
        }
        if(mViewPagerServicio.getPagingEnabled()){
            final Long[] newId = new Long[1];
            mServiciosReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newId[0] =dataSnapshot.getChildrenCount()+1;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            final String key=mServiciosReference.push().getKey();

            Servicio newServicio= new Servicio(key,"Sin Aprobar",getTipoServivio(),getCiudad(),getDirRecogida(),getDirLlevar(),getDirRegreso(),getObservaciones(),getFecha(),getHora(),newId[0]);
            Map<String,Object> ServicioValues= newServicio.toMap();

            Map<String,Object> childUpdates=new HashMap<>();
            childUpdates.put("/Usuarios/"+mAuth.getCurrentUser().getUid()+"/Servicios/"+key,true);
            childUpdates.put("/Acompanados/"+SAcompanado.getUID()+"/Servicios/"+key,true);
            childUpdates.put("/Servicios/"+key,ServicioValues);

           mDatabase.getReference().updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                       mServiciosReference.child(key).child("Usuario").child(mAuth.getCurrentUser().getUid()).setValue(true);
                       mServiciosReference.child(key).child("Acompanado").child(SAcompanado.getUID()).setValue(true);

                   @SuppressLint("RestrictedApi") AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(ProgramarServicioActivity.this,R.style.Theme_AppCompat_Light_Dialog));
                   alertDialogBuilder.setMessage("Hemos recibido tu solicitud. Pronto estaremos confirmando tu servicio de acompañamiento")
                    .setCancelable(false)
                   .setPositiveButton("Aceptar", null);
                   final AlertDialog alertDialog=alertDialogBuilder.create();

                   alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                       @Override
                       public void onShow(DialogInterface dialog) {
                           alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(alertDialog.getContext(), R.color.colorPrimary));

                           Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                           button.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Intent i= new Intent(ProgramarServicioActivity.this,ClienteWelcomeActivity.class);
                                   startActivity(i);
                               }
                           });
                       }
                   });

                   alertDialog.show();

               }
           });




        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return fragmentInicialServicio   ;
                case 1:
                    return fragmentAcompanadoServicio;
                case 2:
                    return fragmentDireccionServicio;
                case 3:
                    return fragmentObservacionesServicio;
                default:
                    return null ;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }
}
