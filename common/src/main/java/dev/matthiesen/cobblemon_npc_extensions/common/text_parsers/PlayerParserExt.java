package dev.matthiesen.cobblemon_npc_extensions.common.text_parsers;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.DoubleValue;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record PlayerParserExt(Player player) {
    public String makeString(PlayerParserExt player) {
        return "{" + "\"playerUUID\": \"" + player.player().getUUID() + "\"" + "}";
    }

    private Map<String,? extends Function<MoParams, Object>> playerFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        // q.player.text_parser.vanilla(<string message>) -> 1 for success, 0 for failure
        map.put("vanilla", moParams -> {
            String message = moParams.getString(0);

            try {
                var parser = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().getTextParser(BuiltInTextParsers.VANILLA);
                player.sendSystemMessage(parser.parse(message));
                return DoubleValue.ONE;
            } catch (Exception e) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message for player " + player.getUUID() + " because of an error: " + e.getMessage());
                return DoubleValue.ZERO;
            }
        });

        // q.player.text_parser.adventure(<string message>) -> 1 for success, 0 for failure
        map.put("adventure", moParams -> {
            String message = moParams.getString(0);

            boolean isAvailable = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().isTextParserRegistered(BuiltInTextParsers.ADVENTURE);
            if (!isAvailable) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message for player " + player.getUUID() + " because the adventure text parser is not available");
                return DoubleValue.ZERO;
            }

            try {
                var parser = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().getTextParser(BuiltInTextParsers.ADVENTURE);
                player.sendSystemMessage(parser.parse(message));
                return DoubleValue.ONE;
            } catch (Exception e) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message for player " + player.getUUID() + " because of an error: " + e.getMessage());
                return DoubleValue.ZERO;
            }
        });

        // q.player.text_parser.emberstextapi(<string message>) -> 1 for success, 0 for failure
        map.put("emberstextapi", moParams -> {
            String message = moParams.getString(0);

            boolean isAvailable = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().isTextParserRegistered(BuiltInTextParsers.EMBERS);
            if (!isAvailable) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message for player " + player.getUUID() + " because the Ember's Text API text parser is not available");
                return DoubleValue.ZERO;
            }

            try {
                var parser = CobblemonNPCExtensionsCommon.INSTANCE.getTextParserManager().getTextParser(BuiltInTextParsers.EMBERS);
                player.sendSystemMessage(parser.parse(message));
                return DoubleValue.ONE;
            } catch (Exception e) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to parse message for player " + player.getUUID() + " because of an error: " + e.getMessage());
                return DoubleValue.ZERO;
            }
        });

        return map;
    }

    public ObjectValue<PlayerParserExt> asMolangValue() {
        ObjectValue<PlayerParserExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(playerFunctions());
        return value;
    }
}
