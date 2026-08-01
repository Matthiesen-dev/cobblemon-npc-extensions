package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.claim;

import com.cobblemon.mod.common.api.molang.ObjectValue;
import com.griefdefender.api.economy.PaymentType;

public record RentalClaim(
        String uuid,
        String displayName,
        String ownerUUID,
        String ownerName,
        String spawnPos,
        boolean isForRent,
        boolean isRented,
        double rentalRate,
        String renter,
        String paymentType,
        int rentMinTime,
        int rentMaxTime
) implements ClaimData.Rental {
    public static String paymentTypeToString(PaymentType paymentType) {
        return switch (paymentType) {
            case UNDEFINED -> "undefined";
            case DAILY -> "daily";
            case HOURLY -> "hourly";
            case WEEKLY -> "weekly";
            case MONTHLY -> "monthly";
        };
    }

    public static String makeString(RentalClaim claimData) {
        return "{" + claimData.baseJsonFields() + ", " +
                claimData.rentalJsonFields() +
                "}";
    }

    public ObjectValue<RentalClaim> asMolangValue() {
        return new ObjectValue<>(this, RentalClaim::makeString, d -> 1.0);
    }
}
