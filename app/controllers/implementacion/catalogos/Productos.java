package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IAtributo;
import controllers.contratos.catalogos.IBusqueda;
import controllers.contratos.catalogos.IProducto;
import controllers.contratos.catalogos.IRecurso;
import models.catalogo.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo on 27/02/16.
 */
public class Productos implements IProducto {
    private static IRecurso recursos= new Recursos();
    private static IBusqueda busquedas = new Busquedas();
    private static IAtributo atributos = new Atributos();

    @Override
    public List<Producto> getProductos() {
        return JPA.em().createNamedQuery("Producto.findAll", Producto.class ).getResultList();
    }

    @Override
    public List<Producto> getProductosByPageByType(Integer numPage,String productType) {
        List<Producto> productos=null;
        List<Producto> productosConsultados=null;

        Query query = JPA.em().createQuery("SELECT pr FROM Producto pr",Producto.class);

        if("ALL"!=productType){
            if("PAQ".equals(productType)){

                query = JPA.em().createQuery("SELECT pr FROM Producto pr where pr.class = 'PAQ'");

            }else if("SER".equals(productType)){
                query = JPA.em().createQuery("SELECT pr FROM Producto pr where pr.class = 'SER'");
            }
        }
        productos=query.getResultList();

        productosConsultados=paginarResultados(productos,query,numPage);
        return productosConsultados;
    }

    @Override
    public List<Producto> getProductosByPageByTypeAndCategory(Integer numPage,String productType, long idCategory) {
        List<Producto> productos=null;
        List<Producto> productosConsultados=null;

        Query query = JPA.em().createQuery("SELECT pr FROM Producto pr where pr.idCategoria= :categoriaId",Producto.class);

        if("ALL"!=productType){
            if("PAQ".equals(productType)){

                query = JPA.em().createQuery("SELECT pr FROM Producto pr where pr.class = 'PAQ' and pr.idCategoria= :categoriaId");

            }else if("SER".equals(productType)){
                query = JPA.em().createQuery("SELECT pr FROM Producto pr where pr.class = 'SER' and pr.idCategoria= :categoriaId");
            }
        }
        query.setParameter("categoriaId", idCategory);
        productos=query.getResultList();
        productosConsultados =paginarResultados(productos,query,numPage);
        return productosConsultados;
    }

    @Override
    public List<Producto> getProductosByPageByFilters(Integer numPage,String name,Double precioInicial, Double precioFinal, String productType) {
        List<Producto> productos=null;
        List<Producto> productosConsultados=null;

        String parameterSentence="SELECT pr FROM Producto pr where 1=1";
        if(!"0name".equals(name)){
            parameterSentence += " and pr.nombre like :name";
            System.out.println("Añado el nombre");
        }
        if(precioInicial >= 0 && precioFinal >= 0){
            parameterSentence += " and (pr.precioActual between :precioInicial and :precioFinal)";
            System.out.println("Añado el Precio");
        }

        if(!"0name".equals(productType) && !"ALL".equals(productType)){
            parameterSentence += " and (pr.class = :productType )";
            System.out.println("Añado el tipo");
        }

        Query query = JPA.em().createQuery(parameterSentence,Producto.class);

        if(!"0name".equals(name)){
            query.setParameter("name","%"+ name+"%");
            System.out.println("parametro nombre");
        }
        if(precioInicial >= 0 && precioFinal >= 0){
            query.setParameter("precioInicial",precioInicial);
            query.setParameter("precioFinal",precioFinal);
            System.out.println("parametro Precio");
        }

        if(!"0type".equals(productType) && !"ALL".equals(productType)){
            query.setParameter("productType",productType);
            System.out.println("parametro tipo");
        }

        productos=query.getResultList();
        productosConsultados =paginarResultados(productos,query,numPage);
        return productosConsultados;
    }


    @Override
    public List<Producto> getProductosByType(String productType) {
        List<Producto> productos = JPA.em().createNamedQuery("Producto.findAll", Producto.class ).getResultList();
        List<Producto> productsByType = filtrarPorProducto(productos,productType);
        return productsByType;
    }

    @Override
    @Transactional
    public Producto getProductById(Long id){
        Producto producto = JPA.em().find(Producto.class, id);
        return producto;
    }

