package dev.matthiesen.cobblemon_npc_extensions.common;

import dev.matthiesen.cobblemon_npc_extensions.common.economy.EconomyCompat;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.economy.GDImpactorProvider;
import dev.matthiesen.cobblemon_npc_extensions.common.griefdefender.GriefDefenderCompat;
import dev.matthiesen.cobblemon_npc_extensions.common.luckperms.LuckPermsCompat;
import dev.matthiesen.cobblemon_npc_extensions.common.text_parsers.TextParserCompat;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import org.jetbrains.annotations.NotNull;

public final class CobblemonNPCExtensionsCommon extends AbstractCommonMod {
    public static final String MOD_ID = "cobblemon_npc_extensions";
    public static final String MOD_NAME = "Cobblemon NPC Extensions";
    public static @Token final String METRICS_TOKEN = "4487f4b5a9ffe7b2e70cf577c505c92f";
    public static final CobblemonNPCExtensionsCommon INSTANCE = new CobblemonNPCExtensionsCommon();

    private GDImpactorProvider GD_ECO_PROVIDER_INSTANCE;

    public CobblemonNPCExtensionsCommon() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public void initialize() {
        super.initialize();

        EconomyCompat.init();
        TextParserCompat.init();

        if (getCommonUtils().isModLoaded("griefdefender")) {
            GriefDefenderCompat.init();
        }

        if (getCommonUtils().isModLoaded("luckperms")) {
            LuckPermsCompat.init();
        }

        createInfoLog("Initialized");
    }

    public GDImpactorProvider getEcoProvider() {
        if (GD_ECO_PROVIDER_INSTANCE == null) {
            GD_ECO_PROVIDER_INSTANCE = new GDImpactorProvider();
        }
        return GD_ECO_PROVIDER_INSTANCE;
    }
}
