package dev.matthiesen.cobblemon_npc_extensions.common.text_parsers;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.function.PlayerMoLangFunctions;
import com.cobblemon.mod.common.api.molang.function.ServerMoLangFunctions;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import kotlin.jvm.functions.Function1;

import java.util.HashMap;
import java.util.Map;

public final class TextParserCompat {
    public static void init() {
        CobblemonNPCExtensionsCommon.INSTANCE.createInfoLog("Text parser platform detected, initializing text parser compatibility");

        PlayerMoLangFunctions.INSTANCE.getCustom().add(player -> {
            Map<String, Function1<MoParams, Object>> map = new HashMap<>();

            // q.player.text_parser() -> { "playerUUID": "string" }
            // q.player.text_parser.vanilla(<string message>) -> 1 for success, 0 for failure
            // q.player.text_parser.adventure(<string message>) -> 1 for success, 0 for failure
            // q.player.text_parser.emberstextapi(<string message>) -> 1 for success, 0 for failure
            map.put("text_parser", moParams -> new PlayerParserExt(player).asMolangValue());

            return map;
        });

        ServerMoLangFunctions.INSTANCE.getCustom().add(server -> {
            Map<String, Function1<MoParams, Object>> map = new HashMap<>();

            // q.server.text_parser() -> { "serverPlatform": "string" }
            // q.server.text_parser.vanilla(<string message>) -> 1 for success, 0 for failure
            // q.server.text_parser.adventure(<string message>) -> 1 for success, 0 for failure
            // q.server.text_parser.emberstextapi(<string message>) -> 1 for success, 0 for failure
            map.put("text_parser", moParams -> new ServerParserExt(server).asMolangValue());

            return map;
        });
    }
}
