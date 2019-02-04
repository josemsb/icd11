package com.appgrouplab.icd11.db;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.appgrouplab.icd11.Datos.Enfermedad;
import com.appgrouplab.icd11.Datos.Tabla.CIETable;
import com.appgrouplab.icd11.Datos.Tabla.ItemColumns;

import java.util.ArrayList;


public class DBAdapter extends Service {

	private final IBinder mBinder = new LocalBinder();
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	public String campo= "campo";
	ArrayList<Enfermedad> enfermedades = new ArrayList<Enfermedad>();

	public class LocalBinder extends Binder {
		public DBAdapter getService() {			
			return DBAdapter.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {		
		return mBinder;
	}

	@Override
	public void onCreate() {

		dbHelper = new DBHelper(this);		
		db = dbHelper.getDataBase();

	}
	
	@Override
	public void onDestroy() {
		db.close();
		Log.d("DB","onDestroy");
	}




	public ArrayList<Enfermedad> getEnfermedadesCapitulo(String strCapitulo) {



		String where = " trim(CAPITULO) = '" + strCapitulo + "'";
		ArrayList<Enfermedad> enfermedades = new ArrayList<Enfermedad>();

		Cursor result = db.query(CIETable.TABLE_NAME, CIETable.COLS, where, null, null, null, null);
		if (result.moveToFirst())
			do {

				Enfermedad enfermedad = new Enfermedad();

				enfermedad.setCodigo(result.getString(result.getColumnIndex(ItemColumns.CODIGO)));
				enfermedad.setTitulo(result.getString(result.getColumnIndex(ItemColumns.TITULO)));
				enfermedades.add(enfermedad);

			} while (result.moveToNext());
		result.close();
		return enfermedades;
	}

	public ArrayList<Enfermedad> getEnfermedadesBusqueda(String strBusqueda) {

		String original = strBusqueda.replace(" ","%");
		String[] separated = strBusqueda.split(" ");
		String select = "SELECT CODIGO,TITULO from CIE WHERE ";
		String where="";
		String all="";
		String sqlPrimario = select + " titulo like '%" + original + "%' or codigo like '%" + original + "%' LIMIT 700 ";
		String sqlSecundario = "";

		//Log.d("DBHADAPTER",db.toString());

		int cantidad = 0;

		for (int i = 0; i <  separated.length; i++) {
			if(separated[i].trim().length() >0)
			{	cantidad=cantidad+1;
				where = " titulo like '%" + separated[i] + "%' or codigo like '%" + separated[i] + "%' ";
				all = all + select + where;
				if (i!=separated.length-1)
				{ all = all + " union "; }
			}
		}

		all = all + " LIMIT 700 ";

		if(cantidad>1)
		{   sqlSecundario = select + " titulo like '" + separated[0] + "%' or codigo like '" + separated[0] + "%' LIMIT 700";


			//hice el cambio porque primero va los resultados like= 'campo%'
			Cursor resultPrimario = db.rawQuery(sqlPrimario,null);
			Cursor resultSecundaria = db.rawQuery(sqlSecundario,null);
			Cursor result = db.rawQuery(all,null);

			if (resultPrimario.moveToFirst())
				do {

					Enfermedad enfermedad = new Enfermedad();

					enfermedad.setCodigo(resultPrimario.getString(resultPrimario.getColumnIndex(ItemColumns.CODIGO)));
					enfermedad.setTitulo(resultPrimario.getString(resultPrimario.getColumnIndex(ItemColumns.TITULO)));
					enfermedades.add(enfermedad);

				} while (resultPrimario.moveToNext());

			if (resultSecundaria.moveToFirst())
				do {
					if(buscarEnfermedad(resultSecundaria.getString(resultSecundaria.getColumnIndex(ItemColumns.CODIGO))))
					{
						Enfermedad enfermedad = new Enfermedad();

						enfermedad.setCodigo(resultSecundaria.getString(resultSecundaria.getColumnIndex(ItemColumns.CODIGO)));
						enfermedad.setTitulo(resultSecundaria.getString(resultSecundaria.getColumnIndex(ItemColumns.TITULO)));
						enfermedades.add(enfermedad);

					}

				} while (resultSecundaria.moveToNext());

			if (result.moveToFirst())
				do {
					if(buscarEnfermedad(result.getString(result.getColumnIndex(ItemColumns.CODIGO))))
					{
						Enfermedad enfermedad = new Enfermedad();

						enfermedad.setCodigo(result.getString(result.getColumnIndex(ItemColumns.CODIGO)));
						enfermedad.setTitulo(result.getString(result.getColumnIndex(ItemColumns.TITULO)));
						enfermedades.add(enfermedad);
					}

				} while (result.moveToNext());

			result.close();
			resultSecundaria.close();
			resultPrimario.close();

		}
		else
		{	sqlPrimario= select +  " titulo like '" + separated[0] + "%' or codigo like '" + separated[0] + "%'";
			Cursor resultPrimario = db.rawQuery(sqlPrimario,null);
			Cursor result = db.rawQuery(all,null);

			if (resultPrimario.moveToFirst())
				do {
					Enfermedad enfermedad = new Enfermedad();

					enfermedad.setCodigo(resultPrimario.getString(resultPrimario.getColumnIndex(ItemColumns.CODIGO)));
					enfermedad.setTitulo(resultPrimario.getString(resultPrimario.getColumnIndex(ItemColumns.TITULO)));
					enfermedades.add(enfermedad);
				} while (resultPrimario.moveToNext());

			if (result.moveToFirst())
				do {
					if(buscarEnfermedad(result.getString(result.getColumnIndex(ItemColumns.CODIGO))))
					{
						Enfermedad enfermedad = new Enfermedad();

						enfermedad.setCodigo(result.getString(result.getColumnIndex(ItemColumns.CODIGO)));
						enfermedad.setTitulo(result.getString(result.getColumnIndex(ItemColumns.TITULO)));
						enfermedades.add(enfermedad);}

				} while (result.moveToNext());

			result.close();
			resultPrimario.close();

		}

		return enfermedades;

	}

	public ArrayList<Enfermedad> getEnfermedadesPreferentes(ArrayList<String> strCampos) {
		//String[] separated = CIETable.CAMPO_BUSQUEDA.split(" ");
		String select = "SELECT CODIGO,TITULO from CIE WHERE trim(CODIGO) IN (";
		String where="";
		String all="";

		for (int i = 0; i <  strCampos.size(); i++) {
			where = where + "'" + remove1(strCampos.get(i).trim()) + "'";
			if (i!= strCampos.size()-1)
			{ where = where + ","; }
		}

		all =  select + where + ") ORDER BY CODIGO";

		Cursor result = db.rawQuery(all,null);

		//Cursor result = db.query(CIETable.TABLE_NAME, CIETable.COLS,where, null, null, null, null);

		if (result.moveToFirst())
			do {

				Enfermedad enfermedad = new Enfermedad();

				enfermedad.setCodigo(result.getString(result.getColumnIndex(ItemColumns.CODIGO)));
				enfermedad.setTitulo(result.getString(result.getColumnIndex(ItemColumns.TITULO)));
				enfermedades.add(enfermedad);

			} while (result.moveToNext());

		result.close();
		return enfermedades;
	}

	public static String remove1(String input) {
		String original = "��������������u�������������������";
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = input;
		for (int i=0; i<original.length(); i++) {
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}
		return output;
	}


	public boolean buscarEnfermedad(String codigo) {		
		for (int i=0; i<enfermedades.size() ; i++) {
	       if(enfermedades.get(i).getCodigo().equals(codigo))
	       {
	    	   return false;
	       }
	    }
		
		return true;
	}

}
