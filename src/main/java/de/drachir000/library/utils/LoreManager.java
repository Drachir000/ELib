package de.drachir000.library.utils;

import de.drachir000.library.ELib;
import de.drachir000.library.enchantments.Enchantment;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The Item-Lore managing class
 *
 * @author Drachir000
 * @since 0.0.3
 */
public class LoreManager {

    private final ELib eLib;

    public LoreManager(ELib eLib) {
        this.eLib = eLib;
    }

    public void updateLore(ItemStack item) {

        ItemStack itemStack;
        try {
            itemStack = item.clone();
        } catch (Exception ignored) {
            return;
        }

        if (!itemStack.hasItemMeta())
            return;

        hideFlags(itemStack);

        removeLore(itemStack);

        addLore(itemStack);

        item = itemStack;

    }

    private void hideFlags(ItemStack item) {

        NBTItem nbtItem = new NBTItem(item);

        nbtItem.setBoolean("HideFlags", true);

        item = nbtItem.getItem();

    }

    private void removeLore(ItemStack item) {

        if (!item.hasItemMeta())
            return;

        NBTItem nbtItem = new NBTItem(item);

        NBTCompound pluginCompound = nbtItem.getOrCreateCompound("ELib");

        if (!pluginCompound.getBoolean("has-lore"))
            return;

        NBTCompound loreCompound = pluginCompound.getOrCreateCompound("lore");

        Set<String> loreLines = loreCompound.getKeys();

        pluginCompound.removeKey("lore");
        pluginCompound.setBoolean("has-lore", false);

        item = nbtItem.getItem();

        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = trimLore(item, loreLines);

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

    }

    private List<String> trimLore(ItemStack item, Set<String> loreLines) {

        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        boolean separator = true;
        for (String currentLine : itemMeta.getLore()) {
            if (currentLine.equals("") && separator) {
                separator = false;
                continue;
            }
            boolean keep = true;
            for (String loreLine : loreLines) {
                if (loreLine.contains(currentLine)) {
                    keep = false;
                    break;
                }
            }
            if (keep)
                lore.add(currentLine);
        }

        return lore;

    }

    private void addLore(ItemStack item) {

        if (!item.hasItemMeta())
            return;

        Map<Enchantment, Short> enchantmentsMap = eLib.getItemManager().getEnchantments(item);

        ItemMeta itemMeta = item.getItemMeta();

        List<String> lore = itemMeta.getLore();

        if (lore == null)
            lore = new ArrayList<>();

        lore.add(0, "");

        HashMap<String, String> addedEnchantments = new HashMap<>();

        for (Map.Entry<Enchantment, Short> enchantment : enchantmentsMap.entrySet()) {
            if (enchantment.getKey() == null)
                continue;
            Enchantment ench = enchantment.getKey();
            String loreLine = getLoreLine(ench, enchantment.getValue());
            lore.add(0, loreLine);
            String namespacedKey = ench.getNamespacedKey().getNamespace() + ":" + ench.getNamespacedKey().getKey();
            addedEnchantments.put(loreLine, namespacedKey);
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        addLoreLinesToNBT(item, addedEnchantments);

    }

    private String getLoreLine(Enchantment enchantment, Short level) {

        StringBuilder resultBuilder = new StringBuilder();

        if (enchantment.getMaxLevel() <= level)
            resultBuilder.append(enchantment.getMaxLevelPrefix());
        else
            resultBuilder.append(enchantment.getDefaultPrefix());

        resultBuilder.append(enchantment.getName());

        resultBuilder.append(" ");

        resultBuilder.append(intToRoman(level));

        return resultBuilder.toString();

    }

    private String intToRoman(int num) {
        if (num < 1) {
            return "0";
        }
        if (num > 100) {
            return Integer.toString(num);
        }
        // TODO add config option to stop using roman format earlier that 100
        StringBuilder resultBuilder = new StringBuilder();
        int[] values = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                resultBuilder.append(symbols[i]);
            }
        }
        return resultBuilder.toString(); // TODO add config option to replace "I" with ""
    }

    private void addLoreLinesToNBT(ItemStack item, Map<String, String> addedLoreLines) {

        NBTItem nbtItem = new NBTItem(item);

        NBTCompound pluginCompound = nbtItem.getOrCreateCompound("ELib");

        pluginCompound.setBoolean("has-lore", true);

        NBTCompound loreCompound = pluginCompound.getOrCreateCompound("lore");

        for (Map.Entry<String, String> loreEntry : addedLoreLines.entrySet()) {
            loreCompound.setString(loreEntry.getKey(), loreEntry.getValue());
        }

        item = nbtItem.getItem();

    }

}
