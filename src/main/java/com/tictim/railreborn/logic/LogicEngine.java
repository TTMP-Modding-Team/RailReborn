package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.api.RJ;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.inventory.Inventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class LogicEngine extends Logic implements RJ {
    protected long capacity = 0, current = 0, in = 0, out = 0;

    @Override
    public abstract ITextComponent getDisplayName();

    @Override
    public JsonElement getDebugInfo() {
        JsonObject obj = new JsonObject();
        if(!(this.getTank() == null)) obj.add("Fluid Tank", Debugable.debugFluidTank(this.getTank()));
        if(!(this.getInventory() == null)) obj.add("Inventory", this.getInventory().getDebugInfo());
        obj.addProperty("currentRJ", this.currentRJ());
        obj.addProperty("capacityRJ", this.capacityRJ());
        obj.addProperty("maxRJIn", this.maxRJIn());
        obj.addProperty("maxRJOut", this.maxRJOut());
        return Debugable.stateClassType(this.getClass(), obj);
    }

    @Override
    public void update() { }

    @Override
    public long capacityRJ(){
        return capacity;
    }

    @Override
    public long currentRJ(){
        return current;
    }

    @Override
    public long maxRJIn(){
        return in;
    }

    @Override
    public long maxRJOut(){
        return out;
    }

    @Override
    public long insertRJ(long amount, boolean internally, boolean simulate){
        long i = Math.min(capacityRJ()-currentRJ(), internally ? amount : Math.min(amount, maxRJIn()));
        if(!simulate) this.current += i;
        return i;
    }

    @Override
    public long extractRJ(long amount, boolean internally, boolean simulate){
        long i = Math.min(currentRJ(), internally ? amount : Math.min(amount, maxRJOut()));
        if(!simulate) this.current += i;
        return i;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public abstract FluidTank getTank();
    public abstract Inventory getInventory();

    public void generateRJfromFluid(long amount, Fluid fluid) {
        this.getTank().setFluid(new FluidStack(fluid, this.getTank().getFluidAmount() - 1));
        this.insertRJ(amount, false, false);
    }

    public boolean isRJFull() {
        if(this.currentRJ() == capacityRJ()) return true;
        else return false;
    }


    public void fillTank(FluidStack fluidstack) {
        this.getTank().fill(fluidstack, true);
    }
}
