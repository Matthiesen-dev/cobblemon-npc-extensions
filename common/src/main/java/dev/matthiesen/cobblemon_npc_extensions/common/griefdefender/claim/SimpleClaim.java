package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.claim;

import com.cobblemon.mod.common.api.molang.ObjectValue;

public record SimpleClaim(
        String uuid,
        String displayName,
        String ownerUUID,
        String ownerName,
        String spawnPos
) implements ClaimData {
    public static String makeString(SimpleClaim claimData) {
        return "{" + claimData.baseJsonFields() + "}";
    }

    public ObjectValue<SimpleClaim> asMolangValue() {
        return new ObjectValue<>(this, SimpleClaim::makeString, d -> 1.0);
    }
}
