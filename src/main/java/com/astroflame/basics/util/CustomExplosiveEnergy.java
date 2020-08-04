package com.astroflame.basics.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomExplosiveEnergy extends EnergyStorage implements INBTSerializable<CompoundNBT> {
    public CustomExplosiveEnergy(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT explosives = new CompoundNBT();
        explosives.putInt("explosives", this.getEnergyStored());
        return explosives;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setEnergy(nbt.getInt("explosives"));
    }
}
