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

package de.markusbordihn.easynpc.client.screen.configuration.objective;

import de.markusbordihn.easynpc.client.screen.components.Checkbox;
import de.markusbordihn.easynpc.data.objective.ObjectiveData;
import de.markusbordihn.easynpc.data.objective.ObjectiveType;
import de.markusbordihn.easynpc.menu.configuration.objective.AttackObjectiveConfigurationMenu;
import de.markusbordihn.easynpc.network.NetworkMessageHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AttackObjectiveConfigurationScreen
    extends ObjectiveConfigurationScreen<AttackObjectiveConfigurationMenu> {

  // Attack Types
  protected Checkbox meleeAttackCheckbox;
  protected Checkbox zombieAttackCheckbox;
  protected Checkbox crossbowAttackCheckbox;
  protected Checkbox getBowAttackCheckbox;

  // Attack Target
  protected Checkbox attackAnimalCheckbox;
  protected Checkbox attackPlayerCheckbox;
  protected Checkbox attackMonsterCheckbox;
  protected Checkbox attackMobCheckbox;
  protected Checkbox attackMobWithoutCreeperCheckbox;
  protected Checkbox attackVillagerCheckbox;

  // Attack Entity by UUID
  protected Checkbox attackEntityByUUIDCheckbox;
  protected EditBox attackEntityByUUIDEditBox;
  protected Button attackEntityByUUIDSaveButton;

  public AttackObjectiveConfigurationScreen(
      AttackObjectiveConfigurationMenu menu, Inventory inventory, Component component) {
    super(menu, inventory, component);
  }

  @Override
  public void init() {
    super.init();

    // Default button stats
    this.attackObjectiveButton.active = false;

    int objectiveEntriesTop = this.contentTopPos + 5;

    // Melee Attacks
    this.meleeAttackCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 10,
                objectiveEntriesTop,
                ObjectiveType.MELEE_ATTACK.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.MELEE_ATTACK),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.MELEE_ATTACK, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    this.zombieAttackCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 150,
                objectiveEntriesTop,
                ObjectiveType.ZOMBIE_ATTACK.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.ZOMBIE_ATTACK),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.ZOMBIE_ATTACK, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Crossbow Attack
    objectiveEntriesTop += SPACE_BETWEEN_ENTRIES;
    this.crossbowAttackCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 10,
                objectiveEntriesTop,
                ObjectiveType.CROSSBOW_ATTACK.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.CROSSBOW_ATTACK),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.CROSSBOW_ATTACK, 3);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Bow Attack
    this.getBowAttackCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 150,
                objectiveEntriesTop,
                ObjectiveType.BOW_ATTACK.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.BOW_ATTACK),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.BOW_ATTACK, 4);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Attack Player
    objectiveEntriesTop += SPACE_BETWEEN_ENTRIES;
    this.attackPlayerCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 10,
                objectiveEntriesTop,
                ObjectiveType.ATTACK_PLAYER.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.ATTACK_PLAYER),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.ATTACK_PLAYER, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Attack Villager
    this.attackVillagerCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 150,
                objectiveEntriesTop,
                ObjectiveType.ATTACK_VILLAGER.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.ATTACK_VILLAGER),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.ATTACK_VILLAGER, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Attack Animal
    objectiveEntriesTop += SPACE_BETWEEN_ENTRIES;
    this.attackAnimalCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 10,
                objectiveEntriesTop,
                ObjectiveType.ATTACK_ANIMAL.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.ATTACK_ANIMAL),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.ATTACK_ANIMAL, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Attack Monster
    this.attackMonsterCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 150,
                objectiveEntriesTop,
                ObjectiveType.ATTACK_MONSTER.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.ATTACK_MONSTER),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.ATTACK_MONSTER, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Attack Mob
    objectiveEntriesTop += SPACE_BETWEEN_ENTRIES;
    this.attackMobCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 10,
                objectiveEntriesTop,
                ObjectiveType.ATTACK_MOB.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.ATTACK_MOB),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(ObjectiveType.ATTACK_MOB, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));

    // Attack Mob without Creeper
    objectiveEntriesTop += SPACE_BETWEEN_ENTRIES;
    this.attackMobWithoutCreeperCheckbox =
        this.addRenderableWidget(
            new Checkbox(
                this.contentLeftPos + 10,
                objectiveEntriesTop,
                ObjectiveType.ATTACK_MOB_WITHOUT_CREEPER.getObjectiveName(),
                objectiveDataSet.hasObjective(ObjectiveType.ATTACK_MOB_WITHOUT_CREEPER),
                checkbox -> {
                  ObjectiveData objectiveData =
                      objectiveDataSet.getOrCreateObjective(
                          ObjectiveType.ATTACK_MOB_WITHOUT_CREEPER, 2);
                  if (checkbox.selected()) {
                    NetworkMessageHandler.addObjective(uuid, objectiveData);
                  } else {
                    NetworkMessageHandler.removeObjective(uuid, objectiveData);
                  }
                }));
  }
}
