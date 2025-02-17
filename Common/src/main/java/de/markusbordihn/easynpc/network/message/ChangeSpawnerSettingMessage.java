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

package de.markusbordihn.easynpc.network.message;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.block.entity.BaseEasyNPCSpawnerBlockEntity;
import de.markusbordihn.easynpc.data.spawner.SpawnerSettingType;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeSpawnerSettingMessage {

  public static final ResourceLocation MESSAGE_ID =
      new ResourceLocation(Constants.MOD_ID, "change_spawner_settings");
  private static final Logger log = LogManager.getLogger(Constants.LOG_NAME);
  private final BlockPos spawnerPos;
  private final SpawnerSettingType settingType;
  private final int value;

  public ChangeSpawnerSettingMessage(BlockPos blockPos, SpawnerSettingType settingType, int value) {
    this.spawnerPos = blockPos;
    this.settingType = settingType;
    this.value = value;
  }

  public static ChangeSpawnerSettingMessage decode(final FriendlyByteBuf buffer) {
    return new ChangeSpawnerSettingMessage(
        buffer.readBlockPos(), buffer.readEnum(SpawnerSettingType.class), buffer.readInt());
  }

  public static FriendlyByteBuf encode(
      final ChangeSpawnerSettingMessage message, final FriendlyByteBuf buffer) {
    buffer.writeBlockPos(message.getSpawnerPos());
    buffer.writeEnum(message.getSettingType());
    buffer.writeInt(message.getValue());
    return buffer;
  }

  public static void handle(final ChangeSpawnerSettingMessage message, ServerPlayer player) {
    if (player == null) {
      return;
    }
    Level level = player.getLevel();
    if (level.getBlockEntity(message.getSpawnerPos())
        instanceof BaseEasyNPCSpawnerBlockEntity spawnerBlockEntity) {

      // Verify if player has permission to change the settings.
      if (!player.isCreative()
          && spawnerBlockEntity.getOwner() != null
          && !spawnerBlockEntity.getOwner().equals(player.getUUID())) {
        log.warn(
            "Player {} has no permission to change the settings of spawner at {}",
            player.getName().getString(),
            message.getSpawnerPos());
        return;
      }

      // Update the spawner settings
      switch (message.getSettingType()) {
        case SPAWN_RANGE:
          log.debug("Set spawner {} spawn range to {}", spawnerBlockEntity, message.getValue());
          spawnerBlockEntity.setSpawnRange(message.getValue());
          break;
        case REQUIRED_PLAYER_RANGE:
          log.debug(
              "Set spawner {} required player range to {}", spawnerBlockEntity, message.getValue());
          spawnerBlockEntity.setRequiredPlayerRange(message.getValue());
          break;
        case DELAY:
          log.debug("Set spawner {} delay to {}", spawnerBlockEntity, message.getValue());
          spawnerBlockEntity.setDelay(message.getValue());
          break;
        case MAX_NEARBY_ENTITIES:
          log.debug(
              "Set spawner {} max nearby entities to {}", spawnerBlockEntity, message.getValue());
          spawnerBlockEntity.setMaxNearbyEntities(message.getValue());
          break;
        case SPAWN_COUNT:
          log.debug("Set spawner {} spawn count to {}", spawnerBlockEntity, message.getValue());
          spawnerBlockEntity.setSpawnCount(message.getValue());
          break;
        default:
          log.error(
              "Unknown spawner setting type {} for {}",
              message.getSettingType(),
              spawnerBlockEntity);
      }
    } else {
      log.error("No valid spawner block entity found at {}", message.getSpawnerPos());
    }
  }

  public FriendlyByteBuf encode() {
    return encode(this, new FriendlyByteBuf(Unpooled.buffer()));
  }

  public BlockPos getSpawnerPos() {
    return this.spawnerPos;
  }

  public SpawnerSettingType getSettingType() {
    return this.settingType;
  }

  public int getValue() {
    return this.value;
  }
}
