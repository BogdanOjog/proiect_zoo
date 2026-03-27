package repository;

import entity.Hranire;

import java.time.LocalDate;
import java.util.List;

public interface IRepositoryHraniri {

    void add(Hranire hranire);

    void update(Hranire hranire);

    void removeById(int id);

    List<Hranire> getAll();

    Hranire getHranireDupaId(int id);

    List<Hranire> getHraniriPeData(LocalDate data);

    void close();
}
