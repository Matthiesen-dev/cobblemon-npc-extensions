package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data;

import com.griefdefender.api.Core;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.ClaimManager;
import com.griefdefender.api.data.PlayerData;
import net.minecraft.world.level.Level;

import java.util.UUID;

public final class GDUtils {
    public static final UUID PUBLIC_UUID = UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77");
    public static final UUID WORLD_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final UUID ADMIN_USER_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID PLOT_USER_UUID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID TEMP_USER_UUID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    public static Core getGriefDefenderCore() {
        return GriefDefender.getCore();
    }

    public static boolean isEconomyEnabled() {
        return getGriefDefenderCore().isEconomyModeEnabled();
    }

    public static UUID getWorldID(Level level) {
        return getGriefDefenderCore().getWorldUniqueId(level);
    }

    public static PlayerData getPlayerData(Level level, UUID playerUUID) {
        return getGriefDefenderCore().getPlayerData(getWorldID(level), playerUUID);
    }

    public static PlayerData getPlayerData(UUID worldId, UUID playerUUID) {
        return getGriefDefenderCore().getPlayerData(worldId, playerUUID);
    }

    public static GDLocation getClaim(Level level, int x, int y, int z) {
        return new GDLocation(level, x, y, z);
    }

    public static ClaimManager getClaimManager(UUID uuid) {
        return getGriefDefenderCore().getClaimManager(uuid);
    }
}
