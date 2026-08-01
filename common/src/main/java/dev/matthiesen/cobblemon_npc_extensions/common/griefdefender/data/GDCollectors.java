package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data;

import com.cobblemon.mod.common.api.molang.ObjectValue;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.ClaimManager;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.claim.*;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public final class GDCollectors {
    public static <T> @NotNull String makeStringList(List<T> claims, Function<T, String> makeString) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < claims.size(); i++) {
            sb.append(makeString.apply(claims.get(i)));
            if (i < claims.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static ObjectValue<List<SimpleClaim>> makeSimpleClaimList(List<SimpleClaim> claims) {
        return new ObjectValue<>(claims, c -> makeStringList(c, SimpleClaim::makeString), d -> 1.0);
    }

    public static ObjectValue<List<RentalClaim>> makeRentalClaimList(List<RentalClaim> claims) {
        return new ObjectValue<>(claims, c -> makeStringList(c, RentalClaim::makeString), d -> 1.0);
    }

    public static ObjectValue<List<ForSaleClaim>> makeForSaleClaimList(List<ForSaleClaim> claims) {
        return new ObjectValue<>(claims, c -> makeStringList(c, ForSaleClaim::makeString), d -> 1.0);
    }

    public static ObjectValue<List<TaxedClaim>> makeTaxedClaimList(List<TaxedClaim> claims) {
        return new ObjectValue<>(claims, c -> makeStringList(c, TaxedClaim::makeString), d -> 1.0);
    }

    public static ObjectValue<List<SimpleClaim>> getPlayerClaims(UUID player) {
        var list = GDUtils.getGriefDefenderCore().getAllPlayerClaims(player)
                .stream()
                .map(GDClaimData::fromClaim)
                .map(gdClaimData -> gdClaimData.toSimpleClaim())
                .toList();

        return makeSimpleClaimList(list);
    }

    public static ObjectValue<List<RentalClaim>> getRentals(Level level) {
        UUID worldID = GDUtils.getWorldID(level);
        ClaimManager claimManager = GDUtils.getClaimManager(worldID);

        Set<Claim> claimsForRent = new HashSet<>();

        for (Claim worldClaim : claimManager.getWorldClaims()) {
            if (worldClaim.isWilderness()) continue;

            if (worldClaim.getEconomyData().isForRent() && worldClaim.getEconomyData().getRentRate() > (double) -1.0F) {
                claimsForRent.add(worldClaim);
            }

            for(Claim child : worldClaim.getChildren(true)) {
                if (child.getEconomyData().isForRent() && child.getEconomyData().getRentRate() > (double) -1.0F) {
                    claimsForRent.add(child);
                }
            }
        }

        var list = claimsForRent
                .stream()
                .map(GDClaimData::fromClaim)
                .map(gdClaimData -> gdClaimData.toRentalClaim())
                .toList();

        return makeRentalClaimList(list);
    }

    public static ObjectValue<List<ForSaleClaim>> getForSale(Level level) {
        UUID worldID = GDUtils.getWorldID(level);
        ClaimManager claimManager = GDUtils.getClaimManager(worldID);

        Set<Claim> claimsForSale = new HashSet<>();

        for (Claim worldClaim : claimManager.getWorldClaims()) {
            if (worldClaim.isWilderness()) continue;

            if (worldClaim.getEconomyData().isForSale() && worldClaim.getEconomyData().getSalePrice() > (double) -1.0F) {
                claimsForSale.add(worldClaim);
            }

            for (Claim child : worldClaim.getChildren(true)) {
                if (child.getEconomyData().isForSale() && child.getEconomyData().getSalePrice() > (double) -1.0F) {
                    claimsForSale.add(child);
                }
            }
        }

        var list = claimsForSale
                .stream()
                .map(GDClaimData::fromClaim)
                .map(gdClaimData -> gdClaimData.toForSaleClaim())
                .toList();

        return makeForSaleClaimList(list);
    }
}
