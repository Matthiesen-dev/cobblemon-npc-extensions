package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data;

import com.griefdefender.api.claim.Claim;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record GDLocation(Level level, int x, int y, int z) {
    public static GDLocation fromUUID(UUID claimID) {
        var claim = GDUtils.getGriefDefenderCore().getClaim(claimID);
        if (claim == null) return null;
        var claimData = claim.getData();
        if (claimData == null) return null;
        var spawnPos = claimData.getSpawnPos();
        if (spawnPos == null) return null;
        Level level = getLevelFromWorldID(claim.getWorldName());
        return new GDLocation(level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
    }

    public static Level getLevelFromWorldID(String worldName) {
        var server = CobblemonNPCExtensionsCommon.INSTANCE.getCommonUtils().getServer();
        if (server == null) return null;
        var levels = server.getAllLevels();

        for (ServerLevel level : levels) {
            if (level.dimension().location().toString().equalsIgnoreCase(worldName)) {
                return level;
            }
        }

        return null;
    }

    public @Nullable Claim getClaim() {
        UUID worldID = GDUtils.getWorldID(level);
        return GDUtils.getGriefDefenderCore().getClaimAt(worldID, x, y, z);
    }

    public GDClaimData getClaimData() {
        return GDClaimData.fromGDLocation(this);
    }

    public boolean isWilderness() {
        Claim claim = getClaim();
        if (claim == null) return false;
        return claim.isWilderness();
    }

    public UUID getUUID() {
        Claim claim = getClaim();
        if (claim == null) return null;
        return claim.getUniqueId();
    }

    public String getDisplayName() {
        Claim claim = getClaim();
        if (claim == null) return null;
        return claim.getDisplayName();
    }

    public UUID getOwnerUUID() {
        Claim claim = getClaim();
        if (claim == null) return null;
        return claim.getOwnerUniqueId();
    }

    public String getOwnerName() {
        Claim claim = getClaim();
        if (claim == null) return null;
        return claim.getOwnerName();
    }
}
