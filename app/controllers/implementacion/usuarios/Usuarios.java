package controllers.implementacion.usuarios;


import models.usuario.Usuario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * Created by manuel on 26/02/16.
 */
public class Usuarios implements IUsuarios{

    public Usuario findByAuthToken (String authToken){
        return JPA.em().createNamedQuery("Usuario.findByAuthToken", Usuario.class ).setParameter("token", authToken).getSingleResult();
    }

    public Usuario findByCorreoAndClave (String correo, String clave){
        return JPA.em().createNamedQuery("Usuario.findByCorreoAndClave", Usuario.class ).setParameter("correo", correo).setParameter("clave", clave).getSingleResult();
    }

    @Transactional
    public String gestionarToken(Usuario usuario, boolean estado){
        EntityManager em = JPA.em();
        String authToken = null;
        if (estado){
            authToken = UUID.randomUUID().toString();
        }
        usuario.setAuthToken(authToken);
        em.merge(usuario);
        return authToken;
    }



}