package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.StringValue;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDLocation;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDUtils;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.universal.UniversalFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public record GDNpcExt(NPCEntity npcEntity) {
    public String makeString(GDNpcExt npc) {
        return "{" + "\"npcUUID\": \"" + npc.npcEntity().getUUID() + "\"" + "}";
    }

    private GDLocation getClaim(NPCEntity npcEntity) {
        Level level = npcEntity.getCommandSenderWorld();
        BlockPos pos = npcEntity.getOnPos();
        return GDUtils.getClaim(level, pos.getX(), pos.getY() + 1, pos.getZ());
    }

    public Map<String, ? extends Function<MoParams, Object>> npcFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        // q.npc.griefdefender.economy_enabled() returns 1 for true, or 0
        map.put("economy_enabled", UniversalFunctions.isEconomyEnabled());

        // q.npc.griefdefender.get_player_claims(<uuid>) returns array of claims for user in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
        map.put("get_player_claims", UniversalFunctions.getPlayerClaims());

        // q.npc.griefdefender.is_wilderness() returns 1 for true, or 0
        map.put("is_wilderness", params -> UniversalFunctions.intToDouble(getClaim(npcEntity).isWilderness() ? 1 : 0));

        // q.npc.griefdefender.available_rentals() returns array of claims in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double, "isForRent": false, "isRented": false, "renter": "string", "paymentType": "string", "rentMinTime": 0, "rentMaxTime": 0 } ]
        map.put("available_rentals", UniversalFunctions.getAvailableRentals(npcEntity.getCommandSenderWorld()));

        // q.npc.griefdefender.available_forsale() returns array of claims in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double } ]
        map.put("available_forsale", UniversalFunctions.getAvailableForSale(npcEntity.getCommandSenderWorld()));

        // q.npc.griefdefender.claim_data() returns claim data for the npc in the following format
        // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" }
        map.put("claim_data", params -> {
            var claim = getClaim(npcEntity);
            var claimData = claim.getClaimData().toSimpleClaim();
            return claimData.asMolangValue();
        });

        // q.npc.griefdefender.claim_uuid() returns string or 0
        map.put("claim_uuid", params -> {
            var claim = getClaim(npcEntity);
            UUID claimUUID = claim.getUUID();
            return claimUUID != null ? new StringValue(claimUUID.toString()) : UniversalFunctions.isNull();
        });

        // q.npc.griefdefender.claim_name() returns string or 0
        map.put("claim_name", params -> {
            var claim = getClaim(npcEntity);
            String displayName = claim.getDisplayName();
            return displayName != null ? new StringValue(displayName) : UniversalFunctions.isNull();
        });

        // q.npc.griefdefender.claim_owner_uuid() returns string or 0
        map.put("claim_owner_uuid", params -> {
            var claim = getClaim(npcEntity);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimOwner = claim.getOwnerUUID();
            return claimOwner != null ? new StringValue(claimOwner.toString()) : UniversalFunctions.isNull();
        });

        // q.npc.griefdefender.claim_owner_name() returns string or 0
        map.put("claim_owner_name", params -> {
            var claim = getClaim(npcEntity);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimOwner = claim.getOwnerName();
            return claimOwner != null ? new StringValue(claimOwner) : UniversalFunctions.isNull();
        });

        // q.npc.griefdefender.claim_rental_data() returns object containing claim info and rental rate in the following format
        // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double, "isForRent": false, "isRented": false, "renter": "string", "paymentType": "string", "rentMinTime": 0, "rentMaxTime": 0 }
        map.put("claim_rental_data", params -> {
            var claim = getClaim(npcEntity);
            var rental = claim.getClaimData().toRentalClaim();
            return rental.asMolangValue();
        });

        // q.npc.griefdefender.claim_sale_data() returns object containing claim info and sale data in the following format
        // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double }
        map.put("claim_sale_data", params -> {
            var claim = getClaim(npcEntity);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimData = claim.getClaimData().toForSaleClaim();
            return claimData.asMolangValue();
        });

        // q.npc.griefdefender.claim_tax_data() returns object containing claim info and tax data in the following format
        // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "spawnPos": "string", "taxPastDueDate": "string", "taxBalance": double }
        map.put("claim_tax_data", params -> {
            var claim = getClaim(npcEntity);
            if (claim.getClaim() == null) return UniversalFunctions.isNull();
            var claimData = claim.getClaimData().toTaxedClaim();
            return claimData.asMolangValue();
        });

        return map;
    }

    public ObjectValue<GDNpcExt> asMolangValue() {
        ObjectValue<GDNpcExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(npcFunctions());
        return value;
    }
}
