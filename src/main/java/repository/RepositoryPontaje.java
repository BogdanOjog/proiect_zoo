package repository;

import entity.Pontaj;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class RepositoryPontaje implements IRepositoryPontaje {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Pontaj pontaj) {
        try {
            em.getTransaction().begin();
            em.persist(pontaj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Pontaj pontaj) {
        try {
            em.getTransaction().begin();
            em.merge(pontaj);
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
            Pontaj p = em.find(Pontaj.class, id);
            if (p != null) {
                em.remove(p);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Pontaj> getAll() {
        return em.createQuery(
                        "SELECT p FROM Pontaj p LEFT JOIN FETCH p.ingrijitor ORDER BY p.id",
                        Pontaj.class)
                .getResultList();
    }

    @Override
    public Pontaj getPontajDupaId(int id) {
        return em.find(Pontaj.class, id);
    }

    @Override
    public double getOreLucratePerioada(int ingrijitorId, LocalDate start, LocalDate end) {
        Double total = em.createQuery(
                        "SELECT COALESCE(SUM(p.oreLucrate), 0) " +
                                "FROM Pontaj p " +
                                "WHERE p.ingrijitor.id = :id " +
                                "AND p.data BETWEEN :start AND :end",
                        Double.class)
                .setParameter("id", ingrijitorId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        return total != null ? total : 0.0;
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
