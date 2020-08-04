package com.astroflame.basics.util;

import com.astroflame.basics.basics;
import com.astroflame.basics.blocks.*;
import com.astroflame.basics.containers.ExplodingBlockContainer;
import com.astroflame.basics.entity.*;
import com.astroflame.basics.entity.StuckHookEntity;
import com.astroflame.basics.items.BasicBomb;
import com.astroflame.basics.items.Hook;
import com.astroflame.basics.items.BombEgg;
import com.astroflame.basics.tabs.basictab;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, basics.MOD_ID);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, basics.MOD_ID);

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, basics.MOD_ID);

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, basics.MOD_ID);

    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, basics.MOD_ID);

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, basics.MOD_ID);

    public static final Item.Properties BASIC_TAB = new Item.Properties().group(basictab.TAB);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITY.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITY.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Items
    public static final RegistryObject<Item> BOMB_EGG = ITEMS.register("bomb_egg", BombEgg::new);
    public static final RegistryObject<Item> BOMB = ITEMS.register("basic_bomb", BasicBomb::new);
    public static final RegistryObject<Item> HOOK = ITEMS.register("hook", Hook::new);

    // Blocks
    public static final RegistryObject<Block> EXPLODING_BLOCK = BLOCKS.register("exploding_block", ExplodingBlock::new);

    // Block Items
    public static final RegistryObject<Item> EXPLODING_BLOCK_ITEM = ITEMS.register("exploding_block",
            () -> new BlockItem(EXPLODING_BLOCK.get(), BASIC_TAB));

    // Tile Entities
    public static final RegistryObject<TileEntityType<ExplodingBlockTile>> EXPLODING_TILE_ENTITY = TILE_ENTITY.register
            ("exploding_block", () -> TileEntityType.Builder.create(ExplodingBlockTile::new, EXPLODING_BLOCK.get()).build(null));

    //Containers
    public static final RegistryObject<ContainerType<ExplodingBlockContainer>> EXPLODING_BLOCK_CONTAINER = CONTAINERS.register("exploding_block",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                World world = inv.player.getEntityWorld();
                return new ExplodingBlockContainer(windowId, inv, data.readBlockPos(), world);
    }));

    // Entities
    public static final RegistryObject<EntityType<BomberEntity>> BOMBER_ENTITY = ENTITY.register("bomber",
            () -> EntityType.Builder.create(BomberEntity::new, EntityClassification.MONSTER)
                        .size(1F, 1F)
                        .setShouldReceiveVelocityUpdates(false)
                        .build("bomber")
    );

    public static final RegistryObject<EntityType<BasicBombEntity>> BOMB_ENTITY = ENTITY.register("basic_bomb",
            () -> EntityType.Builder.<BasicBombEntity>create(BasicBombEntity::new, EntityClassification.MISC)
                        .size(0.5F, 0.5F)
                        .build("basic_bomb")
    );

    public static final RegistryObject<EntityType<PoisonBombEntity>> POISON_BOMB_ENTITY = ENTITY.register("poison_bomb",
            () -> EntityType.Builder.<PoisonBombEntity>create(PoisonBombEntity::new, EntityClassification.MISC)
                    .size(0.5F, 0.5F)
                    .build("poison_bomb")
    );

    public static final RegistryObject<EntityType<HookEntity>> HOOK_ENTITY = ENTITY.register("hook",
            () -> EntityType.Builder.create(HookEntity::new, EntityClassification.MISC)
                    .size(0.5F, 0.5F)
                    .build("hook"));

    public static final RegistryObject<EntityType<StuckHookEntity>> STUCK_HOOK_ENTITY = ENTITY.register("stuck_hook",
            () -> EntityType.Builder.<StuckHookEntity>create(StuckHookEntity::new, EntityClassification.MISC)
                    .size(0.5F, 0.5F)
                    .build("stuck_hook"));

    //Particles
    public static final RegistryObject<BasicParticleType> POISON_BOMB_PARTICLE = PARTICLES.register("poison_gas",
            () -> new BasicParticleType(true)
    );
}
