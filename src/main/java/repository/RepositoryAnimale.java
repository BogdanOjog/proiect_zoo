package repository;

import entity.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class RepositoryAnimale implements IRepositoryAnimale {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Animal animal) {
        try {
            em.getTransaction().begin();
            em.persist(animal);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Animal animal) {
        try {
            em.getTransaction().begin();
            em.merge(animal);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void removeById(int id) {
        try {
            em.getTransaction().begin();
            Animal a = em.find(Animal.class, id);
            if (a != null) {
                em.remove(a);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Animal> getAll() {
        return em.createQuery(
                        "SELECT a FROM Animal a LEFT JOIN FETCH a.zona ORDER BY a.id",
                        Animal.class)
                .getResultList();
    }

    @Override
    public Animal getAnimalDupaId(int id) {
        return em.find(Animal.class, id);
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
