package controllers.implementacion.compras;

import controllers.contratos.compras.IItemCompra;
import models.compra.ItemCompra;
import models.compra.ItemCompraId;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by JoséLuis on 19/03/2016.
 */
public class ItemCompras implements IItemCompra {
    @Transactional
    public List<ItemCompra> getItemCompraByCompra(Long idCompra) {
        return JPA.em().createNamedQuery("ItemCompra.findByItemCompra", ItemCompra.class ).setParameter("idCompra",idCompra).getResultList();
    }

    @Transactional
    public List<ItemCompra> getItemCompraByProducto(Long idProducto) {
        return JPA.em().createNamedQuery("ItemCompra.findByProducto", ItemCompra.class ).setParameter("idProducto",idProducto).getResultList();
    }

    @Transactional
    public ItemCompra save(ItemCompra itemCompra) {
        EntityManager em = JPA.em();
        ItemCompraId pKey = new ItemCompraId();
        pKey.setIdCompra(itemCompra.getIdCompra());
        pKey.setIdProducto(itemCompra.getIdProducto());
        ItemCompra itmCompra = em.find(ItemCompra.class, pKey);
        if(itmCompra == null){
            em.persist(itmCompra);
        }else{
            itmCompra.setCantidad(itemCompra.getCantidad());
            itmCompra.setPrecio(itemCompra.getPrecio());
            em.merge(itmCompra);
            itemCompra = itmCompra;
        }

        return itemCompra;
    }

    @Transactional
    public ItemCompra delete(ItemCompra itemCompra) {
        EntityManager em = JPA.em();
        ItemCompraId pKey = new ItemCompraId();
        pKey.setIdCompra(itemCompra.getIdCompra());
        pKey.setIdProducto(itemCompra.getIdProducto());
        ItemCompra itmSrv = em.find(ItemCompra.class, pKey);
        if(itmSrv!=null) {
            em.remove(itmSrv);
        }
        return itmSrv;
   }
}
