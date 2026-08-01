package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.universal;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.DoubleValue;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDCollectors;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data.GDUtils;
import net.minecraft.world.level.Level;

import java.util.UUID;
import java.util.function.Function;

public final class UniversalFunctions {
    public static DoubleValue intToDouble(int val) {
        return new DoubleValue((double) val);
    }

    public static DoubleValue isNull() {
        return new DoubleValue(0);
    }

    public static Function<MoParams, Object> getPlayerClaims() {
        return params -> {
            String stringUuid = params.getString(0);
            UUID uuid = UUID.fromString(stringUuid);
            return GDCollectors.getPlayerClaims(uuid);
        };
    }

    public static Function<MoParams, Object> isEconomyEnabled() {
        return params -> intToDouble(GDUtils.isEconomyEnabled() ? 1 : 0);
    }

    public static Function<MoParams, Object> getAvailableRentals(Level level) {
        return params -> GDCollectors.getRentals(level);
    }

    public static Function<MoParams, Object> getAvailableForSale(Level level) {
        return params -> GDCollectors.getForSale(level);
    }
}
