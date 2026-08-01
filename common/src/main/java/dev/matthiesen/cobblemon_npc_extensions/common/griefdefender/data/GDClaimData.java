package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data;

import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.economy.PaymentType;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.claim.*;

import java.util.UUID;

public record GDClaimData(Claim gdClaim) {

    public static GDClaimData fromGDLocation(GDLocation location) {
        Claim claim = location.getClaim();
        if (claim == null) return null;
        return fromClaim(claim);
    }

    public static GDClaimData fromClaim(Claim claim) {
        if (claim == null) return null;
        return new GDClaimData(claim);
    }

    public String getBlockPos() {
        String blockPos = "unknown";

        var spawnPos = gdClaim.getData().getSpawnPos();
        if (spawnPos != null) {
            blockPos = spawnPos.getX() + " " + spawnPos.getY() + " " + spawnPos.getZ();
        }
        return blockPos;
    }

    public SimpleClaim toSimpleClaim() {
        return new SimpleClaim(
                gdClaim.getUniqueId().toString(),
                gdClaim.getDisplayName(),
                gdClaim.getOwnerUniqueId().toString(),
                gdClaim.getOwnerName(),
                getBlockPos()
        );
    }

    public ForSaleClaim toForSaleClaim() {
        var economyData = gdClaim.getEconomyData();
        boolean isForSale = economyData.isForSale();
        double salePrice = economyData.getSalePrice();

        return new ForSaleClaim(
                gdClaim.getUniqueId().toString(),
                gdClaim.getDisplayName(),
                gdClaim.getOwnerUniqueId().toString(),
                gdClaim.getOwnerName(),
                getBlockPos(),
                isForSale,
                salePrice
        );
    }

    public RentalClaim toRentalClaim() {
        var economyData = gdClaim.getEconomyData();
        boolean isForRent = gdClaim.getEconomyData().isForRent();
        boolean isRented = gdClaim.getEconomyData().isRented();
        double rentalRate = 0.0;
        UUID renter = null;
        PaymentType paymentType = PaymentType.UNDEFINED;
        int rentMinTime = 0;
        int rentMaxTime = 0;

        if (isForRent) {
            rentalRate = economyData.getRentRate() > (double) -1.0F ? gdClaim.getEconomyData().getRentRate() : 0.0;
            renter = economyData.getRenters().getFirst();
            paymentType = economyData.getPaymentType();
            rentMinTime = economyData.getRentMinTime();
            rentMaxTime = economyData.getRentMaxTime();
        }

        return new RentalClaim(
                gdClaim.getUniqueId().toString(),
                gdClaim.getDisplayName(),
                gdClaim.getOwnerUniqueId().toString(),
                gdClaim.getOwnerName(),
                getBlockPos(),
                isForRent,
                isRented,
                rentalRate,
                renter != null ? new GDUser(renter).getFriendlyName() : "not available",
                RentalClaim.paymentTypeToString(paymentType),
                rentMinTime,
                rentMaxTime
        );
    }

    public TaxedClaim toTaxedClaim() {
        var economyData = gdClaim.getEconomyData();
        String taxPastDueDate = economyData.getTaxPastDueDate() != null ? economyData.getTaxPastDueDate().toString() : "unknown";
        double taxBalance = economyData.getTaxBalance();

        return new TaxedClaim(
                gdClaim.getUniqueId().toString(),
                gdClaim.getDisplayName(),
                gdClaim.getOwnerUniqueId().toString(),
                gdClaim.getOwnerName(),
                getBlockPos(),
                taxPastDueDate,
                taxBalance
        );
    }

}
