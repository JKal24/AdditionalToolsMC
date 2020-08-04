package com.astroflame.basics.tabs;

import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class basictab {

    public static final ItemGroup TAB = new ItemGroup("basic_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.EXPLODING_BLOCK.get());
        }
    };

}
