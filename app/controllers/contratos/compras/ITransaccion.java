package controllers.contratos.compras;

import models.compra.Transaccion;

/**
 * Created by JoséLuis on 26/03/2016.
 */
public interface ITransaccion {

    public Transaccion pagarCompra(Transaccion transaccion);



}
