package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.inventory.InventoryBuilder;
import com.tictim.railreborn.recipe.Machine;
import net.minecraft.util.text.ITextComponent;

public class LogicEngine extends Logic implements InventoryBuilder{
    private Inventory.Name name;
    private final Engines engine;

    public LogicEngine(Engines engine) {
        System.out.println(engine.getName());
        this.engine = engine;
        name = new Inventory.Name(RailReborn.MODID+"."+this.engine.getName().toLowerCase()+"_engine");

    }
    private final Inventory inv = this.createInventory();

    @Override
    public int size(){
      //  if(enginetype != null) {
//        } else
            return 2;
    }

    @Override
    public String invName(){
        return RailReborn.MODID+"."+ this.engine.getName().toLowerCase()+"_engine";
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.name.getDisplayName();
    }

    @Override
    public JsonElement getDebugInfo() {
        JsonObject obj = new JsonObject();
        obj.add("Inventory", this.inv.getDebugInfo());
       // if(this.crafting!=null) obj.add("Crafting", this.crafting.getDebugInfo());
        return Debugable.stateClassType(this.getClass(), obj);
    }

    @Override
    public void update() {

    }
}
