package repository;

import entity.Hranire;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class RepositoryHraniri implements IRepositoryHraniri {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Hranire hranire) {
        try {
            em.getTransaction().begin();
            em.persist(hranire);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Hranire hranire) {
        try {
            em.getTransaction().begin();
            em.merge(hranire);
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
            Hranire h = em.find(Hranire.class, id);
            if (h != null) {
                em.remove(h);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Hranire> getAll() {
        return em.createQuery(
                        "SELECT h FROM Hranire h LEFT JOIN FETCH h.animal LEFT JOIN FETCH h.ingrijitor ORDER BY h.id",
                        Hranire.class)
                .getResultList();
    }

    @Override
    public Hranire getHranireDupaId(int id) {
        return em.find(Hranire.class, id);
    }

    @Override
    public List<Hranire> getHraniriPeData(LocalDate data) {
        return em.createQuery("SELECT h FROM Hranire h WHERE h.data = :d ORDER BY h.id", Hranire.class)
                .setParameter("d", data)
                .getResultList();
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
