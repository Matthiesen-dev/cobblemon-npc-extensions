package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.universal;

import com.bedrockk.molang.runtime.MoParams;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.ClaimResult;
import com.griefdefender.api.claim.ClaimType;
import com.griefdefender.api.claim.ClaimTypes;
import com.griefdefender.api.economy.PaymentType;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.economy.ImpactorProvider;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDLocation;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDUser;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Function;

public final class EconomyFunctions {
    public static String claimNotFound(String claimUUIDString) {
        return "Claim with UUID " + claimUUIDString + " not found. Please contact an administrator.";
    }

    public static String claimNotForSale(String claimUUIDString) {
        return "Claim with UUID " + claimUUIDString + " is not for sale. Please contact an administrator.";
    }

    public static String claimNotForRent(String claimUUIDString) {
        return "Claim with UUID " + claimUUIDString + " is not for rent. Please contact an administrator.";
    }

    public static String claimAlreadyRented(String claimUUIDString) {
        return "Claim with UUID " + claimUUIDString + " is already rented. Please contact an administrator.";
    }

    public static String claimMissingOwner(String claimUUIDString) {
        return "Claim with UUID " + claimUUIDString + " is missing an owner. Please contact an administrator.";
    }

    public static String cannotPurchaseOwnClaim() {
        return "You cannot purchase your own claim.";
    }

    public static String economyAccountError() {
        return "An error occurred while trying to access your economy account. Please contact an administrator.";
    }

    public static String notEnoughFundsPurchase(double amount) {
        return "You do not have enough funds to purchase this claim. You need " + amount + " more.";
    }

    public static String notEnoughFundsRent(double amount) {
        return "You do not have enough funds to rent this claim. You need " + amount + " more.";
    }

    public static String delinquentRenter() {
        return "You have delinquent rent payments. Please contact an administrator.";
    }

    public static String failedToTransfer(String string) {
        return "Failed to transfer " + string + ". Please contact an administrator.";
    }

    public static String noRentalLimit() {
        return "You do not have a rental limit set. Please contact an administrator.";
    }

    public static String claimOwnerNotFound(String claimUUIDString) {
        return "Claim with UUID " + claimUUIDString + " has an owner that could not be found. Please contact an administrator.";
    }

    public static <T> boolean isNull(T result, Player player, Component errorMessage) {
        if (result == null) {
            player.sendSystemMessage(errorMessage.copy().withStyle(ChatFormatting.RED));
            return true;
        }
        return false;
    }

    public static boolean isFalse(boolean condition, Player player, Component errorMessage) {
        if (!condition) {
            player.sendSystemMessage(errorMessage.copy().withStyle(ChatFormatting.RED));
            return true;
        }
        return false;
    }

    public static boolean isFalse(boolean condition, Player player, Component errorMessage, Runnable callback) {
        if (!condition) {
            callback.run();
            player.sendSystemMessage(errorMessage.copy().withStyle(ChatFormatting.RED));
            return true;
        }
        return false;
    }

    public static boolean isTrue(boolean condition, Player player, Component errorMessage) {
        if (condition) {
            player.sendSystemMessage(errorMessage.copy().withStyle(ChatFormatting.RED));
            return true;
        }
        return false;
    }

    public static void sendPlayerMsgIfNotNull(Player player, Component component) {
        if (player != null) {
            player.sendSystemMessage(component);
        }
    }

    public static Function<MoParams, Object> purchaseClaim(Player player) {
        return params -> {
            String claimUUIDString = params.getString(0);
            UUID claimUUID = UUID.fromString(claimUUIDString);
            GDLocation claim = GDLocation.fromUUID(claimUUID);
            if (isNull(claim, player, Component.literal(claimNotFound(claimUUIDString)))) {
                return 0;
            }
            assert claim != null;

            var claimData = claim.getClaimData().toForSaleClaim();

            if (isNull(claim.getOwnerUUID(), player, Component.literal(claimMissingOwner(claimUUIDString)))) {
                return 0;
            }

            GDUser owner = new GDUser(claim.getOwnerUUID());

            if (isFalse(claimData.isForSale(), player, Component.literal(claimNotForSale(claimUUIDString)))) {
                return 0;
            }

            if (isTrue(player.getUUID().toString().equalsIgnoreCase(claimData.ownerUUID()), player, Component.literal(cannotPurchaseOwnClaim()))) {
                return 0;
            }

            ImpactorProvider ecoProvider = CobblemonNPCExtensionsCommon.INSTANCE.getEcoProvider();

            if (isFalse(ecoProvider.hasAccount(player), player, Component.literal(economyAccountError()))) {
                return 0;
            }

            double balance = ecoProvider.getBalance(player);

            if (isTrue(balance < claimData.salePrice(), player, Component.literal(notEnoughFundsPurchase(claimData.salePrice() - balance)))) {
                return 0;
            }

            Claim gdClaim = claim.getClaim();

            if (isNull(gdClaim, player, Component.literal(claimNotFound(claimUUIDString)))) {
                return 0;
            }
            assert gdClaim != null;

            ClaimType originalType = gdClaim.getType();

            if (gdClaim.isAdminClaim()) {
                gdClaim.getData().setType(ClaimTypes.BASIC);
            }

            ClaimResult result = gdClaim.transferOwner(player.getUUID());

            if (isFalse(result.successful(), player, Component.literal(failedToTransfer("claim ownership")), () -> {
                gdClaim.getData().setType(originalType);
            })) {
                return 0;
            }

            double salePrice = claimData.salePrice();
            boolean transactionSuccess = owner.getOfflinePlayer() == null || ecoProvider.depositPlayer(owner.getOfflinePlayer(), salePrice);

            if (isFalse(transactionSuccess, player, Component.literal(failedToTransfer("funds")), () -> {
                gdClaim.getData().setType(originalType);
                gdClaim.transferOwner(UUID.fromString(claimData.ownerUUID()));
            })) {
                return 0;
            }

            ecoProvider.withdrawFunds(player, salePrice);

            Component message = Component.literal("You have successfully purchased the claim for " + salePrice + " currency units.")
                    .withStyle(ChatFormatting.GREEN);
            Component saleMessage = Component.literal("Your claim has been purchased by " + player.getName().getString() + " for " + salePrice + " currency units.")
                    .withStyle(ChatFormatting.GREEN);

            var ownerPlayer = owner.getOnlinePlayer();

            sendPlayerMsgIfNotNull(ownerPlayer, saleMessage);

            gdClaim.getEconomyData().setForSale(false);
            gdClaim.getEconomyData().setSalePrice(0.0F);
            gdClaim.getData().save();

            player.sendSystemMessage(message);

            return 1;
        };
    }

    public static Function<MoParams, Object> rentClaim(Player player) {
        return params -> {
            String claimUUIDString = params.getString(0);
            UUID claimUUID = UUID.fromString(claimUUIDString);
            GDLocation claim = GDLocation.fromUUID(claimUUID);
            return 1;
        };
    }

    public static ChronoUnit getPaymentChronoUnit(PaymentType type) {
        ChronoUnit unit = ChronoUnit.DAYS;
        if (type == PaymentType.HOURLY) {
            unit = ChronoUnit.HOURS;
        } else if (type == PaymentType.MONTHLY) {
            unit = ChronoUnit.MONTHS;
        } else if (type == PaymentType.WEEKLY) {
            unit = ChronoUnit.WEEKS;
        }
        return unit;
    }
}
