package de.drachir000.library.enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * The base Enchantment class
 * @author Drachir000
 * @since 0.0.1
 */
public abstract class Enchantment extends org.bukkit.enchantments.Enchantment {

    private String name, defaultPrefix, maxLevelPrefix;
    private NamespacedKey namespacedKey;
    private short minLevel, maxLevel;
    private EnchantmentTarget enchantmentTarget;
    private boolean curse;
    private List<NamespacedKey> conflicts;
    private List<Material> enchantable;

    /**
     * @since 0.0.1
     * @param name The name of the enchantment<br>
     * @param defaultPrefix The prefix, written before the name in the item lore, when the enchantment doesn't is at max level (minecraft vanilla uses "§r§7")<br>
     * @param maxLevelPrefix The prefix, written before the name in the item lore, when the enchantment is at max level (minecraft vanilla uses "§r§7", ELib uses "§r§6")<br>
     * @param namespacedKey The key of the enchantment. Should be something like "plugin:enchantment" (e.g. "replenishenchantment:replenish")<br>
     * @param minLevel The minimal level of the enchantment. Can't be lower than 1 or higher than the maximum level<br>
     * @param maxLevel The maximal level of the enchantment. Can't be lower than 1<br>
     * @param enchantmentTarget The targeted group of item types, this enchantment should be able to get applied on. (This is not used, to determine if a specific item is enchantable)<br>
     * @param curse Whether this enchantment is a curse (this won't affect the lore color, for the lore color you have to change the defaultPrefix and maxLevelPrefix)<br>
     * @param conflicts A list of namespaces of enchantments this enchantment conflicts with<br>
     * @param enchantable A list of every material type, this enchantment should be able to get applied on<br>
     * */
    public Enchantment(String name, String defaultPrefix, String maxLevelPrefix, NamespacedKey namespacedKey, short minLevel, short maxLevel, EnchantmentTarget enchantmentTarget, boolean curse, List<NamespacedKey> conflicts, List<Material> enchantable) {
        super(namespacedKey);
        this.name = name;
        this.defaultPrefix = defaultPrefix; // TODO: make default value configurable
        this.maxLevelPrefix = maxLevelPrefix; // TODO: make default value configurable
        this.namespacedKey = namespacedKey;
        if (minLevel < 1)
            minLevel = 1;
        if (maxLevel < 1)
            maxLevel = 1;
        if (minLevel > maxLevel)
            minLevel = maxLevel;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.enchantmentTarget = enchantmentTarget;
        this.curse = curse;
        if (conflicts == null)
            conflicts = new ArrayList<>();
        this.conflicts = conflicts;
        if (enchantable == null)
            enchantable = new ArrayList<>();
        this.enchantable = enchantable;
    }

    /**
     * defaultPrefix - The prefix, written before the name in the item lore, when the enchantment doesn't is at max level (minecraft vanilla uses "§r§7"). Will be "§r§7".
     * <br>
     * <br>
     * maxLevelPrefix - The prefix, written before the name in the item lore, when the enchantment is at max level (minecraft vanilla uses "§r§7"). Will be "§r§6".
     * <br>
     * <br>
     * namespacedKey - The key of the enchantment. Will be "plugin:name" (lowercase name).
     * <br>
     * <br>
     * minLevel - The minimal level of the enchantment. Will be 1.
     * <br>
     *
     * @since 0.0.1
     * @param name The name of the enchantment<br>
     * @param plugin The instance of the JavaPlugin, that adds this enchantment<br>
     * @param maxLevel The maximal level of the enchantment. Can't be lower than 1<br>
     * @param enchantmentTarget The targeted group of item types, this enchantment should be able to get applied on. (This is not used, to determine if a specific item is enchantable)<br>
     * @param curse Whether this enchantment is a curse (this won't affect the lore color, for the lore color you have to change the defaultPrefix and maxLevelPrefix)<br>
     * @param conflicts A list of namespaces of enchantments this enchantment conflicts with<br>
     * @param enchantable A list of every material type, this enchantment should be able to get applied on<br>
     * */
    public Enchantment(String name, Plugin plugin, short maxLevel, EnchantmentTarget enchantmentTarget, boolean curse, List<NamespacedKey> conflicts, List<Material> enchantable) {
        this(name, "§r§7", "§r§6", NamespacedKey.fromString(name.toLowerCase(), plugin), (short) 1, maxLevel, enchantmentTarget, curse, conflicts, enchantable);
    }
    /**
     * defaultPrefix - The prefix, written before the name in the item lore, when the enchantment doesn't is at max level (minecraft vanilla uses "§r§7"). Will be "§r§7".
     * <br>
     * <br>
     * maxLevelPrefix - The prefix, written before the name in the item lore, when the enchantment is at max level (minecraft vanilla uses "§r§7"). Will be "§r§6".
     * <br>
     * <br>
     * namespacedKey - The key of the enchantment. Will be "plugin:name" (lowercase name).
     * <br>
     * <br>
     * minLevel - The minimal level of the enchantment. Will be 1.
     * <br>
     * <br>
     * curse - Whether this enchantment is a curse (this won't affect the lore color, for the lore color you have to change the defaultPrefix and maxLevelPrefix). Will be false.
     * <br>
     * <br>
     * conflicts - A list of namespaces of enchantments this enchantment conflicts with. Will be empty.
     * <br>
     * <br>
     * enchantable - A list of every material type, this enchantment should be able to get applied on. Will be empty.
     * <br>
     *
     * @since 0.0.1
     * @param name The name of the enchantment<br>
     * @param plugin The instance of the JavaPlugin, that adds this enchantment<br>
     * @param maxLevel The maximal level of the enchantment. Can't be lower than 1<br>
     * @param enchantmentTarget The targeted group of item types, this enchantment should be able to get applied on. (This is not used, to determine if a specific item is enchantable)<br>
     * */
    public Enchantment(String name, Plugin plugin, short maxLevel, EnchantmentTarget enchantmentTarget) {
        this(name, plugin, maxLevel, enchantmentTarget, false, null, null);
    }

