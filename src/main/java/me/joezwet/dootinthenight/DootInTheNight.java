package me.joezwet.dootinthenight;

import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DootInTheNight implements ModInitializer {
    public static SoundEvent DOOT = new SoundEvent(new Identifier("ditn", "doot"));
    public static long lastDoot = System.currentTimeMillis();

    @Override
    public void onInitialize() {
        Registry.register(Registry.SOUND_EVENT, new Identifier("ditn", "doot"), DootInTheNight.DOOT);
    }

    public static boolean shouldDootTonight() {
        return System.currentTimeMillis() - lastDoot > 30000; // check if doot was in last 30 secs
    }
}
