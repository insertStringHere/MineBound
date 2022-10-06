package com.mason.minebound;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("minebound")
public class MineBound {
    public MineBound() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
