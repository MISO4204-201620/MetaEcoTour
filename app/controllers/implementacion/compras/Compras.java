package controllers.implementacion.compras;

import controllers.contratos.compras.ICompra;
import controllers.contratos.compras.IItemCompra;
import models.compra.Compra;
import models.compra.ItemCompra;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.List;

/**
 * Created by JoséLuis on 19/03/2016.
 */
public class Compras implements ICompra {
    private IItemCompra itemCompras = new ItemCompras();

    @Transactional
    public List<Compra> getCompra(Long idCompra) {
        return JPA.em().createNamedQuery("Compra.findByCompra", Compra.class ).setParameter("idCompra",idCompra).getResultList();
    }

    @Transactional
    public List<Compra> getCompraActivaByUsuario(Long idUsuario) {
        return JPA.em().createNamedQuery("Compra.findCompraAbiertaByUsuario", Compra.class ).setParameter("idUsuario",idUsuario).getResultList();
    }

    @Transactional
    public List<ItemCompra> getItemmsCompra(Long idCompra) {
        return itemCompras.getItemCompraByCompra(idCompra);
    }

    @Transactional
    public ItemCompra addItemmsCompra(ItemCompra itemCompra) {
        return itemCompras.save(itemCompra);
    }

    @Transactional
    public ItemCompra removeItemmsCompra(ItemCompra itemCompra) {
        return itemCompras.delete(itemCompra);
    }

    @Transactional
    public Compra save(Compra compra) {
        EntityManager em = JPA.em();
        Compra compratMP = new Compra();
        System.out.println("El id de la compra :   "+compra.getIdCompra());
        compratMP.setIdCompra(compra.getIdCompra());
        Compra compradb = em.find(Compra.class, compratMP);
        if(compradb == null){
            compra.setEstado("A");
            compra.setFechaCreacion(new Date(System.currentTimeMillis()));
            em.persist(compra);
        }else{
            compradb.setEstado(compra.getEstado());
            compradb.setFechaActualizacion(new Date(System.currentTimeMillis()));
            em.merge(compradb);
            compradb = compra;
        }

        return compradb;
    }

    @Transactional
    public Compra delete(Compra compra) {
        return null;
    }
}
