package com.mineboundteam.minebound.magic;

public enum MagicType {
    FIRE("fire"),
    TELEKINETIC("telekinetic"),
    SHIELD("shield"),
    EARTH("earth"),
    ENDER("ender"),
    ELECTRIC("electric"),
    LIGHT("light"),
    NECROTIC("necrotic");

    private final String name;

    MagicType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
