package com.tictim.railreborn.recipe;

import java.util.Arrays;
import javax.annotation.Nullable;

import com.tictim.railreborn.item.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public final class Crafting implements ITickable{
	private final Machine handler;
	
	@Nullable
	private IngredientStack[] input;
	@Nullable
	private ItemStack[] output;
	@Nullable
	private FluidStack[] fluidInput, fluidOutput;
	
	private long totalTime, currentTime, timeIncrement = 1;
	
	private int energy;
	
	private boolean finalized;
	
	public Crafting(Machine handler){
		this.handler = handler;
	}
	
	public Crafting copy(Crafting c){
		this.input = copy(c.input);
		this.output = copy(c.output);
		this.fluidInput = copy(c.fluidInput);
		this.fluidOutput = copy(c.fluidOutput);
		this.totalTime = c.totalTime;
		this.currentTime = c.currentTime;
		this.timeIncrement = c.timeIncrement;
		this.energy = c.energy;
		this.finalized = c.finalized;
		return this;
	}
	
	public Crafting read(NBTTagCompound nbt){
		return this;
	}
	
	@Nullable
	private static <T> T[] copy(@Nullable T[] original){
		if(original==null) return null;
		else return Arrays.copyOf(original, original.length);
	}
	
	@Override
	public void update(){
		Step s = getStep();
		if(s==Step.DONE) return;
		else if(handler.interact(this)){
			if(s==Step.PROCESSING){
				if(handler.process(this)){
					currentTime = Math.max(0, currentTime-timeIncrement);
					if(!isProcessDone()) return;
				}
			}
			if(hasOutput()){
				handler.collectResult(this);
				if(hasOutput()) return;
			}
			if(!finalized){
				handler.finalize(this);
				finalized = true;
				cancel();
			}
		}
	}
	
	public Step getStep(){
		return isProcessDone() ? Step.PROCESSING : hasOutput() ? Step.WAITING_FOR_SPACE : Step.DONE;
	}
	
	public boolean isProcessDone(){
		return totalTime>currentTime;
	}
	
	public boolean hasOutput(){
		if(output!=null&&output.length>0) for(ItemStack s: output)
			if(!s.isEmpty()) return true;
		if(fluidOutput!=null&&fluidOutput.length>0) for(FluidStack s: fluidOutput)
			if(s.amount>0) return true;
		return false;
	}
	
	public Crafting setInput(IngredientStack... input){
		this.input = input==null||input.length==0 ? null : input;
		return this;
	}
	
	public Crafting setOutput(ItemStack... output){
		this.output = output==null||output.length==0 ? null : output;
		return this;
	}
	
	public Crafting setFluidInput(FluidStack... input){
		this.fluidInput = input==null||input.length==0 ? null : input;
		return this;
	}
	
	public Crafting setFluidOutput(FluidStack... output){
		this.fluidOutput = output==null||output.length==0 ? null : output;
		return this;
	}
	
	public IngredientStack[] getInput(){
		return input;
	}
	
	public ItemStack[] getOutput(){
		return output;
	}
	
	public FluidStack[] getFluidInput(){
		return fluidInput;
	}
	
	public FluidStack[] getFluidOutput(){
		return fluidOutput;
	}
	
	public Crafting setTotalTime(long totalTime){
		this.totalTime = totalTime;
		return this;
	}
	
	public Crafting setCurrentTime(long currentTime){
		this.currentTime = currentTime;
		return this;
	}
	
	public long getTotalTime(){
		return this.totalTime;
	}
	
	public long getCurrentTime(){
		return this.currentTime;
	}
	
	public Crafting setEnergy(int energy){
		this.energy = energy;
		return this;
	}
	
	public int getEnergy(){
		return this.energy;
	}
	
	public void cancel(){
		this.setCurrentTime(this.totalTime);
		setOutput().setFluidOutput();
	}
	
	public void expectInputSize(int value){
		expect(this.input, value, value, "Unexpected input size %d, must be %d");
	}
	
	public void expectInputSize(int min, int max){
		expect(this.input, min, max, "Unexpected input size %d, must between %d and %d");
	}
	
	public void expectOutputSize(int value){
		expect(this.output, value, value, "Unexpected output size %d, must be %d");
	}
	
	public void expectOutputSize(int min, int max){
		expect(this.output, min, max, "Unexpected output size %d, must between %d and %d");
	}
	
	public void expectFluidInputSize(int value){
		expect(this.fluidInput, value, value, "Unexpected fluid input size %d, must be %d");
	}
	
	public void expectFluidInputSize(int min, int max){
		expect(this.fluidInput, min, max, "Unexpected fluid input size %d, must between %d and %d");
	}
	
	public void expectFluidOutputSize(int value){
		expect(this.fluidOutput, value, value, "Unexpected fluid output size %d, must be %d");
	}
	
	public void expectFluidOutputSize(int min, int max){
		expect(this.fluidOutput, min, max, "Unexpected fluid output size %d, must between %d and %d");
	}
	
	public boolean extractInput(IItemHandler handler, boolean simulate){
		if(this.input!=null&&this.input.length>0){
			for(IngredientStack ing: input){
				int quantity = ing.getQuantity();
				for(int i = 0, j = handler.getSlots(); i<j; i++){
					ItemStack s = handler.extractItem(i, 64, simulate);
					if(!s.isEmpty()&&ing.getIngredient().test(s)){
						quantity -= s.getCount();
						if(quantity<=0) break;
					}
				}
				if(quantity>0) return false;
			}
		}
		return true;
	}
	
	public boolean insertOutput(IItemHandler handler, boolean simulate){
		if(this.output!=null&&this.output.length>0){
			for(ItemStack s: output){
				for(int i = 0, j = handler.getSlots(); i<j; i++){
					s = handler.insertItem(i, s, simulate);
					if(s.isEmpty()) break;
				}
				if(!s.isEmpty()) return false;
			}
		}
		if(!simulate) this.output = null;
		return true;
	}
	
	public boolean extractFluidInput(IFluidHandler handler, boolean simulate){
		if(this.fluidInput!=null&&this.fluidInput.length>0){
			for(FluidStack s: fluidInput){
				FluidStack s2 = handler.drain(s, !simulate);
				if(s.amount>s2.amount) return false;
			}
		}
		return true;
	}
	
	public boolean insertFluidOutput(IFluidHandler handler, boolean simulate){
		if(this.fluidOutput!=null&&this.fluidOutput.length>0){
			for(FluidStack s: fluidOutput){
				s = s.copy();
				s.amount = s.amount-handler.fill(s, !simulate);
				if(s.amount>0) return false;
			}
		}
		if(!simulate) this.fluidOutput = null;
		return true;
	}
	
	private static void expect(@Nullable Object[] arr, int min, int max, String error){
		int size = arr==null ? 0 : arr.length;
		if(size<min||size>max) throw new IllegalStateException(String.format(error, size, min, max));
	}
	
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("totalTime", totalTime);
		nbt.setLong("currentTime", currentTime);
		if(timeIncrement!=1) nbt.setLong("timeIncrement", timeIncrement);
		return nbt;
	}
	
	public enum Step{
		PROCESSING, WAITING_FOR_SPACE, DONE;
	}
}
