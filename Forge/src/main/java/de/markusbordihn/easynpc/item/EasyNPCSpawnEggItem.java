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
import de.markusbordihn.easynpc.entity.EasyNPCEntity;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EasyNPCSpawnEggItem extends ForgeSpawnEggItem {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  public EasyNPCSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, Properties props) {
    super(type, MaterialColor.COLOR_RED.col, MaterialColor.COLOR_YELLOW.col, props);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    if (!(level instanceof ServerLevel)) {
      return InteractionResult.SUCCESS;
    }
    Player player = context.getPlayer();
    ItemStack itemStack = context.getItemInHand();
    BlockPos blockPos = context.getClickedPos();
    Direction direction = context.getClickedFace();
    BlockState blockState = level.getBlockState(blockPos);
    BlockPos blockPos1;
    if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
      blockPos1 = blockPos;
    } else {
      blockPos1 = blockPos.relative(direction);
    }

    // Spawn the entity based on the spawn egg type.
    EntityType<?> entityType = this.getType(itemStack.getTag());
    Entity entity =
        entityType.spawn(
            (ServerLevel) level,
            itemStack,
            context.getPlayer(),
            blockPos1,
            MobSpawnType.SPAWN_EGG,
            true,
            !Objects.equals(blockPos, blockPos1) && direction == Direction.UP);

    if (entity != null) {
      if (entity instanceof EasyNPCEntity easyNPCEntity) {
        log.info("Spawned Easy NPC Entity {} by player {} ...", easyNPCEntity, player);
        easyNPCEntity.onInitialSpawn((ServerLevel) level, player, itemStack);
      }
      itemStack.shrink(1);
      level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
    }

    return InteractionResult.CONSUME;
  }
}
