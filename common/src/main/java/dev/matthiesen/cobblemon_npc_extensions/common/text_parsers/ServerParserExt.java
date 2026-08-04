package dev.matthiesen.cobblemon_npc_extensions.common.text_parsers;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.DoubleValue;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.utility.chat.ServerMessagingUtil;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record ServerParserExt(MinecraftServer server) {
    public String makeString(ServerParserExt server) {
        return "{" + "\"serverPlatform\": \"" + server.server().getServerModName() + "\"" + "}";
    }

    public Function<MoParams, Object> parseAndSend(BuiltInTextParsers parsers) {
        return moParams -> {
            String message = moParams.getString(0);

            boolean isAvailable = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().isTextParserRegistered(parsers);
            if (!isAvailable) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message for server " + server.getServerModName() + " because the " + parsers.name() + " text parser is not available");
                return DoubleValue.ZERO;
            }

            try {
                var parser = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().getTextParser(parsers);
                ServerMessagingUtil.sendToAllAndConsole(server, parser.parse(message));
                return DoubleValue.ONE;
            } catch (Exception e) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message for server " + server.getServerModName() + " because of an error: " + e.getMessage());
                return DoubleValue.ZERO;
            }
        };
    }

    public Map<String,? extends Function<MoParams, Object>> serverFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        // q.player.text_parser.vanilla(<string message>) -> 1 for success, 0 for failure
        map.put("vanilla", parseAndSend(BuiltInTextParsers.VANILLA));

        // q.player.text_parser.adventure(<string message>) -> 1 for success, 0 for failure
        map.put("adventure", parseAndSend(BuiltInTextParsers.ADVENTURE));

        // q.player.text_parser.emberstextapi(<string message>) -> 1 for success, 0 for failure
        map.put("emberstextapi", parseAndSend(BuiltInTextParsers.EMBERS));

        return map;
    }

    public ObjectValue<ServerParserExt> asMolangValue() {
        ObjectValue<ServerParserExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(serverFunctions());
        return value;
    }
}
