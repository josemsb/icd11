package com.appgrouplab.icd11.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appgrouplab.icd11.Datos.Wiki;
import com.appgrouplab.icd11.R;

import java.util.ArrayList;

public class listarWikis extends RecyclerView.Adapter<listarWikis.ViewHolder> {

    private ArrayList<Wiki> mDataSet;
    private Context context;

    @Override
    public listarWikis.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_detelle_wikipedia,null,false);


        return new listarWikis.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(listarWikis.ViewHolder holder, int position) {

        Wiki wiki = mDataSet.get(position);

        holder.txtLine.setText(wiki.getTitle());
        holder.txtHTML.setText(Html.fromHtml(wiki.getExtract()));


    }

    public listarWikis(Context context){
        this.context = context;
        mDataSet = new ArrayList<>();
    }

    public void setDataset(ArrayList<Wiki> dataset){
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
        public TextView txtLine, txtHTML;
        private Context vcontext;

        public ViewHolder(View v){
            super(v);
            vcontext = v.getContext();
            txtLine =  v.findViewById(R.id.txtLine);
            txtHTML = v.findViewById(R.id.txtWiki);

        }

    }


}
