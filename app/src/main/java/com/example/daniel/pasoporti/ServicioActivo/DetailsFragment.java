package com.example.daniel.pasoporti.ServicioActivo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private View v;

    private String TAG= this.getClass().getName();
    private TextView servicioId,servicioEstado;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mServiciosReference,mAcompanadosReference;
    private Query query;
    private Servicio servicio;
    private Acompanado acompanado;

    private MaterialEditText TipoServ;
    private MaterialEditText Fecha;
    private MaterialEditText Hora;
    private MaterialEditText IdServicio;
    private MaterialEditText Nombre;
    private MaterialEditText TipoId;
    private MaterialEditText Id;
    private MaterialEditText DirRecogida;
    private MaterialEditText DirCita;
    private MaterialEditText DirRegreso;
    private MaterialEditText Acompañante;
    private MaterialEditText CAcomp;
    private MaterialEditText CCond;
    private MaterialEditText CVehic;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(int param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        v= inflater.inflate(R.layout.fragment_details, container, false);

        mDatabase= FirebaseDatabase.getInstance();
        mServiciosReference=mDatabase.getReference("Servicios");
        mAcompanadosReference=mDatabase.getReference("Acompanados");
        mAuth= FirebaseAuth.getInstance();
        fields();

        mServiciosReference.child(mListener.getServicio()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicio=new Servicio().getConvertedObject(dataSnapshot);
                query=mAcompanadosReference.orderByChild("Servicios/"+servicio.getUID()).equalTo(true);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap:dataSnapshot.getChildren()) {
                            acompanado = new Acompanado().getConvertedObject(snap);
                        }
                        TipoServ.setText(servicio.getTipoServicio());
                        Fecha.setText(servicio.getFecha());
                        Hora.setText(servicio.getHora());
                        IdServicio.setText(String.valueOf(servicio.getId()));
                        Nombre.setText(acompanado.getNombre());
                        TipoId.setText(acompanado.getTipoId());
                        Id.setText(String.valueOf(acompanado.getId()));
                        DirRecogida.setText(servicio.getDirRecogida());
                        DirCita.setText(servicio.getDirLlevar());
                        DirRegreso.setText(servicio.getDirRegreso());
                            /*
                                FALTA ACOMPAÑANTE
                            */
                        CAcomp.setText(servicio.getCAcompanante());
                        CCond.setText(servicio.getCConductor());
                        CVehic.setText(servicio.getCVehiculo());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;

    }

    public void fields(){
        TipoServ = (MaterialEditText)v.findViewById(R.id.activo_tiposerv);
        Fecha = (MaterialEditText)v.findViewById(R.id.activo_fecha);
        Hora = (MaterialEditText)v.findViewById(R.id.activo_hora);
        IdServicio = (MaterialEditText)v.findViewById(R.id.activo_idserv);
        Nombre = (MaterialEditText)v.findViewById(R.id.activo_nombre);
        TipoId = (MaterialEditText)v.findViewById(R.id.activo_tipoDoc);
        Id = (MaterialEditText)v.findViewById(R.id.activo_numidentificacion);
        DirRecogida = (MaterialEditText)v.findViewById(R.id.activo_direcogida);
        DirCita = (MaterialEditText)v.findViewById(R.id.activo_direcita);
        DirRegreso = (MaterialEditText)v.findViewById(R.id.activo_direfinalserv);
        Acompañante = (MaterialEditText)v.findViewById(R.id.activo_acompanante);
        CAcomp = (MaterialEditText)v.findViewById(R.id.activo_califacomp);
        CCond = (MaterialEditText)v.findViewById(R.id.activo_califcond);
        CVehic = (MaterialEditText)v.findViewById(R.id.activo_califveh);

        TipoServ.setFocusable(false);
        TipoServ.setFocusableInTouchMode(false);
        Fecha.setFocusable(false);
        Fecha.setFocusableInTouchMode(false);
        Hora.setFocusable(false);
        Hora.setFocusableInTouchMode(false);
        IdServicio.setFocusable(false);
        IdServicio.setFocusableInTouchMode(false);
        Nombre.setFocusable(false);
        Nombre.setFocusableInTouchMode(false);
        TipoId.setFocusable(false);
        TipoId.setFocusableInTouchMode(false);
        Id.setFocusable(false);
        Id.setFocusableInTouchMode(false);
        DirRecogida.setFocusable(false);
        DirRecogida.setFocusableInTouchMode(false);
        DirCita.setFocusable(false);
        DirCita.setFocusableInTouchMode(false);
        DirRegreso.setFocusable(false);
        DirRegreso.setFocusableInTouchMode(false);
        Acompañante.setFocusable(false);
        Acompañante.setFocusableInTouchMode(false);
        CAcomp.setFocusable(false);
        CAcomp.setFocusableInTouchMode(false);
        CCond.setFocusable(false);
        CCond.setFocusableInTouchMode(false);
        CVehic.setFocusable(false);
        CVehic.setFocusableInTouchMode(false);
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
        void onFragmentInteraction(String text);

        String getServicio();
        String getTipo();
    }
}