    @Override
    public final boolean conflictsWith(org.bukkit.enchantments.Enchantment enchantment) {
        return conflictsWith(enchantment.getKey());
    }

    /**
     * Check if this enchantment conflicts with another enchantment.
     * @param enchantment The other enchantment
     * @return true, if this enchantment conflicts with the given enchantment
     * @since 0.0.1
     * */
    public boolean conflictsWith(Enchantment enchantment) {
        return conflictsWith(enchantment.getNamespacedKey());
    }

    /**
     * Check if this enchantment conflicts with another enchantment.
     * @param namespacedKey The NamespacedKey of the other enchantment
     * @return true, if this enchantment conflicts with the given enchantment
     * @since 0.0.1
     * */
    public boolean conflictsWith(NamespacedKey namespacedKey) {
        return conflicts.contains(namespacedKey);
    }

    /**
     * Check if this enchantment conflicts with at least one other of the given enchantments.
     * @param enchantment All other enchantments
     * @return true, if this enchantment conflicts with at least one of the given enchantments
     * @since 0.0.1
     * */
    public boolean conflictsWith(org.bukkit.enchantments.Enchantment... enchantment) {
        for (org.bukkit.enchantments.Enchantment ench : enchantment) {
            if (conflictsWith(ench))
                return true;
        }
        return false;
    }

    /**
     * Check if this enchantment conflicts with at least one other of the given enchantments.
     * @param enchantment All other enchantments
     * @return true, if this enchantment conflicts with at least one of the given enchantments
     * @since 0.0.1
     * */
    public boolean conflictsWith(Enchantment... enchantment) {
        for (Enchantment ench : enchantment) {
            if (conflictsWith(ench))
                return true;
        }
        return false;
    }

    /**
     * Check if this enchantment conflicts with at least one other of the given (by their NamespacedKey) enchantments.
     * @param namespacedKey The NamespacedKeys of all other enchantments
     * @return true, if this enchantment conflicts with at least one of the given (by their NamespacedKey) enchantments
     * @since 0.0.1
     * */
    public boolean conflictsWith(NamespacedKey... namespacedKey) {
        for (NamespacedKey key : namespacedKey) {
            if (conflictsWith(key))
                return true;
        }
        return false;
    }

    @Override
    public final boolean canEnchantItem(ItemStack item) {
        return isEnchantable(item);
    }

    /**
     * Checks if this Enchantment may be applied to the given ItemStack.
     * This does not check if it conflicts with any enchantments already applied to the item.
     * @param item the item to test
     * @return true, if this enchantment may be applied to the given ItemStack
     * @since 0.0.1
     */
    public boolean isEnchantable(ItemStack item) {
        return enchantable.contains(item.getType());
    }

    /**
     * @since 0.0.1
     * @return The name of the enchantment
     */
    public String getName() {
        return name;
    }

    /**
     * Note: changing this won't automatically update every item lore, it just changes, what the item lore gets updated to.
     * @since 0.0.1
     * @param name The name of the enchantment
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @since 0.0.1
     * @return The prefix, written before the name in the item lore, when the enchantment doesn't is at max level (minecraft vanilla uses "§r§7")
     */
    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    /**
     * Note: changing this won't automatically update every item lore, it just changes, what the item lore gets updated to.
     * @since 0.0.1
     * @param defaultPrefix The prefix, written before the name in the item lore, when the enchantment doesn't is at max level (minecraft vanilla uses "§r§7")
     */
    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    /**
     * @since 0.0.1
     * @return The prefix, written before the name in the item lore, when the enchantment is at max level (minecraft vanilla uses "§r§7", ELib uses "§r§6")
     */
    public String getMaxLevelPrefix() {
        return maxLevelPrefix;
    }

