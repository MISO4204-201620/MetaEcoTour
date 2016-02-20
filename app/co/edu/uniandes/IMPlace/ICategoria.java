/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.IMPlace;

import java.util.Collection;

/**
 * Implementa la clase Categoria
 * @author jlpulgarin
 */
public interface ICategoria {

    /**
     * Crea una categoria para el MP
     *
     * @param nombre : Nombre de la categoria
     * @param descripcion : Descripcion
     * @return int : Id de la categoria
     */
    int crearCategoria(String nombre, String descripcion);

    Collection<?> getCategorias();

    /**
     * Se inactiva la categoria
     *
     * @return Boolean : Estado de la operación
     */
    boolean inActivar(Long Id);

    /**
     * Se reactiva la categoria
     *
     * @return Boolean : Estado de la operación
     */
    boolean reActivar(Long Id);
    
}
