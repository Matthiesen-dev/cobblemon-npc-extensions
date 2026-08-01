package dev.matthiesen.cobblemon_npc_extensions.neoforge;

import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import net.neoforged.fml.common.Mod;

@Mod(CobblemonNPCExtensionsCommon.MOD_ID)
public class CobblemonNPCExtensionsNeoForge {
    public static final CobblemonNPCExtensionsCommon INSTANCE = CobblemonNPCExtensionsCommon.INSTANCE;

    public CobblemonNPCExtensionsNeoForge() {
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        INSTANCE.initialize();
    }
}
