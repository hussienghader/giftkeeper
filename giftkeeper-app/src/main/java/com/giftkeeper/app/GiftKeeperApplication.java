package com.giftkeeper.app;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class GiftKeeperApplication {
    private GiftKeeperApplication() {
    }

    public static Injector createJpaInjector() {
        final String jdbcUrl = System.getProperty("giftkeeper.jdbc.url", "jdbc:postgresql://localhost:5432/giftkeeper");
        final String username = System.getProperty("giftkeeper.jdbc.user", "giftkeeper");
        final String password = System.getProperty("giftkeeper.jdbc.password", "giftkeeper");
        return Guice.createInjector(new GiftKeeperJpaModule(jdbcUrl, username, password));
    }

    public static Injector createInMemoryInjector() {
        return Guice.createInjector(new GiftKeeperInMemoryModule());
    }
}
