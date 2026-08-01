package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.claim;

import com.cobblemon.mod.common.api.molang.ObjectValue;

public record TaxedClaim(
        String uuid,
        String displayName,
        String ownerUUID,
        String ownerName,
        String spawnPos,
        String taxPastDueDate,
        double taxBalance
) implements ClaimData.Taxed {
    public static String makeString(TaxedClaim claimData) {
        return "{" + claimData.baseJsonFields() + ", " +
                claimData.taxedJsonFields() +
                "}";
    }

    public ObjectValue<TaxedClaim> asMolangValue() {
        return new ObjectValue<>(this, TaxedClaim::makeString, d -> 1.0);
    }
}
