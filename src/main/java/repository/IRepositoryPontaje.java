package repository;

import entity.Pontaj;

import java.time.LocalDate;
import java.util.List;

public interface IRepositoryPontaje {

    void add(Pontaj pontaj);

    void update(Pontaj pontaj);

    void removeById(int id);

    List<Pontaj> getAll();

    Pontaj getPontajDupaId(int id);

    double getOreLucratePerioada(int ingrijitorId, LocalDate start, LocalDate end);

    void close();
}
