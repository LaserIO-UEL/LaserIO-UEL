package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE.LaserEnergyStorage;
import com.direwolf20.laserio.common.containers.customhandler.LaserNodeItemHandler;
import it.unimi.dsi.fastutil.bytes.Byte2ByteMap;
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap;

import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NodeSideCache {
    public LaserNodeItemHandler itemHandler;
    public LazyOptional<LaserNodeItemHandler> handlerLazyOptional;
    public LaserEnergyStorage energyStorage;
    public LazyOptional<LaserEnergyStorage> laserEnergyStorage;
    public int overclockers;
    public final List<ExtractorCardCache> extractorCardCaches = new CopyOnWriteArrayList<>();
    public Byte2ByteMap myRedstoneFromSensors = new Byte2ByteOpenHashMap();  //Channel,Strength

    public NodeSideCache() {

    }

    public NodeSideCache(LaserNodeItemHandler itemHandler, LaserEnergyStorage energyStorage, int overclockers) {
        this.itemHandler = itemHandler;
        this.handlerLazyOptional = LazyOptional.of(() -> itemHandler);
        this.energyStorage = energyStorage;
        this.laserEnergyStorage = LazyOptional.of(() -> energyStorage);
        this.overclockers = overclockers;
    }

    public void invalidateEnergy() {
        laserEnergyStorage.invalidate();
        laserEnergyStorage = LazyOptional.of(() -> energyStorage);
    }
}