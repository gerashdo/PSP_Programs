import java.util.Scanner;
import java.io.*;
import static java.lang.Math.abs;
/**
/* Programa: 5, Calcula la Prediccion Mejorada con 4 Variables.
/* Autor: Gerardo Jiménez Argüelles.
/* Fecha: 24 Noviembre, 2020.
/* Descripcion: Calcula la Prediccion Mejorada entre 4 conjuntos.
*/
public class Main {
	public static void main (String[] args) {
		ListaLigada contenido;
		String nombreArchivo;
		double locAnadida;
		double locReutilizada;
		double locModificado;
		double tiempo;
		System.out.println("Introduce el Nombre del Archivo:");
		nombreArchivo = InteraccionUsuario.pedirUsuario().toString();
		System.out.println("Lineas nuevas:");
		locAnadida = Double.parseDouble(InteraccionUsuario.pedirUsuario().toString());
		System.out.println("Lineas a Reutilizar:");
		locReutilizada = Double.parseDouble(InteraccionUsuario.pedirUsuario().toString());
		System.out.println("Lineas a Modificar:");
		locModificado = Double.parseDouble(InteraccionUsuario.pedirUsuario().toString());
		contenido = LeerArchivo.leerArchivo(nombreArchivo);
		Ecuacion paraEcuaciones;
		paraEcuaciones = new Ecuacion(contenido,contenido.getPosicion());
		paraEcuaciones.crearMatriz();
		paraEcuaciones.gauss();
		paraEcuaciones.sustitucionHaciaAtras();
		tiempo = paraEcuaciones.obtenerNuevaVariable(locAnadida,locReutilizada,locModificado);
		double betas[];
		betas = paraEcuaciones.getBetas();
		System.out.println("B0 = "+betas[0]);
		System.out.println("B1 = "+betas[1]);
		System.out.println("B2 = "+betas[2]);
		System.out.println("B3 = "+betas[3]);
		System.out.println("Tiempo Estimado = "+tiempo);
	}
}

class Ecuacion {
	private Matriz matriz;
	private double betas[];
	private ListaLigada contenido;
	private int n;
	public Ecuacion (ListaLigada contenido, int n) {
		matriz = new Matriz(4,5);
		betas = new double[4];
		this.contenido = contenido;
		this.n = n;
	}
	public double[] getBetas () {
		return betas;
	}
	public double obtenerNuevaVariable (double val1, double val2, double val3) {
		// Se puede hacer más generalizable
		double suma;
		suma = betas[0];
		suma = suma + (betas[1] * val1) + (betas[2] * val2) + (betas[3] * val3);
		return suma;
	}
	public void sustitucionHaciaAtras () {
		int limite;
		int col;
		double valorResultado;
		// iteraciones
		for( limite = matriz.columnas()-2;limite >= 0;limite--) {
			valorResultado = Double.parseDouble(matriz.getElemento(limite,matriz.columnas()-1).toString());
			// Pasando los valores al lado contrario
			for( col = matriz.columnas()-2; col > limite;col--){
				 valorResultado = valorResultado + (Double.parseDouble(matriz.getElemento(limite,col).toString()) * betas[col] * -1);
			}
			betas[limite] = valorResultado / Double.parseDouble(matriz.getElemento(limite,limite).toString());
		}
	}
	public void gauss () {
		int columna;
		columna = 0;
		int iteracion;
		// Comienzan iteraciones
		for( iteracion = 0;iteracion < matriz.renglones();iteracion++ ){
			// Si hay una posicion más en los renglones
				int posPivote;
				posPivote = encontrarPivote(columna);
				// Si la columna no esta ya en 0
				if( Double.parseDouble(matriz.getElemento(posPivote,columna).toString()) != 0.0){
					intercambiarPosiciones(columna,posPivote,columna);
					// System.out.println("Privote = "+matriz.getElemento(columna,columna));
					reducirCoeficiente(columna);
					double vectorActual[];
					vectorActual = new double[matriz.columnas()-columna];
					int col;
					int vecPos;
					vecPos = 0;
					// crear vector con valres de la columna actual
					for( col = columna;col < matriz.columnas();col++ ){
						// Valor del vector ya es double
						vectorActual[vecPos] = Double.parseDouble(matriz.getElemento(columna,col).toString());
						vecPos++;
					}
					// Eliminar a cada renglon el vector
					int reng;
					for(reng = columna + 1;reng < matriz.renglones();reng++){
						eliminarEnRenglones(vectorActual,reng,columna);
					}
				}
				columna++;
		}
	}

	public void eliminarEnRenglones (double[] vectorInicial,int renglon, int columna) {
		double vectorEliminar[];
		vectorEliminar = new double[vectorInicial.length];
		int pos2;
		for( pos2 = 0;pos2 < vectorInicial.length;pos2++){
			vectorEliminar[pos2] = vectorInicial[pos2];
		}
		int col;
		int pos;
		double valorEliminar;
		valorEliminar = Double.parseDouble(matriz.getElemento(renglon,columna).toString()) * -1.0;
		// Multiplicar el valor al contrario con el vector original
		for( pos = 0;pos < vectorEliminar.length;pos++ ){
			vectorEliminar[pos] = vectorEliminar[pos] * valorEliminar;
			
		}
		// Sumar valores del vector al renglón actual
		pos = 0;
		for( col = columna;col < matriz.columnas();col++){
			matriz.setElemento(renglon,col,Double.parseDouble(matriz.getElemento(renglon,col).toString())+vectorEliminar[pos]);
			pos++;
		}
	}

