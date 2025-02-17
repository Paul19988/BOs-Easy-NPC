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

package de.markusbordihn.easynpc.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import de.markusbordihn.easynpc.client.model.custom.CustomCatModel;
import de.markusbordihn.easynpc.entity.npc.Cat;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CatCollarLayer extends RenderLayer<Cat, CustomCatModel<Cat>> {
  private static final ResourceLocation CAT_COLLAR_LOCATION =
      new ResourceLocation("textures/entity/cat/cat_collar.png");
  private final CustomCatModel<Cat> catModel;

  public CatCollarLayer(RenderLayerParent<Cat, CustomCatModel<Cat>> parent, EntityModelSet model) {
    super(parent);
    this.catModel = new CustomCatModel<>(model.bakeLayer(ModelLayers.CAT_COLLAR));
  }

  public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int lightLevel,
      Cat cat,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float ageInTicks2,
      float netHeadYaw,
      float headPitch) {
    if (cat.isTame()) {
      float[] afloat = cat.getCollarColor().getTextureDiffuseColors();
      coloredCutoutModelCopyLayerRender(
          this.getParentModel(),
          this.catModel,
          CAT_COLLAR_LOCATION,
          poseStack,
          buffer,
          lightLevel,
          cat,
          limbSwing,
          limbSwingAmount,
          ageInTicks2,
          netHeadYaw,
          headPitch,
          ageInTicks,
          afloat[0],
          afloat[1],
          afloat[2]);
    }
  }
}
