package test;

import entity.Zona;
import jakarta.persistence.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SmokeTestJavaTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    static void setup() {
        emf = Persistence.createEntityManagerFactory("zooPU");
    }

    @AfterAll
    static void teardown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void persist_find_delete_zona() {
        int generatedId;


        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Zona z = new Zona();
            z.setNume("TEST_ZONA_" + System.currentTimeMillis());
            z.setCapacitateMaxima(123); // <-- corect

            em.persist(z);
            em.flush();

            generatedId = z.getId();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }

        assertTrue(generatedId > 0);


        em = emf.createEntityManager();
        try {
            Zona found = em.find(Zona.class, generatedId);
            assertNotNull(found);
            assertEquals(123, found.getCapacitateMaxima());
        } finally {
            em.close();
        }

        em = emf.createEntityManager();
        tx = em.getTransaction();
        try {
            tx.begin();
            Zona toDelete = em.find(Zona.class, generatedId);
            if (toDelete != null) {
                em.remove(toDelete);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
