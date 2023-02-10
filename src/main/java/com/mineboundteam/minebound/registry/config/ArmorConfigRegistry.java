package com.mineboundteam.minebound.registry.config;

import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.HashMap;

public class ArmorConfigRegistry implements IConfig{
    public static final ArmorConfig EFFIGY_SET = new ArmorConfig("Myrial Effigy Set Bonus", 20, 1, 0, 0, 0);
    public static final ArmorConfig EFFIGY_HELMET = new ArmorConfig("Myrial Effigy Helmet", 20, 1, 200, 0, 0);
    public static final ArmorConfig EFFIGY_CHESTPLATE = new ArmorConfig("Myrial Effigy Chestplate", 20, 0, 200, 0, 0);
    public static final ArmorConfig EFFIGY_LEGGINGS = new ArmorConfig("Myrial Effigy Leggings", 20, 0, 200, 1, 0);
    public static final ArmorConfig EFFIGY_BOOTS = new ArmorConfig("Myrial Effigy Boots", 20, 0, 200, 1, 0);

    public static final ArmorConfig SUIT_SET = new ArmorConfig("Myrial Suit Set Bonus", 40, 3, 0, 0, 0);
    public static final ArmorConfig SUIT_HELMET = new ArmorConfig("Myrial Suit Helmet", 30, 1, 400, 1, 0);
    public static final ArmorConfig SUIT_CHESTPLATE = new ArmorConfig("Myrial Suit Chestplate", 60, 0, 400, 0, 1);
    public static final ArmorConfig SUIT_LEGGINGS = new ArmorConfig("Myrial Suit Leggings", 50, 0, 400, 2, 0);
    public static final ArmorConfig SUIT_BOOTS = new ArmorConfig("Myrial Suit Boots", 20, 1, 400, 1, 0);

    public static HashMap<ArmorTier, ArmorConfig> SET_BONUS_MAP = new HashMap<>();

    @Override
    public void build(Builder builder) {
        builder.push("Myrial Effigy (Tier 1)");
        EFFIGY_HELMET.build(builder);
        EFFIGY_CHESTPLATE.build(builder);
        EFFIGY_LEGGINGS.build(builder);
        EFFIGY_BOOTS.build(builder);
        EFFIGY_SET.build(builder);
        builder.pop();
        SET_BONUS_MAP.put(ArmorTier.EFFIGY, EFFIGY_SET);
        
        builder.push("Myrial Suit (Tier 2)");
        SUIT_HELMET.build(builder);
        SUIT_CHESTPLATE.build(builder);
        SUIT_LEGGINGS.build(builder);
        SUIT_BOOTS.build(builder);
        SUIT_SET.build(builder);
        builder.pop();
        SET_BONUS_MAP.put(ArmorTier.SUIT, SUIT_SET);
        
        builder.push("Myrial Synergy (Tier 3)");
        builder.pop();
        
        builder.push("Myrial Singularity (Tier 4)");
        builder.pop();
    }

    @Override
    public void refresh(ModConfigEvent event) {

    }
}
