package controllers.implementacion.usuarios;


import controllers.contratos.usuarios.IUsuarios;
import models.usuario.Usuario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import java.util.UUID;

/**
 * Created by manuel on 26/02/16.
 */
public class Usuarios implements IUsuarios {

    @Transactional
    public Usuario findByAuthToken (String authToken){
        return JPA.em().createNamedQuery("Usuario.findByAuthToken", Usuario.class ).setParameter("token", authToken).getSingleResult();
    }

    @Transactional
    public Usuario findByCorreoAndClave (String correo, String clave){
        return JPA.em().createNamedQuery("Usuario.findByCorreoAndClave", Usuario.class ).setParameter("correo", correo).setParameter("clave", clave).getSingleResult();
    }

    @Transactional
    public String gestionarToken(Usuario usuario, boolean estado){
        String authToken = null;
        if (estado){
            authToken = UUID.randomUUID().toString();
        }
        usuario.setAuthToken(authToken);
        JPA.em().merge(usuario);
        return authToken;
    }



}
