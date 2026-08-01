package dev.matthiesen.cobblemon_npc_extensions.common.luckperms;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.MoLangFunctions;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;

import java.util.HashMap;
import java.util.function.Function;

public final class LuckPermsCompat {
    public static void init() {
        CobblemonNPCExtensionsCommon.INSTANCE.createInfoLog("LuckPerms is loaded, initializing LuckPerms compatibility");

        MoLangFunctions.INSTANCE.getPlayerFunctions().add(player -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.player.luckperms() -> { playerUUID: "string" }
            // q.player.luckperms.promote(<track string>, <dont-add-to-first int-as-boolean>) -> 0
            // q.player.luckperms.demote(<track string>, <dont-remove-from-first int-as-boolean>) -> 0
            // q.player.luckperms.permission_set(<track string>, <dont-remove-from-first int-as-boolean>) -> 0
            // q.player.luckperms.permission_unset(<node string>) -> 0
            // q.player.luckperms.permission_settemp(<node string>, <value boolean>, <duration string>) -> 0
            // q.player.luckperms.permission_unsettemp(<node string>) -> 0
            // q.player.luckperms.permission_check(<node string>) -> Boolean-As-Double
            // q.player.luckperms.parent_set(<group string>) -> 0
            // q.player.luckperms.parent_add(<group string>) -> 0
            // q.player.luckperms.parent_remove(<group string>) -> 0
            // q.player.luckperms.parent_settrack(<track string>, <group string>) -> 0
            // q.player.luckperms.parent_addtemp(<group string>, <duration string>) -> 0
            // q.player.luckperms.parent_removetemp(<group string>) -> 0
            // q.player.luckperms.meta_set(<key string>, <value string>) -> 0
            // q.player.luckperms.meta_unset(<key string>) -> 0
            // q.player.luckperms.meta_settemp(<key string>, <value string>, <duration string>) -> 0
            // q.player.luckperms.meta_unsettemp(<key string>) -> 0
            // q.player.luckperms.meta_get(<key string>) -> string
            map.put("luckperms", moParams -> new LPPlayerExt(player).asMolangValue());

            return map;
        });
    }
}
