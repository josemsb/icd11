package com.appgrouplab.icd11;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.appgrouplab.icd11.Adapters.listarPaginas;
import com.appgrouplab.icd11.Datos.Pagina;
import com.appgrouplab.icd11.Otros.CustomProgressBar;
import com.appgrouplab.icd11.db.AccesoWiki;
import com.appgrouplab.icd11.db.DBAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class listadoPaginas extends AppCompatActivity {
    ArrayList<Pagina> paginas = new ArrayList<Pagina>();
    String  strWiki;
    RecyclerView recyclerView;
    listarPaginas mAdapter;
    DBAdapter dbAdapter;
    private static CustomProgressBar progressBar;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.getDialog().dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_paginas);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            strWiki = extras.getString("wiki");
            //strWiki = strWiki.toString().substring(0,1).toUpperCase() + strWiki.toString().substring(1).toLowerCase();

            getSupportActionBar().setSubtitle(strWiki);

            recyclerView = (RecyclerView) findViewById(R.id.recyclyPaginas);
            recyclerView.setHasFixedSize(true);

            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new listarPaginas(this);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent ventana = new Intent(getApplicationContext(), detalle_wikipedia.class);
                    ventana.putExtra("wiki", paginas.get(recyclerView.getChildAdapterPosition(view)).getTitle());
                    startActivity(ventana);

                }
            });


            progressBar = new CustomProgressBar();
            progressBar.show(this,"Cargando...");
            progressBar.mostrar();

            new cargarPaginas().execute("https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&list=search&utf8=1&srnamespace=0&srlimit=8&srprop=sectiontitle%7Csnippet%7Cwordcount&srsort=relevance&srsearch=" + strWiki.replace(" ","%20"));


        }
    }
    public class cargarPaginas extends AsyncTask<String, Void, String> {
        AccesoWiki a = new AccesoWiki();

        @Override
        protected String doInBackground(String... urls){
            try{

                return a.downloadUrl(urls[0]);

            }catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid";
            }
        }

        @Override
        protected  void onPostExecute(String result){
            try{

                JSONObject obj = new JSONObject(result);
                JSONObject objMobileView =  obj.getJSONObject("query");
                //og.d("objMobileView",objMobileView.get("pages").toString());

                JSONArray ja = objMobileView.getJSONArray("search");


                Log.d("JSONArray",""+ja.length());

                for(int i=0; i<ja.length(); i++)
                {
                    JSONObject jo = ja.getJSONObject(i);
                    if(jo.getInt("wordcount")>40) {
                        Pagina pagina = new Pagina();
                        pagina.setPageid(jo.getString("pageid"));
                        pagina.setSnippet(jo.getString("snippet"));
                        pagina.setTitle(jo.getString("title"));
                        paginas.add(pagina);
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            mAdapter.setDataset(paginas);
            progressBar.getDialog().hide();

            if(paginas.size()==0)
            {
                alert("Ups .. now we do not find results");
            }

        }


    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wiki,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {


        switch (item.getItemId()) {


            case R.id.aviso:
                alert("We try to give you similar results of the selected disease. Occasionally it may not be what you are looking for, excuse us :)");
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
