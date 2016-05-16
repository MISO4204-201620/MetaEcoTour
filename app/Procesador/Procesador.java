package Procesador;

import play.libs.Json;
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

    private boolean mensajeria;

    private boolean reporteBusqueda;

    private boolean reporteConsulta;

    private boolean reporteVentas;

    private boolean consultarCalificacion;

    private boolean comentarCalificacion;

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

    public boolean isComentarCalificacion() {
        return comentarCalificacion;
    }

    public void setComentarCalificacion(boolean comentarCalificacion) {
        this.comentarCalificacion = comentarCalificacion;
    }

    public boolean isConsultarCalificacion() {
        return consultarCalificacion;
    }

    public void setConsultarCalificacion(boolean consultarCalificacion) {
        this.consultarCalificacion = consultarCalificacion;
    }

    public boolean isMensajeria() {
        return mensajeria;
    }

    public void setMensajeria(boolean mensajeria) {
        this.mensajeria = mensajeria;
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

            if (valor.equals(Constants.MENSAJERIA)){
                System.out.println("mensajes");
                mensajeria = true;
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

            if (valor.equals(Constants.CALIFICACIONES_COMENTAR)){
                comentarCalificacion = true;
            }

            if (valor.equals(Constants.CALIFICACIONES_CONSULTAR)){
                consultarCalificacion = true;
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
        FileUtilities.copyFile(ConstantsRutas.CORE_ROUTES , ConstantsRutas.DESTINO_CORE_ROUTES);

        FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, "\n");
        FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");

        //Generar archivo plano con json para el front
        Features ftr= new Features();
        FileUtilities.sobreescribirLinea(ConstantsRutas.FILE_SERVICIOS_FRONT_CONTROLER, Json.toJson(ftr.getVariableFeatures()).toString());


        if (procesador.isRedesSociales()){
            //FileUtilities.copyFile(ConstantsRutas.ORIGEN_FILE_REDES_SOCIALES , ConstantsRutas.DESTINO_FILE_REDES_SOCIALES);
            //Editar el archivo routes
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.SOCIAL_LOGIN_ACTIVE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.SOCIAL_LOGIN_ROUTE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");

        }else {
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.SOCIAL_LOGIN_INACTIVE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, "\n");
        }

        if(procesador.isReporteBusqueda() || procesador.isReporteConsulta()){
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.SEARCH_REPORT_ROUTE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            if(procesador.isReporteBusqueda()){
                FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.REPORTE_BUSQUEDA_ACTIVE);
            }else{
                FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.REPORTE_BUSQUEDA_INACTIVE);
            }

            if(procesador.isReporteConsulta()){
                FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.REPORTE_CONSULTA_ACTIVE);
            }else{
                FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.REPORTE_CONSULTA_INACTIVE);
            }
        }else{
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.REPORTE_BUSQUEDA_INACTIVE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.REPORTE_CONSULTA_INACTIVE);
        }

        if(procesador.isReporteVentas()){
            FileUtilities.copyFile(ConstantsRutas.ORIGEN_FILE_REPORTE_COMPRAS , ConstantsRutas.DESTINO_FILE_REPORTE_COMPRAS);
            FileUtilities.copyFile(ConstantsRutas.ORIGEN_FILE_REPORTE_COMPRAS_CONTROLLER, ConstantsRutas.DESTINO_FILE_REPORTE_COMPRAS_CONTROLLER);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.SEARCH_REPORT_SOLD_ROUTE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.SEARCH_REPORT_SOLD_ROUTE_DETAIL);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
        }else{
            //eliminar clases
            FileUtilities.borrarArchivo(ConstantsRutas.DESTINO_FILE_REPORTE_COMPRAS);
            FileUtilities.borrarArchivo(ConstantsRutas.DESTINO_FILE_REPORTE_COMPRAS_CONTROLLER);
        }

        //crear las anotaciones para la mensajeria
        FileUtilities.editarArchivo(procesador.isMensajeria(), ConstantsRutas.FILE_COMENTARIOS_CONTROLER, 153);
        FileUtilities.editarArchivo(procesador.isMensajeria(), ConstantsRutas.FILE_USUARIOS_CONTROLER, 119);

        if (procesador.isMensajeria()){
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.MENSAJERIA_MENSAJES_ROUTE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.MENSAJERIA_USUARIOS_ROUTE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
        }



        //Crear las variables para las calificaciones
        FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, "\n");
        if (procesador.isComentarCalificacion()){
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.COMENTAR_CALIFICACION_ACTIVE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.CREACION_CALIFICACION_ROUTE);

        } else {
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.COMENTAR_CALIFICACION_INACTIVE);
        }
        FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, "\n");
        if (procesador.isConsultarCalificacion()){
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.CONSULTAR_CALIFICACION_ACTIVE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.CALIFICACION_USUARIOS_ROUTE);
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.CALIFICACION_SERVICIO_ROUTE);
        } else {
            FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_CONFIG, ConstantesPropiedadesArchivoConf.CONSULTAR_CALIFICACION_INACTIVE);
        }

        //Se debe siempre colocar el final de archivo de rutas
        FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
        FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, "\n");
        FileUtilities.escribirLinea(ConstantsRutas.DESTINO_CORE_ROUTES, ConstantesRoutes.FILE_ROUTE);
    }
}
