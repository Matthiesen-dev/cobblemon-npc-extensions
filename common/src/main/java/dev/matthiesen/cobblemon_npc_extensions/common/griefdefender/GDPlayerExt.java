package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record GDPlayerExt(Player player) {
    public String makeString(GDPlayerExt player) {
        return "{" + "\"playerUUID\": \"" + player.player().getUUID() + "\"" + "}";
    }

    public Map<String,? extends Function<MoParams, Object>> playerFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        return map;
    }

    public ObjectValue<GDPlayerExt> asMolangValue() {
        ObjectValue<GDPlayerExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(playerFunctions());
        return value;
    }
}
