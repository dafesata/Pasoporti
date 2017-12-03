package com.example.daniel.pasoporti.Cliente.ProgramarServicios;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.daniel.pasoporti.Clases.Acompanado;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> The class type to use as a model for the data contained in the children of the
 *            given Firebase location
 */
public abstract class FirebaseAcompanadoSpinnerAdapter<T> extends ArrayAdapter<T> {

    private Query mQuery;
    private ArrayList<Acompanado> mItems;
    private ArrayList<String> mKeys;

    public FirebaseAcompanadoSpinnerAdapter(Query query,Context context, int textViewResourceId){
        this(query,null,null,context,textViewResourceId);
    }

    /**
     * @param query The Firebase location to watch for data changes.
     *              Can also be a slice of a location, using some combination of
     *              <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>.
     * @param items List of items that will load the adapter before starting the listener.
     *              Generally null or empty, but this can be useful when dealing with a
     *              configuration change (e.g.: reloading the adapter after a device rotation).
     *              Be careful: keys must be coherent with this list.
     * @param keys  List of keys of items that will load the adapter before starting the listener.
     *              Generally null or empty, but this can be useful when dealing with a
     *              configuration change (e.g.: reloading the adapter after a device rotation).
     *              Be careful: items must be coherent with this list.
     */
    public FirebaseAcompanadoSpinnerAdapter(Query query, @Nullable ArrayList<Acompanado> items,
                                            @Nullable ArrayList<String> keys, Context context, int textViewResourceId) {

        super(context,textViewResourceId, (List<T>) items);
        this.mQuery = query;
        this.mItems = items;
        this.mKeys = keys;
        /*
        if (items != null && keys != null) {
            this.mItems = items;
            this.mKeys = keys;
        } else {
            mItems = new ArrayList<Acompanado>();
            mKeys = new ArrayList<String>();
        }*/
        query.addChildEventListener(mListener);
    }

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            String key = dataSnapshot.getKey();

            if (!mKeys.contains(key)) {
               Acompanado item = (Acompanado) getConvertedObject(dataSnapshot);
                int insertedPosition;
                if (previousChildName == null) {
                    mItems.add(0, item);
                    mKeys.add(0, key);
                    insertedPosition = 0;
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mItems.size()) {
                        mItems.add(item);
                        mKeys.add(key);
                    } else {
                        mItems.add(nextIndex, item);
                        mKeys.add(nextIndex, key);
                    }
                    insertedPosition = nextIndex;
                }
                notifyDataSetChanged();

            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();

            if (mKeys.contains(key)) {
                int index = mKeys.indexOf(key);
                Acompanado oldItem = mItems.get(index);
                Acompanado newItem = getConvertedObject(dataSnapshot);

                mItems.set(index, newItem);
                notifyDataSetChanged();

            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();

            if (mKeys.contains(key)) {
                int index = mKeys.indexOf(key);
                Acompanado item = mItems.get(index);

                mKeys.remove(index);
                mItems.remove(index);
                notifyDataSetChanged();

            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            String key = dataSnapshot.getKey();

            int index = mKeys.indexOf(key);
            Acompanado item = getConvertedObject(dataSnapshot);
            mItems.remove(index);
            mKeys.remove(index);
            int newPosition;
            if (previousChildName == null) {
                mItems.add(0, item);
                mKeys.add(0, key);
                newPosition = 0;
            } else {
                int previousIndex = mKeys.indexOf(previousChildName);
                int nextIndex = previousIndex + 1;
                if (nextIndex == mItems.size()) {
                    mItems.add(item);
                    mKeys.add(key);
                } else {
                    mItems.add(nextIndex, item);
                    mKeys.add(nextIndex, key);
                }
                newPosition = nextIndex;
            }
            notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur.");
        }

    };


    public int getItemCount() {
        return (mItems != null) ? mItems.size() : 0;
    }

    public ArrayList<Acompanado> getItems() {
        return mItems;
    }

    public T getItem(String nombre){
        T result = null;
        for(Acompanado item:mItems){
            if(TextUtils.equals(item.getNombre().trim(),nombre.trim())){
                result=(T) item;
            }
        }
        return result;
    }

    public T getItem(int position) {
        return (T) mItems.get(position);
    }

    /**
     * Converts the data snapshot to generic object
     *
     * @param snapshot Result
     * @return Data converted
     */
    protected Acompanado getConvertedObject(DataSnapshot snapshot) {
        String Nombre= (String)snapshot.child("nombre").getValue();
        String TipoId= (String)snapshot.child("tipoId").getValue();
        String Identificacion= (String)snapshot.child("id").getValue();
        String Direccion= (String)snapshot.child("direccion").getValue();
        String Telefono= (String)snapshot.child("telefono").getValue();
        String Email= (String)snapshot.child("email").getValue();
        String EPS= (String)snapshot.child("eps").getValue();
        String UID= (String)snapshot.child("uid").getValue();
        String Parentesco=(String)snapshot.child("parentesco").getValue();
        return new Acompanado(Nombre,Email,Direccion,TipoId,UID,Identificacion,Telefono,EPS,Parentesco);
    }

}