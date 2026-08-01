package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record GDNpcExt(NPCEntity npcEntity) {
    public String makeString(GDNpcExt npc) {
        return "{" + "\"npcUUID\": \"" + npc.npcEntity().getUUID() + "\"" + "}";
    }

    public Map<String, ? extends Function<MoParams, Object>> npcFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        return map;
    }

    public ObjectValue<GDNpcExt> asMolangValue() {
        ObjectValue<GDNpcExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(npcFunctions());
        return value;
    }
}
