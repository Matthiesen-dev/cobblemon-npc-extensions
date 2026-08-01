package dev.matthiesen.cobblemon_npc_extensions.common.economy;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.MoLangFunctions;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;

import java.util.HashMap;
import java.util.function.Function;

public final class EconomyCompat {
    public static void init() {
        CobblemonNPCExtensionsCommon.INSTANCE.createInfoLog("Economy compatibility is enabled, initializing economy compatibility");

        MoLangFunctions.INSTANCE.getPlayerFunctions().add(player -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.player.economy() -> { "playerUUID": "string" }
            // q.player.economy.get_balance(<string providerID>, <string currency>) -> double
            // q.player.economy.deposit(<string providerID>, <int amount>, <string currency>) -> 1 for success, otherwise 0
            // q.player.economy.withdraw(<string providerID>, <int amount>, <string currency>) -> 1 for success, otherwise 0
            // q.player.economy.has_enough(<string providerID>, <int amount>, <string currency>) -> 1 for true, otherwise 0
            map.put("economy", moParams -> new EcoPlayerExt(player).asMolangValue());

            return map;
        });
    }
}
