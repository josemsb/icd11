package com.appgrouplab.icd11;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appgrouplab.icd11.Adapters.listarEnfermedades;
import com.appgrouplab.icd11.Datos.Enfermedad;
import com.appgrouplab.icd11.Datos.Preferencias;
import com.appgrouplab.icd11.db.DBAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class listadoCIEfavoritos extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    listarEnfermedades mAdapter;
    DBAdapter dbAdapter;
    String strBusqueda;
    ArrayList<String> list;
    private AdView mAdView;
    boolean mIsPremium = false;
    Boolean mBound=false;

    public  ArrayList<String> CAMPO_CODIGOS;

    public listadoCIEfavoritos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("listadoCIEfavoritos","onCreate");
        //hideSoftKeyBoard();


    }

    private void hideSoftKeyBoard()
    {   Log.e("hideSoftKeyBoard1", "preguntar ");
        if(getActivity().getCurrentFocus()!=null)
        {   Log.e("hideSoftKeyBoard1", "cerrar" );
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            Log.e("hideSoftKeyBoard1", "cerrar1" );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_listadocie_favoritos, container, false);
        Log.d("listadoCIEfavoritos","onCreateView");


        list = getStringArrayPref(getActivity(), "Codigo_Guardados");
        Preferencias pPreferencia = new Preferencias(getContext()) ;
        mIsPremium = pPreferencia.obtenerPremium();

        mAdView = rootView.findViewById(R.id.adViewF);

        if(mIsPremium)
        {
            mAdView.setVisibility(View.INVISIBLE);
            mAdView.getLayoutParams().height=1;
        }else {
            mAdView.setVisibility(View.VISIBLE);
            MobileAds.initialize(getContext(), "ca-app-pub-3918734194731544~3264807181");
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleCIEFavoritos);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new listarEnfermedades(getContext());
        recyclerView.setAdapter(mAdapter);



        return rootView;

    }

    private void loadEnfermedades() {
        mAdapter.limpiar();
        ArrayList<Enfermedad> enfermedades = dbAdapter.getEnfermedadesPreferentes(list);
        mAdapter.setDataset(enfermedades);
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false; }

    }

    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
                Collections.sort(urls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    @Override
    public void onDestroy() {

        if(mAdView !=null)
        {mAdView.destroy();}
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false; }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d("mConnection","onStart");
        if(list.size()>0)
        {
            if (mConnection!=null) {
                Intent intent = new Intent(getActivity(), DBAdapter.class);
                getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }

        }else{Toast.makeText(getActivity(), " It does not add diseases",Toast.LENGTH_LONG).show();}

        super.onStart();
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DBAdapter.LocalBinder binder = (DBAdapter.LocalBinder) service;
            dbAdapter = binder.getService();
            mBound = true;
            loadEnfermedades();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
