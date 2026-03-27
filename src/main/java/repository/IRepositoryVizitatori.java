package repository;

import entity.Vizitator;

import java.util.List;

public interface IRepositoryVizitatori {

    void add(Vizitator vizitator);

    void update(Vizitator vizitator);

    void removeById(int id);

    List<Vizitator> getAll();

    Vizitator getVizitatorDupaId(int id);

    void close();
}
