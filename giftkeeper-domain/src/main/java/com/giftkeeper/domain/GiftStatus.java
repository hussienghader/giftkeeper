package com.giftkeeper.domain;

public enum GiftStatus {
    PLANNED,
    BOUGHT,
    GIFTED;

    public boolean canTransitionTo(final GiftStatus nextStatus) {
        return switch (this) {
            case PLANNED -> nextStatus == BOUGHT || nextStatus == GIFTED;
            case BOUGHT -> nextStatus == GIFTED;
            case GIFTED -> false;
        };
    }
}