    /**
     * Note: changing this won't automatically update every item lore, it just changes, what the item lore gets updated to.
     * @since 0.0.1
     * @param maxLevelPrefix The prefix, written before the name in the item lore, when the enchantment is at max level (minecraft vanilla uses "§r§7", ELib uses "§r§6")
     */
    public void setMaxLevelPrefix(String maxLevelPrefix) {
        this.maxLevelPrefix = maxLevelPrefix;
    }

    @Override
    public final NamespacedKey getKey() {
        return getNamespacedKey();
    }

    /**
     * @since 0.0.1
     * @return The key of the enchantment. Should be something like "plugin:enchantment" (e.g. "replenishenchantment:replenish")
     */
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    /**
     * Note: changing this won't update the enchantment key on the items, so they will effectively lose this enchantment!!!
     * @since 0.0.1
     * @param namespacedKey The key of the enchantment. Should be something like "plugin:enchantment" (e.g. "replenishenchantment:replenish")
     */
    public void setNamespacedKey(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    @Override
    public final int getStartLevel() {
        return getMinLevel();
    }

    /**
     * @since 0.0.1
     * @return The minimal level of the enchantment. Can't be lower than 1 or higher than the maximum level
     */
    public short getMinLevel() {
        return minLevel;
    }

    /**
     * Note: changing this won't update any existing items, so they will keep their (maybe now invalid) levels until they get updated
     * @since 0.0.1
     * @param minLevel The minimal level of the enchantment. Can't be lower than 1 or higher than the maximum level
     */
    public void setMinLevel(short minLevel) {
        if (minLevel < 1)
            minLevel = 1;
        if (minLevel > maxLevel)
            minLevel = maxLevel;
        this.minLevel = minLevel;
    }

    /**
     * @since 0.0.1
     * @return The maximal level of the enchantment. Can't be lower than 1 or the minimum level
     */
    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Note: changing this won't update any existing items, so they will keep their (maybe now invalid) levels until they get updated
     * @since 0.0.1
     * @param maxLevel The maximal level of the enchantment. Can't be lower than 1 or the minimum level
     */
    public void setMaxLevel(short maxLevel) {
        if (maxLevel < 1)
            maxLevel = 1;
        if (maxLevel < minLevel)
            maxLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    public final EnchantmentTarget getItemTarget() {
        return getEnchantmentTarget();
    }

    /**
     * @since 0.0.1
     * @return The targeted group of item types, this enchantment should be able to get applied on. (This is not used, to determine if a specific item is enchantable)
     */
    public EnchantmentTarget getEnchantmentTarget() {
        return enchantmentTarget;
    }

    /**
     * @since 0.0.1
     * @param enchantmentTarget The targeted group of item types, this enchantment should be able to get applied on. (This is not used, to determine if a specific item is enchantable)
     */
    public void setEnchantmentTarget(EnchantmentTarget enchantmentTarget) {
        this.enchantmentTarget = enchantmentTarget;
    }

    @Override
    public boolean isCursed() {
        return isCurse();
    }

    /**
     * @since 0.0.1
     * @return Whether this enchantment is a curse (this won't affect the lore color, for the lore color you have to change the defaultPrefix and maxLevelPrefix)
     */
    public boolean isCurse() {
        return curse;
    }

    /**
     * @since 0.0.1
     * @param curse Whether this enchantment is a curse (this won't affect the lore color, for the lore color you have to change the defaultPrefix and maxLevelPrefix)
     */
    public void setCurse(boolean curse) {
        this.curse = curse;
    }

    /**
     * @since 0.0.1
     * @return A list of namespaces of enchantments this enchantment conflicts with
     */
    public List<NamespacedKey> getConflicts() {
        return conflicts;
    }

    /**
     * Note: changing this won't update any existing items, so they will keep their (maybe now invalid) enchantment
     * @since 0.0.1
     * @param conflicts A list of namespaces of enchantments this enchantment conflicts with
     */
    public void setConflicts(List<NamespacedKey> conflicts) {
        this.conflicts = conflicts;
    }

    /**
     * @since 0.0.1
     * @return A list of every material type, this enchantment should be able to get applied on
     */
    public List<Material> getEnchantable() {
        return enchantable;
    }

    /**
     * @since 0.0.1
     * @param enchantable A list of every material type, this enchantment should be able to get applied on
     */
    public void setEnchantable(List<Material> enchantable) {
        this.enchantable = enchantable;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

}
