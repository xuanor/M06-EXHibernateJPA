package com.project;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final String PERSISTENCE_UNIT_NAME = "myPersistenceUnit";
    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        // Crear el directorio "data" si no existe
        String basePath = System.getProperty("user.dir") + "/data/";
        File dir = new File(basePath);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("Error creating 'data' folder");
        }

        // Inicializar la EntityManagerFactory
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        // Usar EntityManager para realizar operaciones de persistencia
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // CREATE - Crear las ciudades
            Ciutat refCiutat1 = new Ciutat("Ciutat 1", "País A", 1000000, new HashSet<>());
            Ciutat refCiutat2 = new Ciutat("Ciutat 2", "País B", 500000, new HashSet<>());
            Ciutat refCiutat3 = new Ciutat("Ciutat 3", "País C", 750000, new HashSet<>());

            em.persist(refCiutat1);
            em.persist(refCiutat2);
            em.persist(refCiutat3);

            // CREATE - Crear los ciudadanos
            Ciutada refCiutada1 = new Ciutada("Ciutada 1");
            Ciutada refCiutada2 = new Ciutada("Ciutada 2");
            Ciutada refCiutada3 = new Ciutada("Ciutada 3");
            Ciutada refCiutada4 = new Ciutada("Ciutada 4");
            Ciutada refCiutada5 = new Ciutada("Ciutada 5");
            Ciutada refCiutada6 = new Ciutada("Ciutada 6");

            em.persist(refCiutada1);
            em.persist(refCiutada2);
            em.persist(refCiutada3);
            em.persist(refCiutada4);
            em.persist(refCiutada5);
            em.persist(refCiutada6);

            // Asociar ciudadanos a ciudades
            refCiutat1.addCiutada(refCiutada1);
            refCiutat1.addCiutada(refCiutada2);

            refCiutat2.addCiutada(refCiutada3);
            refCiutat2.addCiutada(refCiutada4);

            refCiutat3.addCiutada(refCiutada5);
            refCiutat3.addCiutada(refCiutada6);

            em.merge(refCiutat1);
            em.merge(refCiutat2);
            em.merge(refCiutat3);

            em.getTransaction().commit();

            // READ - Mostrar todas las ciudades y sus ciudadanos
            em.getTransaction().begin();
            System.out.println("Punt 1: Després de la creació inicial d'elements");
            Manager.printAll(Ciutat.class, em);
            Manager.printAll(Ciutada.class, em);
            em.getTransaction().commit();

            // DELETE - Eliminar el segundo ciudadano de cada ciudad
            em.getTransaction().begin();
            refCiutat1.getCiutadans().remove(refCiutada2);
            em.merge(refCiutat1);

            refCiutat2.getCiutadans().remove(refCiutada4);
            em.merge(refCiutat2);

            refCiutat3.getCiutadans().remove(refCiutada6);
            em.merge(refCiutat3);

            em.getTransaction().commit();

            // DELETE - Eliminar la segunda ciudad
            em.getTransaction().begin();
            refCiutat2 = em.find(Ciutat.class, refCiutat2.getCiutatId());
            if (refCiutat2 != null) {
                em.remove(refCiutat2);
            }
            em.getTransaction().commit();

            // READ - Mostrar estado después de eliminar ciudadanos y ciudad
            em.getTransaction().begin();
            System.out.println("Punt 2: Després d'esborrar ciutadans i ciutat");
            Manager.printAll(Ciutat.class, em);
            Manager.printAll(Ciutada.class, em);
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    
}
