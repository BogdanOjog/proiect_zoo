package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "bilet")
public class Bilet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tip_bilet")
    private String tip;

    private double pret;

    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vizitator_id", nullable = true)
    private Vizitator vizitator;

    public Bilet() { }

    public Bilet(String tip, double pret, LocalDate data) {
        this.tip = tip;
        this.pret = pret;
        this.data = data;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }

    public double getPret() { return pret; }
    public void setPret(double pret) { this.pret = pret; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Vizitator getVizitator() { return vizitator; }
    public void setVizitator(Vizitator vizitator) { this.vizitator = vizitator; }
}