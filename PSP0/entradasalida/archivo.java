package PROGRAMA1.entradasalida;

import java.io.*;

public class archivo{
	

	public static arreglo leerArchivo(String nombre){
		FileReader input=null;
		String[] arreglo=null;
		
		try{
			String cadena=null;
			entrada = new FileReader("C:\\Users\\Gerashdo\\Documents\\Software\\Semestre V\\PSP\\PROGRAMA1\\"+nombre);
			BufferedReader buffer = new BufferedReader(entrada);


			if((cadena=buffer.readLine()) !=null){
				arreglo = cadena.split(" ");
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				entrada.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		return arreglo;
	}

	public static void escribirArchivo(String nombre, Object[] arreglo){
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		try{
			File file = new File("C:\\Users\\Gerashdo\\Documents\\Software\\Semestre V\\PSP\\PROGRAMA1\\"+nombre);
			fileWriter= new FileWriter(file.getAbsoluteFile(), true);
			bufferedWriter = new BufferedWriter(fileWriter);
			String cadena = "";

			for(Object elemento: arreglo){
				cadena = cadena + elemento.toString() + " ";
			}

			bufferedWriter.writeLine(cadena);
			bufferedWriter.newLine();

		}catch(IOException e){
			e.printStackTrace();
		}finally{

			try{
				fileWriter.close();
				bufferedWriter.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

}