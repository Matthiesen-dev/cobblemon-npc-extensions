package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.cobblemon.mod.common.api.molang.MoLangFunctions;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;

import java.util.HashMap;
import java.util.function.Function;

public final class GriefDefenderCompat {
    public static void init() {
        CobblemonNPCExtensionsCommon.INSTANCE.createInfoLog("GriefDefender is loaded, initializing GriefDefender compatibility");

        MoLangFunctions.INSTANCE.getNpcFunctions().add(npcEntity -> {
           HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.npc.griefdefender() -> { "npcUUID": "string" }
            // q.npc.griefdefender.economy_enabled() returns 1 for true, or 0
            // q.npc.griefdefender.get_player_claims(<uuid>) returns array of claims for user in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
            // q.npc.griefdefender.is_wilderness() returns 1 for true, or 0
            // q.npc.griefdefender.available_rentals() returns array of claims in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double, "isForRent": false, "isRented": false, "renter": "string", "paymentType": "string", "rentMinTime": 0, "rentMaxTime": 0 } ]
            // q.npc.griefdefender.available_forsale() returns array of claims in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double } ]
            // q.npc.griefdefender.claim_data() returns claim data for the npc in the following format
            // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" }
            // q.npc.griefdefender.claim_uuid() returns string or 0
            // q.npc.griefdefender.claim_name() returns string or 0
            // q.npc.griefdefender.claim_owner_uuid() returns string or 0
            // q.npc.griefdefender.claim_owner_name() returns string or 0
            // q.npc.griefdefender.claim_rental_data() returns object containing claim info and rental rate in the following format
            // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double, "isForRent": false, "isRented": false, "renter": "string", "paymentType": "string", "rentMinTime": 0, "rentMaxTime": 0 }
            // q.npc.griefdefender.claim_sale_data() returns object containing claim info and sale data in the following format
            // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double }
            // q.npc.griefdefender.claim_tax_data() returns object containing claim info and tax data in the following format
            // { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "spawnPos": "string", "taxPastDueDate": "string", "taxBalance": double }
            map.put("griefdefender", moParams -> new GDNpcExt(npcEntity).asMolangValue());

           return map;
        });

        MoLangFunctions.INSTANCE.getPlayerFunctions().add(player -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.player.griefdefender() -> { "playerUUID": "string" }
            // q.player.griefdefender.economy_enabled() returns 1 for true, or 0
            // q.player.griefdefender.available_rentals() returns array of claims in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double } ]
            // q.player.griefdefender.available_forsale() returns array of claims in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double } ]
            // q.npc.griefdefender.claims(<uuid>) returns array of claims for user in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
            // q.player.griefdefender.current_claim() returns UUID as string or 0;
            // q.player.griefdefender.accrued_claim_blocks() returns an Integer
            // q.player.griefdefender.blocks_accrued_per_hour() returns an Integer
            // q.player.griefdefender.max_accrued_claim_blocks() returns an Integer
            // q.player.griefdefender.max_bonus_claim_blocks() returns an Integer
            // q.player.griefdefender.max_claim_level() returns an Integer
            // q.player.griefdefender.min_claim_level() returns an Integer
            // q.player.griefdefender.bonus_claim_blocks() returns an Integer
            // q.player.griefdefender.initial_claim_blocks() returns an Integer
            // q.player.griefdefender.remaining_claim_blocks() returns an Integer
            // q.player.griefdefender.max_claimable_blocks() returns an Integer
            // q.player.griefdefender.rental_limit() returns an Integer
            // q.player.griefdefender.start_purchase(<claimUUID>) returns 1 for success or 0
            // TODO q.player.griefdefender.start_rental(<claimUUID>) returns 1 for success or 0
            map.put("griefdefender", moParams -> new GDPlayerExt(player).asMolangValue());

            return map;
        });

        MoLangFunctions.INSTANCE.getServerFunctions().add(server -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.server.griefdefender() -> { "serverPlatform": "string" }
            // q.server.griefdefender.economy_enabled() returns 1 for true, or 0
            // q.server.griefdefender.get_player_claims(<uuid>) returns array of claims for user in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
            map.put("griefdefender", moParams -> new GDServerExt(server).asMolangValue());

            return map;
        });

        MoLangFunctions.INSTANCE.getWorldFunctions().add(levelHolder -> {
            HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

            // q.world.griefdefender() -> { "location": "string" }
            // q.world.griefdefender.economy_enabled() returns 1 for true, or 0
            // q.world.griefdefender.get_player_claims(<uuid>) returns array of claims for user in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
            // q.world.griefdefender.available_rentals() returns array of claims in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double } ]
            // q.world.griefdefender.available_forsale() returns array of claims in the following format
            // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double } ]
            // q.world.griefdefender.is_wilderness(x, y, z) returns 1 for true or 0
            // q.world.griefdefender.get_claim_uuid(x, y, z) returns string or 0
            // q.world.griefdefender.get_claim_name(x, y, z) returns string or 0
            // q.world.griefdefender.get_claim_owner_uuid(x, y, z) returns string or 0
            // q.world.griefdefender.get_claim_owner_name(x, y, z) returns string or 0
            map.put("griefdefender", moParams -> new GDWorldExt(levelHolder.value()).asMolangValue());

            return map;
        });
    }
}
