package com.astroflame.basics.entity.renderers;

import com.astroflame.basics.basics;
import com.astroflame.basics.entity.BomberEntity;
import com.astroflame.basics.entity.models.BomberModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BomberRenderer extends MobRenderer<BomberEntity, BomberModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(basics.MOD_ID, "textures/entity/virus.png");

    public BomberRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BomberModel(), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(BomberEntity entity) {
        return TEXTURE;
    }
}
