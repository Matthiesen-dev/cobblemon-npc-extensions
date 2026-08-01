package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.StringValue;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDLocation;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDUtils;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.universal.UniversalFunctions;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record GDWorldExt(Level level) {
    public String makeString(GDWorldExt player) {
        return "{" + "\"location\": \"" + player.level().dimension().location() + "\"" + "}";
    }

    private GDLocation getClaim(Level level, MoParams params) {
        int x = params.getInt(0);
        int y = params.getInt(1);
        int z = params.getInt(2);
        return GDUtils.getClaim(level, x, y, z);
    }

    public Map<String,? extends Function<MoParams, Object>> worldFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        // q.world.griefdefender.economy_enabled() returns 1 for true, or 0
        map.put("economy_enabled", UniversalFunctions.isEconomyEnabled());

        // q.world.griefdefender.get_player_claims(<uuid>) returns array of claims for user in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
        map.put("get_player_claims", UniversalFunctions.getPlayerClaims());

        // q.world.griefdefender.available_rentals() returns array of claims in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double } ]
        map.put("available_rentals", UniversalFunctions.getAvailableRentals(level));

        // q.world.griefdefender.available_forsale() returns array of claims in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double } ]
        map.put("available_forsale", UniversalFunctions.getAvailableForSale(level));

        // q.world.griefdefender.is_wilderness(x, y, z) returns 1 for true or 0
        map.put("is_wilderness", params -> {
            var claim = getClaim(level, params);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            return UniversalFunctions.intToDouble(claim.isWilderness() ? 1 : 0);
        });

        // q.world.griefdefender.get_claim_uuid(x, y, z) returns string or 0
        map.put("get_claim_uuid", params -> {
            var claim = getClaim(level, params);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimOwner = claim.getUUID();
            return claimOwner != null ? new StringValue(claimOwner.toString()) : UniversalFunctions.isNull();
        });

        // q.world.griefdefender.get_claim_name(x, y, z) returns string or 0
        map.put("get_claim_name", params -> {
            var claim = getClaim(level, params);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimOwner = claim.getDisplayName();
            return claimOwner != null ? new StringValue(claimOwner) : UniversalFunctions.isNull();
        });

        // q.world.griefdefender.get_claim_owner_uuid(x, y, z) returns string or 0
        map.put("get_claim_owner_uuid", params -> {
            var claim = getClaim(level, params);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimOwner = claim.getOwnerUUID();
            return claimOwner != null ? new StringValue(claimOwner.toString()) : UniversalFunctions.isNull();
        });

        // q.world.griefdefender.get_claim_owner_name(x, y, z) returns string or 0
        map.put("get_claim_owner_name", params -> {
            var claim = getClaim(level, params);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimOwner = claim.getOwnerName();
            return claimOwner != null ? new StringValue(claimOwner) : UniversalFunctions.isNull();
        });

        return map;
    }

    public ObjectValue<GDWorldExt> asMolangValue() {
        ObjectValue<GDWorldExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(worldFunctions());
        return value;
    }
}
