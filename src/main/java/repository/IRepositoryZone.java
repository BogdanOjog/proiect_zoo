package repository;

import entity.Zona;

import java.util.List;

public interface IRepositoryZone {

    void add(Zona zona);

    void update(Zona zona);

    void removeById(int id);

    void removeAll();

    List<Zona> getAll();

    Zona getZonaDupaId(int id);

    void close();
}
