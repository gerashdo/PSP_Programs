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
public class ContarLineasCodigo {
	public static int numeroLineas;
	public static String contenidoArchivo[];
	public static String nombresClases[];
	public static int lineasClases[];
	public static int metodosClases[];
	public static String variablesIniciadas[];
	public static int elementosHeader[];
	public static String eleHeaderString[];
	public static int contadorClases;
	public static int contadorMetodos;
	public static int contadorLineasClases;
	public static int contadorLineasGeneral;
	public static int contadorVarInicializadas;
	public static int contadorCorchete;
	public static boolean inicioHeader;
	public static boolean finHeader;
	public static boolean formatoHeader;
	public static int leerHeader;

	public static void main (String[] args) {
		String nameArchivo;
		System.out.println("Ingresa el nombre del Archivo");
		nameArchivo = pedirUsuario().toString();
		contarLOC(nameArchivo);
		imprimirResultados();
	}

	public static Object pedirUsuario () {
		Scanner teclado = new Scanner(System.in);
		Object ingreso = (Object)teclado.nextLine();
		return ingreso;
	}

	// Imprime los resultados del conteo y revisiond el archivo.
	public static void imprimirResultados () {
		System.out.println("RESULTADOS");
		int contador;
		contador=0;
		while( nombresClases[contador] != null ){
			System.out.println("Clase: " + nombresClases[contador]);
			System.out.println("Numero de Metodos: " + metodosClases[contador]);
			System.out.println("Lineas Clase: " + lineasClases[contador]);
			contador++;
		}
		System.out.println("");
		System.out.println("TOTAL LINEAS LOC: " + contadorLineasGeneral);
		System.out.println("");
		System.out.println("Variables Declaradas e Inicializadas en la Misma Linea: ");
		String nomVariable;
		int contVariable;
		contVariable = 0;
		while( variablesIniciadas[contVariable] != null ){
			System.out.println("*" + variablesIniciadas[contVariable]);
			contVariable++;
		}
		if( contVariable == 0 ){
			System.out.println(0);
		}
		System.out.println("");
		boolean todosElementosHeader;
		todosElementosHeader = true;
		for( contVariable=0;contVariable<elementosHeader.length;contVariable++ ){
			if(elementosHeader[contVariable] != 1){
				todosElementosHeader = false;
				break;
			}
		}
		if( todosElementosHeader && formatoHeader ) {
			System.out.println("El formato del header es correcto.");
		}else if( leerHeader == -1 ){
			System.out.println("El archivo no tiene header.");
		}else{
			System.out.println("El formato del header es incorrecto.");
			if( !todosElementosHeader ){
				System.out.println("Faltan los siguientes elementos:");
				for( contVariable = 0;contVariable < elementosHeader.length;contVariable++ ){
					if( elementosHeader[contVariable] != 1 ){
						System.out.println(eleHeaderString[contVariable]);
					}
				}
			}
		}
	}

	// Cuenta las lineas LOC del archivo del nombre que se le pasa.
	public static void contarLOC (String nombreArchivo) {
		numeroLineas = LeerArchivo.contarLineasArchivo(nombreArchivo);
		nombresClases = new String[numeroLineas];
		lineasClases = new int[numeroLineas];
		metodosClases = new int[numeroLineas];
		variablesIniciadas = new String[numeroLineas];
		elementosHeader = new int[4];
		eleHeaderString =  new String[4];
		eleHeaderString[0] = "Programa";
		eleHeaderString[1] = "Autor";
		eleHeaderString[2] = "Fecha";
		eleHeaderString[3] = "Descripcion";
		contenidoArchivo = LeerArchivo.leerArchivo(nombreArchivo, numeroLineas);
		int line;
		String linea;
		linea = "";
		String[] arregloLinea;
		arregloLinea = null;
		contadorClases = 0;
		contadorMetodos = 0;
		contadorLineasClases = 0;
		contadorLineasGeneral = 0;
		contadorVarInicializadas = 0;
		contadorCorchete = 0;
		leerHeader = -1;
		for( line = 0;line < numeroLineas;line++ ){
			linea = contenidoArchivo[line];
			linea = eliminarTabuladores(linea);
			arregloLinea = linea.split(" ");
			if( !esComentario(arregloLinea) ){
				if( esClase(arregloLinea) ){
					nombresClases[contadorClases] = recuperarNombreClase(arregloLinea);
					contadorClases++;
				}else{
 					if( esMetodo(arregloLinea, linea) ){
 						contadorMetodos++;
					}else{
 						if( esVariableIniciada(arregloLinea,linea) ){
 							String nombreVariable;
 							nombreVariable = recuperarNombreVariable(arregloLinea);
 							variablesIniciadas[contadorVarInicializadas] = nombreVariable;
 							contadorVarInicializadas++;
						}
					}
				}
				if(contadorClases>0){
					contadorLineasClases++;
				}
				contarCorchetes(linea);
				contadorLineasGeneral++;
			}else{
				if( leerHeader == -1 && contadorClases == 0 ){
					verificarHeader(arregloLinea,line);
				}
			}
		}	
	}

