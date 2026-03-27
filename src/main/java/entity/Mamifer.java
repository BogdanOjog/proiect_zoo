package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Mamifer")
public class Mamifer extends Animal {

    public Mamifer() {
    }

    public Mamifer(int id,
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
