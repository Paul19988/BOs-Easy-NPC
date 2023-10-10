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

package de.markusbordihn.easynpc.client.screen.configuration.pose;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import de.markusbordihn.easynpc.client.screen.components.SliderButton;
import de.markusbordihn.easynpc.client.screen.configuration.ConfigurationScreen;
import de.markusbordihn.easynpc.data.CustomPosition;
import de.markusbordihn.easynpc.data.model.ModelPart;
import de.markusbordihn.easynpc.menu.configuration.ConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.ConfigurationType;
import de.markusbordihn.easynpc.network.NetworkMessageHandler;

@OnlyIn(Dist.CLIENT)
public class PoseConfigurationScreen<T extends ConfigurationMenu> extends ConfigurationScreen<T> {

  // Buttons
  protected Button defaultPoseButton;
  protected Button advancedPoseButton;
  protected Button customPoseButton;

  public PoseConfigurationScreen(T menu, Inventory inventory, Component component) {
    super(menu, inventory, component);
  }

  protected SliderButton createRotationSlider(int left, int top, ModelPart modelPart,
      String label) {
    return createRotationSlider(left, top, modelPart, label, false);
  }

  protected SliderButton createRotationSliderCompact(int left, int top, ModelPart modelPart,
      String label) {
    return createRotationSlider(left, top, modelPart, label, true);
  }

  protected SliderButton createRotationSlider(int left, int top, ModelPart modelPart, String label,
      boolean compact) {
    int sliderWidth = 34;
    int sliderHeight = 20;
    int sliderLeftPosition = left;

    // Shift left position for compact mode ans specific model parts
    if (compact && (modelPart == ModelPart.BODY || modelPart == ModelPart.LEFT_ARM
        || modelPart == ModelPart.LEFT_LEG)) {
      sliderLeftPosition = left + 10;
    }

    // Model Part Rotation
    Rotations modelPartRotation = this.entity.getModelPartRotation(modelPart);
    SliderButton sliderButtonX = this.addRenderableWidget(
        new SliderButton(sliderLeftPosition, top, sliderWidth, sliderHeight, label + "RotationX",
            (float) Math.toDegrees(modelPartRotation.getX()), SliderButton.Type.DEGREE, slider -> {
              NetworkMessageHandler.rotationChange(uuid, modelPart,
                  new Rotations((float) Math.toRadians(slider.getTargetValue()),
                      modelPartRotation.getY(), modelPartRotation.getZ()));
            }));
    SliderButton sliderButtonY =
        this.addRenderableWidget(new SliderButton(sliderButtonX.x + sliderButtonX.getWidth(), top,
            sliderWidth, sliderHeight, label + "RotationY",
            (float) Math.toDegrees(modelPartRotation.getY()), SliderButton.Type.DEGREE, slider -> {
              NetworkMessageHandler.rotationChange(uuid, modelPart,
                  new Rotations(modelPartRotation.getX(),
                      (float) Math.toRadians(slider.getTargetValue()), modelPartRotation.getZ()));
            }));
    SliderButton sliderButtonZ =
        this.addRenderableWidget(new SliderButton(sliderButtonY.x + sliderButtonY.getWidth(), top,
            sliderWidth, sliderHeight, label + "RotationZ",
            (float) Math.toDegrees(modelPartRotation.getZ()), SliderButton.Type.DEGREE, slider -> {
              NetworkMessageHandler.rotationChange(uuid, modelPart,
                  new Rotations(modelPartRotation.getX(), modelPartRotation.getY(),
                      (float) Math.toRadians(slider.getTargetValue())));
            }));

    if (compact) {
      int resetButtonLeftPosition;
      // Switch reset button left position for compact mode ans specific model parts
      switch (modelPart) {
        case BODY:
        case LEFT_ARM:
        case LEFT_LEG:
          resetButtonLeftPosition = left;
          break;
        default:
          resetButtonLeftPosition = sliderButtonZ.x + sliderButtonZ.getWidth();
          break;
      }
      this.addRenderableWidget(
          menuButton(resetButtonLeftPosition, top, 10, new TextComponent("↺"), button -> {
            sliderButtonX.reset();
            sliderButtonY.reset();
            sliderButtonZ.reset();
            NetworkMessageHandler.rotationChange(uuid, modelPart, new Rotations(0f, 0f, 0f));
          }));
    } else {
      this.addRenderableWidget(
          menuButton(sliderButtonX.x, top + 20, sliderWidth * 3, "reset", button -> {
            sliderButtonX.reset();
            sliderButtonY.reset();
            sliderButtonZ.reset();
            NetworkMessageHandler.rotationChange(uuid, modelPart, new Rotations(0f, 0f, 0f));
          }));
    }
    return sliderButtonX;
  }

  protected SliderButton createPositionSlider(int left, int top, ModelPart modelPart,
      String label) {
    return createPositionSlider(left, top, modelPart, label, false);
  }

  protected SliderButton createPositionSliderCompact(int left, int top, ModelPart modelPart,
      String label) {
    return createPositionSlider(left, top, modelPart, label, true);
  }

