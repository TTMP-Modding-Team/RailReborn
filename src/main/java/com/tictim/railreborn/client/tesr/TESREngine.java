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
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.animation.FastTESR;

public class TESREngine<T extends TEEngine> extends FastTESR<T> {
    private float progress_left;
    private float progress_right;
    private float speed = 0;
    private final float maxProgress = 5/16f;
    private boolean isMoving = false;
    private boolean isStart = false;
    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        isMoving = te.isMoving();
        System.out.println("ismoving in renderfast " + isMoving);
        System.out.println("te.isMoving" + te.isMoving());
        System.out.println("te.getpROGRESS" + te.getProgress());
        getGenerateProgress();
        System.out.println(progress_left);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableCull();
           /* BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            BlockPos pos = te.getPos();
            IBlockState state = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.BASE);
            IBlockState state_left = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.LEFT);
            IBlockState state_right = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.RIGHT);
            IBakedModel block = renderer.getBlockModelShapes().getModelForState(state);
            IBakedModel block_left = renderer.getBlockModelShapes().getModelForState(state_left);
            IBakedModel block_right = renderer.getBlockModelShapes().getModelForState(state_right);
            if(te.isMoving()) {
            double progress = te.getGenerateProgress();
            buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
            renderer.getBlockModelRenderer().renderModel(te.getWorld(), block, state, pos, buffer, true);
            buffer.setTranslation(x - pos.getX(), y - pos.getY() + progress, z - pos.getZ());
            renderer.getBlockModelRenderer().renderModel(te.getWorld(), block_left, state_left, pos, buffer, true);
            buffer.setTranslation(x - pos.getX(), y - pos.getY() + 5/16.0f - progress, z - pos.getZ());
            renderer.getBlockModelRenderer().renderModel(te.getWorld(), block_right, state_right, pos, buffer, true);
        } else {
                buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
                renderer.getBlockModelRenderer().renderModel(te.getWorld(), block, state, pos, buffer, true);
                renderer.getBlockModelRenderer().renderModel(te.getWorld(), block_left, state, pos, buffer, true);
                renderer.getBlockModelRenderer().renderModel(te.getWorld(), block_right, state, pos, buffer, true);

            }*/
           //renderAnimation(te, x, y, z, buffer);
            renderBlock(te, x, y, z, buffer);
        }
        GlStateManager.popMatrix();
        //System.out.println(te.getGenerateProgress() + "imout");
    }

    public void renderAnimation(T te, double x, double y, double z, BufferBuilder buffer) {
            //switch(te.getWorld().getBlockState(te.getPos()).getBlock().getActualState(te.getWorld().getBlockState(te.getPos()), te.getWorld(), te.getPos()).getValue(BlockEngine.LEVEL)) {
              switch(te.getProgress()) {
                case 1:
                    this.speed = 0.001f;
                    renderBlock(te, x, y, z, buffer);
                    break;
                case 2:
                    this.speed = 0.005f;
                    renderBlock(te, x, y, z, buffer);
                    break;
                case 3:
                    this.speed = 0.01f;
                    renderBlock(te, x, y, z, buffer);
                    break;
                case 4:
                    this.speed = 0.02f;
                    renderBlock(te, x, y, z, buffer);
                    break;
                case 5:
                    this.speed = 0.05f;
                    renderBlock(te, x, y, z, buffer);
                    break;
                default:
                    this.speed = 0;
                    renderBlock(te, x, y, z, buffer);
                    break;
            }

    }
    public void renderBlock(T te, double x, double y, double z, BufferBuilder buffer) {
        BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos pos = te.getPos();
        IBlockState state = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.BASE);
        IBlockState state_left = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.LEFT);
        IBlockState state_right = te.getWorld().getBlockState(pos).withProperty(BlockEngine.PART, BlockEngine.Part.RIGHT);
        IBakedModel block = renderer.getBlockModelShapes().getModelForState(state);
        IBakedModel block_left = renderer.getBlockModelShapes().getModelForState(state_left);
        IBakedModel block_right = renderer.getBlockModelShapes().getModelForState(state_right);
        buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        renderer.getBlockModelRenderer().renderModel(te.getWorld(), block, state, pos, buffer, true);
        buffer.setTranslation(x - pos.getX(), y - pos.getY() + this.progress_left, z - pos.getZ());
        renderer.getBlockModelRenderer().renderModel(te.getWorld(), block_left, state_left, pos, buffer, true);
        buffer.setTranslation(x - pos.getX(), y - pos.getY() + this.progress_right, z - pos.getZ());
        renderer.getBlockModelRenderer().renderModel(te.getWorld(), block_right, state_right, pos, buffer, true);
    }
    private boolean waspeak = false;
    public void getGenerateProgress() {
        //LogicEngine l = (LogicEngine) this.logic;
        //return l.getGenerateProgress();
        if(!isMoving) {
            if(!(this.progress_left <= 0)) this.progress_left = this.progress_left - 0.01f;
            if(!(this.progress_right <= 0)) this.progress_right = this.progress_right - 0.01f;
            System.out.println("something");
            return;
        }
        if(!isStart) {
            if(!(this.progress_left >= maxProgress)) this.progress_left = this.progress_left + 0.01f;
            else isStart = true;
            if(!(this.progress_right <= 0)) this.progress_right = this.progress_right - 0.01f;
            return;
        }
        if(this.progress_left >= 5/16.0f) waspeak = true;
        if(this.progress_left <= 0) waspeak = false;
        if(!waspeak) this.progress_left = this.progress_left + this.speed;
        if(waspeak) this.progress_left = this.progress_left - this.speed;
        progress_right = 5/16f - progress_left;
    }
    public void setUpProgress() {
        if(this.speed == 0) setProgress(0, 0, 0.01f);
        else {
            float targetl = maxProgress, targetr = 0;
            if(this.progress_left >= maxProgress) {targetl = 0; targetr = maxProgress;}
            else if(this.progress_left <= 0) {targetl = maxProgress; targetr = 0;}
            setProgress(targetl, targetr, this.speed);
        }
    }

    public void setProgress(float targetl, float targetr, float movingspeed) {
        if(this.progress_left <= targetl) this.progress_left = this.progress_left + movingspeed;
        if(this.progress_left >= targetl) this.progress_left = this.progress_left - movingspeed;
        if(this.progress_right <= targetr) this.progress_right = this.progress_right + movingspeed;
        if(this.progress_right >= targetr) this.progress_right = this.progress_right - movingspeed;
    }
}
