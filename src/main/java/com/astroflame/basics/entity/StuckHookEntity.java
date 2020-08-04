package com.astroflame.basics.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class StuckHookEntity extends HangingEntity {

    public StuckHookEntity(EntityType<? extends HangingEntity> type, World world, BlockPos pos, Direction facing) {
        super(type, world, pos);
        this.updateFacingWithBoundingBox(facing);
    }

    public StuckHookEntity(EntityType<? extends HangingEntity> type, World p_i48561_2_) {
        this(type, p_i48561_2_, null, null);
    }

    @Override
    public int getWidthPixels() {
        return 1;
    }

    @Override
    public int getHeightPixels() {
        return 1;
    }

    @Override
    public void onBroken(@Nullable Entity brokenEntity) {

    }

    @Override
    public void playPlaceSound() {
        world.playSound(this.hangingPosition.getX(), this.hangingPosition.getY(), this.hangingPosition.getZ(), SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.NEUTRAL, 2F, 1F, false);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
