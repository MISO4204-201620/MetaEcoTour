package Activos;


import play.mvc.With;

import java.lang.annotation.*;

/**
 * Created by crimago on 10/05/16.
 */
@With(MensajeriaAction.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mensajeria {
    boolean value() default true;
}
