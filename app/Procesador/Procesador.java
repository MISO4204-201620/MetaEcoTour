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

    private boolean reporteBusqueda;

    private boolean reporteConsulta;

    private boolean reporteVentas;

    public boolean isRedesSociales() {
        return redesSociales;
    }

    public boolean isReporteBusqueda() {
        return reporteBusqueda;
    }

    public void setReporteBusqueda(boolean reporteBusqueda) {
        this.reporteBusqueda = reporteBusqueda;
    }

    public boolean isReporteConsulta() {
        return reporteConsulta;
    }

    public void setReporteConsulta(boolean reporteConsulta) {
        this.reporteConsulta = reporteConsulta;
    }

    public boolean isReporteVentas() {
        return reporteVentas;
    }

    public void setReporteVentas(boolean reporteVentas) {
        this.reporteVentas = reporteVentas;
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

            if (valor.equals(Constants.REPORTE_BUSQUEDAS)){
                System.out.println("reporte busquedas");
                reporteBusqueda = true;

            }
            if (valor.equals(Constants.REPORTE_CONSULTAS)){
                System.out.println("reporte consultas");
                reporteConsulta = true;
            }
            if (valor.equals(Constants.REPORTE_VENTAS)){
                System.out.println("Twitter");
                reporteVentas = true;
            }



        }


    }
        
        


    public static void main (String [ ] args) {

        //Aquí las instrucciones del método
        System.out.println("prueba de ejecucion del procesador");

        Procesador procesador = new Procesador();
        procesador.setConfig( FileUtilities.readConfigFile(ConstantsRutas.DEFAULT_CONFIG) );
        procesador.cargarPropiedades();
        FileUtilities.copyFile(ConstantsRutas.CORE_CONFIG , ConstantsRutas.DESTINO_CORE_CONFIG);

        if (procesador.isRedesSociales()){
            //se comenta la siguiente linea debido a que ya no se hará con binary replacement
            //FileUtilities.copyFile(ConstantsRutas.ORIGEN_FILE_REDES_SOCIALES , ConstantsRutas.DESTINO_FILE_REDES_SOCIALES);
            //Editar el archivo routes
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.SOCIAL_LOGIN_ACTIVE);

        }else {
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.SOCIAL_LOGIN_INACTIVE);
        }

        if(procesador.isReporteBusqueda()){

        }
    }
}
