import java.util.Scanner;
import java.io.*;
import java.math.*;

/**
/* Programa: 2, Contador Lineas Codigo.
/* Autor: Gerardo Jiménez Argüelles.
/* Fecha: 5 de Octubre, 2020.
/* Descripcion: El programa se encarga de contar el total de lineas fisicas del codigo, 
/* numero de clases, lineas y métodos por clase, verificar si hay encabezado y 
/* variables inicializadas en la misma linea.
*/

public class EscribirLeerNumeros {

	public static void main (String args[]) {
		int opcion;
		opcion = 1;
		while(opcion != 0){
			opcion = mostrarMenuInicial();
			// Procede a ingresar nombre del archivo
			if(opcion == 1){
				String nombreArchivo;
				nombreArchivo = ingresoNombreArchivo();
				String escrituraLectura;
				escrituraLectura = escrituraLectura();
				if(escrituraLectura.equalsIgnoreCase("l")){
					mostrarContenidoArchivo(nombreArchivo);
				}else{
					guardarNumeros(nombreArchivo);
				}
			}else if(opcion != 0){
				System.out.println("No existe esa opcion.");
			}
		}
	}

	public static Object[] pedirNumerosUsuario (int numeroElementos) {
		boolean correcto;
		correcto = false;
		Object respuesta;
		respuesta = null;
		Object[] arreglo;
		arreglo = new Object[numeroElementos];
		int contador;
		for(contador = 0;contador < numeroElementos;contador++){
			correcto = false;
			while(!correcto){
				respuesta = pedirUsuario();
				if(esNumero(respuesta)){
					correcto = true;
					arreglo[contador] = Double.parseDouble(respuesta.toString());
				}else{
					System.out.println("Dato incorrecto. No es un numero.");
				}
			}
		}
		return arreglo;
	}

	public static void guardarNumeros(String nombreArchivo){
		boolean correcto;
		correcto = false;
		int cantidadNumeros;
		cantidadNumeros = 0;
		while(!correcto){
			System.out.println("***Ingresa cantidad de numeros a ingresar: ");
			Object respuesta;
			respuesta = pedirUsuario();
			if(esNumero(respuesta)){
				correcto = true;
				cantidadNumeros = Integer.parseInt(respuesta.toString());
			}else{
				System.out.println("Dato incorrecto.");
			}
		}
		System.out.println("Ingresa cada numero dando enter: ");
		Object[] arreglo = pedirNumerosUsuario(cantidadNumeros);
		escribirArchivo(nombreArchivo, arreglo);
	}

	public static void mostrarContenidoArchivo (String nombreArchivo) {
		String[] arreglo;
		arreglo = leerArchivo(nombreArchivo);
		if(arreglo != null){
			for(String elemento: arreglo){
				System.out.println(elemento);
			}
		}
	}

	public static String escrituraLectura () {
		boolean correcto;
		correcto = false;
		Object respuesta;
		respuesta = null;
		while(!correcto){
			System.out.println("Lectura o Escritura (l/e): ");
			respuesta = pedirUsuario();
			if(respuesta.toString().equalsIgnoreCase("l") || respuesta.toString().equalsIgnoreCase("e")){
				correcto=true;
			}else{
				System.out.println("Opcion no valida.");
			}
		}
		return respuesta.toString();
	}

	public static String ingresoNombreArchivo () {
		boolean correcto;
		correcto = false;
		System.out.println("Ingresa Nombre del Archivo:");
		Object nombreArchivo;
		nombreArchivo = null;
		while(!correcto){
			nombreArchivo = pedirUsuario();
			if(nombreArchivo.toString().equalsIgnoreCase("")){
				System.out.println("Nombre Incorrecto.");
			}else{
				correcto = true;
			}
		}
		return nombreArchivo.toString();
	}

	public static int mostrarMenuInicial () {
		System.out.println("***Elige Opcion***");
		System.out.println("1 - Ingresar Nombre del Archivo");
		System.out.println("0 - Salir");
		boolean correcto;
		correcto = false;
		Object opcion;
		opcion = null;
		while(!correcto){
			opcion = pedirUsuario();
			if(esNumero(opcion)){
				correcto=true;
			}else{
				System.out.println("Dato incorrecto.");
			}
		}
		return Integer.parseInt(opcion.toString());
	}

	public static Object pedirUsuario () {
		Scanner teclado;
		teclado = new Scanner(System.in);
		Object ingreso;
		ingreso = (Object)teclado.nextLine();
		return ingreso;
	}

	public static boolean esNumero (Object entrada) {
		boolean es;
		es = false;
		try{
			Double.parseDouble(entrada.toString());
			es = true;
		}catch(NumberFormatException e){
			es = false;
		}
		return es;
	}

	public static String[] leerArchivo (String nombre) {
		FileReader entrada;
		entrada = null;
		String[] arreglo;
		arreglo = null;
		try{
			String cadena;
			cadena = null;
			entrada = new FileReader(".\\"+nombre);
			BufferedReader buffer;
			buffer = new BufferedReader(entrada);
			if((cadena = buffer.readLine()) != null){
				arreglo = cadena.split(" ");
			}
		}catch(FileNotFoundException e){
			System.out.println("Error de archivo.");
		}catch(IOException e){
			System.out.println("Error de archivo.");
			// e.printStackTrace();
		}finally{
			try{
				if(entrada != null){
					entrada.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return arreglo;
	}

	public static void escribirArchivo (String nombre, Object[] arreglo) {
		FileWriter fileWriter;
		fileWriter = null;
		BufferedWriter bufferedWriter;
		bufferedWriter = null;
		try{
			File file;
			file = new File(".\\"+nombre);
			if(file.exists()){
				fileWriter= new FileWriter(file.getAbsoluteFile(), true);
			}else{
				fileWriter= new FileWriter(file);
			}
			bufferedWriter = new BufferedWriter(fileWriter);
			String cadena;
			cadena = "";
			for(Object elemento: arreglo){
				cadena = cadena + elemento.toString() + " ";
			}
			bufferedWriter.write(cadena);
			// bufferedWriter.newLine();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				bufferedWriter.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}