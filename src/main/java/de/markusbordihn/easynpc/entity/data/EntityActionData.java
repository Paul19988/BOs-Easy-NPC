/**
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

package de.markusbordihn.easynpc.entity.data;

import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import de.markusbordihn.easynpc.data.action.ActionData;
import de.markusbordihn.easynpc.data.action.ActionDataHelper;
import de.markusbordihn.easynpc.data.action.ActionType;
import de.markusbordihn.easynpc.entity.EasyNPCEntityData;

public interface EntityActionData extends EntityDataInterface {

  // Synced entity data
  public static final EntityDataAccessor<CompoundTag> DATA_ACTION_DATA =
      SynchedEntityData.defineId(EasyNPCEntityData.class, EntityDataSerializers.COMPOUND_TAG);
  public static final EntityDataAccessor<Integer> DATA_ACTION_PERMISSION_LEVEL =
      SynchedEntityData.defineId(EasyNPCEntityData.class, EntityDataSerializers.INT);

  // CompoundTags
  public static final String DATA_ACTION_DATA_TAG = "ActionData";
  public static final String DATA_ACTION_PERMISSION_LEVEL_TAG = "ActionPermissionLevel";

  default void setAction(ActionType actionType, ActionData actionData) {
    CompoundTag compoundTag =
        ActionDataHelper.setAction(getEntityData(DATA_ACTION_DATA), actionType, actionData);
    setEntityData(DATA_ACTION_DATA, compoundTag);
  }

  default String getAction(ActionType actionType) {
    return ActionDataHelper.getAction(getEntityData(DATA_ACTION_DATA), actionType);
  }

  default ActionData getActionData(ActionType actionType) {
    return ActionDataHelper.getActionData(getEntityData(DATA_ACTION_DATA), actionType);
  }

  default void clearActionData() {
    setEntityData(DATA_ACTION_DATA, new CompoundTag());
  }

  default boolean hasAction(ActionType actionType) {
    return ActionDataHelper.hasActionAndNotEmpty(getEntityData(DATA_ACTION_DATA), actionType);
  }

  default Map<ActionType, de.markusbordihn.easynpc.data.action.ActionData> getActions() {
    return ActionDataHelper.readActionData(getEntityData(DATA_ACTION_DATA));
  }

  default CompoundTag getActionData() {
    return getEntityData(DATA_ACTION_DATA);
  }

  default void setActionData(CompoundTag compoundTag) {
    setEntityData(DATA_ACTION_DATA, compoundTag);
  }

  default int getActionPermissionLevel() {
    return getEntityData(DATA_ACTION_PERMISSION_LEVEL);
  }

  default void setActionPermissionLevel(int actionPermissionLevel) {
    setEntityData(DATA_ACTION_PERMISSION_LEVEL, actionPermissionLevel);
  }

  default boolean hasDistanceAction() {
    return hasAction(ActionType.ON_DISTANCE_NEAR) || hasAction(ActionType.ON_DISTANCE_CLOSE)
        || hasAction(ActionType.ON_DISTANCE_VERY_CLOSE) || hasAction(ActionType.ON_DISTANCE_TOUCH);
  }

  default void defineSynchedActionData() {
    defineEntityData(DATA_ACTION_DATA, new CompoundTag());
    defineEntityData(DATA_ACTION_PERMISSION_LEVEL, 0);
  }

  default void addAdditionalActionData(CompoundTag compoundTag) {
    if (ActionDataHelper.hasActionData(getActionData())) {
      compoundTag.put(DATA_ACTION_DATA_TAG, getActionData());
    }
    compoundTag.putInt(DATA_ACTION_PERMISSION_LEVEL_TAG, this.getActionPermissionLevel());
  }

  default void readAdditionalActionData(CompoundTag compoundTag) {
    if (compoundTag.contains(DATA_ACTION_DATA_TAG)) {
      this.setActionData(compoundTag.getCompound(DATA_ACTION_DATA_TAG));
    }
    if (compoundTag.contains(DATA_ACTION_PERMISSION_LEVEL_TAG)) {
      this.setActionPermissionLevel(compoundTag.getInt(DATA_ACTION_PERMISSION_LEVEL_TAG));
    }
  }
}
