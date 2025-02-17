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

package de.markusbordihn.easynpc.client.screen.configuration.preset;

import com.mojang.blaze3d.vertex.PoseStack;
import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.client.screen.components.Text;
import de.markusbordihn.easynpc.client.screen.components.TextButton;
import de.markusbordihn.easynpc.data.CustomPresetData;
import de.markusbordihn.easynpc.data.skin.SkinModel;
import de.markusbordihn.easynpc.menu.configuration.preset.CustomImportPresetConfigurationMenu;
import de.markusbordihn.easynpc.network.NetworkMessageHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ImportCustomPresetConfigurationScreen
    extends ImportPresetConfigurationScreen<CustomImportPresetConfigurationMenu> {

  // Cache
  protected static Path selectedPreset;
  // Buttons
  protected Button importPresetButton;
  // Preset Selection List
  private ImportCustomPresetConfigurationScreen.ImportFileSelectionList presetSelectionList;

  public ImportCustomPresetConfigurationScreen(
      CustomImportPresetConfigurationMenu menu, Inventory inventory, Component component) {
    super(menu, inventory, component);
  }

  public static void updateSelectedPreset(Path path) {
    selectedPreset = path;
  }

  public void loadPresetConfirm(Path path) {
    Minecraft minecraft = this.minecraft;
    if (minecraft == null) {
      return;
    }
    minecraft.setScreen(
        new ConfirmScreen(
            confirmed -> {
              if (confirmed && uuid != null) {
                loadPreset(path);
                minecraft.setScreen(null);
              } else {
                minecraft.setScreen(this);
              }
            },
            new TranslatableComponent(
                Constants.TEXT_PREFIX + "preset.importQuestion",
                path.toString().substring(path.toString().lastIndexOf("/") + 1)),
            new TranslatableComponent(
                Constants.TEXT_PREFIX + "preset.importWarning",
                this.entity.getDisplayName().getString()),
            new TranslatableComponent(Constants.TEXT_PREFIX + "preset.importButton"),
            CommonComponents.GUI_CANCEL));
  }

  public void loadPreset(Path path) {
    // Read NBT data from file.
    CompoundTag compoundTag;
    try {
      compoundTag = NbtIo.readCompressed(path.toFile());
    } catch (IOException exception) {
      log.error("Failed to read NBT data from {}", path, exception);
      return;
    }
    if (compoundTag == null) {
      log.error("Received empty preset {}", path);
      return;
    }

    // Remove UUID from NBT data, if present.
    if (compoundTag.contains("UUID")) {
      compoundTag.remove("UUID");
    }

    // Remove position from NBT data, if present.
    if (compoundTag.contains("Pos")) {
      compoundTag.remove("Pos");
    }

    NetworkMessageHandler.importPreset(uuid, compoundTag);
  }

  @Override
  public void init() {
    super.init();

    // Default button stats
    this.customImportPresetButton.active = false;

    // File Selection List
    this.presetSelectionList =
        new ImportCustomPresetConfigurationScreen.ImportFileSelectionList(this.minecraft);
    this.addWidget(this.presetSelectionList);
    ImportCustomPresetConfigurationScreen.updateSelectedPreset(null);

    // Import button
    this.importPresetButton =
        this.addRenderableWidget(
            new TextButton(
                this.buttonLeftPos + 25,
                this.bottomPos - 40,
                220,
                "custom",
                button -> {
                  if (selectedPreset != null) {
                    this.loadPresetConfirm(selectedPreset);
                  }
                }));
    this.importPresetButton.active = false;
  }

  @Override
  public void render(PoseStack poseStack, int x, int y, float partialTicks) {
    super.render(poseStack, x, y, partialTicks);
    this.presetSelectionList.render(poseStack, x, y, partialTicks);
    this.importPresetButton.active = ImportCustomPresetConfigurationScreen.selectedPreset != null;
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(poseStack, partialTicks, mouseX, mouseY);

    int fileListTop = this.topPos + 55;
    int fileListHeight = fileListTop + 110;
    int fileListWidth = this.leftPos + 290;

    // File Selection List
    fill(
        poseStack,
        this.contentLeftPos - 1,
        fileListTop - 1,
        fileListWidth + 1,
        fileListHeight + 1,
        0xff000000);
    fill(poseStack, this.contentLeftPos, fileListTop, fileListWidth, fileListHeight, 0xffaaaaaa);
  }

  @OnlyIn(Dist.CLIENT)
  class ImportFileSelectionList
      extends ObjectSelectionList<
          ImportCustomPresetConfigurationScreen.ImportFileSelectionList.Entry> {
    public ImportFileSelectionList(Minecraft minecraft) {
      super(
          minecraft,
          ImportCustomPresetConfigurationScreen.this.width - 5,
          ImportCustomPresetConfigurationScreen.this.height - 150 + 66,
          ImportCustomPresetConfigurationScreen.this.topPos + 66,
          ImportCustomPresetConfigurationScreen.this.height
              - 150
              - ImportCustomPresetConfigurationScreen.this.topPos
              + 66,
          14);
      this.setRenderHeader(false, 0);
      this.setRenderBackground(false);
      this.setRenderTopAndBottom(false);

      // Read relevant preset files.
      CustomPresetData.getPresetFilePathLocations()
          .forEach(
              path -> {
                if (!path.toString()
                    .contains(
                        "preset"
                            + File.separator
                            + skinModel.toString().toLowerCase()
                            + File.separator)) {
                  log.warn(
                      "Skipping preset file {} as it does not match the current skin model {}",
                      path,
                      skinModel.toString().toLowerCase());
                  return;
                }
                ImportCustomPresetConfigurationScreen.ImportFileSelectionList.Entry entry =
                    new ImportCustomPresetConfigurationScreen.ImportFileSelectionList.Entry(
                        path, skinModel);
                this.addEntry(entry);
              });
    }

    @Override
    protected int getScrollbarPosition() {
      return super.getScrollbarPosition() + 12;
    }

    @Override
    public int getRowWidth() {
      return super.getRowWidth() + 40;
    }

    @Override
    protected boolean isFocused() {
      return ImportCustomPresetConfigurationScreen.this.getFocused() == this;
    }

    @Override
    public void render(PoseStack poseStack, int x, int y, float partialTicks) {
      if (this.getItemCount() > 0) {
        super.render(poseStack, x, y, partialTicks);
        return;
      }

      // Display "No presets found" message.
      Text.drawConfigStringShadow(
          poseStack,
          ImportCustomPresetConfigurationScreen.this.font,
          "no_presets_found",
          ImportCustomPresetConfigurationScreen.this.contentLeftPos + 80,
          ImportCustomPresetConfigurationScreen.this.topPos + 105,
          Constants.FONT_COLOR_WHITE);
    }

    @OnlyIn(Dist.CLIENT)
    public class Entry
        extends ObjectSelectionList.Entry<
            ImportCustomPresetConfigurationScreen.ImportFileSelectionList.Entry> {
      final Path path;
      final SkinModel skinModel;
      final String fileName;

      public Entry(Path path, SkinModel skinModel) {
        this.path = path;
        this.skinModel = skinModel;
        this.fileName =
            path.getFileName()
                .toString()
                .replace(
                    "preset"
                        + File.separator
                        + this.skinModel.toString().toLowerCase()
                        + File.separator,
                    "")
                .replace(Constants.NPC_NBT_SUFFIX, "");
      }

      public void render(
          PoseStack poseStack,
          int x,
          int y,
          int unused1,
          int unused2,
          int unused3,
          int unused4,
          int unused5,
          boolean unused6,
          float partialTicks) {

        // File Selection List Header
        int fileListTop = ImportCustomPresetConfigurationScreen.this.topPos + 55;
        int fileListWidth = ImportCustomPresetConfigurationScreen.this.leftPos + 290;
        fill(
            poseStack,
            ImportCustomPresetConfigurationScreen.this.contentLeftPos - 1,
            fileListTop - 4,
            fileListWidth + 1,
            fileListTop + 12,
            0xff000000);
        fill(
            poseStack,
            ImportCustomPresetConfigurationScreen.this.contentLeftPos,
            fileListTop - 3,
            fileListWidth,
            fileListTop + 11,
            0xff888888);
        Text.drawConfigStringShadowWithData(
            poseStack,
            ImportCustomPresetConfigurationScreen.this.font,
            "preset_custom_for",
            this.skinModel,
            ImportCustomPresetConfigurationScreen.this.contentLeftPos + 3,
            fileListTop,
            Constants.FONT_COLOR_WHITE);

        // Display file name.
        Text.drawStringShadow(
            poseStack,
            ImportCustomPresetConfigurationScreen.this.font,
            fileName,
            ImportFileSelectionList.this.width / 2
                - ImportCustomPresetConfigurationScreen.this.font.width(this.fileName) / 2,
            y + 1,
            Constants.FONT_COLOR_WHITE);
      }

      @Override
      public boolean mouseClicked(double unused1, double unused2, int button) {
        if (button == 0) {
          this.select();
          return true;
        } else {
          return false;
        }
      }

      private void select() {
        ImportFileSelectionList.this.setSelected(this);

        // Set selected preset.
        ImportCustomPresetConfigurationScreen.updateSelectedPreset(this.path);
      }

      public Component getNarration() {
        return new TextComponent(this.fileName);
      }
    }
  }
}
