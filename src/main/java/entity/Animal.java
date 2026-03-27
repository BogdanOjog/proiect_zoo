package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "animal")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tip_animal")
public abstract class Animal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nume;
    private String sex;
    private int varsta;
    private double inaltime;
    private double greutate;

    @Column(name = "cantitate_hrana")
    private double cantitateHrana;

    @Column(name = "cantitate_apa")
    private double cantitateApa;

    @Column(name = "frecventa_hrana")
    private double frecventaHrana;

    @Column(name = "tip_animal", insertable = false, updatable = false)
    private String tipAnimal;

    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zona;

    protected Animal() {
    }

    protected Animal(int id,
                     String nume,
                     String sex,
                     int varsta,
                     double inaltime,
                     double greutate,
                     double cantitateHrana,
                     double cantitateApa,
                     double frecventaHrana,
                     Zona zona) {
        this.id = id;
        this.nume = nume;
        this.sex = sex;
        this.varsta = varsta;
        this.inaltime = inaltime;
        this.greutate = greutate;
        this.cantitateHrana = cantitateHrana;
        this.cantitateApa = cantitateApa;
        this.frecventaHrana = frecventaHrana;
        this.zona = zona;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getSex() { return sex; }
    public int getVarsta() { return varsta; }
    public double getInaltime() { return inaltime; }
    public double getGreutate() { return greutate; }
    public double getCantitateHrana() { return cantitateHrana; }
    public double getCantitateApa() { return cantitateApa; }
    public double getFrecventaHrana() { return frecventaHrana; }
    public String getTipAnimal() { return tipAnimal; }
    public Zona getZona() { return zona; }

    public void setId(int id) { this.id = id; }
    public void setNume(String nume) { this.nume = nume; }
    public void setSex(String sex) { this.sex = sex; }
    public void setVarsta(int varsta) { this.varsta = varsta; }
    public void setInaltime(double inaltime) { this.inaltime = inaltime; }
    public void setGreutate(double greutate) { this.greutate = greutate; }
    public void setCantitateHrana(double cantitateHrana) { this.cantitateHrana = cantitateHrana; }
    public void setCantitateApa(double cantitateApa) { this.cantitateApa = cantitateApa; }
    public void setFrecventaHrana(double frecventaHrana) { this.frecventaHrana = frecventaHrana; }
    public void setZona(Zona zona) { this.zona = zona; }

    @Override
    public String toString() {
        return nume + " (" + tipAnimal + "), Zona: " +
                (zona != null ? (zona.getId() + " - " + zona.getNume()) : "N/A");
    }
}
