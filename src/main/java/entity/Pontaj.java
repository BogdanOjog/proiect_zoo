package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "pontaj")
public class Pontaj implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ingrijitor_id")
    private Ingrijitor ingrijitor;

    private LocalDate data;

    @Column(name = "ore_lucrate")
    private double oreLucrate;

    public Pontaj() { }

    public Pontaj(int id,
                  Ingrijitor ingrijitor,
                  LocalDate data,
                  double oreLucrate) {
        this.id = id;
        this.ingrijitor = ingrijitor;
        this.data = data;
        this.oreLucrate = oreLucrate;
    }

    @Override
    public String toString() {
        return "Pontaj{" +
                "id=" + id +
                ", ingrijitor=" + (ingrijitor != null ? ingrijitor.getNume() : "null") +
                ", data=" + data +
                ", oreLucrate=" + oreLucrate +
                '}';
    }
}
