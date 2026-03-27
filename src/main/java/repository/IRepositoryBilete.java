package repository;

import entity.Bilet;

import java.time.LocalDate;
import java.util.List;

public interface IRepositoryBilete {

    void add(Bilet bilet);

    void update(Bilet bilet);

    void removeById(int id);

    List<Bilet> getAll();

    Bilet getBiletDupaId(int id);

    List<Bilet> getBiletePeData(LocalDate data);

    void close();
}
