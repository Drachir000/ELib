package de.drachir000.library.utils;

import com.google.gson.internal.LinkedTreeMap;
import de.drachir000.library.ELib;
import de.drachir000.library.configuration.JsonConfig;
import de.drachir000.library.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The Enchantment managing class
 *
 * @author Drachir000
 * @since 0.0.5
 */
public class EnchantmentManager {

    ELib eLib;

    private final List<Enchantment> registeredEnchantments;

    public EnchantmentManager(ELib eLib) {
        this.eLib = eLib;
        this.registeredEnchantments = new ArrayList<>();
    }

    public void registerVanillaEnchantments() throws FileNotFoundException {

        JsonConfig vanillaEnchantmentsConfiguration = new JsonConfig(eLib, "vanilla-enchantments.json");

        vanillaEnchantmentsConfiguration.reload();

        for (org.bukkit.enchantments.Enchantment vanillaEnchantment : Enchantment.values()) {

            String namespace = vanillaEnchantment.getKey().getNamespace();
            if (!namespace.equalsIgnoreCase("minecraft"))
                continue;

            String key = vanillaEnchantment.getKey().getKey();

            Object obj = vanillaEnchantmentsConfiguration.get(key);

            LinkedTreeMap<String, Object> entry;

            try {
                entry = (LinkedTreeMap<String, Object>) obj;
            } catch (Exception ignored) {
                eLib.getLogger().log(Level.WARNING, "Couldn't register from vanilla-enchantments configuration: \"" + key + "\"! Invalid entry");
                continue;
            }

            if (entry == null) {
                eLib.getLogger().log(Level.WARNING, "Couldn't register from vanilla-enchantments configuration: \"" + key + "\"! Invalid entry");
                continue;
            }

            Enchantment enchantment = loadVanillaEnchantment(key, entry);

            if (enchantment != null)
                registerEnchantment(enchantment);

        }

    }

    private Enchantment loadVanillaEnchantment(String key, LinkedTreeMap<String, Object> entry) {

        NamespacedKey namespacedKey = NamespacedKey.fromString(key, null);

        if (namespacedKey == null) {
            eLib.getLogger().log(Level.WARNING, "Couldn't register from vanilla-enchantments configuration: \"" + key + "\"! Invalid key");
            return null;
        }

        String name, defaultPrefix = "§r§7", maxLevelPrefix = "§r§6"; // TODO default values
        short minLevel = 1, maxLevel = 1;
        EnchantmentTarget enchantmentTarget = EnchantmentTarget.ALL;
        boolean curse = false;
        List<NamespacedKey> conflicts = new ArrayList<>();
        List<Material> enchantable = new ArrayList<>();

        Object o = entry.get("name");
        if (o != null) {
            name = o.toString();
        } else {
            name = key.replace('_', ' ');
        }

        o = entry.get("default-prefix");
        if (o != null) {
            defaultPrefix = o.toString();
        }

        o = entry.get("maxLevel-prefix");
        if (o != null) {
            maxLevelPrefix = o.toString();
        }

        o = entry.get("min-level");
        if (o != null) {
            double d = (double) o;
            if (d < 1)
                d = 1;
            if (d > Short.MAX_VALUE)
                d = Short.MAX_VALUE;
            minLevel = (short) d;
        }

        o = entry.get("max-level");
        if (o != null) {
            double d = (double) o;
            if (d < 1)
                d = 1;
            if (d > Short.MAX_VALUE)
                d = Short.MAX_VALUE;
            maxLevel = (short) d;
        }

        o = entry.get("enchantment-target");
        if (o != null) {
            String s = o.toString();
            enchantmentTarget = EnchantmentTarget.valueOf(s);
        } else {
            eLib.getLogger().log(Level.WARNING, "Couldn't find 'enchantment-target' for \"" + key + "\" in vanilla-enchantments.json! Continuing with ALL");
        }

        o = entry.get("curse");
        if (o != null) {
            try {
                curse = (boolean) o;
            } catch (Exception ignored) {
                eLib.getLogger().log(Level.WARNING, "Invalid 'curse' for \"" + key + "\" in vanilla-enchantments.json! Continuing with false");
            }
        } else {
            eLib.getLogger().log(Level.WARNING, "Couldn't find 'curse' for \"" + key + "\" in vanilla-enchantments.json! Continuing with false");
        }

        o = entry.get("conflicts");
        if (o != null) {
            if (o instanceof ArrayList) {
                try {
                    conflicts = (ArrayList<NamespacedKey>) o;
                } catch (Exception e) {
                    eLib.getLogger().log(Level.WARNING, "Invalid 'conflicts' for \"" + key + "\" in vanilla-enchantments.json! Continuing with none");
                }
            }
        } else {
            eLib.getLogger().log(Level.WARNING, "Couldn't find 'conflicts' for \"" + key + "\" in vanilla-enchantments.json! Continuing with none");
        }

        o = entry.get("enchantable");
        if (o != null) {
            if (o instanceof ArrayList) {
                try {
                    enchantable = (ArrayList<Material>) o;
                } catch (Exception e) {
                    eLib.getLogger().log(Level.WARNING, "Invalid 'enchantable' for \"" + key + "\" in vanilla-enchantments.json! Continuing with none");
                }
            }
        } else {
            eLib.getLogger().log(Level.WARNING, "Couldn't find 'enchantable' for \"" + key + "\" in vanilla-enchantments.json! Continuing with none");
        }

        return new Enchantment(name, defaultPrefix, maxLevelPrefix, namespacedKey, minLevel, maxLevel, enchantmentTarget, curse, conflicts, enchantable) {
        };

    }

    /**
     * @return A List of all currently registered enchantments
     * @since 0.0.2
     */
    public List<Enchantment> getRegisteredEnchantments() {
        return registeredEnchantments;
    }

    /**
     * Register a custom enchantment
     *
     * @param enchantment The enchantment to register
     * @return false, if the enchantment already is registered
     * @since 0.0.2
     */
    public boolean registerEnchantment(Enchantment enchantment) {
        if (registeredEnchantments.contains(enchantment))
            return false;
        registeredEnchantments.add(enchantment);
        return true;
    }

    /**
     * @param enchantment The enchantment to test
     * @return true, if the enchantment already is registered
     * @since 0.0.2
     */
    public boolean isRegistered(Enchantment enchantment) {
        return registeredEnchantments.contains(enchantment);
    }

    /**
     * @param namespacedKey The NamespacedKey of the enchantment to test
     * @return true, if the enchantment already is registered
     * @since 0.0.2
     */
    public boolean isRegistered(NamespacedKey namespacedKey) {
        for (Enchantment registeredEnchantment : registeredEnchantments) {
            if (registeredEnchantment.getNamespacedKey().equals(namespacedKey))
                return true;
        }
        return false;
    }

    /**
     * @param namespacedKey The NamespacedKey of the enchantment to test
     * @return true, if the enchantment already is registered
     * @since 0.0.2
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
     *
     * @param enchantment the enchantment to unregister
     * @return true, if the enchantment was registered
     * @since 0.0.2
     */
    public boolean unregisterEnchantment(Enchantment enchantment) {
        return registeredEnchantments.remove(enchantment);
    }

    /**
     * Gets an enchantment by its namespacedKey.
     *
     * @param namespacedKey the namespacedKey of the enchantment to get
     * @return the enchantment with the namespacedKey if registered, null otherwise
     * @since 0.0.2
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
     *
     * @param namespacedKey the namespacedKey of the enchantment to get
     * @return the enchantment with the namespacedKey if registered, null otherwise
     * @since 0.0.2
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

}
