package dev.matthiesen.cobblemon_npc_extensions.common.economy;

import com.bedrockk.molang.runtime.MoParams;
import com.bedrockk.molang.runtime.value.DoubleValue;
import com.cobblemon.mod.common.api.molang.ObjectValue;
import dev.matthiesen.cobblemon_npc_extensions.common.CobblemonNPCExtensionsCommon;
import dev.matthiesen.matthiesen_core.common.utility.player_data.ServerUser;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record EcoPlayerExt(Player player) {
    public String makeString(EcoPlayerExt player) {
        return "{" + "\"playerUUID\": \"" + player.player().getUUID() + "\"" + "}";
    }

    public ServerUser getServerUser(Player player) {
        return new ServerUser(player);
    }

    public Map<String,? extends Function<MoParams, Object>> playerFunctions() {
        HashMap<String, Function<MoParams, Object>> map = new HashMap<>();

        // q.player.economy.get_balance(<string providerID>, <string currency>) -> double
        map.put("get_balance", moParams -> {
            String providerID = moParams.getString(0);
            String currency = moParams.getString(1);

            var provider = CobblemonNPCExtensionsCommon.INSTANCE.getEconomyManager().getEconomyProvider(providerID);
            if (provider == null) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to get balance for player " + player.getUUID() + " because provider " + providerID + " was not found");
                return new DoubleValue(0);
            }

            int balance = provider.getBalance(getServerUser(player), currency);
            return new DoubleValue(balance);
        });

        // q.player.economy.deposit(<string providerID>, <int amount>, <string currency>) -> 1 for success, otherwise 0
        map.put("deposit", moParams -> {
            String providerID = moParams.getString(0);
            int amount = moParams.getInt(1);
            String currency = moParams.getString(2);

            var provider = CobblemonNPCExtensionsCommon.INSTANCE.getEconomyManager().getEconomyProvider(providerID);
            if (provider == null) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to deposit for player " + player.getUUID() + " because provider " + providerID + " was not found");
                return new DoubleValue(0);
            }

            try {
                provider.deposit(getServerUser(player), amount, currency);
                return new DoubleValue(1);
            } catch (Exception e) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to deposit for player " + player.getUUID() + " because of an error: " + e.getMessage());
                return new DoubleValue(0);
            }
        });

        // q.player.economy.withdraw(<string providerID>, <int amount>, <string currency>) -> 1 for success, otherwise 0
        map.put("withdraw", moParams -> {
            String providerID = moParams.getString(0);
            int amount = moParams.getInt(1);
            String currency = moParams.getString(2);

            var provider = CobblemonNPCExtensionsCommon.INSTANCE.getEconomyManager().getEconomyProvider(providerID);
            if (provider == null) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to withdraw for player " + player.getUUID() + " because provider " + providerID + " was not found");
                return new DoubleValue(0);
            }

            boolean success = provider.withdraw(getServerUser(player), amount, currency);
            return new DoubleValue(success ? 1 : 0);
        });

        // q.player.economy.has_enough(<string providerID>, <int amount>, <string currency>) -> 1 for true, otherwise 0
        map.put("has_enough", moParams -> {
            String providerID = moParams.getString(0);
            int amount = moParams.getInt(1);
            String currency = moParams.getString(2);

            var provider = CobblemonNPCExtensionsCommon.INSTANCE.getEconomyManager().getEconomyProvider(providerID);
            if (provider == null) {
                CobblemonNPCExtensionsCommon.INSTANCE.createErrorLog("Failed to check if player " + player.getUUID() + " has enough because provider " + providerID + " was not found");
                return new DoubleValue(0);
            }

            boolean hasEnough = provider.hasEnough(getServerUser(player), amount, currency);
            return new DoubleValue(hasEnough ? 1 : 0);
        });

        return map;
    }

    public ObjectValue<EcoPlayerExt> asMolangValue() {
        ObjectValue<EcoPlayerExt> value = new ObjectValue<>(this, this::makeString, d -> 1.0);
        value.functions.putAll(playerFunctions());
        return value;
    }
}
