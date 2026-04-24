package com.thousand_uncles.dashboard;

import jakarta.persistence.*;

public class MapRecordDAO {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");

    public void saveMap(MapRecord mapRecord) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(mapRecord);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void removeMapByName(String mapName) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            MapRecord removedMapRecord = em.find(MapRecord.class, mapName);
            em.remove(removedMapRecord);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public MapRecord getMapRecordById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(MapRecord.class, id);
        } finally {
            em.close();
        }
    }

    // Add other CRUD methods as necessary
}
