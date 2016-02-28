package devUtils;

import controllers.implementacion.catalogos.Productos;
import controllers.implementacion.catalogos.Recursos;
import models.catalogo.Recurso;
import models.catalogo.Servicio;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class LoadData extends Controller{
    @Transactional
    public Result loadProductsTest(){
        for(int i=1 ; i<=10; i++){
            //inserta un servicio
            Servicio prd = new Servicio();
            //prd.setId(i);
            prd.setNombre("Producto"+i);
            prd.setDescripcion("Descripcion Producto"+i);
            prd.setPrecioActual(2.5+i);
            Productos prdImpl = new Productos();
            prdImpl.save(prd);
            //inserta un recurso Asociado
            Recurso rcs = new Recurso();
            //rcs.setId((long) i);
            rcs.setIdProducto(prd.getId());
            rcs.setNombre("Producto"+prd.getId()+" recurso"+i);
            rcs.setComentario("Descripcion Producto"+rcs.getIdProducto()+" recurso"+i);
            rcs.setTipo("JPG");
            rcs.setContenido(this.loadFile(""+i));
            Recursos srcImpl = new Recursos();
            srcImpl.save(rcs);

        }
        return null;
    }

    @Transactional
    public Result loadRecursosTest(Long prdId){
        System.out.println("IN :: loadRecursosTest");
        for(int i=1 ; i<=10; i++){
            Recurso rcs = new Recurso();
            //rcs.setId((long) i);
            rcs.setIdProducto(prdId+i);
            rcs.setNombre("Producto"+rcs.getIdProducto()+" recurso"+i);
            rcs.setComentario("Descripcion Producto"+rcs.getIdProducto()+" recurso"+i);
            rcs.setTipo("JPG");
            rcs.setContenido(this.loadFile(""+i));
            Recursos prdImpl = new Recursos();
            prdImpl.save(rcs);
            System.out.println("Ok_ins.");
        }
        System.out.println("OUT :: loadRecursosTest");
        return null;
    }

    private byte[] loadFile(String fileName){
        File imgPath = new File("ImgsTest/"+fileName+".jpg");
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imgPath);
            WritableRaster raster = bufferedImage .getRaster();
            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
            return ( data.getData() );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
