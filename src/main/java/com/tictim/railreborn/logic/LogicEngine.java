package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.tictim.railreborn.api.RJ;
import com.tictim.railreborn.enums.Engines;
import net.minecraft.util.text.ITextComponent;

public abstract class LogicEngine extends Logic implements RJ {
    protected long capacity = 0, current = 0, in = 0, out = 0;

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public JsonElement getDebugInfo() {return null;

    }

    @Override
    public void update() {

    }

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
}
