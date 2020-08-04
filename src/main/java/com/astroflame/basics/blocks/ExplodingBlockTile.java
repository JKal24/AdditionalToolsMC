package com.astroflame.basics.blocks;

import com.astroflame.basics.containers.ExplodingBlockContainer;
import com.astroflame.basics.setup.Config;
import com.astroflame.basics.util.CustomExplosiveEnergy;
import com.astroflame.basics.util.ExplosionStatus;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ExplodingBlockTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private ItemStackHandler itemHandler = createHandler();
    private CustomExplosiveEnergy explosive_energy = createEnergy();


    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> explosive_energy);

    public ExplodingBlockTile() {
        super(RegistryHandler.EXPLODING_TILE_ENTITY.get());
    }

    @Override
    public void read(CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        explosive_energy.deserializeNBT(nbt.getCompound("explosives"));
        super.read(nbt);
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("inv", itemHandler.serializeNBT());
        nbt.put("explosives", explosive_energy.serializeNBT());
        return super.write(nbt);
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            return;
        }
        handler.ifPresent(h -> {
            if (h.isItemValid(Config.COBBLE_SLOT.get(), h.getStackInSlot(Config.COBBLE_SLOT.get())) &&
                    h.isItemValid(Config.COAL_SLOT.get(),h.getStackInSlot(Config.COAL_SLOT.get()))) {

                energy.ifPresent(e -> {
                    if (e.getEnergyStored() == 0) {
                        fillEnergy();
                    }
                    e.extractEnergy(Config.EXPLOSION_BLOCK_MAX_TRANSFER.get(), Config.FALSE_SIMULATION.get());
                    if (e.getEnergyStored() % 50 == 0) {
                        // Decrease count by 1
                        h.extractItem(Config.COBBLE_SLOT.get(), 1, Config.FALSE_SIMULATION.get());
                        h.extractItem(Config.COAL_SLOT.get(),1, Config.FALSE_SIMULATION.get());
                        ExplosionStatus.setStatus(e.getEnergyStored());
                    }

                    // When count reaches 0, it blows up
                    if (e.getEnergyStored() == 0) {
                        world.createExplosion(null, (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), Config.EXPLOSION_RADIUS.get().floatValue(), Explosion.Mode.NONE);
                        e.extractEnergy(Config.EXPLOSION_BLOCK_CAPACITY.get(), false);
                    }
                });
                markDirty();
            }
        });
        BlockState state = world.getBlockState(pos);
        if (state.get(BlockStateProperties.TRIGGERED) != ExplosionStatus.isHazardous()) {
            world.setBlockState(pos, state.with(BlockStateProperties.TRIGGERED, ExplosionStatus.isHazardous()),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    private void fillEnergy() {
        explosive_energy.setEnergy(Config.EXPLOSION_BLOCK_CAPACITY.get());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch(slot) {
                    case 0:
                        return stack.getItem() == Items.COBBLESTONE;
                    case 1:
                        return stack.getItem() == Items.COAL;
                }
                return false;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    private CustomExplosiveEnergy createEnergy() {
        return new CustomExplosiveEnergy(Config.EXPLOSION_BLOCK_CAPACITY.get(), Config.EXPLOSION_BLOCK_MAX_TRANSFER.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        } else if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(RegistryHandler.EXPLODING_TILE_ENTITY.get().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory inv, PlayerEntity player) {
        return new ExplodingBlockContainer(i, inv, this.getPos(), this.getWorld());
    }
}
