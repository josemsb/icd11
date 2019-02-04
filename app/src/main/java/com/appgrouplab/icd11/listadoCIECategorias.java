package com.appgrouplab.icd11;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appgrouplab.icd11.Adapters.listarCapitulos;
import com.appgrouplab.icd11.Datos.Capitulo;
import com.appgrouplab.icd11.content.MenuCategoria;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class listadoCIECategorias extends Fragment {

    RecyclerView recyclerView;
    private MenuCategoria.contentItem mItem;
    ArrayList<Capitulo> capitulos = new ArrayList<Capitulo>();
    listarCapitulos mAdapter;
    View rootView;

    public listadoCIECategorias() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_listadocie_categorias, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclyCapitulos);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new listarCapitulos(getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.getDialog().dismiss();
                Intent ventana = new Intent(getContext(),listadoEnfermedades.class);
                ventana.putExtra("capitulo",capitulos.get(recyclerView.getChildAdapterPosition(view)).getCapitulo());
                ventana.putExtra("codigo",capitulos.get(recyclerView.getChildAdapterPosition(view)).getCodigos());
                ventana.putExtra("titulo",capitulos.get(recyclerView.getChildAdapterPosition(view)).getTitulo());

                startActivity(ventana);
            }
        });

        for(int i = 0; i< MenuCategoria.ITEMS.size(); i++)
        {
            Capitulo capitulo = new Capitulo();
            capitulo.setCapitulo(MenuCategoria.ITEMS.get(i).capitulo);
            capitulo.setCodigos(MenuCategoria.ITEMS.get(i).codigos);
            capitulo.setTitulo(MenuCategoria.ITEMS.get(i).titulo);
            capitulos.add(capitulo);
        }

        mAdapter.setDataset(capitulos);

        return rootView;
    }

}
