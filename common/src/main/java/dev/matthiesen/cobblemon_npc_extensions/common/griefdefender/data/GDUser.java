package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.data;

import com.griefdefender.api.User;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.data.PlayerData;
import com.mojang.authlib.GameProfile;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import dev.matthiesen.matthiesen_core.common.core.data.fakeplayer.FakePlayerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public final class GDUser implements User {
    private String userName;
    private final String identifier;
    private final UUID uniqueId;
    private UUID worldUniqueId;
    private Player offlinePlayer;
    private PlayerData playerData;

    public GDUser(ServerPlayer player) {
        this.uniqueId = player.getUUID();
        this.identifier = player.getUUID().toString();
        this.worldUniqueId = GDUtils.getWorldID(player.level());
        this.offlinePlayer = player;
        this.userName = player.getScoreboardName();
    }

    public GDUser(Player player) {
        this.uniqueId = player.getUUID();
        this.identifier = player.getUUID().toString();
        this.userName = player.getScoreboardName();
    }

    public GDUser(UUID uuid) {
        this.uniqueId = uuid;
        this.identifier = uuid.toString();
    }

    public GDUser(UUID uuid, String name) {
        this.uniqueId = uuid;
        this.identifier = uuid.toString();
        this.userName = name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public PlayerData getPlayerData() {
        if (this.playerData == null) {
            this.playerData = GDUtils.getPlayerData(worldUniqueId, uniqueId);
        }

        return this.playerData;
    }

    @Override
    public boolean isOnline() {
        return getOnlinePlayer() != null;
    }

    @Override
    public ServerPlayer getOnlinePlayer() {
        return CobblemonNPCExtensionsCommon.INSTANCE.getCommonUtils().getServer().getPlayerList().getPlayer(uniqueId);
    }

    @Override
    public Claim getCurrentClaim() {
        return this.getPlayerData().getCurrentClaim();
    }

    public String getName() {
        if (this.userName == null) {
            if (this.uniqueId.equals(GDUtils.PUBLIC_UUID)) {
                this.userName = "public";
            } else if (!this.uniqueId.equals(GDUtils.ADMIN_USER_UUID) && !this.uniqueId.equals(GDUtils.WORLD_USER_UUID)) {
                if (this.getOfflinePlayer() != null) {
                    this.userName = this.getOfflinePlayer().getScoreboardName();
                } else {
                    this.userName = this.getFriendlyName();
                }
            } else {
                this.userName = "administrator";
            }

            if (this.userName == null) {
                this.userName = "unknown";
            }
        }

        return this.userName;
    }

    public Player getOfflinePlayer() {
        Player player = this.getOnlinePlayer();
        if (player != null) {
            return player;
        } else {
            if (this.offlinePlayer == null) {
                MinecraftServer server = CobblemonNPCExtensionsCommon.INSTANCE.getCommonUtils().getServer();
                this.offlinePlayer = server.getPlayerList().getPlayer(this.uniqueId);

                if (this.offlinePlayer == null) {
                    GameProfileCache gameProfileCache = server.getProfileCache();
                    if (gameProfileCache != null) {
                        GameProfile gameProfile = gameProfileCache.get(this.uniqueId).orElse(null);
                        if (gameProfile != null) {
                            ServerLevel level = server.getLevel(Level.OVERWORLD);
                            if (level == null) {
                                return null;
                            }
                            return FakePlayerFactory.get(level, gameProfile);
                        }
                    }
                }
            }

            return this.offlinePlayer;
        }
    }

    @Override
    public String getFriendlyName() {
        return this.getName();
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }
}
