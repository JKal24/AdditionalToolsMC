package com.astroflame.basics.entity;

import com.astroflame.basics.setup.Config;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class BasicBombEntity extends ProjectileItemEntity {

    private boolean stoppedMotion = false;
    private int bounces = 3;

    public BasicBombEntity(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super(type, worldIn);
        this.noClip = true;
    }

    public BasicBombEntity(double x, double y, double z, World worldIn) {
        this(RegistryHandler.BOMB_ENTITY.get(), worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected Item getDefaultItem() {
        return RegistryHandler.BOMB.get();
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            Vec3d currentMotion = this.getMotion();
            this.setMotion(currentMotion.getX() * 0.1, 0.2, currentMotion.getZ() * 0.1);
            this.bounces--;
            if (bounces <= 0) {
                this.stoppedMotion = true;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.stoppedMotion) {
            Vec3d vec = this.getPositionVector();
            world.createExplosion(null, vec.x, vec.y, vec.z, 4F, Explosion.Mode.NONE);
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