	public static void verificarHeader (String[] linea, int numLinea) {
		if(linea[0].equalsIgnoreCase("/**") && linea.length == 1){
			inicioHeader = true;
			String strLinea;
			numLinea++;
			strLinea = contenidoArchivo[numLinea];
			linea = strLinea.split(" ");
			finHeader = false;
			formatoHeader = true;
			while( !strLinea.isEmpty() && !(strLinea.indexOf("class") != -1) && !tieneModificadorAcceso(linea) && !finHeader ){
				if(formatoHeader && (!linea[0].equalsIgnoreCase("/*") && !linea[0].equalsIgnoreCase("*/"))){
					formatoHeader = false;
				}
				if( linea[0].equalsIgnoreCase("*/") ){
					finHeader = true;
				}
				if( !finHeader ){
					if( linea[1].equalsIgnoreCase("Programa:") && (linea.length > 2) ){
						elementosHeader[0] = 1;
					}else if( linea[1].equalsIgnoreCase("Autor:") && (linea.length > 2) ){
						elementosHeader[1] = 1;
					}else if( linea[1].equalsIgnoreCase("Fecha:") && (linea.length > 2) ){
						elementosHeader[2] = 1;
					}else if( linea[1].equalsIgnoreCase("Descripcion:") && (linea.length > 2) ){
						elementosHeader[3] = 1;
					}
				}
				numLinea++;
				strLinea = contenidoArchivo[numLinea];
				linea = strLinea.split(" ");
			}
			leerHeader = 0;
		}
	}

	// Verficar corchetes para saber si ya terminó la clase.
	public static void contarCorchetes (String strLinea) {
		if( contadorClases > 0 ){
			if( strLinea.charAt(strLinea.length()-1) == '{' ){
				contadorCorchete++;
			}
			if( strLinea.charAt(strLinea.length()-1) == '}' || strLinea.charAt(0) == '}' ){
				contadorCorchete--;
			}
			if( contadorCorchete == 0 ){
				lineasClases[contadorClases-1] = contadorLineasClases;
				contadorLineasClases = 0;
				metodosClases[contadorClases-1] = contadorMetodos;
				contadorMetodos = 0;
			}
		}
	}

	// Recupera el nombre de una variable.
	public static String recuperarNombreVariable (String[] linea) {
		String nombreVariable;
		nombreVariable = "";
		int cont;
		for( cont = 0;cont < linea.length;cont++ ){
			if( linea[cont].equalsIgnoreCase("=") ){
				nombreVariable = linea[cont-1];
				break;
			}
		}
		return nombreVariable;
	}

	// Verifica si en un arreglo existe algun elemento que sea un tipo primitivo.
	public static boolean contienePrimitivo (String[] linea) {
		boolean contiene;
		contiene = false;
		int cont;
		for( cont = 0;cont < linea.length;cont++ ){
			if(linea[cont].equals("int") || linea[cont].equals("char") || linea[cont].equals("double") || linea[cont].equals("boolean") || linea[cont].equals("byte") || linea[cont].equals("short") || linea[cont].equals("long")){
				contiene = true;
				break;
			}
		}
		return contiene;
	}

