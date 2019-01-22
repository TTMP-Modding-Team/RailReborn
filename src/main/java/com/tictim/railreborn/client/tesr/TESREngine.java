package com.tictim.railreborn.client.tesr;

import com.tictim.railreborn.block.BlockEngine;
import com.tictim.railreborn.tileentity.TEEngine;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.animation.FastTESR;

public class TESREngine <T extends TEEngine> extends FastTESR<T> {

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableCull();
            BlockPos pos = te.getPos();
            IBlockState state = te.getWorld().getBlockState(pos);
            BlockRendererDispatcher rendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBakedModel model = rendererDispatcher.getBlockModelShapes().getModelForState(state);
            //double slideProgress = te.getGenerateProgress(partialTicks);
            //Vec3i vec = state.getValue(BlockHorizontal.FACING).rotateYCCW().getDirectionVec();
            //buffer.setTranslation(x - pos.getX() + slideProgress * vec.getX(), y - pos.getY(), z - pos.getZ() + slideProgress * vec.getZ());
            buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
            buffer.addVertexData(new int[] { 0, 0, 0, 1, 1, 1 });
            buffer.pos(x, y + te.getGenerateProgress(), z);
            rendererDispatcher.getBlockModelRenderer().renderModel(te.getWorld(), model, state, pos, buffer, true);
        }GlStateManager.popMatrix();

    }
}
