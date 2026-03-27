package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Peste")
public class Peste extends Animal {

    public Peste() {
    }

    public Peste(int id,
                 String nume,
                 String sex,
                 int varsta,
                 double inaltime,
                 double greutate,
                 double cantitateHrana,
                 double cantitateApa,
                 double frecventaHrana,
                 Zona zona) {
        super(id, nume, sex, varsta, inaltime, greutate,
                cantitateHrana, cantitateApa, frecventaHrana, zona);
    }
}
