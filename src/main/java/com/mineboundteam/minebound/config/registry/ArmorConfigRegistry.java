package com.mineboundteam.minebound.config.registry;

import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.io.File;
import java.util.HashMap;

public class ArmorConfigRegistry extends ServerConfigRegistry implements IConfig{
    private static ServerConfigRegistry registry;
    public ArmorConfigRegistry(){
        this.configName = "MineBound" + File.separator + "Armor.toml";
        this.addConfig(this);
    }
    public static ServerConfigRegistry get(){
        if(registry == null){
            registry = new ArmorConfigRegistry();
        }
        return registry;
    }


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

    public static final ArmorConfig SYNERGY_SET = new ArmorConfig("Myrial Synergy Set Bonus", 100, 5, 0, 0, 0);
    public static final ArmorConfig SYNERGY_HELMET = new ArmorConfig("Myrial Synergy Helmet", 60, 2, 1000, 2, 0);
    public static final ArmorConfig SYNERGY_CHESTPLATE = new ArmorConfig("Myrial Synergy Chestplate", 100, 1, 1000, 1, 2);
    public static final ArmorConfig SYNERGY_LEGGINGS = new ArmorConfig("Myrial Synergy Leggings", 80, 1, 1000, 2, 1);
    public static final ArmorConfig SYNERGY_BOOTS = new ArmorConfig("Myrial Synergy Boots", 60, 1, 1000, 2, 0);

    public static final ArmorConfig SINGULARITY_SET = new ArmorConfig("Myrial Singularity Set Bonus", 250, 7, 0, 0, 0);
    public static final ArmorConfig SINGULARITY_HELMET = new ArmorConfig("Myrial Singularity Helmet", 120, 3, 1500, 2, 1);
    public static final ArmorConfig SINGULARITY_CHESTPLATE = new ArmorConfig("Myrial Singularity Chestplate", 200, 2, 1500, 2, 2);
    public static final ArmorConfig SINGULARITY_LEGGINGS = new ArmorConfig("Myrial Singularity Leggings", 150, 1, 1500, 3, 2);
    public static final ArmorConfig SINGULARITY_BOOTS = new ArmorConfig("Myrial Singularity Boots", 80, 2, 1500, 3, 1);

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
        SYNERGY_HELMET.build(builder);
        SYNERGY_CHESTPLATE.build(builder);
        SYNERGY_LEGGINGS.build(builder);
        SYNERGY_BOOTS.build(builder);
        SYNERGY_SET.build(builder);
        builder.pop();
        SET_BONUS_MAP.put(ArmorTier.SYNERGY, SYNERGY_SET);

        builder.push("Myrial Singularity (Tier 4)");
        SINGULARITY_HELMET.build(builder);
        SINGULARITY_CHESTPLATE.build(builder);
        SINGULARITY_LEGGINGS.build(builder);
        SINGULARITY_BOOTS.build(builder);
        SINGULARITY_SET.build(builder);
        builder.pop();
        SET_BONUS_MAP.put(ArmorTier.SINGULARITY, SINGULARITY_SET);
    }

    @Override
    public void refresh(ModConfigEvent event) {

    }
}
