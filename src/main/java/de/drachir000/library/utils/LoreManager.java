package de.drachir000.library.utils;

import de.drachir000.library.ELib;
import de.drachir000.library.enchantments.Enchantment;
import de.tr7zw.changeme.nbtapi.*;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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

    /**
     * Updates the lore of an item.
     *
     * @param item The item whose lore is to be updated
     * @since 0.0.6
     */
    public void updateLore(ItemStack item) {

        if (!item.hasItemMeta())
            return;

        NBTItem nbtItem = new NBTItem(item, true);

        hideFlags(nbtItem);

        removeLore(nbtItem);

        addLore(nbtItem);

        nbtItem.applyNBT(item);

    }

    private void hideFlags(NBTItem nbtItem) {

        nbtItem.setBoolean("HideFlags", true);

    }

    private void removeLore(NBTItem nbtItem) {

        NBTCompound displayCompound = nbtItem.getCompound("display");

        if (displayCompound == null)
            return;

        NBTList<String> loreEntries = displayCompound.getStringList("Lore");
        Collection<String> toRemove = new ArrayList<>();

        for (String loreEntry : loreEntries) {
            NBTCompoundList lore = getLoreList(loreEntry);
            if (lore.get(0).getString("ELib-loreLine").equals("true") || lore.get(0).getBoolean("ELib-loreLine"))
                toRemove.add(loreEntry);
        }

        loreEntries.removeAll(toRemove);

    }

    private NBTCompoundList getLoreList(String loreEntry) {

        String jsonString = "{" +
                "List: " +
                loreEntry +
                "}";

        NBTCompound compound = new NBTContainer(jsonString);

        return compound.getCompoundList("List");

    }

    private void addLore(NBTItem nbtItem) {

        NBTCompound displayCompound = nbtItem.getOrCreateCompound("display");

        NBTList<String> lore = displayCompound.getStringList("Lore");

        Map<Enchantment, Short> enchantmentsMap = eLib.getItemManager().getEnchantments(nbtItem);

        if (enchantmentsMap.isEmpty())
            return;

        lore.add(0, "[{\"ELib-loreLine\": true, \"text\": \"\"}]");

        for (Map.Entry<Enchantment, Short> enchantmentEntry : enchantmentsMap.entrySet()) {

            NBTCompound loreCompound = createLoreLine(enchantmentEntry.getKey(), enchantmentEntry.getValue());
            String line = "[" + loreCompound + "]";
            line = line.replaceAll("1b", "\"true\"")
                    .replaceAll("0b", "\"false\"")
                    .replaceAll("ELib-loreLine", "\"ELib-loreLine\"")
                    .replaceAll("extra", "\"extra\"")
                    .replaceAll("bold", "\"bold\"")
                    .replaceAll("italic", "\"italic\"")
                    .replaceAll("obfuscated", "\"obfuscated\"")
                    .replaceAll("text", "\"text\"")
                    .replaceAll("underlined", "\"underlined\"")
                    .replaceAll("color", "\"color\"");
            lore.add(0, line);

        }

    }

    private NBTCompound createLoreLine(Enchantment enchantment, Short level) {

        String loreLineString = getFullLoreLineString(enchantment, level);

        String jsonLoreLineString = toJsonLore(loreLineString);

        NBTCompound loreCompound = new NBTContainer(jsonLoreLineString);

        loreCompound.setString("ELib-loreLine", "true");

        if (!loreCompound.hasTag("italic"))
            loreCompound.setBoolean("italic", false);

        return loreCompound;

    }

    private String getFullLoreLineString(Enchantment enchantment, Short level) {

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
        // TODO maybe? increase max possible value
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

    private String toJsonLore(String lore) {

        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().hexColors().character('§').build();
        TextComponent textComponent = serializer.deserialize(lore);
        return GsonComponentSerializer.gson().serialize(textComponent);

    }

}
