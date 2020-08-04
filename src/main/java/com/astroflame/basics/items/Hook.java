package com.astroflame.basics.items;

import com.astroflame.basics.entity.HookEntity;
import com.astroflame.basics.tabs.basictab;
import com.astroflame.basics.util.CustomLeash;
import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class Hook extends Item {

    public Hook() {
        super(new Item.Properties().group(basictab.TAB));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            ItemStack triggeredStack = playerIn.getHeldItem(handIn);
            HookEntity thrownHook = new HookEntity(RegistryHandler.HOOK_ENTITY.get(), worldIn);
            thrownHook.setItem(triggeredStack);

            CustomLeash leash = new CustomLeash(new Item.Properties());
            leash.onItemUse(new ItemUseContext(playerIn, playerIn.getActiveHand(),
                    new BlockRayTraceResult(thrownHook.getPositionVec(), thrownHook.getHorizontalFacing(), thrownHook.getPosition(), false)));

            thrownHook.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0F, 1.5F, 1F);
            worldIn.addEntity(thrownHook);

            playerIn.addStat(Stats.ITEM_USED.get(this));
            if (!playerIn.abilities.isCreativeMode) {
                triggeredStack.shrink(1);
            }

            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }
}
