package Procesador;

import views.html.provider_details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manuel on 22/04/16.
 */
public class Procesador {




    public Procesador() {
       config = new ArrayList<String>();
    }

    private List<String> config;

    private boolean redesSociales;

    public boolean isRedesSociales() {
        return redesSociales;
    }

    public void setRedesSociales(boolean redesSociales) {
        this.redesSociales = redesSociales;
    }

    public List getConfig() {
        return config;
    }

    public void setConfig(List config) {
        this.config = config;
    }
    
    public void cargarPropiedades(){

        for (String valor: config) {


            if (valor.equals(Constants.FACEBOOK)){
                System.out.println("Facebook");
                redesSociales = true;

            }
            if (valor.equals(Constants.GOOGLE)){
                System.out.println("Google");
                redesSociales = true;
            }
            if (valor.equals(Constants.TWITTER)){
                System.out.println("Twitter");
                redesSociales = true;
            }



        }


    }
        
        


    public static void main (String [ ] args) {

        //Aquí las instrucciones del método
        System.out.println("prueba de ejecucion del procesador");

        Procesador procesador = new Procesador();
        procesador.setConfig( FileUtilities.readConfigFile(ConstantsRutas.DEFAULT_CONFIG) );
        procesador.cargarPropiedades();

        if (procesador.isRedesSociales()){
            FileUtilities.copyFile(ConstantsRutas.ORIGEN_FILE_REDES_SOCIALES , ConstantsRutas.DESTINO_FILE_REDES_SOCIALES);
            //Editar el archivo routes
        }
    }
}
