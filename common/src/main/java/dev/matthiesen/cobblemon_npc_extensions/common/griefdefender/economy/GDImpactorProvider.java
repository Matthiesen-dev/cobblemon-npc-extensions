package dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.economy;

import net.impactdev.impactor.api.Impactor;
import net.impactdev.impactor.api.economy.EconomyService;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.currency.Currency;
import net.impactdev.impactor.api.economy.transactions.EconomyTransaction;
import net.minecraft.world.entity.player.Player;

import java.math.BigDecimal;
import java.util.UUID;

public final class GDImpactorProvider {
    private EconomyService vaultApi;

    public GDImpactorProvider() {
        this.initEconomy();
    }

    public EconomyService getApi() {
        if (this.vaultApi == null) {
            this.initEconomy();
        }

        return this.vaultApi;
    }

    private void initEconomy() {
        this.vaultApi = Impactor.instance().services().provide(EconomyService.class);
    }

    public boolean hasAccount(Player player) {
        if (this.getApi() == null) {
            return false;
        } else {
            try {
                return this.vaultApi.hasAccount(player.getUUID()).get();
            } catch (Throwable var3) {
                return false;
            }
        }
    }

    public double getBalance(Player player) {
        return this.getBalance(player.getUUID());
    }

    public double getBalance(UUID uuid) {
        if (this.getApi() == null) {
            return 0.0F;
        } else {
            Currency currency = this.vaultApi.currencies().primary();
            Account account = this.vaultApi.account(currency, uuid).join();
            return account.balance().doubleValue();
        }
    }

    public boolean depositPlayer(Player player, double amount) {
        return this.depositPlayer(player.getUUID(), amount);
    }

    public boolean depositPlayer(UUID uuid, double amount) {
        if (this.getApi() == null) {
            return false;
        } else {
            Currency currency = this.vaultApi.currencies().primary();
            Account account = this.vaultApi.account(currency, uuid).join();
            EconomyTransaction transaction = account.deposit(BigDecimal.valueOf(amount));
            return transaction.successful();
        }
    }

    public void withdrawFunds(Player player, double funds) {
        this.withdrawFunds(player.getUUID(), funds);
    }

    public void withdrawFunds(UUID uuid, double funds) {
        double balance = this.getBalance(uuid);
        if (!(funds < (double)0.0F) && !(balance < funds)) {
            Currency currency = this.vaultApi.currencies().primary();
            Account account = this.vaultApi.account(currency, uuid).join();
            EconomyTransaction transaction = account.withdraw(BigDecimal.valueOf(funds));
            transaction.successful();
        }
    }
}
