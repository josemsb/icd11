package com.appgrouplab.icd11.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	//private static String DB_PATH = "/data/data/com.cie10/databases/";
	private static String DB_PATH = "/databases/";

	private static String DB_NAME = "DBICD.db";

	private static int DATABASE_VERSION = 1;

	private final Context myContext;


	/**
	 * Constructor: Toma referencia hacia el contexto de la aplicaci�n que lo
	 * invoca para poder acceder a los 'assets' y 'resources' de la aplicaci�n.
	 * Crea un objeto DBOpenHelper que nos permitir� controlar la apertura de la
	 * base de datos.
	 *
	 * @param context
	 */
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;


	}

	/**
	 * Crea una base de datos vac�a en el sistema y la reescribe con nuestro
	 * fichero de base de datos.
	 * */
	public void createDataBase() throws IOException {
		//Log.d("DBHELPER","createDataBase");

		boolean dbExist = checkDataBase();

		if (dbExist) {	Log.d("DBHELPER","dbExist");
		} else {
			// Llamando a este metodo se crea la base de datos vacia en la ruta
			// por defecto del sistema de nuestra aplicaci�n
			// por lo que podremos sobreescribirla con
			// nuestra base de datos.
			this.getReadableDatabase();
			this.close();
			try {

				copyDataBase();

			} catch (IOException e) {
				throw new Error("Error copiando Base de Datos");
			}
		}

	}

	/**
	 * Comprueba si la base de datos existe para evitar copiar siempre el
	 * fichero cada vez que se abra la aplicaci�n.
	 *
	 * @return true si existe, false si no existe
	 */

	private boolean checkDataBase() {

		try {
			//Log.d("DBHELPER","checkDataBase");
			File databasePath = myContext.getDatabasePath(DB_NAME);
			return databasePath.exists();


		} catch (SQLiteException e) {
			throw new Error(e.getMessage());
		}
	}

	/**
	 * Copia nuestra base de datos desde la carpeta assets a la reci�n creada
	 * base de datos en la carpeta de sistema, desde d�nde podremos acceder a
	 * ella. Esto se hace con bytestream.
	 * */
	private void copyDataBase() throws IOException {

		//Log.d("DBHELPER","copyDataBase");
		// Abrimos el fichero de base de datos como entrada
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		//Log.d("copyDataBase",myInput);

		// Ruta a la base de datos vac�a reci�n creada
		String outFileName = myContext.getApplicationInfo().dataDir+ DB_PATH + DB_NAME;


		// Abrimos la base de datos vac�a como salida
		OutputStream myOutput = new FileOutputStream(outFileName);

		// Transferimos los bytes desde el fichero de entrada al de salida
		byte[] buffer = new byte[2048];
		int length;
		while ((length = myInput.read(buffer)) > 0) {

			myOutput.write(buffer, 0, length);
		}

		// Liberamos los streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	/**
	 * Inicia el proceso de copia del fichero de base de datos, o crea una base
	 * de datos vac�a en su lugar
	 * */
	public SQLiteDatabase getDataBase() {
		try {
			Log.d("DBHELPER","getDataBase");
			createDataBase();
		} catch (IOException e) {
			throw new Error("Ha sido imposible crear la Base de Datos");
		}

		String myPath = myContext.getApplicationInfo().dataDir + DB_PATH + DB_NAME;
		return SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
