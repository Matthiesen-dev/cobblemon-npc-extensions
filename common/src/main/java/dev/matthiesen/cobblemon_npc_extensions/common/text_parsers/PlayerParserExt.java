package dev.matthiesen.cobblemon_npc_extensions.common.text_parsers;

import com.cobblemon.mod.common.api.molang.ObjectValue;
import net.minecraft.world.entity.player.Player;

public record PlayerParserExt(Player player) {
    public String makeString(PlayerParserExt player) {
        return "{" + "\"playerUUID\": \"" + player.player().getUUID() + "\"" + "}";
    }

    public ObjectValue<PlayerParserExt> asMolangValue() {
        ObjectValue<PlayerParserExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);

        var sharedFunctions = Shared.buildFunctionMap(player::sendSystemMessage);
        value.functions.putAll(sharedFunctions);

        return value;
    }
}
