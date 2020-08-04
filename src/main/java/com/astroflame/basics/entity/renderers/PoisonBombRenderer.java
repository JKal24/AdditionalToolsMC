package com.astroflame.basics.entity.renderers;

import com.astroflame.basics.basics;
import com.astroflame.basics.entity.PoisonBombEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class PoisonBombRenderer extends EntityRenderer<PoisonBombEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(basics.MOD_ID ,"textures/entity/poison_bomb.png");
    private static final RenderType renderType = RenderType.getEntityCutoutNoCull(TEXTURE);

    public PoisonBombRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(PoisonBombEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        MatrixStack.Entry entry = matrixStackIn.getLast();
        Matrix4f matrix4f = entry.getMatrix();
        Matrix3f matrix3f = entry.getNormal();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderType);

        buildVertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        buildVertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        buildVertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        buildVertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        matrixStackIn.pop();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void buildVertex(IVertexBuilder builder, Matrix4f mat4f, Matrix3f mat3f, int lightmapUV, float x, int y, int z, int tex) {
        builder.pos(mat4f, x - 0.5F, (float)y - 0.25F, 0.0F).color(255, 255, 255, 255).tex((float)z, (float)tex).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(mat3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getEntityTexture(PoisonBombEntity entity) {
        return TEXTURE;
    }
}
