package controllers.implementacion.compras;

import controllers.contratos.compras.ICompra;
import controllers.contratos.compras.ITransaccion;
import models.compra.Compra;
import models.compra.Transaccion;
import play.db.jpa.Transactional;

import java.sql.Date;
import java.util.Random;

/**
 * Created by JoséLuis on 26/03/2016.
 */
public class Transacciones implements ITransaccion {
    private static ICompra compras = new Compras();

    @Override
    @Transactional
    public Transaccion pagarCompra(Transaccion transaccion) {

        String[] errorPago = {"Usuario no reconocido","Fondos insuficientes","Validacion de seguridad no satisfactoria","Numero de tarjeta no reconocido","Plataforma de pagos no responde"};
        Random randomGenerator = new Random();

        if(transaccion.getIdCompra() != null){
            System.out.println("IdCompra:["+transaccion.getIdCompra()+"]");
            Compra compra = compras.getCompra(transaccion.getIdCompra()).get(0);
            if (compra != null){
                int valor = randomGenerator.nextInt(100);
                if(transaccion != null){
                    transaccion.setFecha(new Date(System.currentTimeMillis()));
                    if(valor <= 80){
                        transaccion.setEstado("CORRECTO");
                        transaccion.setDescripcion("El pago ha sido satisfactorio.");
                        valor = randomGenerator.nextInt(1000);
                        transaccion.setNumAutorizacion("AUT000"+valor);
                        valor = randomGenerator.nextInt(10000);
                        transaccion.setNumReferencia("REF1234"+valor);
                        /*Actualiza estado Compra*/
                        compra.setEstado("C");
                        compra.setFechaActualizacion(new Date(System.currentTimeMillis()));
                        compra.setMedioPago(transaccion.getTipoPago());
                        compra.setDescripcion(transaccion.getDescripcion()+" :: {Fecha:"+transaccion.getFecha()+", No.Aturizacion:"+transaccion.getNumAutorizacion()+", No.Referencia:"+transaccion.getNumReferencia()+"}");
                        compras.save(compra);
                    }else{
                        transaccion.setEstado("ERROR");
                        valor = randomGenerator.nextInt(5);
                        transaccion.setDescripcion("ERROR :: "+errorPago[valor]);
                        /*Actualiza estado Compra*/
                        compra.setEstado("E");
                        compra.setFechaActualizacion(new Date(System.currentTimeMillis()));
                        compra.setMedioPago(transaccion.getTipoPago());
                        compra.setDescripcion(transaccion.getDescripcion()+" :: {Fecha:"+transaccion.getFecha()+"}");
                        compras.save(compra);
                    }
                }
            }else{
                transaccion.setEstado("ERROR");
                transaccion.setDescripcion("ERROR :: Compra no hallada");
            }
        }else{
            transaccion.setEstado("ERROR");
            transaccion.setDescripcion("ERROR :: idCompra no identifiicado");
        }

        return transaccion;
    }
}