    @Override
    @Transactional
    public List<Producto> getProductsByProveedorId(Long idProveedor){
        Query query = JPA.em().createQuery("SELECT pr FROM Producto pr where pr.idProveedor= :idProveedor");
        List<Producto> productos = null;
        query.setParameter("idProveedor", idProveedor);
        productos=query.getResultList();

        for (Producto producto: productos) {
            if (producto instanceof Servicio){
                producto.setTipo("SER");
            } else {
                producto.setTipo("PAQ");
            }
        }
        
        return productos;
    }

    public List<Producto> paginarResultados(List<Producto> productos, Query query, int numPage){

        List<Producto> productosConsultados=null;
        int countResult = productos.size();
        int pageIndex = 0;
        if(numPage >= 0){
            pageIndex = numPage-1;
        }
        int primerResultado= pageIndex * 5;
        if(countResult==0 && numPage ==1){
            productosConsultados = new ArrayList<Producto>();
        }else {
            if(!(countResult<5 && numPage >=2)) {

                if (primerResultado <= countResult) {
                    if (primerResultado == countResult && countResult>5) {
                        primerResultado = primerResultado - 1;
                    }
                    query = query.setMaxResults(5)
                            .setFirstResult(primerResultado);
                    productosConsultados = query.getResultList();
                    productosConsultados= productosConsultados.isEmpty()?null:productosConsultados;
                } else if ((primerResultado - countResult) <= 5) {
                    query = query.setMaxResults(5)
                            .setFirstResult(((pageIndex - 1) * 5) + (5 - (primerResultado - countResult)));
                    productosConsultados = query.getResultList();
                    productosConsultados= productosConsultados.isEmpty()?null:productosConsultados;
                }

            }

        }
        return productosConsultados;
    }

    @Override
    @Transactional
    public Producto save(Producto producto) {
        EntityManager em = JPA.em();
        Long productoId = producto.getId();

        Producto productoTmp = em.find(Producto.class, productoId);

        if(productoTmp == null){
            if(0==productoId){
                em.persist(producto);
            }else{
                producto=null;
            }

        }else{

            if(!"".equals(producto.getNombre())){
                productoTmp.setNombre(producto.getNombre());
            }
            if(!"".equals(producto.getDescripcion())){
                productoTmp.setDescripcion(producto.getDescripcion());
            }
            if( -1.0 !=producto.getPrecioActual()){
                productoTmp.setPrecioActual(producto.getPrecioActual());
            }
            if(-1 !=producto.getPuntuacion()){
                productoTmp.setPuntuacion(producto.getPuntuacion());
            }
            if(!"".equals(producto.getImagen())){
                productoTmp.setImagen(producto.getImagen());
            }

            em.merge(productoTmp);
            producto = productoTmp;
        }

        return producto;
    }

    @Override
    @Transactional
    public Producto delete(Long id) {
        EntityManager em = JPA.em();
        Producto producto = em.find(Producto.class, id);
        if(producto!=null) {
            recursos.deleteAllResourceByProdId(id);
            busquedas.deleteAllSearchByProdId(id);
            em.remove(producto);

        }
        return producto;
    }

    public List<Producto> filtrarPorProducto(List<Producto> productosAFiltrar,String typeProducto){
        List<Producto> productsByType = new ArrayList<Producto>();
        for (Producto pro : productosAFiltrar ) {

            if("PAQ".equals(typeProducto)) {
                if (pro.getClass().equals(Paquete.class)) {
                    productsByType.add(pro);
                }
            }
            if("SER".equals(typeProducto)) {
                if (pro.getClass().equals(Servicio.class)) {
                    productsByType.add(pro);
                }
            }
        }
        return productsByType;
    }

    @Override
    @Transactional
    public List<Atributo> getAttributeByProductId(Long idProducto) {
        return atributos.getAtributoByProducto(idProducto);
    }

    @Override
    @Transactional
    public Atributo addAtributo(Atributo atributo) {
        return atributos.save(atributo);
    }

    @Override
    @Transactional
    public Atributo removeAtributo(Atributo atributo) {
        return atributos.delete(atributo);
    }
}