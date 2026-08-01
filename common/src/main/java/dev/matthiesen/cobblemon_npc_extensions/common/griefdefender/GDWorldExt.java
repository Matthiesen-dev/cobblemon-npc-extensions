package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record GDWorldExt(Level level) {
    public String makeString(GDWorldExt player) {
        return "{" + "\"location\": \"" + player.level().dimension().location() + "\"" + "}";
    }

    public Map<String,? extends Function<MoParams, Object>> playerFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        return map;
    }

    public ObjectValue<GDWorldExt> asMolangValue() {
        ObjectValue<GDWorldExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(playerFunctions());
        return value;
    }
}
