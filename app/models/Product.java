package models;


import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "competitionGen")
    @SequenceGenerator(name = "competitionGen",
            sequenceName = "competition_seq")
    private long id;

    @Column(unique=true, nullable=false)
    private String ean;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String description;

    public Product() {
    }

    public Product(String ean, String name, String description) {
        this.setEan(ean);
        this.setName(name);
        this.setDescription(description);
    }

    public String toString() {
        return String.format("%s - %s", getEan(), getName());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}