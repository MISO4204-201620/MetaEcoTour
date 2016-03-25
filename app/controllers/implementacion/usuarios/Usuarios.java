package controllers.implementacion.usuarios;


import controllers.contratos.usuarios.IUsuarios;
import models.usuario.Administrador;
import models.usuario.Cliente;
import models.usuario.Proveedor;
import models.usuario.Usuario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by manuel on 26/02/16.
 */
public class Usuarios implements IUsuarios {

    @Transactional
    public Usuario findByAuthToken (String authToken){
        try {
            return JPA.em().createNamedQuery("Usuario.findByAuthToken", Usuario.class ).setParameter("token", authToken).getSingleResult();
        } catch (javax.persistence.NoResultException e){
            return null;
        }
    }

    @Transactional
    public Usuario findByCorreoAndClave (String correo, String clave){
        try {
            return JPA.em().createNamedQuery("Usuario.findByCorreoAndClave", Usuario.class ).setParameter("correo", correo).setParameter("clave", clave).getSingleResult();
        } catch (javax.persistence.NoResultException e){
            return null;
        }
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

    @Override
    @Transactional
    public Usuario crearUsuario(Usuario usuario, String tipo) {

        EntityManager em = JPA.em();
        Long userId = usuario.getId();

        Usuario usuarioTmp=null;
        if("ADMIN".equals(tipo)){
            usuarioTmp = em.find(Administrador.class, userId);
        }else if ("PROVIDER".equals(tipo)){
            usuarioTmp = em.find(Proveedor.class, userId);
        }else if ("CLIENT".equals(tipo)){
            usuarioTmp = em.find(Cliente.class, userId);
        }

        if(usuarioTmp == null){
            if(0==userId){
                em.persist(usuario);
            }else{
                usuario=null;
            }

        }else{

            if(!"".equals(usuario.getNombre())){
                usuarioTmp.setNombre(usuario.getNombre());
            }
            if(!"".equals(usuario.getCorreo())){
                usuarioTmp.setCorreo(usuario.getCorreo());
            }
            if(!"".equals(usuario.getClave())){
                usuarioTmp.setClave(usuario.getClave());
            }
            if(!"".equals(usuario.getDocumento())){
                usuarioTmp.setDocumento(usuario.getDocumento());
            }
            if(!"".equals(usuario.getTipoDoc())){
                usuarioTmp.setTipoDoc(usuario.getTipoDoc());
            }
            if ("PROVIDER".equals(tipo)){
                boolean activo = ((Proveedor)usuario).getActivo();
                ((Proveedor)usuarioTmp).setActivo(activo);

                String descripcion = ((Proveedor)usuario).getDescripcion();
                if(!"".equals(descripcion)){
                    ((Proveedor)usuarioTmp).setDescripcion(descripcion);
                }
            }else if ("CLIENT".equals(tipo)){

                String apellido = ((Cliente)usuario).getApellido();
                if(!"".equals(apellido)){
                    ((Cliente)usuarioTmp).setApellido(apellido);
                }
            }
            usuario=em.merge(usuarioTmp);
        }

        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario getUsuarioById(Long idProvider) {
        Usuario usuario =JPA.em().find(Usuario.class, idProvider);
        return usuario;
    }

    @Override
    public Usuario deleteUserById(Long userId) {
        EntityManager em = JPA.em();
        Usuario usuario = em.find(Usuario.class, userId);
        if(usuario!=null) {
            em.remove(usuario);
        }
            return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getUsuariosByTypeAndPage(Integer numPage, String tipo) {
        List<Usuario> usuarios =null;
        List<Usuario> usuariosConsultados=null;

        Query query = getQueryFromUserType(tipo);
        usuarios= query.getResultList();

        usuariosConsultados=paginarResultados(usuarios,query,numPage);
        return usuariosConsultados;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getUsuariosByType(String tipo) {
        List<Usuario> usuarios =null;
        Query query = getQueryFromUserType(tipo);
        usuarios= query.getResultList();

        return usuarios;
    }

    public Query getQueryFromUserType(String tipo){
        Query query = null;
        if("PROVIDER".equals(tipo)){
            query = JPA.em().createQuery("SELECT us FROM Proveedor us",Proveedor.class);

        }else if("ADMIN".equals(tipo)){
            query = JPA.em().createQuery("SELECT us FROM Administrador us",Administrador.class);
        }else if("CLIENT".equals(tipo)){
            query = JPA.em().createQuery("SELECT us FROM Cliente us",Cliente.class);
        }
        return query;
    }
    public List<Usuario> paginarResultados(List<Usuario> usuarios, Query query, int numPage){

        List<Usuario> usuariosConsultados=null;
        int countResult = usuarios.size();
        int pageIndex = 0;
        if(numPage >= 0){
            pageIndex = numPage-1;
        }
        int primerResultado= pageIndex * 5;
        if(countResult==0 && numPage ==1){
            usuariosConsultados = new ArrayList<Usuario>();
        }else {
            if(!(countResult<5 && numPage >=2)) {

                if (primerResultado <= countResult) {
                    if (primerResultado == countResult && countResult>5) {
                        primerResultado = primerResultado - 1;
                    }
                    query = query.setMaxResults(5)
                            .setFirstResult(primerResultado);
                    usuariosConsultados = query.getResultList();
                    usuariosConsultados= usuariosConsultados.isEmpty()?null:usuariosConsultados;
                } else if ((primerResultado - countResult) <= 5) {
                    query = query.setMaxResults(5)
                            .setFirstResult(((pageIndex - 1) * 5) + (5 - (primerResultado - countResult)));
                    usuariosConsultados = query.getResultList();
                    usuariosConsultados= usuariosConsultados.isEmpty()?null:usuariosConsultados;
                }

            }

        }
        return usuariosConsultados;
    }
}
