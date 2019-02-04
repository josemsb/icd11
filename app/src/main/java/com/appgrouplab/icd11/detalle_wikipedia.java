package com.appgrouplab.icd11;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.appgrouplab.icd11.Datos.Preferencias;
import com.appgrouplab.icd11.Otros.CustomProgressBar;
import com.appgrouplab.icd11.db.AccesoWiki;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class detalle_wikipedia extends AppCompatActivity {
    String  strWiki;
    WebView webView;
    private static CustomProgressBar progressBar;
    boolean mIsPremium = false;
    private AdView mAdView;

    Preferencias pPreferencia = new Preferencias(this) ;

    @Override
    protected void onPause() {
        super.onPause();
        progressBar.getDialog().dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
       }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_wikipedia);

        mIsPremium = pPreferencia.obtenerPremium();

        mAdView = findViewById(R.id.adViewW);

        if(mIsPremium)
        {
            mAdView.setVisibility(View.INVISIBLE);
            mAdView.getLayoutParams().height=1;
        }else {
            mAdView.setVisibility(View.VISIBLE);
            MobileAds.initialize(this, "ca-app-pub-3918734194731544~3264807181");
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            strWiki = extras.getString("wiki");

            getSupportActionBar().setSubtitle(strWiki);

            progressBar = new CustomProgressBar();
            progressBar.show(this,"Cargando...");


            webView = findViewById(R.id.webView1);
            WebSettings websettings = webView.getSettings();
            websettings.setJavaScriptEnabled(false);

            progressBar.mostrar();

            new cargarWiki().execute("https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&utf8=1&prop=extracts&titles=" + strWiki.replace(" ","%20"));


        }
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
                alert("This app contains information collected from Wikipedia about medical issues. However, it is not possible to guarantee the veracity of the content in the articles, since these may present errors, falsehoods or outdated. And, although the information may be correct or reliable and its content well documented, it is possible that what is described does not correspond to a specific health situation.");
                break;

        }

        return super.onOptionsItemSelected(item);
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
                JSONObject objMobileView =  obj.getJSONObject("query");
                JSONArray ja = objMobileView.getJSONArray("pages");
                JSONObject jo = ja.getJSONObject(0);
                String unencodeHTML = "<html><body>" + jo.getString("extract") + "</body></html>";
                String encodeHTML = Base64.encodeToString(unencodeHTML.getBytes(),Base64.NO_PADDING);

                webView.loadData(encodeHTML,"text/html; charset=utf-8","base64");

            }catch (JSONException e){
                e.printStackTrace();
            }
            progressBar.getDialog().hide();

        }


    }


    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    @Override
    public void onDestroy() {

        if(mAdView !=null)
        {mAdView.destroy();}
        super.onDestroy();
    }


}
