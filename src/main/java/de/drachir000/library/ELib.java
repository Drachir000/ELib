package de.drachir000.library;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class ELib extends JavaPlugin {

    private static final int bStatsID = 17412;

    private Metrics metrics;

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadMetrics();
    }

    private void loadMetrics() {
        this.metrics = new Metrics(this, bStatsID);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
