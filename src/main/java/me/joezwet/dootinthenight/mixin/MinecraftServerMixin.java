package me.joezwet.dootinthenight.mixin;

import me.joezwet.dootinthenight.DootInTheNight;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow private PlayerManager playerManager;

    @Shadow protected abstract boolean shouldKeepTicking();

    @Shadow public abstract @Nullable ServerWorld getWorld(RegistryKey<World> key);

    @Shadow public abstract PlayerManager getPlayerManager();

    @Shadow @Final private Random random;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(BooleanSupplier supplier, CallbackInfo info) {
        if (this.shouldKeepTicking()) {
            long tick = this.getWorld(World.OVERWORLD).getLevelProperties().getTimeOfDay();
            if (tick >= 17950L && tick <= 18050L && DootInTheNight.shouldDootTonight()) {
                DootInTheNight.lastDoot = System.currentTimeMillis();
                for (ServerPlayerEntity e : this.getPlayerManager().getPlayerList()) {
                    e.playSound(DootInTheNight.DOOT, SoundCategory.MASTER, 0.01f, this.random.nextFloat() * (1.3f - 0.5f));
                    e.sendMessage(
                            new LiteralText("[DITN]")
                                    .setStyle(
                                            Style.EMPTY
                                                    .withBold(true)
                                                    .withColor(Formatting.GOLD)
                                                    .withHoverEvent(
                                                            new HoverEvent(
                                                                    HoverEvent.Action.SHOW_TEXT,
                                                                    new LiteralText("Provided by ")
                                                                            .append(
                                                                                    new LiteralText("Doot in the Night")
                                                                                            .setStyle(Style.EMPTY.withBold(true))
                                                                            )
                                                            )
                                                    )
                                    ).append(" ").append(
                                    new LiteralText("You hear a spooky skeleton play his trumpet, you feel blessed.")
                                            .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
                            ),
                            false);
                }
            }
        }
    }
}
