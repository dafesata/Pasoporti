package com.example.daniel.pasoporti.Cliente.ProgramarServicios;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by Daniel on 9/28/2017.
 */

public class SpinnerAdapterAcompanado extends FirebaseAcompanadoSpinnerAdapter<Acompanado> {

    private Context context;

    public SpinnerAdapterAcompanado(Query query, Context context, int textViewResourceId) {
        super(query, context, textViewResourceId);
    }

    public SpinnerAdapterAcompanado(Query query, @Nullable ArrayList<Acompanado> items, @Nullable ArrayList<String> keys, Context context, int textViewResourceId) {
        super(query, items, keys, context, textViewResourceId);
        this.context=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setText(getItem(position).getNombre());
        return textView;


    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(getItem(position).getNombre());
        return convertView;

    }
}
