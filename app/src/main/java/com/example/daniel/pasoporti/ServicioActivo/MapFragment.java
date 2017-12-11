package com.example.daniel.pasoporti.ServicioActivo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.example.daniel.pasoporti.BuildConfig;
import com.example.daniel.pasoporti.Clases.Servicio;
import com.example.daniel.pasoporti.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String TAG= this.getClass().getName();
    private TextView servicioId,servicioEstado;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mServiciosReference;
    private Query query;
    private Servicio servicio;
    private GoogleMap mGoogleMap;
    private LocationManager mManager;
    private MapView mapView;
    private View v;
    private SwipeButton swipeButton;

    private int REQUEST_CODE = 1;
    private BroadcastReceiver mLocationReceiver;
    protected Boolean mRequestingLocationUpdates;
    private Intent mRequestLocationIntent;
    protected final static String LOCATION_KEY = "location-key";
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";

    private Location mCurrentLocation;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(int param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        v= inflater.inflate(R.layout.fragment_map, container, false);
        swipeButton=(SwipeButton) v.findViewById(R.id.SwipeButton);

        if(mListener.getTipo().equals("Cliente")){
            swipeButton.setVisibility(View.GONE);
        }

        mDatabase= FirebaseDatabase.getInstance();
        mServiciosReference=mDatabase.getReference("Servicios");
        mAuth= FirebaseAuth.getInstance();

        servicioId= (TextView) v.findViewById(R.id.Map_ServicioId);
        servicioEstado=(TextView) v.findViewById(R.id.Map_ServicioEstado);
        mapView=(MapView) v.findViewById(R.id.map);



        mServiciosReference.child(mListener.getServicio()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicio=new Servicio().getConvertedObject(dataSnapshot);
                servicioId.setText("Servicio "+servicio.getId());
                servicioEstado.setText("Estado: "+servicio.getEstado());

                swipeButton.setOnActiveListener(new OnActiveListener() {
                    @Override
                    public void onActive() {
                        Log.d(TAG, "onActive: Activo");
                        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);

                        dialogBuilder.setTitle("Cambiar Estado");
                        dialogBuilder.setMessage("¿Seguro desea cambiar el estado?");
                        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newEstado = null;
                                switch (servicio.getEstado()){
                                    case "Aprobado":
                                        newEstado="Iniciado";
                                        break;
                                    case "Iniciado":
                                        newEstado="En Cita";
                                        break;
                                    case "En Cita":
                                        newEstado="Retorno";
                                        break;
                                    case "Retorno":
                                        newEstado="Finalizado";
                                        break;
                                }
                                Map<String,Object> ServicioMap = new HashMap<>();
                                ServicioMap.put("estado",newEstado);

                                mDatabase.getReference("Servicios").child(servicio.getUID()).updateChildren(ServicioMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        servicioEstado.setText("Estado: "+servicio.getEstado());
                                        Toast.makeText(getActivity(), "Estado Cambiado", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });

                        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        dialogBuilder.show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        InitiateService(savedInstanceState);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        mapView=(MapView) v.findViewById(R.id.map);
        if(mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
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
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle("GPS Desactivado");
        builder.setMessage("Su GPS se encuentra desactivado, desea activarlo?");
        builder.setCancelable(false);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                launchGPSOptions();

            }
        });
    }

    private void launchGPSOptions()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_CODE);
    }


    private void updateMap() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()), 21f));
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Updating values from bundle");
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);

//        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void InitiateService(Bundle savedInstanceState){
        mManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        mLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final LatLng cLocation = intent.getParcelableExtra(LocationUpdaterServices.COPA_MESSAGE);
                mCurrentLocation = new Location("");
                mCurrentLocation.setLatitude(cLocation.latitude);
                mCurrentLocation.setLongitude(cLocation.longitude);
                mServiciosReference.child(mListener.getServicio()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        servicio=new Servicio().getConvertedObject(dataSnapshot);
                        Map<String,Object> ServicioMap = new HashMap<>();
                        ServicioMap.put("Lat",cLocation.latitude);
                        ServicioMap.put("Lon",cLocation.longitude);

                        mDatabase.getReference("Servicios").child(servicio.getUID()).child("Localizacion").updateChildren(ServicioMap);
                        Log.d(TAG, "onDataChange: "+cLocation.latitude);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                updateMap();

            }
        };

        mRequestLocationIntent = new Intent(getActivity(), LocationUpdaterServices.class);
        getActivity().startService(mRequestLocationIntent);

        updateValuesFromBundle(savedInstanceState);
    }

}
