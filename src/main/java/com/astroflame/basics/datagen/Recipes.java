package com.astroflame.basics.datagen;

import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator datagen) {
        super(datagen);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> cons) {
        ShapedRecipeBuilder.shapedRecipe(RegistryHandler.EXPLODING_BLOCK.get())
                .patternLine("xxx")
                .patternLine("x x")
                .patternLine("xxx")
                .key('x', Blocks.IRON_BLOCK)
                .setGroup("basics")
                .addCriterion("iron_ore", InventoryChangeTrigger.Instance.forItems(Blocks.IRON_ORE))
                .build(cons);
    }
}
