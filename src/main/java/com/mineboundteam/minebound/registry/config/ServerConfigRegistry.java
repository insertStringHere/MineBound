package com.mineboundteam.minebound.registry.config;

import java.util.ArrayList;

import com.mineboundteam.minebound.config.IConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * Contains the server registry file generation  
 * <br></br>
 * Used https://github.com/CoFH/CoFHCore/blob/1.18.2/src/main/java/cofh/core/config/ConfigManager.java for reference
 */
public class ServerConfigRegistry {
    private static ServerConfigRegistry registry;

    private ArrayList<IConfig> configList;
    private ForgeConfigSpec.Builder builder; 

    private ServerConfigRegistry(){  
        builder = new ForgeConfigSpec.Builder();
        configList = new ArrayList<>();
    }

    /**
     * Returns a static {@link ServerConfigRegistry} and creates one if
     * there wasn't
     * @return a ServerConfigRegistry
     */
    public static ServerConfigRegistry get(){
        if(registry == null){
            registry = new ServerConfigRegistry();
        }
        
        return registry;
    }

    /**
     * Adds a new config to be built within this registry
     * @param config - an {@link IConfig} to be added to the list.
     * @return this {@link ServerConfigRegistry} for method chaining
     */
    public ServerConfigRegistry addConfig(IConfig config){
        if(!configList.contains(config))
            configList.add(config);
        return this;
    }

    /**
     * Registers all {@link IConfig} objects stored in this object
     * and completes the config build
     * @param eventBus
     */
    public void register(IEventBus eventBus){    
        eventBus.register(registry);

        for(IConfig c : configList)
            c.build(builder);

        ModLoadingContext.get().registerConfig(Type.SERVER, builder.build());
    }

    /**
     * Refreshes all {@link IConfig} registered with this object
     * @param event - the {@link ModConfigEvent} being signaled
     */
    @SubscribeEvent
    public void ConfigRefresh(ModConfigEvent event){
        if(event.getConfig().getType() == Type.SERVER)
            for(IConfig c : configList)
                c.refresh(event);
    }
}
