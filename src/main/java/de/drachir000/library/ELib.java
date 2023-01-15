package de.drachir000.library;

import de.drachir000.library.enchantments.Enchantment;
import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ELib extends JavaPlugin {

    private static final int bStatsID = 17412;
    private Metrics metrics;

    private static ELib instance;

    private List<Enchantment> registeredEnchantments;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        this.registeredEnchantments = new ArrayList<>();
        // TODO: register vanilla enchantments
        // TODO: register enchantments from enchantments-configuration file

        loadMetrics();
    }

    /**
     * @since 0.0.2
     * @return The instance of this Library (null if this plugin isn't enabled)
     */
    public static ELib getInstance() {
        return instance;
    }

    /**
     * @since 0.0.2
     * @return A List of all currently registered enchantments
     */
    public List<Enchantment> getRegisteredEnchantments() {
        return registeredEnchantments;
    }

    /**
     * Register a custom enchantment
     * @since 0.0.2
     * @param enchantment The enchantment to register
     * @return false, if the enchantment already is registered
     */
    public boolean registerEnchantment(Enchantment enchantment) {
        if (registeredEnchantments.contains(enchantment))
            return false;
        registeredEnchantments.add(enchantment);
        return true;
    }

    /**
     * @since 0.0.2
     * @param enchantment The enchantment to test
     * @return true, if the enchantment already is registered
     */
    public boolean isRegistered(Enchantment enchantment) {
        return registeredEnchantments.contains(enchantment);
    }

    /**
     * @since 0.0.2
     * @param namespacedKey The NamespacedKey of the enchantment to test
     * @return true, if the enchantment already is registered
     */
    public boolean isRegistered(NamespacedKey namespacedKey) {
        for (Enchantment registeredEnchantment : registeredEnchantments) {
            if (registeredEnchantment.getNamespacedKey().equals(namespacedKey))
                return true;
        }
        return false;
    }

    /**
     * @since 0.0.2
     * @param namespacedKey The NamespacedKey of the enchantment to test
     * @return true, if the enchantment already is registered
     */
    public boolean isRegistered(String namespacedKey) {
        for (Enchantment registeredEnchantment : registeredEnchantments) {
            NamespacedKey registeredNamespacedKey = registeredEnchantment.getNamespacedKey();
            String registeredNamespacedKeyString = registeredNamespacedKey.getNamespace() + ":" + registeredNamespacedKey.getKey();
            if (registeredNamespacedKeyString.equals(namespacedKey))
                return true;
        }
        return false;
    }

    /**
     * Unregisters an Enchantment. This won't delete the enchantment from items, it will just mean, that, none of this libraries features works for this enchantment anymore.
     * Note: This will also mean, that when updating the lore, this Enchantment will disappear from the list, but it is still there!
     * @since 0.0.2
     * @param enchantment the enchantment to unregister
     * @return true, if the enchantment was registered
     */
    public boolean unregisterEnchantment(Enchantment enchantment) {
        return registeredEnchantments.remove(enchantment);
    }

    /**
     * Gets an enchantment by its namespacedKey.
     * @since 0.0.2
     * @param namespacedKey the namespacedKey of the enchantment to get
     * @return the enchantment with the namespacedKey if registered, null otherwise
     */
    public Enchantment getByNamespacedKey(NamespacedKey namespacedKey) {
        for (Enchantment registeredEnchantment : registeredEnchantments) {
            NamespacedKey registeredNamespacedKey = registeredEnchantment.getNamespacedKey();
            if (namespacedKey.equals(registeredNamespacedKey))
                return registeredEnchantment;
        }
        return null;
    }

    /**
     * Gets an enchantment by its namespacedKey.
     * @since 0.0.2
     * @param namespacedKey the namespacedKey of the enchantment to get
     * @return the enchantment with the namespacedKey if registered, null otherwise
     */
    public Enchantment getByNamespacedKey(String namespacedKey) {
        for (Enchantment registeredEnchantment : registeredEnchantments) {
            NamespacedKey registeredNamespacedKey = registeredEnchantment.getNamespacedKey();
            String registeredNamespacedKeyString = registeredNamespacedKey.getNamespace() + ":" + registeredNamespacedKey.getKey();
            if (namespacedKey.equals(registeredNamespacedKeyString))
                return registeredEnchantment;
        }
        return null;
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
