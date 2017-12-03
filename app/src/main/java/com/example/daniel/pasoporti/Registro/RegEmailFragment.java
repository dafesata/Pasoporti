package com.example.daniel.pasoporti.Registro;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daniel.pasoporti.NonSwipeableViewPager;
import com.example.daniel.pasoporti.R;
import com.rengwuxian.materialedittext.MaterialEditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegEmailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegEmailFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";
    private static final String TAG = "RegEmailFragment";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private MaterialEditText email, password, confirm_password;
    private NonSwipeableViewPager pager;

    public RegEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment RegEmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegEmailFragment newInstance(int page, String title) {
        RegEmailFragment fragment = new RegEmailFragment();
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
        View v=inflater.inflate(R.layout.fragment_reg_email, container, false);
        pager=(NonSwipeableViewPager) container;
        email=(MaterialEditText) v.findViewById(R.id.register_email);
        password=(MaterialEditText) v.findViewById(R.id.register_password);
        confirm_password=(MaterialEditText) v.findViewById(R.id.register_confirm_password);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(email.getText().toString())) {
                    mListener.setEmail(email.getText().toString());
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(password.getText().toString())) {
                    mListener.setPassword(password.getText().toString());
                }
            }
        });

        validate();

        // Inflate the layout for this fragment
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
    private void validate (){

        if(!TextUtils.isEmpty(email.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
            if(password.getText().toString().equals(confirm_password.getText().toString())){
                pager.setPagingEnabled(true);
            }else{
                pager.setPagingEnabled(false);
            }
        }else{
            pager.setPagingEnabled(false);
        }

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())){
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        pager.setPagingEnabled(true);
                    }else{
                        pager.setPagingEnabled(false);
                    }
                }else{
                    pager.setPagingEnabled(false);
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
        void onFragmentInteraction(String Email, String Password);
        String getEmail();
        String getPassword();

        void setEmail(String email);
        void setPassword(String password);


    }
}
