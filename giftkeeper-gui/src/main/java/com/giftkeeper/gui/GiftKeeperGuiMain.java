package com.giftkeeper.gui;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;

public final class GiftKeeperGuiMain {
    private GiftKeeperGuiMain() {
    }

    public static void main(final String[] args) {
        final GiftKeeperUseCases service = GiftKeeperApplication.createJpaInjector().getInstance(GiftKeeperUseCases.class);
        GiftKeeperFrame.showFrame(service);
    }
}
