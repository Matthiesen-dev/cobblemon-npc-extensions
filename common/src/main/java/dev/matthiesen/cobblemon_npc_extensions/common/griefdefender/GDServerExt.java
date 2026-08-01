package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.universal.UniversalFunctions;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record GDServerExt(MinecraftServer server) {
    public String makeString(GDServerExt server) {
        return "{" + "\"serverPlatform\": \"" + server.server().getServerModName() + "\"" + "}";
    }

    public Map<String,? extends Function<MoParams, Object>> serverFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        // q.server.griefdefender.economy_enabled() returns 1 for true, or 0
        map.put("economy_enabled", UniversalFunctions.isEconomyEnabled());

        // q.server.griefdefender.get_player_claims(<uuid>) returns array of claims for user in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
        map.put("get_player_claims", UniversalFunctions.getPlayerClaims());

        return map;
    }

    public ObjectValue<GDServerExt> asMolangValue() {
        ObjectValue<GDServerExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(serverFunctions());
        return value;
    }
}
