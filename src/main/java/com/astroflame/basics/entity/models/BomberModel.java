package com.astroflame.basics.entity.models;

import com.astroflame.basics.entity.BomberEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BomberModel extends SegmentedModel<BomberEntity> {

    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer tail;

    public BomberModel() {
        this.textureHeight = 64;
        this.textureWidth = 64;
        body = new ModelRenderer(this, 0, 14);
        body.addBox(0, 2, 0, 7, 7, 7);

        head = new ModelRenderer(this, 0, 0);
        head.addBox(0, -5, 0, 7, 7, 7);
        head.rotateAngleX = 0.2F;

        tail = new ModelRenderer(this, 28, 0);
        tail.addBox(0,9,0,7,7,7);

        body.addChild(head);
        body.addChild(tail);
    }

    @Override
    public void setRotationAngles(BomberEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.rotateAngleX = netHeadYaw * ((float)Math.PI / 180F);
        this.body.rotateAngleY = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body);
    }
}
