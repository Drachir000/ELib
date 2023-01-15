package de.drachir000.library.utils;

import de.drachir000.library.ELib;
import de.drachir000.library.enchantments.Enchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * The Item utility class
 * @since 0.0.4
 * @author Drachir000
 */
public class ItemManager {

    private final ELib eLib;

    public ItemManager(ELib eLib) {
        this.eLib = eLib;
    }

    /**
     * Get a map containing all registered enchantments on an item with the corresponding levels
     * @since 0.0.4
     * @param item the item whose enchantments are to be obtained
     * @return a map containing all registered enchantments on the item with the corresponding levels
     */
    public Map<Enchantment, Short> getEnchantments(ItemStack item) {

        Map<Enchantment, Short> result = new HashMap<>();

        if (item == null || item.getType().isAir())
            return result;
        if (!item.hasItemMeta())
            return result;

        ItemMeta itemMeta = item.getItemMeta();

        Map<org.bukkit.enchantments.Enchantment, Integer> enchantments = itemMeta.getEnchants();

        for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> enchantment : enchantments.entrySet()) {
            NamespacedKey namespacedKey = enchantment.getKey().getKey();
            Enchantment registeredEnchantment = eLib.getByNamespacedKey(namespacedKey);
            if (registeredEnchantment == null)
                continue;
            if (enchantment.getValue() < 1)
                continue;
            short level;
            if (enchantment.getValue() > Short.MAX_VALUE)
                level = Short.MAX_VALUE;
            else
                level = enchantment.getValue().shortValue();
            result.put(registeredEnchantment, level);
        }

