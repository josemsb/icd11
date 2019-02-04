package com.appgrouplab.icd11.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appgrouplab.icd11.Datos.Pagina;
import com.appgrouplab.icd11.R;

import java.util.ArrayList;

public class listarPaginas extends RecyclerView.Adapter<listarPaginas.ViewHolder> implements  View.OnClickListener  {
    private ArrayList<Pagina> mDataSet;
    private Context context;
    private View.OnClickListener listener;

    @Override
    public listarPaginas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_listado_paginas,null,false);

        v.setOnClickListener(this);
        return new listarPaginas.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(listarPaginas.ViewHolder holder, int position) {

        Pagina pagina = mDataSet.get(position);

        holder.txtPagina_title.setText(pagina.getTitle());
        holder.txtPagina_snippet.setText(Html.fromHtml(pagina.getSnippet()));

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public listarPaginas(Context context){
        this.context = context;
        mDataSet = new ArrayList<>();

    }

    public void setDataset(ArrayList<Pagina> dataset){
        mDataSet = dataset;
        notifyDataSetChanged();
    }

    public void limpiar(){
        notifyItemRangeRemoved(0,mDataSet.size());
        mDataSet.clear();
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView txtPagina_title, txtPagina_snippet;
        private Context vcontext;

        public ViewHolder(View v){
            super(v);
            vcontext = v.getContext();
            txtPagina_title =  v.findViewById(R.id.txtPagina_title);
            txtPagina_snippet = v.findViewById(R.id.txtPagina_snippet);
        }

    }

}
