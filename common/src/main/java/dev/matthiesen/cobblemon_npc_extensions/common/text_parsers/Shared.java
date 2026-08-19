package dev.matthiesen.cobblemon_npc_extensions.common.text_parsers;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.DoubleValue;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Shared {
    public static Map<String, ? extends Function<MoParams, Object>> buildFunctionMap(
            Consumer<Component> broadcastMessageConsumer
    ) {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        map.put("vanilla", parseAndSend(broadcastMessageConsumer, BuiltInTextParsers.VANILLA));
        map.put("adventure", parseAndSend(broadcastMessageConsumer, BuiltInTextParsers.ADVENTURE));
        map.put("emberstextapi", parseAndSend(broadcastMessageConsumer, BuiltInTextParsers.EMBERS));

        return map;
    }

    public static Function<MoParams, Object> parseAndSend(
            Consumer<Component> broadcastMessageConsumer,
            BuiltInTextParsers parser
    ) {
        return moParams -> {
            String message = moParams.getString(0);

            boolean isAvailable = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().isTextParserRegistered(parser);
            if (!isAvailable) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message because the " + parser.name() + " text parser is not available");
                return DoubleValue.ZERO;
            }

            try {
                var usableParser = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().getTextParser(parser);
                broadcastMessageConsumer.accept(usableParser.parse(message));
                return DoubleValue.ONE;
            } catch (Exception e) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message because of an error: " + e.getMessage());
                return DoubleValue.ZERO;
            }
        };
    }
}
