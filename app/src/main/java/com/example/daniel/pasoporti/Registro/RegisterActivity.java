package com.example.daniel.pasoporti.Registro;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Usuario;
import com.example.daniel.pasoporti.NonSwipeableViewPager;
import com.example.daniel.pasoporti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.layer_net.stepindicator.StepIndicator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.math.BigInteger;

public class RegisterActivity extends AppCompatActivity implements RegEmailFragment.OnFragmentInteractionListener, RegNombreFragment.OnFragmentInteractionListener,RegDatosFragment.OnFragmentInteractionListener{

    private static final int ACTIVITY_TIME_OUT = 150;
    private final String TAG = this.getClass().getName();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NonSwipeableViewPager mViewPager;

    private ImageButton leftNav,rightNav;

    //Interface variable
    private String Email, Password,Nombre,TipoId,Direccion,Ciudad;
    private BigInteger Identificacion,Telefono;
    private boolean Aceptar;


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferUsuarios;

    private ProgressDialog progressDialog;


    private RegEmailFragment regEmailFragment;
    private RegNombreFragment regNombreFragment;
    private RegDatosFragment regDatosFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regEmailFragment= RegEmailFragment.newInstance(0, "1/3");
        regNombreFragment = RegNombreFragment.newInstance(1,"2/3");
        regDatosFragment = RegDatosFragment.newInstance(2,"3/3");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        StepIndicator stepIndicator = (StepIndicator) findViewById(R.id.step_indicator);
        stepIndicator.setupWithViewPager(mViewPager);
        stepIndicator.setClickable(false);
        mViewPager.setPagingEnabled(false);


        leftNav=(ImageButton) findViewById(R.id.left_nav);
        rightNav=(ImageButton) findViewById(R.id.right_nav);

