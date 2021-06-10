package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MimicDeath {

    private static final String[] messages = {"Mimic killed!", "Mimic exorcised!", "Mimic demolished!", "Mimic vaporized!", "Mimic banished!"};

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if(Config.mimicDeathMessage != 0 && Utils.inDungeon) {
            if(event.entity.getClass() == EntityZombie.class) {
                EntityZombie entity = (EntityZombie) event.entity;
                if(entity.isChild()) {
                    if(entity.getCurrentArmor(0) == null && entity.getCurrentArmor(1) == null && entity.getCurrentArmor(2) == null && entity.getCurrentArmor(3) == null) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc "+messages[Config.mimicDeathMessage-1]);
                    }
                }
            }
        }
    }

}
