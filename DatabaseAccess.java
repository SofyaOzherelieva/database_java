package ru.mipt.java2017.hw3;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DatabaseAccess {

  // Java Persistence API
  public EntityManager entityManager;
  private final EntityManagerFactory entityManagerFactory;

  DatabaseAccess(String connectionUrl) {

    Map<String, String> map = new HashMap<>();
    map.put("hibernate.connection.url", connectionUrl);
    entityManagerFactory = Persistence.createEntityManagerFactory("Books", map);
    entityManager = entityManagerFactory.createEntityManager();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      entityManagerFactory.close();
    }));
  }

  public void shutdown() {
    entityManagerFactory.close();
  }

}
