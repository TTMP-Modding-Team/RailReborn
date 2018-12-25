package com.tictim.railreborn.block;

import com.google.common.collect.ImmutableList;
import com.tictim.railreborn.api.RailRebornAPI;
import com.tictim.railreborn.enums.Diameter;
import com.tictim.railreborn.pipelink.PipeNode;
import com.tictim.railreborn.pipelink.attachment.PipeAttachments;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.tileentity.TEPipe;
import com.tictim.railreborn.util.Microblock;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPipe extends Block implements Microblock{
	private final PipeHandler pipe;
	
	public BlockPipe(PipeHandler pipe){
		super(pipe.getBlockMaterial());
		this.pipe = pipe;
		this.setSoundType(pipe.getBlockSound());
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty()){
			if(!world.isRemote){
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TEPipe){
					TEPipe pipe = (TEPipe)te;
					if(RailRebornAPI.isCrowbar(stack)){
						AxisAlignedBB aabb = Microblock.getNearestAABB(this.getBoundingBoxes(world, pos, state), hitX, hitY, hitZ);
						if(this.pipe.getDiameter().getCenterBB()==aabb){
							if(pipe.tryConnect(facing)) stack.damageItem(1, player);
						}else for(EnumFacing f: EnumFacing.VALUES){
							if(this.pipe.getDiameter().getSidedBB(f)==aabb){
								if(pipe.tryDisconnect(f)) stack.damageItem(1, player);
								break;
							}
						}
						return true;
					}else{
						PipeAttachmentProvider p = PipeAttachments.deserialize(stack);
						if(p!=PipeAttachments.INVALID){
							AxisAlignedBB aabb = Microblock.getNearestAABB(this.getBoundingBoxes(world, pos, state), hitX, hitY, hitZ);
							if(this.pipe.getDiameter().getCenterBB()==aabb){
								if(pipe.onAttachment(p, facing)) stack.shrink(1);
							}else for(EnumFacing f: EnumFacing.VALUES){
								if(this.pipe.getDiameter().getSidedBB(f)==aabb){
									if(pipe.onAttachment(p, f)) stack.shrink(1);
									break;
								}
							}
							return true;
						}
					}
					return false;
				}
			}
			return RailRebornAPI.isCrowbar(stack)||PipeAttachments.deserialize(stack)!=PipeAttachments.INVALID;
		}
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEPipe){
			TEPipe pipe = (TEPipe)te;
			pipe.onPlacement();
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEPipe){
			TEPipe pipe = (TEPipe)te;
			pipe.onBreak();
		}
		world.removeTileEntity(pos);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face){
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState){
		for(AxisAlignedBB aabb: getBoundingBoxes(world, pos, state)) addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb);
	}
	
	@Override
	public AxisAlignedBB[] getBoundingBoxes(World world, BlockPos pos, IBlockState state){
		ImmutableList.Builder<AxisAlignedBB> b = new ImmutableList.Builder<>();
		Diameter d = pipe.getDiameter();
		b.add(d.getCenterBB());
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEPipe){
			TEPipe pipe = (TEPipe)te;
			if(world.isRemote){
				if(pipe.visual!=null) for(EnumFacing f: EnumFacing.VALUES)
					if(pipe.visual.getVisual(f)!=null) b.add(d.getSidedBB(f));
			}else{
				PipeNode node = pipe.getNode();
				if(node!=null) for(EnumFacing f: EnumFacing.VALUES)
					if(node.isConnected(f)) b.add(d.getSidedBB(f));
			}
		}
		return b.build().toArray(new AxisAlignedBB[0]);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return pipe.getDiameter().getCenterBB();
	}
	
	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end){
		start = start.subtract(pos.getX(), pos.getY(), pos.getZ());
		end = end.subtract(pos.getX(), pos.getY(), pos.getZ());
		RayTraceResult ray = null;
		double len = Double.POSITIVE_INFINITY;
		for(AxisAlignedBB aabb: getBoundingBoxes(world, pos, state)){
			RayTraceResult ray2 = aabb.calculateIntercept(start, end);
			if(ray2!=null){
				double len2 = start.distanceTo(ray2.hitVec);
				if(len2<len){
					ray = ray2;
					len = len2;
				}
			}
		}
		return ray==null ? null : new RayTraceResult(ray.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), ray.sideHit, pos);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state){
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state){
		return new TEPipe().setHandler(pipe);
	}
}
