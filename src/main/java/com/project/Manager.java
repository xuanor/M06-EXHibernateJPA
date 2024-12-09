package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Manager {

    private static EntityManagerFactory entityManagerFactory;

    // Crear EntityManagerFactory con una unidad de persistencia
    public static void createEntityManagerFactory(String persistenceUnitName) {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        }
    }

    // Cerrar EntityManagerFactory
    public static void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    // Agregar una nueva ciudad (Ciutat)
    public static Ciutat addCiutat(String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Ciutat ciutat = new Ciutat(nom, pais, poblacio, ciutadans);
        try {
            transaction.begin();
            entityManager.persist(ciutat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
            ciutat = null;
        } finally {
            entityManager.close();
        }
        return ciutat;
    }

    // Agregar un nuevo ciudadano (Ciutada)
    public static Ciutada addCiutada(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Ciutada ciutada = new Ciutada(name);
        try {
            transaction.begin();
            entityManager.persist(ciutada);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
            ciutada = null;
        } finally {
            entityManager.close();
        }
        return ciutada;
    }

    // Actualizar un ciudadano (Ciutada)
    public static void updateCiutada(long ciutadaId, String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Ciutada ciutada = entityManager.find(Ciutada.class, ciutadaId);
            if (ciutada != null) {
                ciutada.setName(name);
                entityManager.merge(ciutada);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Actualizar una ciudad (Ciutat)
    public static void updateCiutat(long ciutatId, String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Ciutat ciutat = entityManager.find(Ciutat.class, ciutatId);
            if (ciutat != null) {
                ciutat.setNom(nom);
                ciutat.setPais(pais);
                ciutat.setPoblacio(poblacio);
                ciutat.setCiutadans(ciutadans);
                entityManager.merge(ciutat);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Obtener una ciudad con sus ciudadanos
    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Ciutat ciutat = null;
        try {
            ciutat = entityManager.find(Ciutat.class, ciutatId);
            if (ciutat != null) {
                ciutat.getCiutadans().size(); // Forzar la carga de ciudadanos
            }
        } finally {
            entityManager.close();
        }
        return ciutat;
    }

    // Obtener un objeto genérico por ID
    public static <T> T getById(Class<T> clazz, long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        T obj = null;
        try {
            obj = entityManager.find(clazz, id);
        } finally {
            entityManager.close();
        }
        return obj;
    }

    // Eliminar un objeto por su ID
    public static <T> void delete(Class<T> clazz, Serializable id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T obj = entityManager.find(clazz, id);
            if (obj != null) {
                entityManager.remove(obj);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Listar objetos de una clase
    public static <T> List<T> listCollection(Class<T> clazz) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<T> resultList = null;
        try {
            TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz);
            resultList = query.getResultList();
        } finally {
            entityManager.close();
        }
        return resultList;
    }

    public static <T> void printAll(Class<T> clazz, EntityManager em) {
        em.createQuery("FROM " + clazz.getSimpleName(), clazz)
          .getResultList()
          .forEach(System.out::println);
    }
}
