package dev.matthiesen.cobblemon_npc_extensions.common.text_parsers;

import com.cobblemon.mod.common.api.molang.ObjectValue;
import dev.matthiesen.matthiesen_core.common.utility.chat.ServerMessagingUtil;
import net.minecraft.server.MinecraftServer;

public record ServerParserExt(MinecraftServer server) {
    public String makeString(ServerParserExt server) {
        return "{" + "\"serverPlatform\": \"" + server.server().getServerModName() + "\"" + "}";
    }

    public ObjectValue<ServerParserExt> asMolangValue() {
        ObjectValue<ServerParserExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);

        var sharedFunctions = Shared.buildFunctionMap(message -> ServerMessagingUtil.sendToAllAndConsole(server, message));
        value.functions.putAll(sharedFunctions);

        return value;
    }
}
