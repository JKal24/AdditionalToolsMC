package com.astroflame.basics.entity;

import com.astroflame.basics.effects.PoisonEffect;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class PoisonGasEntity extends AreaEffectCloudEntity {

    public PoisonGasEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public void init(LivingEntity shooter) {
        this.setOwner(shooter);
        this.setParticleData(RegistryHandler.POISON_BOMB_PARTICLE.get());
        this.setRadius(2F);
        this.setDuration(40);
        this.setRadiusPerTick((6F - this.getRadius()) / (float)this.getDuration());
        this.addEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 1, 1));
    }

}
