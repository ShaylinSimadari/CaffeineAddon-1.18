package com.pieman.caffeine.fluids;

import java.util.Locale;

public enum Coffee {
    COFFEE(-3957193),
    SWEET_COFFEE(-5198286);

    private final String id;
    private final int color;

    private Coffee(int color) {
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
