package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.claim;

public interface ClaimData {
    String uuid();
    String displayName();
    String ownerUUID();
    String ownerName();
    String spawnPos();

    default String baseJsonFields() {
        return "\"uuid\": \"" + uuid() + "\", " +
                "\"displayName\": \"" + displayName() + "\", " +
                "\"ownerUUID\": \"" + ownerUUID() + "\", " +
                "\"ownerName\": \"" + ownerName() + "\", " +
                "\"spawnPos\": \"" + spawnPos() + "\"";
    }

    interface ForSale extends ClaimData {
        boolean isForSale();
        double salePrice();

        default String forSaleJsonFields() {
            return "\"isForSale\": " + isForSale() + ", " +
                    "\"salePrice\": " + salePrice();
        }
    }

    interface Rental extends ClaimData {
        boolean isForRent();
        boolean isRented();
        double rentalRate();
        String renter();
        String paymentType();
        int rentMinTime();
        int rentMaxTime();

        default String rentalJsonFields() {
            return "\"isForRent\": " + isForRent() + ", " +
                    "\"isRented\": " + isRented() + ", " +
                    "\"rentalRate\": \"" + rentalRate() + "\", " +
                    "\"renter\": \"" + renter() + "\", " +
                    "\"paymentType\": \"" + paymentType() + "\", " +
                    "\"rentMinTime\": \"" + rentMinTime() + "\", " +
                    "\"rentMaxTime\": \"" + rentMaxTime() + "\"";
        }
    }

    interface Taxed extends ClaimData {
        String taxPastDueDate();
        double taxBalance();

        default String taxedJsonFields() {
            return "\"taxPastDueDate\": \"" + taxPastDueDate() + "\", " +
                    "\"taxBalance\": " + taxBalance();
        }
    }
}