  protected SliderButton createPositionSlider(int left, int top, ModelPart modelPart, String label,
      boolean compact) {
    int sliderWidth = 34;
    int sliderHeight = 20;
    int sliderLeftPosition = left;

    // Shift left position for compact mode ans specific model parts
    if (compact && (modelPart == ModelPart.BODY || modelPart == ModelPart.LEFT_ARM
        || modelPart == ModelPart.LEFT_LEG)) {
      sliderLeftPosition = left + 10;
    }

    // Model Part Position
    CustomPosition modelPartPosition = this.entity.getModelPartPosition(modelPart);
    SliderButton sliderButtonX =
        this.addRenderableWidget(new SliderButton(sliderLeftPosition, top, sliderWidth, sliderHeight,
            label + "PositionX", modelPartPosition.getX(), SliderButton.Type.POSITION, slider -> {
              NetworkMessageHandler.modelPositionChange(uuid, modelPart, new CustomPosition(
                  slider.getTargetValue(), modelPartPosition.getY(), modelPartPosition.getZ()));
            }));
    SliderButton sliderButtonY = this.addRenderableWidget(
        new SliderButton(sliderButtonX.x + sliderButtonX.getWidth(), top, sliderWidth, sliderHeight,
            label + "PositionY", modelPartPosition.getY(), SliderButton.Type.POSITION, slider -> {
              NetworkMessageHandler.modelPositionChange(uuid, modelPart, new CustomPosition(
                  modelPartPosition.getX(), slider.getTargetValue(), modelPartPosition.getZ()));
            }));
    SliderButton sliderButtonZ = this.addRenderableWidget(
        new SliderButton(sliderButtonY.x + sliderButtonY.getWidth(), top, sliderWidth, sliderHeight,
            label + "PositionZ", modelPartPosition.getZ(), SliderButton.Type.POSITION, slider -> {
              NetworkMessageHandler.modelPositionChange(uuid, modelPart, new CustomPosition(
                  modelPartPosition.getX(), modelPartPosition.getY(), slider.getTargetValue()));
            }));

    if (compact) {
      int resetButtonLeftPosition;
      // Switch reset button left position for compact mode ans specific model parts
      switch (modelPart) {
        case BODY:
        case LEFT_ARM:
        case LEFT_LEG:
          resetButtonLeftPosition = left;
          break;
        default:
          resetButtonLeftPosition = sliderButtonZ.x + sliderButtonZ.getWidth();
          break;
      }
      this.addRenderableWidget(menuButton(resetButtonLeftPosition, top, 10,
          new TextComponent("↺"), button -> {
            sliderButtonX.reset();
            sliderButtonY.reset();
            sliderButtonZ.reset();
            NetworkMessageHandler.modelPositionChange(uuid, modelPart,
                new CustomPosition(0f, 0f, 0f));
          }));
    } else {
      this.addRenderableWidget(
          menuButton(sliderButtonX.x, top + 20, sliderWidth * 3, "reset", button -> {
            sliderButtonX.reset();
            sliderButtonY.reset();
            sliderButtonZ.reset();
            NetworkMessageHandler.modelPositionChange(uuid, modelPart,
                new CustomPosition(0f, 0f, 0f));
          }));
    }
    return sliderButtonX;
  }

  @Override
  public void init() {
    super.init();

    // Pose Types
    int poseButtonWidth = 80;
    this.defaultPoseButton = this.addRenderableWidget(menuButton(this.buttonLeftPos,
        this.buttonTopPos, poseButtonWidth - 10, "default_pose", button -> {
          NetworkMessageHandler.openConfiguration(uuid, ConfigurationType.DEFAULT_POSE);
        }));

    this.advancedPoseButton =
        this.addRenderableWidget(menuButton(this.buttonLeftPos + this.defaultPoseButton.getWidth(),
            this.buttonTopPos, poseButtonWidth + 10, "advanced_pose", button -> {
              NetworkMessageHandler.openConfiguration(uuid, ConfigurationType.ADVANCED_POSE);
            }));

    this.customPoseButton =
        this.addRenderableWidget(menuButton(advancedPoseButton.x + advancedPoseButton.getWidth(),
            this.buttonTopPos, poseButtonWidth + 20, "custom_pose", button -> {
              NetworkMessageHandler.openConfiguration(uuid, ConfigurationType.CUSTOM_POSE);
            }));

    // Default button stats
    this.defaultPoseButton.active =
        this.hasPermissions(COMMON.defaultPoseConfigurationEnabled.get(),
            COMMON.defaultPoseConfigurationAllowInCreative.get(),
            COMMON.defaultPoseConfigurationPermissionLevel.get());
    this.advancedPoseButton.active =
        this.hasPermissions(COMMON.advancedPoseConfigurationEnabled.get(),
            COMMON.advancedPoseConfigurationAllowInCreative.get(),
            COMMON.advancedPoseConfigurationPermissionLevel.get());
    this.customPoseButton.active = this.hasPermissions(COMMON.customPoseConfigurationEnabled.get(),
        COMMON.customPoseConfigurationAllowInCreative.get(),
        COMMON.customPoseConfigurationPermissionLevel.get());
  }

}
