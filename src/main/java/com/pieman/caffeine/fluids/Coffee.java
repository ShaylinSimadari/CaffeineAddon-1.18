package com.pieman.caffeine.fluids;

import java.util.Locale;

public enum Coffee {
    COFFEE(0xFF210B00),
    SWEET_COFFEE(0xFF210B00);

    private final String id;
    private final int color;

    Coffee(int color) {
        this.id = this.name().toLowerCase(Locale.ROOT);
        this.color = color;
    }

    public String getId() {
        return this.id;
    }

    public int getColor() {
        return this.color;
    }
}
