package com.appgrouplab.icd11.db;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccesoWiki {

    public String downloadUrl(String myurl) throws IOException {
        Log.d("URL", myurl);
        myurl = myurl.replace(" ","%20");
        InputStream is = null;
        int len=0;

        try{
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000 /* milliseconds */);
            conn.setConnectTimeout(5500);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //Empeza el query

            conn.connect();

            int response = conn.getResponseCode();
            //Log.d("JSONArray",""+response);
            is = conn.getInputStream();

            len = conn.getContentLength();

            //Convertir InputStream en string

            Log.d("accesoWiki", "readIt1");
            String contenAsString = readIt(is,len);
            Log.d("accesoWiki", "readIt2");
            return contenAsString;
        } finally {
            if(is !=null){is.close();}
        }
    }

    private String readIt(InputStream stream, int len) throws IOException, UnsupportedOperationException{

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer))!=-1)
        {
            result.write(buffer,0,length);
        }
        return result.toString("UTF-8");


    }
}