	// Verifica si en una string existe un caracter como el ingresado.
	public static boolean contieneCaracter (String strLinea, char caracter) {
		boolean contiene;
		contiene = false;
		int cont;
		for( cont = 0;cont < strLinea.length();cont++ ){
			if(strLinea.charAt(cont) == caracter){
				contiene = true;
				break;
			}
		}
		return contiene;
	}

	// Verifica si en la linea hay una variable inicializada y declarada.
	public static boolean esVariableIniciada (String[] linea, String strLinea) {
		boolean igual;
		igual = contieneCaracter(strLinea,'=');
		boolean hayPrimitivo;
		hayPrimitivo = contienePrimitivo(linea);
		if( igual == true && hayPrimitivo == true ){
			return true;
		}else{
			return false;
		}
	}

	// Verifica si un arreglo contiene el indiador de un modificador de acceso entre sus elementos.
	public static boolean tieneModificadorAcceso (String[] linea) {
		boolean tiene;
		tiene = false;
		if( linea[0].equalsIgnoreCase("public") || linea[0].equalsIgnoreCase("default") || linea[0].equalsIgnoreCase("private") || linea[0].equalsIgnoreCase("protected") ){
			tiene = true;
		}
		return tiene;
	}

	// Verifica si una linea es una declaracion para un metodo.
	public static boolean esMetodo (String[] linea, String strLinea) {
		boolean hayModiAcceso;
		hayModiAcceso = tieneModificadorAcceso(linea);
		boolean hayParentesis;
		hayParentesis = false;
		boolean corcheteFinal;
		corcheteFinal = false;
		boolean esMetodo;
		esMetodo = false;
		if( hayModiAcceso ){
			if( strLinea.charAt(strLinea.length()-1 ) == '{'){
				corcheteFinal = true;
				int caracter;
				for( caracter = 0;caracter < strLinea.length();caracter++ ){
					if( strLinea.charAt(caracter) == '(' ){
						hayParentesis = true;
					}
				}
			}
		}
		if( hayModiAcceso == true && hayParentesis == true && corcheteFinal == true ){
			esMetodo = true;
		}
		return esMetodo;
	}

	// Recupera el nombre de una clase.
	public static String recuperarNombreClase (String[] linea){
		String nombre;
		nombre = "";
		int cont;
		for( cont = 0;cont < linea.length;cont++ ){
			if( linea[cont].equalsIgnoreCase("class") ){
				nombre = linea[cont+1];
				break;
			}
		}
		return nombre;
	}

	// Verifica que una linea sea declaracion de una clase o no.
	public static boolean esClase (String[] linea) {
		boolean loEs;
		loEs = false;
		int pos;
		for( pos = 0;pos < linea.length;pos++ ){
			if( linea[pos].equalsIgnoreCase("class") ){
				loEs = true;
				break;
			}
		}
		return loEs;
	}

	// Elimina los caracteres de tabuladores o saltos de linea de una string.
	public static String eliminarTabuladores (String linea) {
		String nuevaLinea;
		nuevaLinea = "";
		int cont;
		for( cont = 0;cont < linea.length();cont++ ){
			if( linea.charAt(cont) != '\t' && linea.charAt(cont) != '\n' ){
				nuevaLinea = nuevaLinea+linea.charAt(cont);
			}
		}
		return nuevaLinea;
	}

	// Verifica si una linea de codigo cueenta o no.
	public static boolean esComentario (String[] linea) {
		boolean cuenta;
		cuenta = true;
		if( !linea[0].equalsIgnoreCase("/**") && !linea[0].equalsIgnoreCase("/*") && !linea[0].equalsIgnoreCase("*/") && !linea[0].equalsIgnoreCase("//") && !(linea[0].equalsIgnoreCase("") && linea.length == 1) ){
			cuenta = false;
		}
		return cuenta;
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