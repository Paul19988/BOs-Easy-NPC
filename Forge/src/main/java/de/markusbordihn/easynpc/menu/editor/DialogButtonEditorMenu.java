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

package de.markusbordihn.easynpc.menu.editor;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.data.dialog.DialogButtonData;
import de.markusbordihn.easynpc.data.dialog.DialogDataSet;
import de.markusbordihn.easynpc.entity.EasyNPCEntity;
import de.markusbordihn.easynpc.entity.EntityManager;
import de.markusbordihn.easynpc.menu.ModMenuTypes;
import de.markusbordihn.easynpc.menu.configuration.ConfigurationType;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogButtonEditorMenu extends AbstractContainerMenu {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  // Cache
  protected final DialogDataSet dialogDataSet;
  protected final DialogButtonData dialogButtonData;
  protected final Inventory playerInventory;
  protected final EasyNPCEntity entity;
  protected final UUID dialogId;
  protected final UUID uuid;
  protected final UUID dialogButtonId;
  protected final ConfigurationType formerConfigurationType;
  protected final int pageIndex;

  public DialogButtonEditorMenu(
      int windowId,
      Inventory playerInventory,
      UUID uuid,
      DialogDataSet dialogDataSet,
      UUID dialogId,
      UUID dialogButtonId,
      ConfigurationType formerConfigurationType,
      int pageIndex) {
    this(
        ModMenuTypes.DIALOG_BUTTON_EDITOR_MENU.get(),
        windowId,
        playerInventory,
        uuid,
        dialogDataSet,
        dialogId,
        dialogButtonId,
        formerConfigurationType,
        pageIndex);
  }

  public DialogButtonEditorMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
    this(
        windowId,
        playerInventory,
        data.readUUID(),
        new DialogDataSet(data.readNbt()),
        data.readUUID(),
        data.readUUID(),
        data.readEnum(ConfigurationType.class),
        data.readInt());
  }

  public DialogButtonEditorMenu(
      final MenuType<?> menuType,
      int windowId,
      Inventory playerInventory,
      UUID uuid,
      DialogDataSet dialogDataSet,
      UUID dialogId,
      UUID dialogButtonId,
      ConfigurationType formerConfigurationType,
      int pageIndex) {
    super(menuType, windowId);
    this.playerInventory = playerInventory;
    this.dialogDataSet = dialogDataSet;
    this.dialogButtonData = dialogDataSet.getDialog(dialogId).getButton(dialogButtonId);
    this.dialogButtonId = dialogButtonId;
    this.uuid = uuid;
    this.dialogId = dialogId;
    this.formerConfigurationType = formerConfigurationType;
    this.pageIndex = pageIndex;

    // Get entity from cache
    this.entity = EntityManager.getEasyNPCEntityByUUID(uuid);
  }

  public static MenuProvider getMenuProvider(
      UUID uuid,
      Entity entity,
      DialogDataSet dialogDataSet,
      UUID dialogId,
      UUID dialogButtonId,
      ConfigurationType formerConfigurationType,
      int pageIndex) {
    return new MenuProvider() {
      @Override
      public Component getDisplayName() {
        return new TextComponent("Edit Dialog Button " + dialogButtonId);
      }

      @Nullable
      @Override
      public AbstractContainerMenu createMenu(
          int windowId, Inventory inventory, Player serverPlayer) {
        return new DialogButtonEditorMenu(
            windowId,
            inventory,
            uuid,
            dialogDataSet,
            dialogId,
            dialogButtonId,
            formerConfigurationType,
            pageIndex);
      }
    };
  }

  public DialogDataSet getDialogDataSet() {
    return this.dialogDataSet;
  }

  public DialogButtonData getDialogButtonData() {
    return this.dialogButtonData;
  }

  public EasyNPCEntity getEntity() {
    return this.entity;
  }

  public UUID getUUID() {
    return this.uuid;
  }

  public UUID getDialogId() {
    return this.dialogId;
  }

  public UUID getDialogButtonId() {
    return this.dialogButtonId;
  }

  public ConfigurationType getFormerConfigurationType() {
    return this.formerConfigurationType;
  }

  public int getPageIndex() {
    return this.pageIndex;
  }

  public Inventory getPlayerInventory() {
    return this.playerInventory;
  }

  @Override
  public boolean stillValid(Player player) {
    return player != null && player.isAlive() && entity != null && entity.isAlive();
  }
}