	public void reducirCoeficiente (int reng) {
		int col;
		double valorAnterior;
		double valorActual;
		valorAnterior =  Double.parseDouble(matriz.getElemento(reng,reng).toString());
		for( col = reng;col < matriz.columnas();col++){
			valorActual =  Double.parseDouble(matriz.getElemento(reng,col).toString());
			matriz.setElemento(reng,col,(1/valorAnterior)*valorActual);
		}
	}

	public void intercambiarPosiciones (int reng1, int reng2, int col) {
		int pos;
		// si el pivote es el mismo que el primero no se intercambian
		if( reng2 != reng1){
			for( pos = col;pos < matriz.columnas();pos++){
				Object aux;
				aux = matriz.getElemento(reng1,pos);
				matriz.setElemento(reng1,pos,matriz.getElemento(reng2,pos));
				matriz.setElemento(reng2,pos,aux);
			}
		}
		
	}

	public int encontrarPivote (int col) {
		double valorMax;
		valorMax = 0.0;
		int pos;
		pos = col;
		int reng; 
		for( reng = col;reng < matriz.renglones();reng++ ){
			if( Math.abs(Double.parseDouble(matriz.getElemento(reng,col).toString())) > Math.abs(valorMax)){
				valorMax = Double.parseDouble(matriz.getElemento(reng,col).toString());
				pos = reng;
			}
		}
		return pos;
	}

	public void matrizLinea1 () {
		matriz.setElemento(0,0,n);
		int col;
		for( col = 1;col <= 4;col++ ){
			matriz.setElemento(0,col,OperacionesConjuntos.sumatoriaConjunto(contenido,col-1));
		}
		
	}

	public void crearMatriz () {
		matriz.inicializar(0.0);
		matrizLinea1();
		int letra;
		letra = 0;
		int reng;
		int col;
		for( reng = 1;reng < 4;reng++ ){
			matriz.setElemento(reng,0,OperacionesConjuntos.sumatoriaConjunto(contenido,letra));
			for( col = 1;col <= 4;col++ ){
				matriz.setElemento(reng,col,OperacionesConjuntos.sumMultDosConjuntos(contenido,letra,col-1));
			}
			letra++;
		}
	}
}

class OperacionesConjuntos {
	public static double sumMultDosConjuntos (ListaLigada lista, int posicion1, int posicion2) {
		Nodo nodo;
		double suma;
		suma = 0;
		String contenido;
		String[] arrCont;
		lista.inicializaIterador();
		while(lista.existeNodo()){
			nodo = (Nodo)lista.obtenerSiguiente();
			contenido =  nodo.getInfo1().toString();
			arrCont = contenido.split(" ");
			suma = suma + (Double.parseDouble(arrCont[posicion1]) * Double.parseDouble(arrCont[posicion2]));
		}
		return suma;
	}

	public static double sumatoriaConjunto (ListaLigada lista, int posicion) {
		Nodo nodo;
		double suma;
		suma = 0;
		String contenido;
		String[] arrCont;
		lista.inicializaIterador();
		while(lista.existeNodo()){
			nodo = (Nodo)lista.obtenerSiguiente();
			contenido =  nodo.getInfo1().toString();
			arrCont = contenido.split(" ");
			suma = suma + Double.parseDouble(arrCont[posicion]);
		}
		return suma;
	}
}

class Matriz {
	protected Object datos[][];
	protected int RENG;
	protected int COL;

	public Matriz (int reng, int col) {
		RENG = reng;
		COL = col;
		datos = new Object[reng][col];
	}

	public void inicializar (Object valor) {
		int reng;
		int col;
		for(reng = 0;reng<RENG;reng++){
			for(col = 0;col<COL;col++){
				datos[reng][col]=valor;   
			}
		}
	}

	private boolean validarDim (int valor, int tam) {
		if(valor >= 0 && valor < tam){
			return true;
		}else{
			return false;
		}
	}

	public boolean setElemento (int reng, int col, Object valor) {
		if(validarDim(reng,RENG) && validarDim(col, COL)){
			datos[reng][col] = valor;
			 return true;
		}else{
			return false;
		}
	}

	public Object getElemento (int reng, int col) {
		if(validarDim(reng,RENG) && validarDim(col, COL)){
			return datos[reng][col];
		}else{
			return null;
		}
	}

	public int renglones () {
		return RENG;
	}

	public int columnas () {
		return COL;
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
	public Nodo getLigaDer () {
		return ligaDer;
	}
	public void setLigaDer (Nodo ligaDer) {
		this.ligaDer = ligaDer;
	}
	@Override
	public String toString () {
		return info1.toString();
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
					Nodo nodo;
					nodo = new Nodo();
					nodo.setInfo1(cadena);
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