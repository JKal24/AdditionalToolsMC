package com.astroflame.basics.items;

import com.astroflame.basics.entity.BasicBombEntity;
import com.astroflame.basics.tabs.basictab;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BasicBomb extends Item {
    public BasicBomb() {
        super(new Item.Properties()
                .group(basictab.TAB)
                .setNoRepair()
        );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //Play a sound when bomb is triggered
        worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0.4F);
        ItemStack bombItem = playerIn.getHeldItem(handIn);

        if(!worldIn.isRemote) {
            BasicBombEntity bomb = new BasicBombEntity(playerIn.getPosX(), playerIn.getPosYEye(), playerIn.getPosZ(), worldIn);
            bomb.setItem(bombItem);
            bomb.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0F, 1.5F, 1F);
            worldIn.addEntity(bomb);
        }

        playerIn.addStat(Stats.ITEM_USED.get(this));
        if (!playerIn.abilities.isCreativeMode) {
            bombItem.shrink(1);
        }

        return ActionResult.resultSuccess(bombItem);
    }


}
