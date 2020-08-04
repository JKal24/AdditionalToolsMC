package com.astroflame.basics.setup;

import com.astroflame.basics.basics;
import com.astroflame.basics.entity.renderers.BasicBombRenderer;
import com.astroflame.basics.effects.BombParticle;
import com.astroflame.basics.entity.renderers.HookRenderer;
import com.astroflame.basics.entity.renderers.PoisonBombRenderer;
import com.astroflame.basics.entity.renderers.BomberRenderer;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = basics.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.BOMB_ENTITY.get(), BasicBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.BOMBER_ENTITY.get(), BomberRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.POISON_BOMB_ENTITY.get(), PoisonBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.HOOK_ENTITY.get(), HookRenderer::new);
    }

    public static void onParticleRegistration(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(RegistryHandler.POISON_BOMB_PARTICLE.get(), BombParticle.Factory::new);
    }
}
