package controllers.implementacion.catalogos;


import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import controllers.contratos.catalogos.IBusqueda;
import controllers.contratos.catalogos.ICategorias;
import controllers.contratos.catalogos.IProducto;
import models.catalogo.*;
import play.Configuration;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.db.jpa.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Camilo on 5/03/16.
 */
public class BusquedasController extends Controller {

    private static IBusqueda busquedas = new Busquedas();
    private static IProducto productos = new Productos();
    private static ICategorias categorias = new Categorias();
    Configuration conf = Play.application().configuration();
    boolean reporteBusquedas= conf.getBoolean("reporte.busqueda");
    boolean reporteConsultas= conf.getBoolean("reporte.consulta");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat dfBusqueda = new SimpleDateFormat("dd-MM-yyyy");
    ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public Result getBusquedas() {
        return ok(Json.toJson(busquedas.getBusquedas()));
    }

    @Transactional(readOnly = true)
    public Result getBusquedasByProductId(Long productId) {

        return ok(Json.toJson(busquedas.getBusquedasByProductId(productId)));

    }

    @Transactional(readOnly = true)
    public Result getBusquedasByTypeAndDate(String tipo, long providerId, String fechaInicio, String fechaFin, String cuenta) {

        JsonNode respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El servicio no se encuentra activo\"}");

        if(reporteBusquedas || reporteConsultas) {

            if("BUSQUEDA".equals(tipo) && !reporteBusquedas){
                respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El servicio de reportes de Busquedas no se encuentra activo\"}");
            }else if("CONSULTA".equals(tipo) && !reporteConsultas){
                respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El servicio de reportes de consulta no se encuentra activo\"}");
            }else {
                objectMapper.setDateFormat(df);
                Json.setObjectMapper(objectMapper);
                respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la busqueda no existe\"}");
                try {
                    JSONArray jsonArray = new JSONArray();
                    Date fechaIni = null;

                    Date fechaFinal = null;
                    if (!"-1".equals(fechaInicio) && !"-1".equals(fechaFin)) {
                        fechaIni = df.parse(df.format(dfBusqueda.parse(fechaInicio)));
                        fechaFinal = df.parse(df.format(dfBusqueda.parse(fechaFin)));
                    }

                    if ("-1".equals(cuenta)) {
                        List<Busqueda> busquedaList = busquedas.getBusquedasByTypeAndDate(tipo, fechaIni, fechaFinal);
                        for (Busqueda busqueda : busquedaList) {
                            JSONObject jsonObjectTmp = new JSONObject();

                            jsonObjectTmp.put("fechaBusqueda", busqueda.getFechaBusqueda());
                            long productId = busqueda.getIdProducto();

                            jsonArray = obtenerObjetoBusquedas(jsonArray, jsonObjectTmp, providerId, productId);
                        }
                    } else {

                        HashMap<String, String> busquedasList = busquedas.getBusquedasByTypeDateAndCount(tipo, fechaIni, fechaFinal);
                        Set<Map.Entry<String, String>> busquedasProductos = busquedasList.entrySet();
                        for (Map.Entry<String, String> stringEntry : busquedasProductos) {
                            JSONObject jsonObjectTmp = new JSONObject();

                            jsonObjectTmp.put("totalBusquedas", stringEntry.getValue());
                            long productId = Long.parseLong(stringEntry.getKey());

                            jsonArray = obtenerObjetoBusquedas(jsonArray, jsonObjectTmp, providerId, productId);
                        }
                    }
                    respuesta = Json.parse(jsonArray.toString());
                } catch (Exception e) {
                    System.out.println("Se ha presentando un error:  " + e.getMessage());
                }
            }
        }
        return ok(respuesta);

    }

    public JSONArray obtenerObjetoBusquedas(JSONArray jsonArray, JSONObject jsonObjectTmp,long providerId, long productId){

        try {
            Producto producto = productos.getProductById(productId);
            if (providerId == producto.getIdProveedor()) {
                JSONObject jsonProducto = new JSONObject();
                long categoriaId = producto.getIdCategoria();
                jsonProducto.put("descripcion", producto.getDescripcion());
                jsonProducto.put("tipo", producto.getTipo());
                jsonProducto.put("imagen", producto.getImagen());
                jsonProducto.put("nombre", producto.getNombre());
                jsonProducto.put("precioActual", producto.getPrecioActual());
                jsonProducto.put("puntuacion", producto.getPuntuacion());
                jsonProducto.put("id", producto.getId());
                jsonObjectTmp.put("producto", jsonProducto);
                Categoria categoria = categorias.getCategoriaById(categoriaId);
                JsonNode jsonNode = Json.toJson(categoria);
                JSONObject jsonCategoria = new JSONObject(jsonNode.toString());
                jsonObjectTmp.put("nombreCategoria", jsonCategoria.get("nombre"));
                jsonArray.put(jsonObjectTmp);

            }
        }catch (Exception e){
            System.out.println("Se ha presentando un error:  " + e.getMessage());
        }

        return jsonArray;
    }

    @Transactional
    public Result save() {

        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la busqueda no existe\"}");
        Busqueda busquedaProducto = Json.fromJson(json, Busqueda.class);
        if (busquedaProducto != null) {

            busquedaProducto = busquedas.save(busquedaProducto);
            objectMapper.setDateFormat(df);
            Json.setObjectMapper(objectMapper);
            respuesta = Json.toJson(busquedaProducto);
        }
        return ok(Json.toJson(respuesta));
    }


}
