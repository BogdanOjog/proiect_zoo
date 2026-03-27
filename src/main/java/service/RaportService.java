package service;

import entity.Bilet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaportService {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("zooPU");


    public Double incasariPeZi(LocalDate data) {
        EntityManager em = emf.createEntityManager();
        try {
            // Selectăm suma prețurilor biletelor din ziua respectivă
            Double suma = em.createQuery("SELECT SUM(b.pret) FROM Bilet b WHERE b.data = :data", Double.class)
                    .setParameter("data", data)
                    .getSingleResult();

            return suma != null ? suma : 0.0;
        } finally {
            em.close();
        }
    }

    public Double incasariPeLuna(int an, int luna) {
        EntityManager em = emf.createEntityManager();
        try {
            Double suma = em.createQuery(
                            "SELECT SUM(b.pret) FROM Bilet b WHERE YEAR(b.data) = :an AND MONTH(b.data) = :luna", Double.class)
                    .setParameter("an", an)
                    .setParameter("luna", luna)
                    .getSingleResult();

            return suma != null ? suma : 0.0;
        } finally {
            em.close();
        }
    }

    public Double incasariPeAn(int an) {
        EntityManager em = emf.createEntityManager();
        try {
            Double suma = em.createQuery("SELECT SUM(b.pret) FROM Bilet b WHERE YEAR(b.data) = :an", Double.class)
                    .setParameter("an", an)
                    .getSingleResult();
            return suma != null ? suma : 0.0;
        } finally {
            em.close();
        }
    }

    // --- 2. STATISTICI VIZITATORI / BILETE VÂNDUTE ---

    public int numarVizitatoriPeZi(LocalDate data) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(b) FROM Bilet b WHERE b.data = :data", Long.class)
                    .setParameter("data", data)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }


    public Map<String, Integer> statisticaTipBileteZi(LocalDate data) {
        EntityManager em = emf.createEntityManager();
        Map<String, Integer> rezultat = new HashMap<>();
        try {
            // Grupăm după coloana 'tip' adăugată recent
            List<Object[]> list = em.createQuery(
                            "SELECT b.tip, COUNT(b) FROM Bilet b WHERE b.data = :data GROUP BY b.tip", Object[].class)
                    .setParameter("data", data)
                    .getResultList();

            for (Object[] row : list) {
                String tip = (String) row[0];
                Long count = (Long) row[1];
                if (tip == null) tip = "Nedefinit"; // Pentru bilete vechi
                rezultat.put(tip, count.intValue());
            }
        } finally {
            em.close();
        }
        return rezultat;
    }

    // --- 3. RAPOARTE HRANĂ ---
    public Double totalHranaInterval(LocalDate start, LocalDate end) {
        EntityManager em = emf.createEntityManager();
        try {
            long zile = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
            Double totalZi = em.createQuery("SELECT SUM(a.cantitateHrana) FROM Animal a", Double.class)
                    .getSingleResult();

            return (totalZi != null ? totalZi : 0.0) * zile;
        } finally {
            em.close();
        }
    }

    public Map<String, Double> incasariPeTipSiInterval(LocalDate start, LocalDate end) {
        EntityManager em = emf.createEntityManager();
        Map<String, Double> rezultat = new HashMap<>();
        try {
            List<Object[]> list = em.createQuery(
                            "SELECT b.tip, SUM(b.pret) FROM Bilet b " +
                                    "WHERE b.data BETWEEN :start AND :end " +
                                    "GROUP BY b.tip", Object[].class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();

            for (Object[] row : list) {
                String tip = (String) row[0];
                Double total = (Double) row[1];
                if (tip == null) tip = "Nedefinit";
                rezultat.put(tip, total);
            }
        } finally {
            em.close();
        }
        return rezultat;
    }

    public void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}