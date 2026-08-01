package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.ObjectValue;
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

        return map;
    }

    public ObjectValue<GDServerExt> asMolangValue() {
        ObjectValue<GDServerExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(serverFunctions());
        return value;
    }
}
