package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vizitator")
public class Vizitator implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nume;
    private int varsta;

    public Vizitator() { }

    public Vizitator(int id, String nume, int varsta) {
        this.id = id;
        this.nume = nume;
        this.varsta = varsta;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public int getVarsta() { return varsta; }

    public void setId(int id) { this.id = id; }
    public void setNume(String nume) { this.nume = nume; }
    public void setVarsta(int varsta) { this.varsta = varsta; }

    @Override
    public String toString() {
        return "Vizitator{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", varsta=" + varsta +
                '}';
    }
}
