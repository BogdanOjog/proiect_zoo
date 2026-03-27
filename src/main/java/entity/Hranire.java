package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "hranire")
public class Hranire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "ingrijitor_id")
    private Ingrijitor ingrijitor;

    @Column(name = "tip_hrana")
    private String tipHrana;

    private LocalDate data;

    @Column(name = "cantitate_hrana")
    private double cantitateHrana;

    @Column(name = "cantitate_apa")
    private double cantitateApa;

    public Hranire() { }

    public Hranire(int id,
                   Animal animal,
                   Ingrijitor ingrijitor,
                   String tipHrana,
                   LocalDate data,
                   double cantitateHrana,
                   double cantitateApa) {
        this.id = id;
        this.animal = animal;
        this.ingrijitor = ingrijitor;
        this.tipHrana = tipHrana;
        this.data = data;
        this.cantitateHrana = cantitateHrana;
        this.cantitateApa = cantitateApa;
    }

    @Override
    public String toString() {
        return "Hranire{" +
                "id=" + id +
                ", animal=" + (animal != null ? animal.getNume() : "null") +
                ", ingrijitor=" + (ingrijitor != null ? ingrijitor.getNume() : "null") +
                ", tipHrana='" + tipHrana + '\'' +
                ", data=" + data +
                ", cantitateHrana=" + cantitateHrana +
                ", cantitateApa=" + cantitateApa +
                '}';
    }
}
