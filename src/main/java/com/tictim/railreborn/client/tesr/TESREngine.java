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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.animation.FastTESR;

public class TESREngine<T extends TEEngine> extends FastTESR<T> {

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableCull();
            BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            BlockPos pos = te.getPos();
            IBlockState state = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.BASE);
            IBlockState state_left = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.LEFT);
            IBlockState state_right = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.RIGHT);
            IBakedModel block = renderer.getBlockModelShapes().getModelForState(state);
            IBakedModel block2 = renderer.getBlockModelShapes().getModelForState(state_left);
            IBakedModel block_right = renderer.getBlockModelShapes().getModelForState(state_right);
            Vec3i vec = state.getValue(BlockHorizontal.FACING).rotateYCCW().getDirectionVec();
            double progress = te.getGenerateProgress();
            buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
            renderer.getBlockModelRenderer().renderModel(te.getWorld(), block, state, pos, buffer, true);
            buffer.setTranslation(x - pos.getX(), y - pos.getY() + progress, z - pos.getZ());
            renderer.getBlockModelRenderer().renderModel(te.getWorld(), block2, state_left, pos, buffer, true);
            buffer.setTranslation(x - pos.getX(), y - pos.getY() + 5/16.0f - progress, z - pos.getZ());
            renderer.getBlockModelRenderer().renderModel(te.getWorld(), block_right, state_right, pos, buffer, true);
        }
        GlStateManager.popMatrix();
        //System.out.println(te.getGenerateProgress() + "imout");
    }
}
