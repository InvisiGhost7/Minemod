package io.github.invisighost7.minemod.mixin;

import io.github.invisighost7.minemod.client.TorchLightState;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelRenderer.BrightnessGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Inject(method = "getLightCoords(Lnet/minecraft/client/renderer/LevelRenderer$BrightnessGetter;Lnet/minecraft/world/level/BlockAndLightGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I", at = @At("RETURN"), cancellable = true)
    private static void minemod$heldTorchLightsUp(BrightnessGetter brightnessGetter, BlockAndLightGetter level, BlockState blockState, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        TorchLightState.Snapshot snapshot = TorchLightState.current();
        if (snapshot.lightLevel() <= 0) {
            return;
        }

        BlockPos sourcePos = snapshot.sourcePos();
        int distance = Math.abs(sourcePos.getX() - pos.getX())
            + Math.abs(sourcePos.getY() - pos.getY())
            + Math.abs(sourcePos.getZ() - pos.getZ());

        int dynamicBlockLight = snapshot.lightLevel() - distance;
        if (dynamicBlockLight <= 0) {
            return;
        }

        int originalLight = cir.getReturnValue();
        int originalBlockLight = LightCoordsUtil.block(originalLight);
        if (dynamicBlockLight <= originalBlockLight) {
            return;
        }

        cir.setReturnValue(LightCoordsUtil.withBlock(originalLight, dynamicBlockLight));
    }
}
