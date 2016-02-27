package devUtils;

import controllers.implementacion.catalogos.Productos;
import controllers.implementacion.catalogos.Recursos;
import models.catalogo.Recurso;
import models.catalogo.Servicio;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class LoadData extends Controller{
    public Result loadProductsTest(){
        for(int i=0 ; i<10; i++){
            Servicio prd = new Servicio();
            prd.setId(i);
            prd.setNombre("Producto"+i);
            prd.setDescripcion("Descripcion Producto"+i);
            prd.setPrecioActual(2.5+i);
            Productos prdImpl = new Productos();
            prdImpl.save(prd);
        }
        return null;
    }


    public Result loadRecursosTest(Long prdId){
        System.out.println("IN :: loadRecursosTest");
        for(int i=0 ; i<10; i++){
            Recurso rcs = new Recurso();
            rcs.setId((long) i);
            rcs.setNombre("recurso"+prdId+" recurso"+i);
            rcs.setComentario("Descripcion Producto_"+prdId+" recurso"+i);
            Recursos prdImpl = new Recursos();
            prdImpl.save(rcs);
            System.out.println("Ok_ins.");
        }
        System.out.println("OUT :: loadRecursosTest");
        return null;
    }

}
