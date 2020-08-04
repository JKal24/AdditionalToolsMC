package com.astroflame.basics.datagen;

import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableGeneration {
    public LootTables(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addTables() {
        lootTables.put(RegistryHandler.EXPLODING_BLOCK.get(), createStandardTable("exploding_pool", RegistryHandler.EXPLODING_BLOCK.get()));
    }
}
