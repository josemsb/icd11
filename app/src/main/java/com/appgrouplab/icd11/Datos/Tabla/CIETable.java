package com.appgrouplab.icd11.Datos.Tabla;

import java.util.ArrayList;

public class CIETable implements ItemColumns {

	//COBERTURA = PEAS     Campo donde estan las enfermedades del PEAS
	//ERH = (EAC) Campo donde estan las enfermedades raras y huerfanas


	public final static String TABLE_NAME = "CIE";

	public final static String[] COLS = {CODIGO, TITULO};

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (CAPITULO TEXT,CODIGO TEXT,TITULO TEXT);";
	
	public static String CAMPO_CAPITULO = "";
	public static String CAMPO_BUSQUEDA = "";
	public static ArrayList<String> CAMPO_CODIGOS;

}
