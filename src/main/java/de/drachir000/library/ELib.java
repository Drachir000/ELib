package de.drachir000.library;

import de.drachir000.library.utils.EnchantmentManager;
import de.drachir000.library.utils.ItemManager;
import de.drachir000.library.utils.LoreManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.util.logging.Level;

/**
 * The main Enchantment-Library class
 *
 * @author Drachir000
 * @since 0.0.0
 */
public final class ELib extends JavaPlugin {

    private static final int bStatsID = 17412;
    private Metrics metrics;

    private static ELib instance;

    private EnchantmentManager enchantmentManager;
    private LoreManager loreManager;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        this.enchantmentManager = new EnchantmentManager(this);
        this.loreManager = new LoreManager(this);
        this.itemManager = new ItemManager(this, enchantmentManager);

        try {
            enchantmentManager.registerVanillaEnchantments();
        } catch (FileNotFoundException e) {
            getLogger().log(Level.SEVERE, "Failed to register vanilla enchantments!!!");
            e.printStackTrace();
            getPluginLoader().disablePlugin(this);
            return;
        }
        // TODO: register enchantments from enchantments-configuration file

        loadMetrics();

    }

    /**
     * @return The instance of this Library (null if this plugin isn't enabled)
     * @since 0.0.2
     */
    public static ELib getInstance() {
        return instance;
    }

    /**
     * get the Enchantment Manager. Used for a buch of enchantment related actions.
     *
     * @return th EnchantmentManager
     * @since 0.0.5
     */
    public EnchantmentManager getEnchantmentManager() {
        return enchantmentManager;
    }

    /**
     * get the lore manager. Necessary for the updateLore() method.
     *
     * @return th LoreManager
     * @since 0.0.4
     */
    public LoreManager getLoreManager() {
        return loreManager;
    }

    /**
     * get the Item Manager. Used for a buch of item related actions, except the lore
     *
     * @return the ItemManager
     * @since 0.0.4
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    private void loadMetrics() {
        this.metrics = new Metrics(this, bStatsID);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        instance = null;

    }

}
