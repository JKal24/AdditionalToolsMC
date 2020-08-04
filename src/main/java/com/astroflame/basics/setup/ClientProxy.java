package com.astroflame.basics.setup;

import com.astroflame.basics.containers.ExplodingBlockScreen;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ScreenManager.registerFactory(RegistryHandler.EXPLODING_BLOCK_CONTAINER.get(),
                ExplodingBlockScreen::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

}
