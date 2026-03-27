package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "zona")
public class Zona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nume;

    @Column(name = "capacitate_maxima")
    private int capacitateMaxima;

    public Zona() {
    }

    public Zona(int id, String nume, int capacitateMaxima) {
        this.id = id;
        this.nume = nume;
        this.capacitateMaxima = capacitateMaxima;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public int getCapacitateMaxima() { return capacitateMaxima; }

    public void setId(int id) { this.id = id; }
    public void setNume(String nume) { this.nume = nume; }
    public void setCapacitateMaxima(int capacitateMaxima) {
        this.capacitateMaxima = capacitateMaxima;
    }

    @Override
    public String toString() {
        return "Zona{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", capacitateMaxima=" + capacitateMaxima +
                '}';
    }
}
