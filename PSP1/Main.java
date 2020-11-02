import java.util.Scanner;
import java.io.*;
import java.lang.Math.*;
/**
/* Programa: 3, Correlacion.
/* Autor: Gerardo Jiménez Argüelles.
/* Fecha: 28 de Octubre, 2020
/* Descripcion: El programa calcula la correlacion entre dos conjuntos dados en un archivo.
*/
public class Main {
	public static void main(String[] args) {
		String nombreArchivo;
		System.out.println("Nombre del Archivo");
		nombreArchivo = InteraccionUsuario.pedirUsuario().toString();
		int lineasArchivo;
		lineasArchivo = LeerArchivo.contarLineasArchivo(nombreArchivo);
		String contenidoArchivo[] = LeerArchivo.leerArchivo(nombreArchivo,lineasArchivo);
		Object conjunto1[];
		Object conjunto2[];
		conjunto1 = OperacionesArchivo.extraerArreglo(contenidoArchivo,1);
		conjunto2 = OperacionesArchivo.extraerArreglo(contenidoArchivo,2);
		double correlacion;
		correlacion = Estadistica.correlacion(conjunto1,conjunto2);
		if( correlacion >= -1 && correlacion <= 1) {
			System.out.println("La correlacion es: " + correlacion);
		}else{
			System.out.println("Error en los datos.");
		}
	}	
}

class Estadistica {
	public static double correlacion (Object[] conjunto1, Object[] conjunto2) {
		int n;
		double correlacion;
		correlacion = -2.0;
		if ( conjunto1.length == conjunto2.length) {
			n = conjunto1.length;
		}else{
			n = -1;
		}
		if ( n > 2 ) {
			double arriba;
			arriba = (n*OperacionesConjuntos.sumMultDosConjuntos(conjunto1,conjunto2)) - (OperacionesConjuntos.sumatoriaConjunto(conjunto1) * OperacionesConjuntos.sumatoriaConjunto(conjunto2));
			double izquierda;
			izquierda = ((n * OperacionesConjuntos.sumConjuntoCuadrado(conjunto1))-Math.pow(OperacionesConjuntos.sumatoriaConjunto(conjunto1),2));
			double derecha;
			derecha = ((n * OperacionesConjuntos.sumConjuntoCuadrado(conjunto2))-Math.pow(OperacionesConjuntos.sumatoriaConjunto(conjunto2),2));
			correlacion = arriba / Math.sqrt(izquierda * derecha);
		}
		return correlacion;
	}
}

class OperacionesConjuntos {
	public static double sumMultDosConjuntos (Object[] conj1, Object[] conj2) {
		double suma;
		suma = 0.0;
		int cont;
		// System.out.println(conj1.length);
		for ( cont = 0;cont < conj1.length;cont++ ) {
			// System.out.println(cont);
			suma += Double.parseDouble(conj1[cont].toString()) * Double.parseDouble(conj2[cont].toString());
		}
		return suma;
	}

	public static double sumatoriaConjunto (Object[] conj1) {
		double suma;
		suma = 0.0;
		int cont;
		for ( cont = 0;cont < conj1.length;cont++ ) {
			suma += Double.parseDouble(conj1[cont].toString());
		}
		return suma;
	}

	public static double sumConjuntoCuadrado (Object[] conj1) {
		double suma;
		suma = 0.0;
		int cont;
		for ( cont = 0;cont < conj1.length;cont++ ) {
			suma += Math.pow(Double.parseDouble(conj1[cont].toString()),2);
		}
		return suma;
	}
}

class OperacionesArchivo {
	public static Object[] extraerArreglo (Object[] arreglo, int posicion) {
		Object nuevoArreglo[];
		nuevoArreglo = new Object[arreglo.length];
		Object arregloBase[];
		int cont;
		for( cont = 0;cont < arreglo.length;cont++){
			arregloBase = arreglo[cont].toString().split(" ");
			nuevoArreglo[cont] = arregloBase[posicion-1];
		}
		return nuevoArreglo;
	}
}

class InteraccionUsuario {
	public static Object pedirUsuario () {
		Scanner teclado;
		teclado = new Scanner(System.in);
		Object ingreso;
		ingreso = (Object)teclado.nextLine();
		return ingreso;
	}
}

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