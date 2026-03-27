package repository;

import entity.Zona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class RepositoryZone implements IRepositoryZone {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Zona zona) {
        try {
            em.getTransaction().begin();
            em.persist(zona);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Zona zona) {
        try {
            em.getTransaction().begin();
            em.merge(zona);
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
            Zona z = em.find(Zona.class, id);
            if (z != null) {
                em.remove(z);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void removeAll() {
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Zona z").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Zona> getAll() {
        return em.createQuery("SELECT z FROM Zona z ORDER BY z.id", Zona.class)
                .getResultList();
    }

    @Override
    public Zona getZonaDupaId(int id) {
        return em.find(Zona.class, id);
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
