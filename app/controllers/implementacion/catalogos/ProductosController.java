package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.catalogos.IProducto;
import models.catalogo.*;
import models.usuario.Proveedor;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo on 27/02/16.
 */
public class ProductosController extends Controller {
    private static IProducto productos = new Productos();


    @Transactional
    public Result save() {
        JsonNode json = request().body().asJson();

        String tipoProducto=json.get("tipoProducto").textValue();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto no existe, para crear uno envíe el id vacío\"}");
        JsonNode productoJson =json.get("producto");
        JsonNode recursos =json.get("producto").get("recursos");
        JsonNode itemServicios =json.get("producto").get("itemServicios");

        Producto producto = null;
        Producto productoGuardado = null;
        if("PAQ".equals(tipoProducto)){
            producto= Json.fromJson(productoJson, Paquete.class);

        }else if("SER".equals(tipoProducto)){
            producto = Json.fromJson(productoJson, Servicio.class);
        }

        if(producto != null){
           productoGuardado=productos.save(producto);

            if(productoGuardado !=null) {

                List<Recurso> recursosUpdate = producto.getRecursos();

                for (int i = 0; i < recursos.size(); i++) {
                    if (recursosUpdate == null) {
                        recursosUpdate = new ArrayList<Recurso>();
                    }
                    Recurso recursoTmp = Json.fromJson(recursos.get(i), Recurso.class);
                    recursoTmp.setIdProducto(producto.getId());
                    recursosUpdate.add(recursoTmp);
                }
                producto.setRecursos(recursosUpdate);

                if("PAQ".equals(tipoProducto)){
                    List<ItemServicio> itemServiciosUpdate = ((Paquete)producto).getItemServicios();

                    for (int i = 0; i < itemServicios.size(); i++) {
                        if (itemServiciosUpdate == null) {
                            itemServiciosUpdate = new ArrayList<ItemServicio>();
                        }
                        ItemServicio itemServicioTmp = Json.fromJson(itemServicios.get(i), ItemServicio.class);
                        itemServicioTmp.setIdProducto(producto.getId());
                        itemServiciosUpdate.add(itemServicioTmp);
                    }
                    ((Paquete)producto).setItemServicios(itemServiciosUpdate);

                }
                respuesta = Json.toJson(producto);
            }
        }

        return ok(respuesta);

    }

    @Transactional
    public Result delete(Long id) {
        Producto producto = productos.delete(id);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto no existe\"}");
        if(producto!=null){
           respuesta= Json.toJson(producto);
        }
        return ok(respuesta);
    }

    @Transactional(readOnly=true)
    public Result getProductos() {
        return ok(Json.toJson(productos.getProductos()));
    }

    @Transactional(readOnly=true)
    public Result getProductosByType(String productType) {
        return ok(Json.toJson(productos.getProductosByType(productType)));
    }

    @Transactional(readOnly=true)
    public Result getProductById(Long productId) {
        return ok(Json.toJson(productos.getProductById(productId)));
    }

    @Transactional(readOnly=true)
    public Result getProductsByProveedorId(Long proveedorId) {
        return ok(Json.toJson(productos.getProductsByProveedorId(proveedorId)));
    }

    @Transactional(readOnly=true)
    public Result getProductosByPageByType(Integer numPagina,String productType){
        List<Producto> productosList =productos.getProductosByPageByType(numPagina,productType);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"La página solicitada no existe\"}");
        if(productosList!=null){
            respuesta= Json.toJson(productosList);
        }
        return ok(respuesta);
    }

    @Transactional(readOnly=true)
    public Result getProductosByPageByFilters(Integer numPage,String name,Double precioInicial, Double precioFinal, String productType, Long idProvider){
        List<Producto> productosList =productos.getProductosByPageByFilters(numPage,name,precioInicial,precioFinal,productType, idProvider);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"La página solicitada no existe\"}");
        if(productosList!=null){
            respuesta= Json.toJson(productosList);
        }
        return ok(respuesta);
    }

    @Transactional(readOnly=true)
    public Result getProductosByPageByTypeAndCategory(Integer numPagina,String productType, long categoriaId){
        List<Producto> productosList =productos.getProductosByPageByTypeAndCategory(numPagina,productType, categoriaId);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"La página solicitada no existe\"}");
        if(productosList!=null){
            respuesta= Json.toJson(productosList);
        }
        return ok(respuesta);
    }


    @Transactional(readOnly=true)
    public Result getAttributeByProductId(Long idProducto) {
        return ok(Json.toJson(productos.getAttributeByProductId(idProducto)));
    }

    @Transactional
    public Result addAtributo() {
        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
        Atributo atributo = Json.fromJson(json,Atributo.class);
        if(atributo!=null){

            atributo= productos.addAtributo(atributo);
            respuesta=Json.toJson(atributo);
        }
        return ok(Json.toJson(respuesta));
    }

    @Transactional
    public Result removeAtributo() {
        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
        Atributo atributo = Json.fromJson(json,Atributo.class);
        if(atributo!=null){

            atributo= productos.removeAtributo(atributo);
            respuesta=Json.toJson(atributo);
        }
        return ok(Json.toJson(respuesta));
    }


}
