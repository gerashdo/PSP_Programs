


















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
}