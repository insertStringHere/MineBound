package com.mineboundteam.minebound.magic;

public enum SpellLevel {
    Level1(1),
    Level2(2),
    Level3(3),
    Level4(4);

    private int value;

    private SpellLevel(int val){
        value = val; 
    }

    public int getValue(){
        return value;
    }
}
