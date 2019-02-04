package com.appgrouplab.icd11.Datos;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {
    private Context mContext;

    public Preferencias(Context context){
        this.mContext = context;
    }


    public String obtenerCorreo(){
        SharedPreferences spreferencias = mContext.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        return spreferencias.getString("correo", "");
    }
    public String obtenerDefecto(){
        SharedPreferences spreferencias = mContext.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        return spreferencias.getString("defecto", "");
    }

    public void guardarCorreo(String v_correo){
        SharedPreferences spreferencias = mContext.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spreferencias.edit();
        editor.putString("correo",v_correo);
        editor.apply();
    }
    public void guardarDefecto(Integer v_defecto){
        SharedPreferences spreferencias = mContext.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spreferencias.edit();
        editor.putInt("defecto",v_defecto);
        editor.apply();
    }

    public Boolean obtenerPremium(){
        SharedPreferences spreferencias = mContext.getSharedPreferences("billing", Context.MODE_PRIVATE);
        return spreferencias.getBoolean("mIsPremium",false);
    }


    public void guardarPreferencias(Boolean vPremiun){
       SharedPreferences spreferencias = mContext.getSharedPreferences("billing", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spreferencias.edit();
        editor.putBoolean("mIsPremium",vPremiun);
        editor.apply();
    }


}
