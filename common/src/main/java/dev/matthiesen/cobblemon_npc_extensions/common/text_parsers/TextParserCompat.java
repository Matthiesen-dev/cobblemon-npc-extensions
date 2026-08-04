package dev.matthiesen.cobblemon_npc_extensions.common.text_parsers;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.MoLangFunctions;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;

import java.util.HashMap;
import java.util.function.Function;

public final class TextParserCompat {
    public static void init() {
        CobblemonNPCExtensionsCommon.INSTANCE.createInfoLog("Text parser compatibility is enabled, initializing text parser compatibility");

        MoLangFunctions.INSTANCE.getPlayerFunctions().add(player -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.player.text_parser() -> { "playerUUID": "string" }
            // q.player.text_parser.vanilla(<string message>) -> 1 for success, 0 for failure
            // q.player.text_parser.adventure(<string message>) -> 1 for success, 0 for failure
            // q.player.text_parser.emberstextapi(<string message>) -> 1 for success, 0 for failure
            map.put("text_parser", moParams -> new PlayerParserExt(player).asMolangValue());

            return map;
        });

        MoLangFunctions.INSTANCE.getServerFunctions().add(server -> {
           HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.server.text_parser() -> { "serverPlatform": "string" }
            // q.server.text_parser.vanilla(<string message>) -> 1 for success, 0 for failure
            // q.server.text_parser.adventure(<string message>) -> 1 for success, 0 for failure
            // q.server.text_parser.emberstextapi(<string message>) -> 1 for success, 0 for failure
            map.put("text_parser", moParams -> new ServerParserExt(server).asMolangValue());

            return map;
        });
    }
}
