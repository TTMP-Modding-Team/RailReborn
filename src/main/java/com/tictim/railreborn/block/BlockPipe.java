package com.tictim.railreborn.block;

import com.google.common.collect.ImmutableList;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.Registries;
import com.tictim.railreborn.api.RailRebornAPI;
import com.tictim.railreborn.enums.Diameter;
import com.tictim.railreborn.pipelink.PipeNode;
import com.tictim.railreborn.pipelink.attachment.PipeAttachments;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.pipelink.handler.PipeHandlers;
import com.tictim.railreborn.tileentity.TEPipe;
import com.tictim.railreborn.util.Microblock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPipe extends Block implements Microblock{
	public BlockPipe(){
		super(Material.IRON);
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEPipe) return ((TEPipe)te).getHandler().getBlockSound();
		else return SoundType.METAL;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEPipe) return ((TEPipe)te).getHandler().getBlockMaterial().getMaterialMapColor();
		else return this.blockMapColor;
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
						PipeHandler handler = pipe.getHandler();
						AxisAlignedBB aabb = Microblock.getNearestAABB(this.getBoundingBoxes(world, pos, state), hitX, hitY, hitZ);
						if(handler.getDiameter().getCenterBB()==aabb){
							if(pipe.tryConnect(facing)) stack.damageItem(1, player);
						}else for(EnumFacing f: EnumFacing.VALUES){
							if(handler.getDiameter().getSidedBB(f)==aabb){
								if(pipe.tryDisconnect(f)) stack.damageItem(1, player);
								break;
							}
						}
						return true;
					}else{
						PipeAttachmentProvider p = PipeAttachments.deserialize(stack);
						if(p!=PipeAttachments.INVALID){
							PipeHandler handler = pipe.getHandler();
							AxisAlignedBB aabb = Microblock.getNearestAABB(this.getBoundingBoxes(world, pos, state), hitX, hitY, hitZ);
							if(handler.getDiameter().getCenterBB()==aabb){
								if(pipe.onAttachment(p, facing)) stack.shrink(1);
							}else for(EnumFacing f: EnumFacing.VALUES){
								if(handler.getDiameter().getSidedBB(f)==aabb){
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
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if(player.capabilities.isCreativeMode){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TEPipe){
				TEPipe pipe = (TEPipe)te;
				pipe.setNoDrop(true);
			}
		}
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune){}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		if(!world.isRemote){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TEPipe){
				TEPipe pipe = (TEPipe)te;
				pipe.onPlacement(PipeHandlers.fromItem(stack));
			}
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
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		if(!world.isRemote){
			EnumFacing facing = getBlockPosFacing(pos, fromPos);
			if(facing==null){
				RailReborn.LOGGER.info("this game is broken.");
			}else{
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TEPipe){
					TEPipe pipe = (TEPipe)te;
					pipe.updateConnection(facing);
				}
			}
		}
	}
	
	@Nullable
	private static EnumFacing getBlockPosFacing(BlockPos center, BlockPos direction){
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
		for(EnumFacing f: EnumFacing.VALUES){
			mpos.setPos(center).move(f);
			if(direction.getX()==mpos.getX()&&direction.getY()==mpos.getY()&&direction.getZ()==mpos.getZ()) return f;
		}
		return null;
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
	
	private static final AxisAlignedBB[] INVALID_BB = new AxisAlignedBB[]{PipeHandlers.INVALID.getDiameter().getCenterBB()};
	
	@Override
	public AxisAlignedBB[] getBoundingBoxes(World world, BlockPos pos, IBlockState state){
		TileEntity te = world.getTileEntity(pos);
		TEPipe pipe = (te instanceof TEPipe) ? (TEPipe)te : null;
		if(pipe==null) return INVALID_BB;
		return world.isRemote ? collectPipeBoundingBoxesClient(pipe.visual) : collectPipeBoundingBoxes(pipe.getHandler(), pipe);
	}
	
	private static AxisAlignedBB[] collectPipeBoundingBoxes(PipeHandler handler, TEPipe pipe){
		ImmutableList.Builder<AxisAlignedBB> b = new ImmutableList.Builder<>();
		Diameter d = handler.getDiameter();
		b.add(d.getCenterBB());
		PipeNode node = pipe.getNode();
		if(node!=null) for(EnumFacing f: EnumFacing.VALUES)
			if(node.isConnected(f)) b.add(d.getSidedBB(f));
		return b.build().toArray(new AxisAlignedBB[0]);
	}
	
	private static AxisAlignedBB[] collectPipeBoundingBoxesClient(@Nullable TEPipe.PipeVisual visual){
		if(visual==null) return INVALID_BB;
		ImmutableList.Builder<AxisAlignedBB> b = new ImmutableList.Builder<>();
		Diameter d = visual.getHandler().getDiameter();
		b.add(d.getCenterBB());
		for(EnumFacing f: EnumFacing.VALUES)
			if(visual.getVisual(f)!=null) b.add(d.getSidedBB(f));
		return b.build().toArray(new AxisAlignedBB[0]);
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
		return new TEPipe();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items){
		for(PipeHandler h: Registries.PIPE_HANDLERS){
			items.add(h.of().copy());
		}
	}
}
