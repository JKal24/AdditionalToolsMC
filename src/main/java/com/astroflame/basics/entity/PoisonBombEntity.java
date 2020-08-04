package com.astroflame.basics.entity;


import java.util.List;

import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class PoisonBombEntity extends ThrowableEntity {

    public PoisonBombEntity(EntityType<? extends PoisonBombEntity> poisonBombEntityEntityType, World world) {
        super(poisonBombEntityEntityType, world);
    }

    protected PoisonBombEntity(EntityType<? extends ThrowableEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK || result.getType() == RayTraceResult.Type.ENTITY) {
            if (!this.world.isRemote) {
                List<LivingEntity> targetsHit = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(4D,2D,4D));
                PoisonGasEntity poisonGas = new PoisonGasEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
                poisonGas.init(this.owner);
                poisonGas.setParticleData(RegistryHandler.POISON_BOMB_PARTICLE.get());
                if (!targetsHit.isEmpty()) {
                    for(LivingEntity livingentity : targetsHit) {
                        double d0 = this.getDistanceSq(livingentity);
                        if (d0 < 16.0D) {
                            poisonGas.setPosition(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                            break;
                        }
                    }
                }
                this.world.addEntity(poisonGas);
                this.remove();
            }
        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
