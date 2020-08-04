package com.astroflame.basics.util;

import com.astroflame.basics.entity.HookEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.item.LeashKnotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.LeadItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomLeash extends LeadItem {
    public CustomLeash(Properties builder) {
        super(builder);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        Block block = world.getBlockState(blockpos).getBlock();
        if (block.isIn(BlockTags.FENCES)) {
            PlayerEntity playerentity = context.getPlayer();
            if (!world.isRemote && playerentity != null) {
                bindPlayerMobs(playerentity, world, blockpos);
            }

            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    public static ActionResultType bindPlayerMobs(PlayerEntity playerIn, World worldIn, BlockPos pos) {
        LeashKnotEntity leashknotentity = null;
        boolean flag = false;
        double d0 = 7.0D;
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for(HookEntity hook : worldIn.getEntitiesWithinAABB(HookEntity.class, new AxisAlignedBB((double)i - 7.0D, (double)j - 7.0D, (double)k - 7.0D, (double)i + 7.0D, (double)j + 7.0D, (double)k + 7.0D))) {
            if (hook.getLeashHolder() == playerIn) {
                if (leashknotentity == null) {
                    leashknotentity = LeashKnotEntity.create(worldIn, pos);
                }

                hook.setLeashHolder(leashknotentity);
                flag = true;
            }
        }

        return flag ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }
}
