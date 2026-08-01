package dev.matthiesen.cobblemon_npc_extensions.common.economy.impactor;

import com.griefdefender.api.claim.Claim;
import net.impactdev.impactor.api.Impactor;
import net.impactdev.impactor.api.economy.EconomyService;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.currency.Currency;
import net.impactdev.impactor.api.economy.transactions.EconomyTransaction;
import net.minecraft.world.entity.player.Player;

import java.math.BigDecimal;
import java.util.UUID;

public final class ImpactorProvider {
    private EconomyService vaultApi;

    public ImpactorProvider() {
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
        return this.getBalance(player.getUUID(), true);
    }

    public double getBalance(UUID uuid, boolean isPlayer) {
        if (this.getApi() == null) {
            return 0.0F;
        } else {
            Currency currency = this.vaultApi.currencies().primary();
            Account account = this.vaultApi.account(currency, uuid).join();
            return account.balance().doubleValue();
        }
    }

    public boolean depositPlayer(Player player, double amount) {
        return this.depositPlayer(player.getUUID(), amount, true);
    }

    public boolean depositPlayer(UUID uuid, double amount, boolean isPlayer) {
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
        this.withdrawFunds(player.getUUID(), funds, true);
    }

    public boolean withdrawFunds(UUID uuid, double funds, boolean isPlayer) {
        double balance = this.getBalance(uuid, isPlayer);
        if (funds < (double)0.0F) {
            return false;
        } else if (balance < funds) {
            return false;
        } else {
            Currency currency = this.vaultApi.currencies().primary();
            Account account = this.vaultApi.account(currency, uuid).join();
            EconomyTransaction transaction = account.withdraw(BigDecimal.valueOf(funds));
            return transaction.successful();
        }
    }

    public boolean hasBankSupport() {
        return true;
    }

    public boolean hasBankAccount(UUID bankUniqueId) {
        return true;
    }

    public boolean bankWithdraw(UUID uuid, double amount) {
        return this.withdrawFunds(uuid, amount, false);
    }

    public double bankBalance(UUID uuid) {
        return this.getBalance(uuid, false);
    }

    public boolean bankDeposit(UUID uuid, double depositAmount) {
        return this.depositPlayer(uuid, depositAmount, false);
    }

    public boolean withdrawTax(Claim claim, Player player, double taxOwed) {
        return false;
    }

    public boolean createAccount(UUID uuid) {
        try {
            this.vaultApi.account(uuid).get();
            return true;
        } catch (Throwable var3) {
            return false;
        }
    }

    public void deleteBank(UUID uuid) {
        this.vaultApi.deleteAccount(uuid);
    }

}
