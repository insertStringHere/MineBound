package com.mineboundteam.minebound.magic;

public enum SpellType {
    OFFENSIVE("offensive"),
    DEFENSIVE("defensive"),
    UTILITY("utility");

    private final String name;

    SpellType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
