package repository;

import entity.Animal;

import java.util.List;

public interface IRepositoryAnimale {

    void add(Animal animal);

    void update(Animal animal);

    void removeById(int id);

    List<Animal> getAll();

    Animal getAnimalDupaId(int id);

    void close();
}
