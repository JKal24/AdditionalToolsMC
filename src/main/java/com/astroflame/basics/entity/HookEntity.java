package com.astroflame.basics.entity;

import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SMountEntityPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class HookEntity extends ProjectileItemEntity {

    public Entity leashHolder;
    public CompoundNBT leashTag;

    public HookEntity(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected Item getDefaultItem() {
        return RegistryHandler.HOOK.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {
            this.updateLeashedState();
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("Leash", 10)) {
            this.leashTag = compound.getCompound("Leash");
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getLeashHolder() != null) {
            CompoundNBT compoundNBT = new CompoundNBT();
            UUID uuid = this.getLeashHolder().getUniqueID();
            compoundNBT.putUniqueId("UUID", uuid);
            compound.put("leash", compoundNBT);
        } else if (this.leashTag != null) {
            compound.put("Leash", this.leashTag.copy());
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote) {
            if (result.getType() == RayTraceResult.Type.BLOCK) {
                this.setNoGravity(true);
                Vec3d pos = result.getHitVec();
                this.setPosition(pos.x, pos.y, pos.z);
            }
        }
    }

    public final boolean processInitialInteract(PlayerEntity player, Hand hand) {
        if (!this.isAlive()) {
            return false;
        } else if (this.getLeashHolder() == player) {
            this.clearLeashed(true, !player.abilities.isCreativeMode);
            return true;
        } else {
            ItemStack itemstack = player.getHeldItem(hand);
            if (itemstack.getItem() == Items.LEAD && this.getLeashHolder() != null) {
                this.setLeashHolder(player);
                itemstack.shrink(1);
                return true;
            } else {
                return false;
            }
        }
    }

    public void setLeashHolder(Entity entity) {
        this.leashHolder = entity;
    }

    public Entity getLeashHolder() {
        return this.leashHolder;
    }

    protected void updateLeashedState() {
        if (this.leashTag != null) {
            this.recreateLeash();
        }

        if (this.leashHolder != null) {
            if (!this.isAlive() || !this.leashHolder.isAlive()) {
                this.clearLeashed(true, true);
            }
        }
    }

    private void recreateLeash() {
        if (this.leashTag != null && this.world instanceof ServerWorld) {
            if (this.leashTag.hasUniqueId("UUID")) {
                UUID uuid = this.leashTag.getUniqueId("UUID");
                Entity entity = ((ServerWorld)this.world).getEntityByUuid(uuid);
                if (entity != null) {
                    this.setLeashHolder(entity);
                }
            } else {
                this.clearLeashed(false, true);
            }
            this.leashTag = null;
        }
    }

    public void clearLeashed(boolean sendPacket, boolean dropLead) {
        if (this.leashHolder != null) {
            this.forceSpawn = false;
            if (!(this.leashHolder instanceof PlayerEntity)) {
                this.leashHolder.forceSpawn = false;
            }

            this.leashHolder = null;
            if (!this.world.isRemote && dropLead) {
                this.entityDropItem(Items.LEAD);
            }

            if (!this.world.isRemote && sendPacket && this.world instanceof ServerWorld) {
                ((ServerWorld)this.world).getChunkProvider().sendToAllTracking(this, new SMountEntityPacket(this, (Entity)null));
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
