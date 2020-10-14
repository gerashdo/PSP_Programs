import java.util.Scanner;
import java.io.*;
/**
/* Programa: 2, Contador Lineas Codigo.
/* Autor: Gerardo Jiménez Argüelles.
/* Fecha: 5 de Octubre, 2020
/* Descripcion: El programa se encarga de contar el total de lineas fisicas del codigo, 
/* numero de clases, lineas y métodos por clase, verificar si hay encabezado y 
/* variables inicializadas en la misma linea.
*/


class LeerArchivo {





	
	// Cuenta el número de lineas que contiene el archivo sin contar las que estan en blanco o vacias.
	public static int contarLineasArchivo (String nombre) {
		FileReader entrada;
		entrada = null;
		String[] arreglo;
		arreglo = null;
		int contador;
		contador = 0;
		try{
			String cadena;
			cadena = null;
			entrada = new FileReader(nombre);
			BufferedReader buffer;
			buffer = new BufferedReader(entrada);

			while( (cadena = buffer.readLine()) != null ){
				if( !cadena.isEmpty() ){
					contador++;
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("Error de archivo.");
		}catch(IOException e){
			System.out.println("Error de archivo.");
		}finally{
			try{
				if( entrada != null ){
					entrada.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return contador;
	}

	// Crea un arrreglo con el contenido del archivo a partir de un numero de lineas indicadas.
	public static String[] leerArchivo (String nombre, int numLineas) {
		FileReader entrada;
		entrada = null;
		String[] arreglo;
		arreglo = new String[numLineas];
		try{
			String cadena;
			cadena = null;
			entrada = new FileReader(nombre);
			BufferedReader buffer;
			buffer = new BufferedReader(entrada);
			int cont;
			cont = 0;
			while( (cadena = buffer.readLine()) != null ){
				if( !cadena.isEmpty() ){
					arreglo[cont] = cadena;
					cont++;
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("Error de archivo.");
		}catch(IOException e){
			System.out.println("Error de archivo.");
		}finally{
			try{
				if( entrada != null ){
					entrada.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return arreglo;
	}
}