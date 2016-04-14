package aspectos;

/**
 * Created by camilo on 13/04/16.
 */
public aspect Prueba {

    pointcut prueba():call(public Result controllers.implementacion.catalogos.CategoriasController.getCategorias());

    before():prueba(){

        System.out.println("Cargando el aspectoooooooooooooooooooooo.........");
    }
}
