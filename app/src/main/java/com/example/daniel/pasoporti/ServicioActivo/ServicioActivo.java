package com.example.daniel.pasoporti.ServicioActivo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.daniel.pasoporti.R;

public class ServicioActivo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_activo);
    }

    public void onClick_Inicio(View view) {
        finish();
    }
}
