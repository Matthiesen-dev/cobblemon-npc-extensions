package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.claim;

import com.cobblemon.mod.common.api.molang.ObjectValue;

public record ForSaleClaim(
        String uuid,
        String displayName,
        String ownerUUID,
        String ownerName,
        String spawnPos,
        boolean isForSale,
        double salePrice
) implements ClaimData.ForSale {
    public static String makeString(ForSaleClaim claimData) {
        return "{" + claimData.baseJsonFields() + ", " +
                claimData.forSaleJsonFields() +
                "}";
    }

    public ObjectValue<ForSaleClaim> asMolangValue() {
        return new ObjectValue<>(this, ForSaleClaim::makeString, d -> 1.0);
    }
}
