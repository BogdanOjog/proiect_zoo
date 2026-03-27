package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Pasare")
public class Pasare extends Animal {

    public Pasare() {
    }

    public Pasare(int id,
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
