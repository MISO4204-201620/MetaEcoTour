package Procesador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 25/04/16.
 */
public class FileUtilities {

    public static void editarArchivo(boolean mensajeria){
        try {
            RandomAccessFile fichero = new RandomAccessFile(ConstantsRutas.FILE_COMENTARIOS_CONTROLER, "rw");
            fichero.seek(151);
            if (mensajeria){
                fichero.writeChars("@Mensajeria(true)");
            } else {
                fichero.writeChars("@Mensajeria(false)");
            }

        }catch (Exception e) {

        }



    }

    public static  List<String> readConfigFile( String file) {
        List<String> config = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader( new FileReader(file));
            String line = null;
            while( ( line = reader.readLine() ) != null ) {
                config.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public static void copyFile (String rutaOrigen, String rutaDestino){

        File origen = new File(rutaOrigen);
        File destino = new File(rutaDestino);
        try {
            InputStream in = new FileInputStream(origen);
            OutputStream out = new FileOutputStream(destino);

            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void escribirLinea(String rutaArchivo, String linea) {
        File fout = new File(rutaArchivo);
        try {
            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(linea);

            bw.close();
        }catch (Exception e){
            System.out.println("Se ha presentado un error en la lectura o escritura del archivo");
        }
    }
}
