package dev.matthiesen.cobblemon_npc_extensions.fabric;

import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import net.fabricmc.api.ModInitializer;

public class CobblemonNPCExtensionsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        var instance = CobblemonNPCExtensionsCommon.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();
    }
}
