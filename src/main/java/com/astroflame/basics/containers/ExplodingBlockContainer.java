package com.astroflame.basics.containers;

import com.astroflame.basics.basics;
import com.astroflame.basics.util.CustomExplosiveEnergy;
import com.astroflame.basics.util.ExplosionStatus;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ExplodingBlockContainer extends Container {

    private TileEntity myTile;
    private IItemHandler inv;

    public ExplodingBlockContainer(int id, PlayerInventory inv, BlockPos pos, World world) {
        super(RegistryHandler.EXPLODING_BLOCK_CONTAINER.get(), id);
        myTile = world.getTileEntity(pos);
        this.inv = new InvWrapper(inv);

        if (myTile != null) {
            myTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
                    h -> {
                        // Initialize input slots
                        addSlot(new SlotItemHandler(h, 0, 44, 18));
                        addSlot(new SlotItemHandler(h, 1, 122, 18));
                    }
            );
        }
        createPlayerInventory(18, 39);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(myTile.getWorld(), myTile.getPos())
                , basics.proxy.getClientPlayer(), RegistryHandler.EXPLODING_BLOCK.get());
    }

    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();
            if (index == 0 || index == 1) {
                if (!this.mergeItemStack(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemStack);
            } else {
                if (stack.getItem() == Items.COBBLESTONE) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.getItem() == Items.COAL) {
                    if (!this.mergeItemStack(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 28) {
                    if (!this.mergeItemStack(stack,28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37 && !this.mergeItemStack(stack, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }
        return itemStack;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amt, int dx) {
        for (int i = 0; i < amt; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horizAmt, int dx, int verticAmt, int dy) {
        for (int j = 0; j < verticAmt; j++) {
            index = addSlotRange(handler, index, x, y, horizAmt, dx);
            y += dy;
        }
        return index;
    }

    private void createPlayerInventory(int leftCol, int topRow) {
        // Initialize the slots on the player inventory in the GUI
        addSlotBox(inv, 9, leftCol, topRow, 9, 16, 3, 16);

        // Another initialization for the quick inventory bar
        topRow += 53;
        addSlotRange(inv, 0, leftCol, topRow, 9, 16);
    }

}
