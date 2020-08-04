package com.astroflame.basics.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {

    public static final String SUBCATEGORY_EXPLOSION_BLOCK = "explosion_block";

    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue EXPLOSION_BLOCK_CAPACITY;
    public static ForgeConfigSpec.IntValue EXPLOSION_BLOCK_MAX_TRANSFER;
    public static ForgeConfigSpec.DoubleValue EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue COBBLE_SLOT;
    public static ForgeConfigSpec.IntValue COAL_SLOT;
    public static ForgeConfigSpec.BooleanValue FALSE_SIMULATION;

    public static ForgeConfigSpec.IntValue TIME_UNTIL_EXPLOSION;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        setupExplosionBlockConfig(SERVER_BUILDER);

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupExplosionBlockConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("ExplosionBlock Settings").push(SUBCATEGORY_EXPLOSION_BLOCK);

        EXPLOSION_BLOCK_CAPACITY = SERVER_BUILDER.comment("Maximum capacity for fuel in Explosion Block")
                .defineInRange("capacity", 1000, 0, 1000);

        EXPLOSION_BLOCK_MAX_TRANSFER = SERVER_BUILDER.comment("Maximum transfer limit for fuel")
                .defineInRange("max_transfer", 5, 0, 5);

        EXPLOSION_RADIUS = SERVER_BUILDER.comment("Explosion radius for the Explosion Block")
                .defineInRange("explosion_radius", 25F, 5F, 50F);

        COBBLE_SLOT = SERVER_BUILDER.comment("Slot in Explosion Block for Cobblestone")
                .defineInRange("cobblestone", 0, 0, 0);

        COAL_SLOT = SERVER_BUILDER.comment("Slot in Explosion Block for Coal")
                .defineInRange("coal", 1, 1, 1);

        FALSE_SIMULATION = SERVER_BUILDER.comment("Not a simulation")
                .define("simulate", false);

        TIME_UNTIL_EXPLOSION = SERVER_BUILDER.comment("Time until a hand-held bomb explodes")
                .defineInRange("basic_bomb_explosion", 30,30,30);
    }
}
