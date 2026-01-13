package me.argennova.createportal.mixin;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsHandler;
import me.argennova.createportal.teleportation.TeleportControlUtil;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsHandler.class)
public abstract class MixinControlsHandler {
    @Inject(
            method = "startControlling",
            at = @At("RETURN")
    )
    // Save the last entity controlled to replay interaction after teleporting
    private static void onStartControlling(AbstractContraptionEntity entity, BlockPos controllerLocalPos, CallbackInfo ci) {
        TeleportControlUtil.lastInteractionEntity = entity;
        TeleportControlUtil.lastInteractionPos = controllerLocalPos;
    }
}
