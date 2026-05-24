package com.giftkeeper.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class JpaPersistence {
    private JpaPersistence() {
    }

    public static EntityManagerFactory createDefaultFactory() {
        return Persistence.createEntityManagerFactory("giftkeeper-pu");
    }

    public static EntityManagerFactory createFactory(final String jdbcUrl, final String username, final String password) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", jdbcUrl);
        properties.put("jakarta.persistence.jdbc.user", username);
        properties.put("jakarta.persistence.jdbc.password", password);
        properties.put("hibernate.hbm2ddl.auto", "update");
        return Persistence.createEntityManagerFactory("giftkeeper-pu", properties);
    }

    public static EntityManager createEntityManager(final String jdbcUrl, final String username, final String password) {
        return createFactory(jdbcUrl, username, password).createEntityManager();
    }
}
