package com.jeromepaulos.hyaddons.mixins;

import com.jeromepaulos.hyaddons.events.BlockChangeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Modified from Skytils under GPL-3.0
 * https://github.com/Skytils/SkytilsMod/blob/master/LICENSE
 */
@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Shadow
    public abstract IBlockState getBlockState(final BlockPos pos);

    @Shadow public abstract World getWorld();

    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void onBlockChange(BlockPos position, IBlockState newBlock, CallbackInfoReturnable<IBlockState> cir) {
        IBlockState oldBlock = this.getBlockState(position);

        if(oldBlock != newBlock) {
            try {
                MinecraftForge.EVENT_BUS.post(new BlockChangeEvent(position, oldBlock, newBlock));
            } catch(Throwable ignored) {}
        }
    }

}
