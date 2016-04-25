package Procesador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 25/04/16.
 */
public class FileUtilities {



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

}
