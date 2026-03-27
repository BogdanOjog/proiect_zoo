package repository;

import entity.Bilet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class RepositoryBilete implements IRepositoryBilete {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Bilet bilet) {
        try {
            em.getTransaction().begin();
            em.persist(bilet);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Bilet bilet) {
        try {
            em.getTransaction().begin();
            em.merge(bilet);
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
            Bilet b = em.find(Bilet.class, id);
            if (b != null) {
                em.remove(b);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Bilet> getAll() {
        return em.createQuery("SELECT b FROM Bilet b LEFT JOIN FETCH b.vizitator ORDER BY b.id", Bilet.class)
                .getResultList();
    }

    @Override
    public Bilet getBiletDupaId(int id) {
        return em.find(Bilet.class, id);
    }

    @Override
    public List<Bilet> getBiletePeData(LocalDate data) {
        return em.createQuery("SELECT b FROM Bilet b WHERE b.data = :d ORDER BY b.id", Bilet.class)
                .setParameter("d", data)
                .getResultList();
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
