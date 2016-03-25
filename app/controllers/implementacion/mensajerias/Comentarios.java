package controllers.implementacion.mensajerias;

import controllers.contratos.mensajerias.IComentario;
import models.mensajeria.Comentario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by crimago on 11/03/16.
 */
public class Comentarios implements IComentario {

    private int PAG_NUM_OF_RECORDS_CONTEST = 10;

    @Override
    @Transactional
    public Comentario getComentarioById(Long id) {
        return JPA.em().find(Comentario.class, id);
    }

    @Override
    @Transactional
    public Comentario save(Comentario comentario) {
        EntityManager em = JPA.em();
        Long comentarioId = comentario.getId();
        Comentario comentarioTemp = em.find(Comentario.class, comentarioId);

        if(comentarioTemp == null){
            em.persist(comentario);
        }else{
            if (!"".equals(comentario.getTexto())){
                comentarioTemp.setTexto(comentario.getTexto());
            }
            if (comentario.getFecha() != null){
                comentarioTemp.setFecha(comentario.getFecha());
            }
            comentarioTemp.setIdUsuario(comentario.getIdUsuario());
            if (comentario.getOrigen() != null){
                comentarioTemp.setOrigen(comentario.getOrigen());
            }
            if (comentario.getSubComentarios() != null && comentario.getSubComentarios().size() > 0){
                comentarioTemp.setSubComentarios(comentario.getSubComentarios());
            }
            em.merge(comentarioTemp);
            comentario = comentarioTemp;
        }

        return comentario;
    }

    @Override
    @Transactional
    public Comentario delete(Long id) {
        EntityManager em = JPA.em();
        Comentario comentario = em.find(Comentario.class, id);
        em.remove(comentario);
        return comentario;
    }

    @Override
    @Transactional
    public Comentario getComentario(Long id) {
        Comentario comentario = null;
        try {
            EntityManager em = JPA.em();
            comentario = em.find(Comentario.class, id);
        } catch (javax.persistence.NoResultException e){
            return comentario;
        }
        return comentario;
    }

    @Override
    @Transactional
    public List<Object> getComentariosByIdProductoAndType(Long id, int page, Comentario.Tipo type) {
        int pageIndex = 0;
        if(page >= 0){
            pageIndex = page -1;
        }

        return JPA.em().createNamedQuery("ComentarioDTO.findByIdProductoUsuario")
                .setMaxResults(PAG_NUM_OF_RECORDS_CONTEST)
                .setFirstResult(pageIndex * PAG_NUM_OF_RECORDS_CONTEST)
                .setParameter("productoId",id)
                .setParameter("tipo", type).getResultList();
    }

    @Override
    @Transactional
    public List<Object> getComentariosByIdPadreComentario(Long id) {
        Comentario comentario = new Comentario();
        comentario.setId(id);
        return JPA.em().createNamedQuery("ComentarioDTO.findComentariosByPadre")
                .setParameter("comentarioId",comentario).getResultList();
    }

    @Override
    @Transactional
    public List<Comentario> getComentariosByIdUsuario(Long id) {
        return JPA.em().createNamedQuery("Comentario.findByIdUsuario", Comentario.class ).setParameter("usuarioId",id).getResultList();
    }

    @Override
    @Transactional
    public List<Comentario> getSubComentarios(Long id) {
        Comentario comentario = JPA.em().find(Comentario.class, id);
        if (comentario != null && comentario.getSubComentarios() != null){
            return (List<Comentario>) comentario.getSubComentarios();
        }
        return null;
    }
}
