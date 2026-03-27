package repository;

import entity.Ingrijitor;

import java.util.List;

public interface IRepositoryIngrijitori {

    void add(Ingrijitor ingrijitor);

    void update(Ingrijitor ingrijitor);

    void removeById(int id);

    List<Ingrijitor> getAll();

    Ingrijitor getIngrijitorDupaId(int id);

    void close();
}
