package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryRapoarte implements IRepositoryRapoarte {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public int getNumarVizitatoriPeZi(LocalDate data) {
        Long nr = em.createQuery(
                        "SELECT COUNT(DISTINCT b.vizitator.id) " +
                                "FROM Bilet b " +
                                "WHERE b.data = :data",
                        Long.class)
                .setParameter("data", data)
                .getSingleResult();
        return nr != null ? nr.intValue() : 0;
    }

    @Override
    public Map<String, Integer> vizitatoriPeVarsteZi(LocalDate data) {
        String jpql = """
                SELECT
                  CASE
                    WHEN v.varsta < 13 THEN 'copii'
                    WHEN v.varsta < 18 THEN 'adolescenti'
                    WHEN v.varsta < 65 THEN 'adulti'
                    ELSE 'seniori'
                  END AS categorie,
                  COUNT(DISTINCT v.id) AS nr
                FROM Bilet b
                  JOIN b.vizitator v
                WHERE b.data = :data
                GROUP BY categorie
                """;

        List<Object[]> rows = em.createQuery(jpql, Object[].class)
                .setParameter("data", data)
                .getResultList();

        Map<String, Integer> rezultat = new HashMap<>();
        for (Object[] r : rows) {
            String categorie = (String) r[0];
            Number nr = (Number) r[1];
            rezultat.put(categorie, nr.intValue());
        }
        return rezultat;
    }

    @Override
    public double getIncasariInterval(LocalDate start, LocalDate end) {
        Double total = em.createQuery(
                        "SELECT COALESCE(SUM(b.pret), 0) " +
                                "FROM Bilet b " +
                                "WHERE b.data BETWEEN :start AND :end",
                        Double.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        return total != null ? total : 0.0;
    }

    @Override
    public double getTotalHranaInterval(LocalDate start, LocalDate end) {
        Double totalHrana = em.createQuery(
                        "SELECT COALESCE(SUM(h.cantitateHrana), 0) " +
                                "FROM Hranire h " +
                                "WHERE h.data BETWEEN :start AND :end",
                        Double.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        return totalHrana != null ? totalHrana : 0.0;
    }

    @Override
    public void close() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }
}