        return result;

    }

    /**
     * Checks if an item contains a registered enchantment
     * @since 0.0.4
     * @param item the item to check
     * @param enchantment the enchantment to check (needs to be registered)
     * @return true, if the enchantment is registered and the item is enchanted with it
     */
    public boolean hasEnchantment(ItemStack item, Enchantment enchantment) {

        if (enchantment == null)
            return false;

        if (item == null || item.getType().isAir())
            return false;

        return getEnchantments(item).containsKey(enchantment);

    }

    /**
     * Checks if an item contains a registered enchantment
     * @since 0.0.4
     * @param item the item to check
     * @param namespacedKey the namespacedKey of the enchantment to check (needs to be registered)
     * @return true, if the enchantment is registered and the item is enchanted with it
     */
    public boolean hasEnchantment(ItemStack item, NamespacedKey namespacedKey) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return hasEnchantment(item, enchantment);

    }

    /**
     * Checks if an item contains a registered enchantment
     * @since 0.0.4
     * @param item the item to check
     * @param namespacedKey the namespacedKey of the enchantment to check (needs to be registered)
     * @return true, if the enchantment is registered and the item is enchanted with it
     */
    public boolean hasEnchantment(ItemStack item, String namespacedKey) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return hasEnchantment(item, enchantment);

    }

    /**
     * Get the level of a specific enchantment on an item
     * @since 0.0.4
     * @param item the item to check
     * @param enchantment the enchantment to check for
     * @return the level of the enchantment on the item (when the item isn't enchanted with this enchantment 0)
     */
    public short getEnchantmentLevel(ItemStack item, Enchantment enchantment) {

        if (enchantment == null)
            return 0;

        if (!hasEnchantment(item, enchantment))
            return 0;

        Map<Enchantment, Short> enchantments = getEnchantments(item);

        return enchantments.get(enchantment);

    }

    /**
     * Get the level of a specific enchantment on an item
     * @since 0.0.4
     * @param item the item to check
     * @param namespacedKey the namespacedKey of the enchantment to check for
     * @return the level of the enchantment on the item (when the item isn't enchanted with this enchantment 0)
     */
    public short getEnchantmentLevel(ItemStack item, NamespacedKey namespacedKey) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return getEnchantmentLevel(item, enchantment);

    }

    /**
     * Get the level of a specific enchantment on an item
     * @since 0.0.4
     * @param item the item to check
     * @param namespacedKey the namespacedKey of the enchantment to check for
     * @return the level of the enchantment on the item (when the item isn't enchanted with this enchantment 0)
     */
    public short getEnchantmentLevel(ItemStack item, String namespacedKey) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return getEnchantmentLevel(item, enchantment);

    }

    /**
     * remove an enchantment from an item
     * @since 0.0.4
     * @param item the item
     * @param enchantment the enchantment to remove
     * @return the level of the enchantment, if previously present, otherwise 0
     */
    public short removeEnchantment(ItemStack item, Enchantment enchantment) {

        if (enchantment == null)
            return 0;

        if (item == null || item.getType().isAir())
            return 0;

        int level = item.removeEnchantment(enchantment);

        if (level < 1)
            return 0;

        if (level > Short.MAX_VALUE)
            return Short.MAX_VALUE;

        eLib.getLoreManager().updateLore(item);

        return (short) level;

    }

    /**
     * remove an enchantment from an item
     * @since 0.0.4
     * @param item the item
     * @param namespacedKey the namespacedKey of the enchantment to remove
     * @return the level of the enchantment, if previously present, otherwise 0
     */
    public short removeEnchantment(ItemStack item, NamespacedKey namespacedKey) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return removeEnchantment(item, enchantment);

    }

    /**
     * remove an enchantment from an item
     * @since 0.0.4
     * @param item the item
     * @param namespacedKey the namespacedKey of the enchantment to remove
     * @return the level of the enchantment, if previously present, otherwise 0
     */
    public short removeEnchantment(ItemStack item, String namespacedKey) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return removeEnchantment(item, enchantment);

    }

    /**
     * Set the level of an enchantment on an item.<br>
     * If the enchantment wasn't already present it will add the enchantment with the given level and return 0<br>
     * If the enchantment was already present it updates the level to the given level and return the old level<br>
     * If the given level is below 1 it will remove the enchantment form the item instead and return the old level or 0 if it wasn't present
     * @since 0.0.4
     * @param item the item to update
     * @param enchantment the enchantment to update
     * @param level the new level of the enchantment (this ignores the maxLevel and minLevel of the enchantment)
     * @return the old level if the enchantment was already present, 0 otherwise
     */
    public short setEnchantment(ItemStack item, Enchantment enchantment, short level) {

        if (enchantment == null)
            return 0;

        if (item == null || item.getType().isAir())
            return 0;

        if (level < 1)
            return removeEnchantment(item, enchantment);

        short oldLevel = removeEnchantment(item, enchantment);

        item.addEnchantment(enchantment, level);

        eLib.getLoreManager().updateLore(item);

        return oldLevel;

    }

    /**
     * Set the level of an enchantment on an item.<br>
     * If the enchantment wasn't already present it will add the enchantment with the given level and return 0<br>
     * If the enchantment was already present it updates the level to the given level and return the old level<br>
     * If the given level is below 1 it will remove the enchantment form the item instead and return the old level or 0 if it wasn't present
     * @since 0.0.4
     * @param item the item to update
     * @param namespacedKey the namespacedKey of the enchantment to update (has to be registered)
     * @param level the new level of the enchantment (this ignores the maxLevel and minLevel of the enchantment)
     * @return the old level if the enchantment was already present, 0 otherwise
     */
    public short setEnchantment(ItemStack item, NamespacedKey namespacedKey, short level) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return setEnchantment(item,enchantment, level);

    }

    /**
     * Set the level of an enchantment on an item.<br>
     * If the enchantment wasn't already present it will add the enchantment with the given level and return 0<br>
     * If the enchantment was already present it updates the level to the given level and return the old level<br>
     * If the given level is below 1 it will remove the enchantment form the item instead and return the old level or 0 if it wasn't present
     * @since 0.0.4
     * @param item the item to update
     * @param namespacedKey the namespacedKey of the enchantment to update (has to be registered)
     * @param level the new level of the enchantment (this ignores the maxLevel and minLevel of the enchantment)
     * @return the old level if the enchantment was already present, 0 otherwise
     */
    public short setEnchantment(ItemStack item, String namespacedKey, short level) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return setEnchantment(item,enchantment, level);

    }

    /**
     * remove an enchantment from an item
     * @since 0.0.4
     * @param item the item
     * @param enchantment the enchantment to remove
     * @param updateLore whether to update the item lore
     * @return the level of the enchantment, if previously present, otherwise 0
     */
    public short removeEnchantment(ItemStack item, Enchantment enchantment, boolean updateLore) {

        if (enchantment == null)
            return 0;

        if (item == null || item.getType().isAir())
            return 0;

        int level = item.removeEnchantment(enchantment);

        if (level < 1)
            return 0;

        if (level > Short.MAX_VALUE)
            return Short.MAX_VALUE;

        if (updateLore)
            eLib.getLoreManager().updateLore(item);

        return (short) level;

    }

    /**
     * remove an enchantment from an item
     * @since 0.0.4
     * @param item the item
     * @param namespacedKey the namespacedKey of the enchantment to remove
     * @param updateLore whether to update the item lore
     * @return the level of the enchantment, if previously present, otherwise 0
     */
    public short removeEnchantment(ItemStack item, NamespacedKey namespacedKey, boolean updateLore) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return removeEnchantment(item, enchantment, updateLore);

    }

    /**
     * remove an enchantment from an item
     * @since 0.0.4
     * @param item the item
     * @param namespacedKey the namespacedKey of the enchantment to remove
     * @param updateLore whether to update the item lore
     * @return the level of the enchantment, if previously present, otherwise 0
     */
    public short removeEnchantment(ItemStack item, String namespacedKey, boolean updateLore) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return removeEnchantment(item, enchantment, updateLore);

    }

    /**
     * Set the level of an enchantment on an item.<br>
     * If the enchantment wasn't already present it will add the enchantment with the given level and return 0<br>
     * If the enchantment was already present it updates the level to the given level and return the old level<br>
     * If the given level is below 1 it will remove the enchantment form the item instead and return the old level or 0 if it wasn't present
     * @since 0.0.4
     * @param item the item to update
     * @param enchantment the enchantment to update
     * @param level the new level of the enchantment (this ignores the maxLevel and minLevel of the enchantment)
     * @param updateLore whether to update the item lore
     * @return the old level if the enchantment was already present, 0 otherwise
     */
    public short setEnchantment(ItemStack item, Enchantment enchantment, short level, boolean updateLore) {

        if (enchantment == null)
            return 0;

        if (item == null || item.getType().isAir())
            return 0;

        if (level < 1)
            return removeEnchantment(item, enchantment);

        short oldLevel = removeEnchantment(item, enchantment);

        item.addEnchantment(enchantment, level);

        if (updateLore)
            eLib.getLoreManager().updateLore(item);

        return oldLevel;

    }

    /**
     * Set the level of an enchantment on an item.<br>
     * If the enchantment wasn't already present it will add the enchantment with the given level and return 0<br>
     * If the enchantment was already present it updates the level to the given level and return the old level<br>
     * If the given level is below 1 it will remove the enchantment form the item instead and return the old level or 0 if it wasn't present
     * @since 0.0.4
     * @param item the item to update
     * @param namespacedKey the namespacedKey of the enchantment to update (has to be registered)
     * @param level the new level of the enchantment (this ignores the maxLevel and minLevel of the enchantment)
     * @param updateLore whether to update the item lore
     * @return the old level if the enchantment was already present, 0 otherwise
     */
    public short setEnchantment(ItemStack item, NamespacedKey namespacedKey, short level, boolean updateLore) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return setEnchantment(item,enchantment, level, updateLore);

    }

    /**
     * Set the level of an enchantment on an item.<br>
     * If the enchantment wasn't already present it will add the enchantment with the given level and return 0<br>
     * If the enchantment was already present it updates the level to the given level and return the old level<br>
     * If the given level is below 1 it will remove the enchantment form the item instead and return the old level or 0 if it wasn't present
     * @since 0.0.4
     * @param item the item to update
     * @param namespacedKey the namespacedKey of the enchantment to update (has to be registered)
     * @param level the new level of the enchantment (this ignores the maxLevel and minLevel of the enchantment)
     * @param updateLore whether to update the item lore
     * @return the old level if the enchantment was already present, 0 otherwise
     */
    public short setEnchantment(ItemStack item, String namespacedKey, short level, boolean updateLore) {

        Enchantment enchantment = eLib.getByNamespacedKey(namespacedKey);

        return setEnchantment(item,enchantment, level, updateLore);

    }

}