        //Firebase

        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getPagingEnabled()) {
                    mViewPager.arrowScroll(View.FOCUS_LEFT);
                }else{
                    ErrorMsgNav();
                }

            }
        });

        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getPagingEnabled()) {
                    mViewPager.arrowScroll(View.FOCUS_RIGHT);
                }else{
                    ErrorMsgNav();
                }

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mViewPager.setPagingEnabled(false);
                switch (position) {
                    case 0:
                        validateEmail();
                        break;
                    case 1:
                        validateNombre();
                        break;
                    case 2:
                        validateDatos();
                        break;
                }
            }
        });


    }

    private void ErrorMsgNav(){
        int position=mViewPager.getCurrentItem();
        switch (position){
            case 0:
                MaterialEditText email=(MaterialEditText) findViewById(R.id.register_email);
                MaterialEditText password= (MaterialEditText) findViewById(R.id.register_password);
                MaterialEditText confirm_password= (MaterialEditText) findViewById(R.id.register_confirm_password);

                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Digíte Email");
                }else{
                    if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                        email.setError("Digíte Email Correcto");
                    }
                }
                if(TextUtils.isEmpty(password.getText().toString())){
                    password.setError("Digíte Contraseña");
                }else{
                    if(password.getText().length()<6){
                        password.setError("Contraseña no puede tener menos de 6 digítos");
                    }
                }

                if(!confirm_password.getText().toString().equals(password.getText().toString())){
                    confirm_password.setError("Contraseñas deben ser iguales");
                }

                break;
            case 1:
                MaterialEditText Nombre=(MaterialEditText) findViewById(R.id.register_nombre);
                MaterialBetterSpinner TipoId=(MaterialBetterSpinner) findViewById(R.id.spinner_TipoDoc);
                MaterialEditText Identificacion=(MaterialEditText) findViewById(R.id.register_identificacion);
                if(TextUtils.isEmpty(Nombre.getText().toString())){
                    Nombre.setError("Digíte Nombre");
                }

                if(TextUtils.isEmpty(TipoId.getText().toString())){
                    TipoId.setError("Seleccione Tipo de Identificación");
                }

                if(TextUtils.isEmpty(Identificacion.getText().toString())){
                    Identificacion.setError("Digíte Número de Identificación");
                }

                break;
            case 2:
                MaterialEditText Direccion=(MaterialEditText) findViewById(R.id.register_direccion);
                MaterialBetterSpinner Ciudad=(MaterialBetterSpinner) findViewById(R.id.spinner_register_ciudad);
                MaterialEditText Telefono=(MaterialEditText) findViewById(R.id.register_telefono);
                CheckBox AceptTerm=(CheckBox)findViewById(R.id.checkBoxAuth);
                if(TextUtils.isEmpty(Direccion.getText().toString())){
                    Direccion.setError("Digíte Dirección");
                }
                if(TextUtils.isEmpty(Ciudad.getText().toString())){
                    Ciudad.setError("Seleccione Ciudad");
                }
                if(TextUtils.isEmpty(Telefono.getText().toString())){
                    Telefono.setError("Digíte Teléfono");
                }

                if(!AceptTerm.isChecked()){
                    AceptTerm.setError("Acepte Terminos");
                }

                break;

        }
    }

    private void validateDatos() {
        final MaterialEditText Direccion=(MaterialEditText) findViewById(R.id.register_direccion);
        final MaterialBetterSpinner Ciudad=(MaterialBetterSpinner) findViewById(R.id.spinner_register_ciudad);
        final MaterialEditText Telefono=(MaterialEditText) findViewById(R.id.register_telefono);
        final CheckBox AceptTerm=(CheckBox)findViewById(R.id.checkBoxAuth);
        if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
            mViewPager.setPagingEnabled(true);
        }else{
            mViewPager.setPagingEnabled(false);
        }

        Direccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });

        Ciudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });

        Telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Direccion.getText().toString()) && !TextUtils.isEmpty(Ciudad.getText().toString()) && !TextUtils.isEmpty(Telefono.getText().toString()) && !AceptTerm.isChecked()){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });
    }

    private void validateNombre() {
        final MaterialEditText Nombre=(MaterialEditText) findViewById(R.id.register_nombre);
        final MaterialBetterSpinner TipoId=(MaterialBetterSpinner) findViewById(R.id.spinner_TipoDoc);
        final MaterialEditText Identificacion=(MaterialEditText) findViewById(R.id.register_identificacion);

        if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
            mViewPager.setPagingEnabled(true);
        }else{
            mViewPager.setPagingEnabled(false);
        }

        Nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });

        TipoId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });

        Identificacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Nombre.getText().toString()) && !TextUtils.isEmpty(Identificacion.getText().toString()) && !TextUtils.isEmpty(TipoId.getText().toString())){
                    mViewPager.setPagingEnabled(true);
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });
    }

    private void validateEmail (){
        final MaterialEditText email=(MaterialEditText) findViewById(R.id.register_email);
        final MaterialEditText password=(MaterialEditText) findViewById(R.id.register_password);
        final MaterialEditText confirm_password=(MaterialEditText) findViewById(R.id.register_confirm_password);

        if(!TextUtils.isEmpty(email.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
            if(password.getText().toString().length()>=6) {
                if (password.getText().toString().equals(confirm_password.getText().toString())) {
                    mViewPager.setPagingEnabled(true);
                } else {
                    mViewPager.setPagingEnabled(false);
                }
            }else{
                mViewPager.setPagingEnabled(false);
            }
        }else{
            mViewPager.setPagingEnabled(false);
        }

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!TextUtils.isEmpty(email.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().length()>=6) {
                        if (password.getText().toString().equals(confirm_password.getText().toString())) {
                            mViewPager.setPagingEnabled(true);
                        } else {
                            mViewPager.setPagingEnabled(false);
                        }
                    }else{
                        mViewPager.setPagingEnabled(false);
                    }
                }else{
                    mViewPager.setPagingEnabled(false);
                }
            }
        });

    }



    public void onClick_Guardar(View view) {

        final MaterialEditText Direccion=(MaterialEditText) findViewById(R.id.register_direccion);
        final MaterialBetterSpinner Ciudad=(MaterialBetterSpinner) findViewById(R.id.spinner_register_ciudad);
        final MaterialEditText Telefono=(MaterialEditText) findViewById(R.id.register_telefono);
        CheckBox AceptTerm=(CheckBox)findViewById(R.id.checkBoxAuth);

        if(TextUtils.isEmpty(Direccion.getText().toString())){
            Direccion.setError("Digíte Dirección");
        }
        if(TextUtils.isEmpty(Ciudad.getText().toString())){
            Ciudad.setError("Digíte Ciudad");
        }
        if(TextUtils.isEmpty(Telefono.getText().toString())){
            Telefono.setError("Digíte Teléfono");
        }

        if(!AceptTerm.isChecked()){
            AceptTerm.setError("Debe Aceptar Terminos y Condiciones");
        }

        if(mViewPager.getPagingEnabled() && AceptTerm.isChecked()){
            progressDialog=new ProgressDialog(RegisterActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creando usuario...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(getEmail(), getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else{
                                mReferUsuarios=mDatabase.getReference("Usuarios");
                                Usuario NUsuario= new Usuario(getNombre(),getEmail(),getDireccion(),getTipoId(),getCiudad(),mAuth.getCurrentUser().getUid(),String.valueOf(getIdentificacion()),String.valueOf(getTelefono()));
                                mReferUsuarios.child(NUsuario.getUID()).setValue(NUsuario);

                                Toast.makeText(RegisterActivity.this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, ACTIVITY_TIME_OUT);



                            }


                        }
                    });

        }

    }



    @Override
    public void onFragmentInteraction(String Email, String Password) {
        //String tag= "android:switcher:" + R.id.container + ":" + 2;
        //RegDatosFragment Fragment=(RegDatosFragment)getSupportFragmentManager().findFragmentByTag(tag);
        //function in regDatos Fragment used for recieve data

    }

    @Override
    public void onFragmentInteraction(String Nombre, String TipoId, BigInteger Identificacion) {
        //String tag= "android:switcher:" + R.id.container + ":" + 2;



    }

    @Override
    public String getEmail() {
        return Email;
    }

    @Override
    public String getPassword() {
        return Password;
    }

    @Override
    public String getNombre() {
        return Nombre;
    }

    @Override
    public String getTipoId() {
        return TipoId;
    }

    @Override
    public void onFragmentInteraction(String Direccion, BigInteger Telefono, String Ciudad) {

    }

    @Override
    public Boolean getCheck() {
        return Aceptar;
    }

    @Override
    public String getDireccion() {
        return Direccion;
    }

    @Override
    public String getCiudad() {
        return Ciudad;
    }

    @Override
    public BigInteger getIdentificacion() {
        return Identificacion;
    }

    @Override
    public BigInteger getTelefono() {
        return Telefono;
    }

    @Override
    public void setCheck(Boolean check) {
        this.Aceptar=check;
    }

    @Override
    public void setEmail(String email) {
        this.Email=email;
    }

    @Override
    public void setPassword(String password) {
        this.Password=password;
    }

    @Override
    public void setNombre(String nombre) {
        this.Nombre=nombre;
    }

    @Override
    public void setTipoId(String tipoId) {
        this.TipoId=tipoId;
    }

    @Override
    public void setDireccion(String direccion) {
        this.Direccion=direccion;
    }

    @Override
    public void setCiudad(String ciudad) {
        this.Ciudad=ciudad;

    }

    @Override
    public void setIdentificacion(BigInteger identificacion) {
        this.Identificacion=identificacion;
    }

    @Override
    public void setTelefono(BigInteger telefono) {
        this.Telefono=telefono;
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
                    return regEmailFragment;
                case 1:
                    return regNombreFragment;
                case 2:
                    return regDatosFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "1/3";
                case 1:
                    return "2/3";
                case 2:
                    return "3/3";
            }
            return null;
        }

    }

}
