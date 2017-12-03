package com.example.daniel.pasoporti.Cliente.ProgramarServicios;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.example.daniel.pasoporti.NonSwipeableViewPager;
import com.example.daniel.pasoporti.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgSFechaTipoCiudadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgSFechaTipoCiudadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgSFechaTipoCiudadFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener,  TimePickerDialogFragment.TimePickerDialogHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private NonSwipeableViewPager PagerServicio;

    private MaterialEditText Fecha,Hora;
    private MaterialBetterSpinner TipoServicio, Ciudad;

    public ProgSFechaTipoCiudadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment ProgSFechaTipoCiudadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgSFechaTipoCiudadFragment newInstance(int page, String title) {
        ProgSFechaTipoCiudadFragment fragment = new ProgSFechaTipoCiudadFragment();
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
        View v= inflater.inflate(R.layout.fragment_progsfechatipociudad, container, false);

        PagerServicio=(NonSwipeableViewPager) container;
        Fecha=(MaterialEditText) v.findViewById(R.id.servicio_fecha);
        Fecha.setFocusable(false);
        Hora=(MaterialEditText) v.findViewById(R.id.servicio_hora);
        Hora.setFocusable(false);

        TipoServicio=(MaterialBetterSpinner) v.findViewById(R.id.spinner_Servicio_TipoServ);
        String [] servicios=getResources().getStringArray(R.array.tiposServicios);
        ArrayAdapter<String> adapterServicios = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, servicios);
        adapterServicios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TipoServicio.setAdapter(adapterServicios);

        TipoServicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(TipoServicio.getText().toString())){
                    mListener.setTipoServicio(TipoServicio.getText().toString());
                }
            }
        });



        Ciudad= (MaterialBetterSpinner) v.findViewById(R.id.spinner_Servicio_Ciudad);
        String [] ciudades=getResources().getStringArray(R.array.ciudades);
        ArrayAdapter<String> adapterCiudades = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, ciudades);
        Ciudad.setAdapter(adapterCiudades);

        Ciudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(Ciudad.getText().toString())){
                    mListener.setCiudad(Ciudad.getText().toString());
                }
            }
        });


        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(ProgSFechaTipoCiudadFragment.this);
                cdp.show(getFragmentManager(), FRAG_TAG_DATE_PICKER);

            }
        });


        Hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .addTimePickerDialogHandler(ProgSFechaTipoCiudadFragment.this)
                        .setFragmentManager(getFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
            }
        });

        validate();
        return v;
    }

    private void validate() {
        if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
            PagerServicio.setPagingEnabled(false);
        }else{
            PagerServicio.setPagingEnabled(true);
        }

        Fecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }
        });

        Hora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }
        });

        TipoServicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }
        });

        Ciudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(Fecha.getText().toString()) && TextUtils.isEmpty(Hora.getText().toString()) && TextUtils.isEmpty(TipoServicio.getText().toString()) && TextUtils.isEmpty(Ciudad.getText().toString())){
                    PagerServicio.setPagingEnabled(false);
                }else{
                    PagerServicio.setPagingEnabled(true);
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

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        Fecha.setText(getString(R.string.date_picker_result_value, (dayOfMonth < 10 ? "0" : "") + String.valueOf(dayOfMonth) ,((monthOfYear+1) < 10 ? "0" : "") +  String.valueOf(monthOfYear+1),String.valueOf(year)));
        mListener.setFecha(Fecha.getText().toString());
        validate();

    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        Hora.setText(getString(R.string.time_picker_result_value,(hourOfDay < 10 ? "0" : "")+ String.valueOf(hourOfDay),(minute < 10 ? "0" : "")+String.valueOf(minute)));
        mListener.setHora(Hora.getText().toString());
        validate();
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

        String getFecha();
        String getHora();
        String getTipoServivio();
        String getCiudad();

        void setFecha(String fecha);
        void setHora(String Hora);
        void setTipoServicio(String servicio);
        void setCiudad(String Ciudad);
    }
}
