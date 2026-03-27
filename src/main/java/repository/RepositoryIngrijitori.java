package repository;

import entity.Ingrijitor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class RepositoryIngrijitori implements IRepositoryIngrijitori {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Ingrijitor ingrijitor) {
        try {
            em.getTransaction().begin();
            em.persist(ingrijitor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Ingrijitor ingrijitor) {
        try {
            em.getTransaction().begin();
            em.merge(ingrijitor);
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
            Ingrijitor i = em.find(Ingrijitor.class, id);
            if (i != null) {
                em.remove(i);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Ingrijitor> getAll() {
        return em.createQuery("SELECT i FROM Ingrijitor i LEFT JOIN FETCH i.zonaAsignata ORDER BY i.id", Ingrijitor.class)
                .getResultList();
    }

    @Override
    public Ingrijitor getIngrijitorDupaId(int id) {
        return em.find(Ingrijitor.class, id);
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
