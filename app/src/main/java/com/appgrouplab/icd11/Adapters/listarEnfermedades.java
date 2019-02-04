package com.appgrouplab.icd11.Adapters;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appgrouplab.icd11.Datos.Enfermedad;
import com.appgrouplab.icd11.Otros.CustomProgressBar;
import com.appgrouplab.icd11.R;
import com.appgrouplab.icd11.db.AccesoWiki;
import com.appgrouplab.icd11.detalle_wikipedia;
import com.appgrouplab.icd11.listadoPaginas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class listarEnfermedades extends RecyclerView.Adapter<listarEnfermedades.ViewHolder> {
    private ArrayList<Enfermedad> mDataSet;
    private Context context;
    public Boolean bolBusqueda=false;
    public String strBusqueda="";
    public String strWiki="";
    ArrayList<String> list = new ArrayList<>();
    private static CustomProgressBar progressBar;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_listado_enfermedades,null,false);


        return new ViewHolder(v);
    }

    private String cargarHTML(String strHTML,String Campo)
    {
        String[] separated = Campo.split(" ");

        for (int i = 0; i <  separated.length; i++) {
            if(separated[i].trim().length() >0)
            {
               strHTML = strHTML.replace(separated[i].trim().toUpperCase(),"<font color=\"red\">" + separated[i].trim().toUpperCase() + "</font>");
            }
        }

        return strHTML;
    }

    @Override
    public void onBindViewHolder(listarEnfermedades.ViewHolder holder, int position) {

        Boolean insertar=true;
        Enfermedad enfermedad = mDataSet.get(position);

        holder.txtEnfermedad_codigo.setText(enfermedad.getCodigo() + " ");

        if (bolBusqueda) {
            holder.txtEnfermedad_titulo.setText(Html.fromHtml(cargarHTML(enfermedad.getTitulo(),strBusqueda)));
        }
            else
            holder.txtEnfermedad_titulo.setText(enfermedad.getTitulo());

        holder.imgEnfermedad_favorito.setTag(holder.txtEnfermedad_codigo.getText().toString());
        holder.imgEnfermedad_web.setTag(holder.txtEnfermedad_titulo.getText().toString());
        holder.imgEnfermedad_wiki.setTag(holder.txtEnfermedad_titulo.getText().toString());
        holder.imgEnfermedad_shared.setTag(holder.txtEnfermedad_codigo.getText().toString() + " " + holder.txtEnfermedad_titulo.getText().toString());

        holder.imgEnfermedad_favorito.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v){

                  ImageButton imgEnfermedad;
                  imgEnfermedad = v.findViewById(R.id.imgEnfermedad_favorito);
                  int i;
                  Boolean insertar=true;

                  list = new ArrayList<>();
                  list = getStringArrayPref(v.getContext(), "Codigo_Guardados");

                  if (list.size()>0)
                  {
                      for (i=0;i <= list.size()-1;i++) {
                          if(list.get(i).equals(v.getTag().toString()))
                          {insertar=false;list.remove(i);}

                      }
                  }
                  if(insertar)
                  {
                      list.add(v.getTag().toString());
                      setStringArrayPref(v.getContext(), "Codigo_Guardados", list);
                      imgEnfermedad.setImageResource(R.drawable.baseline_favorite_black_24);
                      Toast.makeText(v.getContext(), v.getTag().toString() + " a favoritos",Toast.LENGTH_LONG).show();
                  }
                  else{
                      setStringArrayPref(v.getContext(), "Codigo_Guardados", list);
                      imgEnfermedad.setImageResource(R.drawable.baseline_favorite_border_black_24);
                      Toast.makeText(v.getContext(), "Código eliminado de favoritos",Toast.LENGTH_LONG).show();
                      }

              }});

        holder.imgEnfermedad_web.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH );
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(SearchManager.QUERY,v.getTag().toString().trim());
                    context.startActivity(intent);
                }
            });

        holder.imgEnfermedad_wiki.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                strWiki = v.getTag().toString().trim().toLowerCase();
                progressBar.mostrar();
                new cargarWiki().execute("https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&list=search&utf8=1&srwhat=nearmatch&srprop=sectiontitle%7Cwordcount&srsearch=" + strWiki.replace(" ","%20"));

            }
        });

        holder.imgEnfermedad_shared.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //try
                //{
                //Intent intent = new Intent(Intent.ACTION_SEND );
                //intent.setType("text/plain");
                //intent.putExtra(Intent.EXTRA_TEXT,v.getTag().toString());
                //intent.setPackage("com.facebook.katana");
                //intent.setPackage("com.whatsapp");
                //context.startActivity(intent);
                //}catch (ActivityNotFoundException e)
                //{
                //    alert("Ummm, parce que no tiene instalado whaspp");
                //}
            }
        });

        int i;

        if (list.size()>0)
        {
            for (i=0;i <= list.size()-1;i++) {
                if(list.get(i).equals(holder.txtEnfermedad_codigo.getText().toString()))
                {insertar=false;}

            }
        }
        if(insertar)
            holder.imgEnfermedad_favorito.setImageResource(R.drawable.baseline_favorite_border_black_24);
        else
            holder.imgEnfermedad_favorito.setImageResource(R.drawable.baseline_favorite_black_24);



    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    public listarEnfermedades(Context context){
        this.context = context;
        mDataSet = new ArrayList<>();
        progressBar = new CustomProgressBar();
        progressBar.show(context,"Cargando...");

        list = getStringArrayPref(context, "Codigo_Guardados");

    }

    public void setDataset(ArrayList<Enfermedad> dataset){
        mDataSet = dataset;
        notifyDataSetChanged();
    }

    public void limpiar(){
        notifyItemRangeRemoved(0,mDataSet.size());
        mDataSet.clear();
    }


    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d("onDetached","hola");
        progressBar.getDialog().dismiss();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView txtEnfermedad_codigo, txtEnfermedad_titulo;
        public ImageButton imgEnfermedad_favorito,imgEnfermedad_web,imgEnfermedad_wiki,imgEnfermedad_shared;
        private Context vcontext;

        public ViewHolder(View v){
            super(v);
            vcontext = v.getContext();
            txtEnfermedad_codigo =  v.findViewById(R.id.txtEnfermedad_codigo);
            txtEnfermedad_titulo = v.findViewById(R.id.txtEnfermedad_titulo);
            imgEnfermedad_favorito =  v.findViewById(R.id.imgEnfermedad_favorito);
            imgEnfermedad_web = v.findViewById(R.id.imgEnfermedad_web);
            imgEnfermedad_wiki = v.findViewById(R.id.imgEnfermedad_wiki);
            imgEnfermedad_shared = v.findViewById(R.id.imgEnfermedad_shared);
        }

    }


    private static ArrayList<String> getStringArrayPref(Context context, String key) {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private static void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public class cargarWiki extends AsyncTask<String, Void, String> {
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
                    Log.d("obj",result);
                    JSONObject objMobileView =  obj.getJSONObject("query");
                    Log.d("objMobileView",objMobileView.get("search").toString());

                    JSONArray ja = objMobileView.getJSONArray("search");

                    progressBar.getDialog().hide();

                    if(ja.length()>0)
                    {
                        JSONObject jo = ja.getJSONObject(0);
                        if(jo.getInt("wordcount")>40) {
                            strWiki = jo.get("title").toString();
                            Intent ventana = new Intent(context, detalle_wikipedia.class);
                            ventana.putExtra("wiki", strWiki);
                            context.startActivity(ventana);
                        }
                        else
                        {
                            Intent ventana = new Intent(context,listadoPaginas.class);
                            ventana.putExtra("wiki",strWiki);
                            context.startActivity(ventana);
                        }
                    }
                    else
                    {
                        Intent ventana = new Intent(context,listadoPaginas.class);
                        ventana.putExtra("wiki",strWiki);
                        context.startActivity(ventana);
                    }


            }catch (JSONException e){
                e.printStackTrace();
            }


        }




    }
}
