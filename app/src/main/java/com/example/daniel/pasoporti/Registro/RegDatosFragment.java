package com.example.daniel.pasoporti.Registro;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.daniel.pasoporti.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.math.BigInteger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegDatosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegDatosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private MaterialEditText direccion,telefono;
    private MaterialBetterSpinner ciudad;
    private CheckBox Aceptar;

    public RegDatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment RegDatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegDatosFragment newInstance(int page, String title) {
        RegDatosFragment fragment = new RegDatosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_reg_datos, container, false);
        ciudad=(MaterialBetterSpinner) v.findViewById(R.id.spinner_register_ciudad);
        direccion=(MaterialEditText) v.findViewById(R.id.register_direccion);
        telefono=(MaterialEditText) v.findViewById(R.id.register_telefono);
        Aceptar=(CheckBox) v.findViewById(R.id.checkBoxAuth);
        mListener.setCheck(false);

        String[] ciudades = getResources().getStringArray(R.array.ciudades);
        ArrayAdapter<String> adapterCiudades = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, ciudades);
        ciudad.setAdapter(adapterCiudades);

        ciudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(ciudad.getText().toString())) {
                    mListener.setCiudad(ciudad.getText().toString());
                }
            }
        });

        direccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(direccion.getText().toString())) {
                    mListener.setDireccion(direccion.getText().toString());
                }
            }
        });

        telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(telefono.getText().toString())) {
                    mListener.setTelefono(BigInteger.valueOf(Long.parseLong(telefono.getText().toString())));
                }
            }
        });

        Aceptar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.setCheck(Aceptar.isChecked());
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String Direccion,BigInteger Telefono, String Ciudad);

        Boolean getCheck();
        String getDireccion();
        String getCiudad();
        BigInteger getTelefono();

        void setCheck(Boolean check);
        void setDireccion(String direccion);
        void setCiudad(String ciudad);
        void setTelefono(BigInteger telefono);

    }
}
