package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.StringValue;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import com.griefdefender.api.data.PlayerData;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDCollectors;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDUtils;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.universal.EconomyFunctions;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.universal.UniversalFunctions;
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

        // q.player.griefdefender.economy_enabled() returns 1 for true, or 0
        map.put("economy_enabled", UniversalFunctions.isEconomyEnabled());

        // q.player.griefdefender.available_rentals() returns array of claims in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "rentalRate": double } ]
        map.put("available_rentals", UniversalFunctions.getAvailableRentals(player.level()));

        // q.player.griefdefender.available_forsale() returns array of claims in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string", "isForSale": false, "salePrice": double } ]
        map.put("gd_available_forsale", UniversalFunctions.getAvailableForSale(player.level()));

        // q.npc.griefdefender.claims(<uuid>) returns array of claims for user in the following format
        // [ { "uuid": "string", "displayName": "string", "ownerUUID": "string", "ownerName": "string" } ]
        map.put("claims", params -> GDCollectors.getPlayerClaims(player.getUUID()));

        // q.player.griefdefender.current_claim() returns UUID as string or 0;
        map.put("current_claim", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            var claim = playerData.getCurrentClaim();
            if (claim == null) return UniversalFunctions.isNull();
            return new StringValue(claim.getUniqueId().toString());
        });

        // q.player.griefdefender.accrued_claim_blocks() returns an Integer
        map.put("accrued_claim_blocks", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getAccruedClaimBlocks());
        });

        // q.player.griefdefender.blocks_accrued_per_hour() returns an Integer
        map.put("blocks_accrued_per_hour", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getBlocksAccruedPerHour());
        });

        // q.player.griefdefender.max_accrued_claim_blocks() returns an Integer
        map.put("max_accrued_claim_blocks", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getMaxAccruedClaimBlocks());
        });

        // q.player.griefdefender.max_bonus_claim_blocks() returns an Integer
        map.put("max_bonus_claim_blocks", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getMaxBonusClaimBlocks());
        });

        // q.player.griefdefender.max_claim_level() returns an Integer
        map.put("max_claim_level", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getMaxClaimLevel());
        });

        // q.player.griefdefender.min_claim_level() returns an Integer
        map.put("min_claim_level", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getMinClaimLevel());
        });

        // q.player.griefdefender.bonus_claim_blocks() returns an Integer
        map.put("bonus_claim_blocks", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getBonusClaimBlocks());
        });

        // q.player.griefdefender.initial_claim_blocks() returns an Integer
        map.put("initial_claim_blocks", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getInitialClaimBlocks());
        });

        // q.player.griefdefender.remaining_claim_blocks() returns an Integer
        map.put("remaining_claim_blocks", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getRemainingClaimBlocks());
        });

        // q.player.griefdefender.max_claimable_blocks() returns an Integer
        map.put("max_claimable_blocks", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getMaxClaimableBlocks());
        });

        // q.player.griefdefender.rental_limit() returns an Integer
        map.put("rental_limit", params -> {
            PlayerData playerData = GDUtils.getPlayerData(player.level(), player.getUUID());
            return UniversalFunctions.intToDouble(playerData.getRentalLimit());
        });

        // q.player.griefdefender.start_purchase(<claimUUID>) returns 1 for success or 0
        map.put("start_purchase", EconomyFunctions.purchaseClaim(player));

        // q.player.griefdefender.start_rental(<claimUUID>) returns 1 for success or 0
        // TODO: Rental system is more annoying to ensure will work correctly, leaving this for now as a later task
//            map.put("start_rental", EconomyFunctions.rentClaim(player));

        return map;
    }

    public ObjectValue<GDPlayerExt> asMolangValue() {
        ObjectValue<GDPlayerExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(playerFunctions());
        return value;
    }
}
