package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.MoLangFunctions;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;

import java.util.HashMap;
import java.util.function.Function;

public final class GriefDefenderCompat {
    public static void init() {
        CobblemonNPCExtensionsCommon.INSTANCE.createInfoLog("GriefDefender is loaded, initializing GriefDefender compatibility");

        MoLangFunctions.INSTANCE.getNpcFunctions().add(npcEntity -> {
           HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.npc.griefdefender() -> { "npcUUID": "string" }
            map.put("griefdefender", moParams -> new GDNpcExt(npcEntity).asMolangValue());

           return map;
        });

        MoLangFunctions.INSTANCE.getPlayerFunctions().add(player -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.player.griefdefender() -> { "playerUUID": "string" }
            map.put("griefdefender", moParams -> new GDPlayerExt(player).asMolangValue());

            return map;
        });


        MoLangFunctions.INSTANCE.getServerFunctions().add(server -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.server.griefdefender() -> { "serverPlatform": "string" }
            map.put("griefdefender", moParams -> new GDServerExt(server).asMolangValue());

            return map;
        });

        MoLangFunctions.INSTANCE.getWorldFunctions().add(levelHolder -> {
            var world = levelHolder.value();
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.world.griefdefender() -> { "location": "string" }
            map.put("griefdefender", moParams -> new GDWorldExt(world).asMolangValue());

            return map;
        });
    }
}
