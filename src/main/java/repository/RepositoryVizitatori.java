package repository;

import entity.Vizitator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class RepositoryVizitatori implements IRepositoryVizitatori {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Vizitator vizitator) {
        try {
            em.getTransaction().begin();
            em.persist(vizitator);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Vizitator vizitator) {
        try {
            em.getTransaction().begin();
            em.merge(vizitator);
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
            Vizitator v = em.find(Vizitator.class, id);
            if (v != null) {
                em.remove(v);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Vizitator> getAll() {
        return em.createQuery("SELECT v FROM Vizitator v ORDER BY v.id", Vizitator.class)
                .getResultList();
    }

    @Override
    public Vizitator getVizitatorDupaId(int id) {
        return em.find(Vizitator.class, id);
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
