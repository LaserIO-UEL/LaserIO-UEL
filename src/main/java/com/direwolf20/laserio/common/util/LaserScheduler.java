package com.direwolf20.laserio.common.util;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.setup.Config;
import net.minecraft.world.level.Level;

import java.util.*;

public class LaserScheduler {
    private static final Map<Level, SchedulerState> states = new HashMap<>();

    public static class SchedulerState {
        public final ArrayDeque<LaserNodeBE> activeQueue = new ArrayDeque<>();
        public final Set<LaserNodeBE> cooldownList = new LinkedHashSet<>();
    }

    public static void tick(Level level) {
        if (level.isClientSide) return;
        SchedulerState state = states.get(level);
        if (state == null) return;

        long start = System.nanoTime();
        long budgetNs = (long) (Config.MAX_TICK_MS.get() * 1_000_000);

        // 1. Promote from Cooldown to Active
        long currentTick = level.getGameTime();
        Iterator<LaserNodeBE> cooldownIterator = state.cooldownList.iterator();
        while (cooldownIterator.hasNext()) {
            LaserNodeBE node = cooldownIterator.next();
            if (currentTick >= node.getNextAvailableTime()) {
                cooldownIterator.remove();
                if (node.getNodeState() != LaserNodeBE.NodeState.ACTIVE) {
                    node.setNodeState(LaserNodeBE.NodeState.ACTIVE);
                    state.activeQueue.add(node);
                }
                node.setWakeRequestedThisTick(false);
            }
        }

        // 2. Execute Active Nodes
        int processedThisTick = 0;
        int initialActiveCount = state.activeQueue.size();

        while (!state.activeQueue.isEmpty() && processedThisTick < initialActiveCount) {
            if (System.nanoTime() - start >= budgetNs) break;

            LaserNodeBE node = state.activeQueue.poll();
            processedThisTick++;

            if (node == null || node.isRemoved()) continue;

            LaserNodeBE.NodeWorkResult result = node.doNodeTick();
            node.setWakeRequestedThisTick(false);

            if (result.didWork()) {
                node.setNextAvailableTime(currentTick + result.cooldownTicks());
                node.setNodeState(LaserNodeBE.NodeState.COOLDOWN);
                state.cooldownList.add(node);
            } else {
                if (result.cooldownTicks() > 0) {
                    node.setNextAvailableTime(currentTick + result.cooldownTicks());
                    node.setNodeState(LaserNodeBE.NodeState.COOLDOWN);
                    state.cooldownList.add(node);
                } else {
                    node.setNodeState(LaserNodeBE.NodeState.IDLE);
                }
            }
        }
    }

    public static void requestWakeUp(LaserNodeBE node) {
        if (node.getLevel() == null || node.getLevel().isClientSide || node.isRemoved()) return;
        if (node.isWakeRequestedThisTick()) return;

        SchedulerState state = states.computeIfAbsent(node.getLevel(), k -> new SchedulerState());

        if (node.getNodeState() == LaserNodeBE.NodeState.IDLE) {
            node.setNodeState(LaserNodeBE.NodeState.ACTIVE);
            state.activeQueue.add(node);
        } else if (node.getNodeState() == LaserNodeBE.NodeState.COOLDOWN) {
            state.cooldownList.remove(node);
            node.setNodeState(LaserNodeBE.NodeState.ACTIVE);
            state.activeQueue.add(node);
        }

        node.setWakeRequestedThisTick(true);
        node.setNextAvailableTime(0); // Ready now
    }

    public static void addNode(LaserNodeBE node) {
        if (node.getLevel() == null || node.getLevel().isClientSide) return;
        requestWakeUp(node);
    }

    public static void removeNode(LaserNodeBE node) {
        if (node.getLevel() == null) return;
        SchedulerState state = states.get(node.getLevel());
        if (state != null) {
            state.activeQueue.remove(node);
            state.cooldownList.remove(node);
        }
        node.setNodeState(LaserNodeBE.NodeState.IDLE);
    }

    public static void clear(Level level) {
        states.remove(level);
    }
}
