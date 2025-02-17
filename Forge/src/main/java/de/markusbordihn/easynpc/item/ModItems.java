/*
 * Copyright 2023 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.easynpc.item;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.block.EasyNPCSpawnerBlock;
import de.markusbordihn.easynpc.block.ModBlocks;
import de.markusbordihn.easynpc.entity.npc.Allay;
import de.markusbordihn.easynpc.entity.npc.Cat;
import de.markusbordihn.easynpc.entity.npc.Chicken;
import de.markusbordihn.easynpc.entity.npc.Fairy;
import de.markusbordihn.easynpc.entity.npc.Humanoid;
import de.markusbordihn.easynpc.entity.npc.HumanoidSlim;
import de.markusbordihn.easynpc.entity.npc.IronGolem;
import de.markusbordihn.easynpc.entity.npc.ModEntityType;
import de.markusbordihn.easynpc.entity.npc.Skeleton;
import de.markusbordihn.easynpc.entity.npc.Villager;
import de.markusbordihn.easynpc.entity.npc.Zombie;
import de.markusbordihn.easynpc.entity.npc.ZombieVillager;
import de.markusbordihn.easynpc.item.configuration.EasyNPCPresetEmptyItem;
import de.markusbordihn.easynpc.item.configuration.EasyNPCPresetItem;
import de.markusbordihn.easynpc.item.configuration.EasyNPCWandItem;
import de.markusbordihn.easynpc.item.configuration.MoveEasyNPCItem;
import de.markusbordihn.easynpc.tabs.EasyNPCTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

  public static final RegistryObject<Item> EASY_NPC_WAND =
      ITEMS.register(
          EasyNPCWandItem.ID,
          () -> new EasyNPCWandItem(new Item.Properties().tab(EasyNPCTab.TAB_CONFIG_ITEMS)));
  public static final RegistryObject<Item> MOVE_EASY_NPC =
      ITEMS.register(
          MoveEasyNPCItem.ID,
          () -> new MoveEasyNPCItem(new Item.Properties().tab(EasyNPCTab.TAB_CONFIG_ITEMS)));

  public static final RegistryObject<Item> EASY_NPC_PRESET_ITEM =
      ITEMS.register(EasyNPCPresetItem.NAME, () -> new EasyNPCPresetItem(new Item.Properties()));

  public static final RegistryObject<Item> EASY_NPC_PRESET_EMPTY_ITEM =
      ITEMS.register(
          EasyNPCPresetEmptyItem.NAME,
          () -> new EasyNPCPresetEmptyItem(new Item.Properties().tab(EasyNPCTab.TAB_CONFIG_ITEMS)));

  public static final RegistryObject<Item> EASY_NPC_SPAWNER =
      ITEMS.register(
          EasyNPCSpawnerBlock.NAME,
          () ->
              new BlockItem(
                  ModBlocks.EASY_NPC_SPAWNER.get(),
                  new Item.Properties().tab(EasyNPCTab.TAB_CONFIG_ITEMS)));

  private static final String SPAWN_EGG_PREFIX = "_spawn_egg";
  public static final RegistryObject<Item> ALLAY_NPC_SPAWN_EGG =
      ITEMS.register(
          Allay.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.ALLAY,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> CAT_NPC_SPAWN_EGG =
      ITEMS.register(
          Cat.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.CAT,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> CHICKEN_NPC_SPAWN_EGG =
      ITEMS.register(
          Chicken.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.CHICKEN,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> FAIRY_NPC_SPAWN_EGG =
      ITEMS.register(
          Fairy.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.FAIRY,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> HUMANOID_NPC_SPAWN_EGG =
      ITEMS.register(
          Humanoid.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.HUMANOID,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> HUMANOID_SLIM_NPC_SPAWN_EGG =
      ITEMS.register(
          HumanoidSlim.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.HUMANOID_SLIM,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> IRON_GOLEM_NPC_SPAWN_EGG =
      ITEMS.register(
          IronGolem.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.IRON_GOLEM,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> SKELETON_NPC_SPAWN_EGG =
      ITEMS.register(
          Skeleton.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.SKELETON,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> VILLAGER_NPC_SPAWN_EGG =
      ITEMS.register(
          Villager.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.VILLAGER,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> DROWNED_NPC_SPAWN_EGG =
      ITEMS.register(
          Zombie.ID_DROWNED + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.DROWNED,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> HUSK_NPC_SPAWN_EGG =
      ITEMS.register(
          Zombie.ID_HUSK + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.HUSK,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> WITHER_SKELETON_NPC_SPAWN_EGG =
      ITEMS.register(
          Skeleton.ID_WITHER_SKELETON + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.WITHER_SKELETON,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> STRAY_NPC_SPAWN_EGG =
      ITEMS.register(
          Skeleton.ID_STRAY + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.STRAY,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> ZOMBIE_NPC_SPAWN_EGG =
      ITEMS.register(
          Zombie.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.ZOMBIE,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));
  public static final RegistryObject<Item> ZOMBIE_VILLAGER_NPC_SPAWN_EGG =
      ITEMS.register(
          ZombieVillager.ID + SPAWN_EGG_PREFIX,
          () ->
              new EasyNPCSpawnEggItem(
                  ModEntityType.ZOMBIE_VILLAGER,
                  new Item.Properties().rarity(Rarity.EPIC).tab(EasyNPCTab.TAB_SPAWN_EGGS)));

  protected ModItems() {}
}
