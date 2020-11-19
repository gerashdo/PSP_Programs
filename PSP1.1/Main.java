import java.util.Scanner;
import java.io.*;
import static java.lang.Math.pow;
/**
/* Programa: 4, Calcula la Prediccion Mejorada.
/* Autor: Gerardo Jiménez Argüelles.
/* Fecha: 13 Noviembre, 2020.
/* Descripcion: Calcula la Prediccion Mejorada entre dos conjuntos de datos y usando el valor de X.
*/
public class Main {
	public static void main (String[] args) {
		ListaLigada contenido;
		String nombreArchivo;
		double prediccionMejorada;
		double x;
		double b0;
		double b1;
		System.out.println("Introduce el Nombre del Archivo:");
		nombreArchivo = InteraccionUsuario.pedirUsuario().toString();
		System.out.println("Dame el Valor de X:");
		x = Double.parseDouble(InteraccionUsuario.pedirUsuario().toString());
		contenido = LeerArchivo.leerArchivo(nombreArchivo);
		b1 = Estadistica.calcularB1(contenido);
		b0 = Estadistica.calcularB0(contenido,b1);
		prediccionMejorada = Estadistica.calcularPrediccionMejorada(b0,b1,x);
		System.out.println("b0 = "+b0);
		System.out.println("b1 = "+b1);
		System.out.println("Yk = "+prediccionMejorada);
	}
}

class Estadistica {
	public static double calcularB1 (ListaLigada lista) {
		double b1;
		int n;
		double arriba;
		double abajo;
		n = lista.getPosicion();
		arriba = OperacionesConjuntos.sumMultDosConjuntos(lista) - (n * OperacionesConjuntos.promedio(lista,1) * OperacionesConjuntos.promedio(lista,2));
		abajo = OperacionesConjuntos.sumConjuntoCuadrado(lista,1) - (n * Math.pow(OperacionesConjuntos.promedio(lista,1),2));
		b1 = arriba/abajo;
		return b1;
	}

	public static double calcularB0 (ListaLigada lista, double b1) {
		return OperacionesConjuntos.promedio(lista,2) - (b1 * OperacionesConjuntos.promedio(lista,1));
	}

	public static double calcularPrediccionMejorada (double b0, double b1, double x) {
		return b0+(b1 * x);
	}
}

class OperacionesConjuntos {
	public static double sumMultDosConjuntos (ListaLigada lista) {
		Nodo nodo;
		double val1;
		double val2;
		double suma;
		suma = 0;

		lista.inicializaIterador();

		while(lista.existeNodo()){
			nodo = (Nodo)lista.obtenerSiguiente();
			val1 = Double.parseDouble(nodo.getInfo1().toString());
			val2 = Double.parseDouble(nodo.getInfo2().toString());
			suma = suma + (val1*val2);
		}
		return suma;
	}

	public static double promedio (ListaLigada lista, int posicion) {
		double suma;
		suma = sumatoriaConjunto(lista,posicion);
		return suma/Double.parseDouble(((Integer)lista.getPosicion()).toString());
	}

	public static double sumatoriaConjunto (ListaLigada lista, int posicion) {
		double val;
		Nodo nodo;
		double suma;
		suma = 0;
		lista.inicializaIterador();
		while(lista.existeNodo()){
			nodo = (Nodo)lista.obtenerSiguiente();
			if( posicion == 1 ){
				val = Double.parseDouble(nodo.getInfo1().toString());
			}else{
				val = Double.parseDouble(nodo.getInfo2().toString());
			}
			suma = suma + val;
		}
		return suma;
	}

	public static double sumConjuntoCuadrado (ListaLigada lista, int posicion) {
		double val;
		Nodo nodo;
		double suma;
		suma = 0;
		lista.inicializaIterador();
		while(lista.existeNodo()){
			nodo = (Nodo)lista.obtenerSiguiente();
			if( posicion == 1 ){
				val = Double.parseDouble(nodo.getInfo1().toString());
			}else{
				val = Double.parseDouble(nodo.getInfo2().toString());
			}
			val = Math.pow(val,2);
			suma = suma + val;
		}
		return suma;
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

class Nodo {
	protected Object info1;
	protected Object info2;
	protected Nodo ligaDer;
	public Nodo () {
		ligaDer = null;
	}
	public Object getInfo1 () {
		return info1;
	}
	public void setInfo1 (Object info1) {
		this.info1 = info1;
	}
	public Object getInfo2 () {
		return info2;
	}
	public void setInfo2 (Object info2) {
		this.info2 = info2;
	}
	public Nodo getLigaDer () {
		return ligaDer;
	}
	public void setLigaDer (Nodo ligaDer) {
		this.ligaDer = ligaDer;
	}
	@Override
	public String toString () {
		return info1.toString()+' '+info2.toString();
	}
}

class ListaLigada {
	protected Nodo primero;
	protected Nodo ultimo;
	protected Nodo nodoActualIterador;
	protected int posicion;
	public ListaLigada () {
		primero = null;
		ultimo = null;
		nodoActualIterador = null;
		posicion = 0;
	}
	public boolean insertar (Nodo nodo) {
		// Insertar al final
		if( nodo != null ){
			if( ultimo == null ){
				// vacia
				primero=nodo;
				ultimo=nodo;
				posicion ++;
			}else{
				ultimo.setLigaDer(nodo);
				ultimo=nodo;
				posicion++;
			}
				return true;
		}else{
			return false;
		}
	}

	public void inicializaIterador () {
		nodoActualIterador = primero;
	}

	public boolean existeNodo () {
		if( nodoActualIterador!=null ){
			return true;
		}else{
			return false;
		}
	}

	public Object obtenerSiguiente () {
		if( existeNodo() ){
			Object contenido;
			contenido=nodoActualIterador;
			nodoActualIterador = nodoActualIterador.getLigaDer();
			return contenido;
		}else{
			return null;
		}
	}

	public int getPosicion () {
		return posicion;
	}
}

class LeerArchivo {
	public static ListaLigada leerArchivo (String nombre) {
		FileReader entrada;
		entrada = null;
		ListaLigada arreglo;
		arreglo = new ListaLigada();
		String cadenaDivid[];
		cadenaDivid=null;
		try{
			String cadena;
			cadena = null;
			entrada = new FileReader(nombre);
			BufferedReader buffer;
			buffer = new BufferedReader(entrada);
			while( (cadena = buffer.readLine()) != null ){
				if( !cadena.isEmpty() ){
					cadenaDivid = cadena.split(" ");
					Nodo nodo;
					nodo = new Nodo();
					nodo.setInfo1(cadenaDivid[0]);
					nodo.setInfo2(cadenaDivid[1]);
					arreglo.insertar(nodo);
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