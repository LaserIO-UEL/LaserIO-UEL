package com.direwolf20.laserio.client.particles;

import com.direwolf20.laserio.client.particles.fluidparticle.FluidFlowParticle;
import com.direwolf20.laserio.client.particles.itemparticle.ItemFlowParticle;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.integration.ModIntegration;
import com.direwolf20.laserio.integration.mekanism.client.particles.MekanismModParticles;
import com.direwolf20.laserio.integration.mekanism.client.particles.chemicalparticle.ChemicalFlowParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LaserIO.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRenderDispatcher {
    @SubscribeEvent
    public static void registerProviders(RegisterParticleProvidersEvent evt) {
        evt.registerSpecial(ModParticles.ITEM_FLOW_PARTICLE.get(), ItemFlowParticle.FACTORY);
        evt.registerSpecial(ModParticles.FLUID_FLOW_PARTICLE.get(), FluidFlowParticle.FACTORY);
        //Mekanism particles
        if (ModIntegration.MEKANISM.isLoaded()) {
            evt.registerSpecial(MekanismModParticles.CHEMICAL_FLOW_PARTICLE.get(), ChemicalFlowParticle.FACTORY);
        }
    }
}