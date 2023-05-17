package com.mineboundteam.minebound.capabilities.network;

import java.util.function.Supplier;

import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider.UtilityToggle;
import com.mineboundteam.minebound.magic.UtilitySpells.LightUtilitySpell;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class UtilitySync {
    private int utilityFlags = 0;

    public UtilitySync(boolean fire, boolean earth, boolean light, boolean ender){
        if(fire)
            utilityFlags |= 1;
        if(earth)
            utilityFlags |= 2;
        if(light)
            utilityFlags |= 4;
        if(ender)
            utilityFlags |= 8; 
    }

    public boolean fire(){
        return fire(utilityFlags);
    }
    public boolean earth(){
        return earth(utilityFlags);
    }
    public boolean light(){
        return light(utilityFlags); 
    }
    public boolean ender(){
        return ender(utilityFlags);
    }
    protected static boolean fire(int utilityFlags){
        return 0 != (utilityFlags & 1); 
    }
    protected static boolean earth(int utilityFlags){
        return 0 != (utilityFlags & 2); 
    }
    protected static boolean light(int utilityFlags){
        return 0 != (utilityFlags & 4); 
    }
    protected static boolean ender(int utilityFlags){
        return 0 != (utilityFlags & 8); 
    }

    public static void encode(UtilitySync msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.utilityFlags);
    }

    public static UtilitySync decode(FriendlyByteBuf buf) {
        int utilityFlags = buf.readInt();
        return new UtilitySync(fire(utilityFlags), earth(utilityFlags), light(utilityFlags), ender(utilityFlags));
    }

    
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("resource")
    public static void handleToggles(UtilitySync msg, Supplier<NetworkEvent.Context> ctx) {
        UtilityToggle toggles = Minecraft.getInstance().player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).orElse(null);
        if (toggles == null) return;

        toggles.fire = msg.fire();
        toggles.earth = msg.earth();
        LightUtilitySpell.active = toggles.light = msg.light();
        toggles.ender = msg.ender();
    }
}