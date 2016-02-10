package models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by manuel on 10/02/16.
 */
@Entity
public class MarketPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "marketPlaceGen")
    @SequenceGenerator(name = "marketPlaceGen",
            sequenceName = "marketPlace_seq")
    private long id;

    @Column(nullable=false)
    private String nombre;

    @Column(nullable=false)
    private String descripcion;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name ="idMarketPlace")
    private Set<Categoria> categorias;

}
