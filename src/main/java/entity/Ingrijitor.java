package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ingrijitor")
public class Ingrijitor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nume;
    private int varsta;

    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zonaAsignata;

    @Column(name = "tarif_orar")
    private double tarifOrar;

    public Ingrijitor() { }

    public Ingrijitor(int id,
                      String nume,
                      int varsta,
                      Zona zonaAsignata,
                      double tarifOrar) {
        this.id = id;
        this.nume = nume;
        this.varsta = varsta;
        this.zonaAsignata = zonaAsignata;
        this.tarifOrar = tarifOrar;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public int getVarsta() { return varsta; }
    public Zona getZonaAsignata() { return zonaAsignata; }
    public double getTarifOrar() { return tarifOrar; }

    public void setId(int id) { this.id = id; }
    public void setNume(String nume) { this.nume = nume; }
    public void setVarsta(int varsta) { this.varsta = varsta; }
    public void setZonaAsignata(Zona zonaAsignata) { this.zonaAsignata = zonaAsignata; }
    public void setTarifOrar(double tarifOrar) { this.tarifOrar = tarifOrar; }

    @Override
    public String toString() {
        return "Ingrijitor{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", zonaAsignata=" + (zonaAsignata != null ? zonaAsignata.getNume() : "null") +
                ", tarifOrar=" + tarifOrar +
                '}';
    }
}
