package com.example.daniel.pasoporti.Cliente.ProgramarServicios;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.daniel.pasoporti.NonSwipeableViewPager;
import com.example.daniel.pasoporti.R;
import com.example.daniel.pasoporti.Registro.RegDatosFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgSDireccionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgSDireccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgSDireccionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private NonSwipeableViewPager PagerServicio;

    private MaterialEditText Recogida,Llevar,Regreso;
    private CheckBox checkRegreso;
    private LinearLayout layout_DirRegreso;

    public ProgSDireccionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment ProgSDireccionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgSDireccionFragment newInstance(int page, String title) {
        ProgSDireccionFragment fragment = new ProgSDireccionFragment();
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
        View v= inflater.inflate(R.layout.fragment_prog_sdireccion, container, false);
        PagerServicio=(NonSwipeableViewPager) container;
        Recogida= (MaterialEditText) v.findViewById(R.id.servicio_direccion_recogida);
        Llevar= (MaterialEditText) v.findViewById(R.id.servicio_direccion_destino);
        Regreso= (MaterialEditText) v.findViewById(R.id.servicio_direccion_regreso);
        checkRegreso = (CheckBox) v.findViewById(R.id.checkBox_Servicio_direccion);
        layout_DirRegreso=(LinearLayout) v.findViewById(R.id.layout_dirRegreso);

        Recogida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Recogida.getText().toString())){
                    mListener.setDirRecogida(Recogida.getText().toString().trim());
                    if(checkRegreso.isChecked()){
                        mListener.setDirRegreso(Recogida.getText().toString().trim());
                    }
                }
            }
        });

        Llevar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Llevar.getText().toString())){
                    mListener.setDirLlevar(Llevar.getText().toString().trim());
                }
            }
        });

        Regreso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!checkRegreso.isChecked()){
                    if(!TextUtils.isEmpty(Regreso.getText().toString())){
                        mListener.setDirRegreso(Regreso.getText().toString());
                    }
                }

            }
        });

        validate();
        return v;
    }

    private void validate(){
        if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
            if (checkRegreso.isChecked()){
                PagerServicio.setPagingEnabled(true);
            }else{
                if(TextUtils.isEmpty(Regreso.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }
        }else{
            PagerServicio.setPagingEnabled(false);
        }

        Recogida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }
        });

        Llevar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }
        });

        Regreso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
            }
        });

        checkRegreso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!TextUtils.isEmpty(Recogida.getText().toString()) && !TextUtils.isEmpty(Llevar.getText().toString())){
                    if (checkRegreso.isChecked()){
                        PagerServicio.setPagingEnabled(true);
                    }else{
                        if(TextUtils.isEmpty(Regreso.getText().toString())){
                            PagerServicio.setPagingEnabled(false);
                        }else{
                            PagerServicio.setPagingEnabled(true);
                        }
                    }
                }else{
                    PagerServicio.setPagingEnabled(false);
                }
                if(checkRegreso.isChecked()){
                    Regreso.setText(null);
                    layout_DirRegreso.setBackgroundResource(R.drawable.rounded_disabled);
                    Regreso.setFocusable(false);
                    Regreso.setFocusableInTouchMode(false);
                    mListener.setDirRegreso(mListener.getDirRecogida());
                }else{
                    layout_DirRegreso.setBackgroundResource(R.drawable.rounded);
                    Regreso.setFocusable(true);
                    Regreso.setFocusableInTouchMode(true);
                }
            }
        });

    }


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
        //void onFragmentInteraction(Uri uri);
        String getDirRecogida();
        String getDirLlevar();
        String getDirRegreso();

        void setDirRecogida(String dirRecogida);
        void setDirLlevar(String dirLlevar);
        void setDirRegreso(String dirRegreso);
    }
}
