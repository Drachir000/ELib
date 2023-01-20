package de.drachir000.library.configuration;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A JSON configuration
 *
 * @author <a href="https://www.spigotmc.org/threads/json-configuration-files.212794/#post-2195490">IAlIstannen</a>
 * @since 0.0.5
 */
public class JsonConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Plugin plugin;
    private final Path path;
    private final Map<String, Object> values = new HashMap<>();

    /**
     * Creates a JSON config
     *
     * @param plugin The plugin
     * @param path   The path to the config
     * @since 0.0.5
     */
    public JsonConfig(Plugin plugin, Path path) {
        this.plugin = plugin;
        this.path = path;
    }

    /**
     * Creates a JSON config in the plugin's data folder
     *
     * @param plugin The plugin
     * @param name   The name of the config
     * @see #JsonConfig(Plugin, Path)
     * @since 0.0.5
     */
    public JsonConfig(Plugin plugin, String name) {
        this(plugin, plugin.getDataFolder().toPath().resolve(name));
    }

    /**
     * Returns a reference to the underlying map
     *
     * @return All values in this map
     * @since 0.0.5
     */
    public Map<String, Object> getValuesMap() {
        return values;
    }

    /**
     * Returns a Set with all entries
     *
     * @return All values as a Entry Set
     * @since 0.0.5
     */
    public Set<Map.Entry<String, Object>> getEntries() {
        return values.entrySet();
    }

    /**
     * Returns the Object behind the given key
     *
     * @return The Object if the key is set, the defaultValue otherwise
     * @since 0.0.5
     */
    public Object get(String key) {
        return values.get(key);
    }

    /**
     * Returns the Object behind the given key or the defaultValue
     *
     * @return The Object if the key is set, null otherwise
     * @since 0.0.5
     */
    public Object getOrDefault(String key, Object defaultValue) {
        return values.getOrDefault(key, defaultValue);
    }

    /**
     * Returns a Set with all keys set
     *
     * @return The String Set containing all set keys
     * @since 0.0.5
     */
    public Set<String> getKeys() {
        return values.keySet();
    }

    /**
     * Check if a given key is set
     *
     * @return true, if the key is set
     * @since 0.0.5
     */
    public boolean hasKey(String key) {
        return values.containsKey(key);
    }

    /**
     * Reloads the config
     *
     * @throws RuntimeException wrapping any IO exception that may occur
     * @since 0.0.5
     */
    public void reload() {
        if (Files.notExists(path)) {
            plugin.saveResource(path.getFileName().toString(), false);
        }
        try {
            values.putAll(GSON.fromJson(Files.newBufferedReader(path), values.getClass()));
        } catch (IOException e) {
            Throwables.throwIfUnchecked(e);
        }
    }

    /**
     * Saves the config
     *
     * @throws RuntimeException wrapping any IO exception that may occur
     * @since 0.0.5
     */
    public void save() {
        String json = GSON.toJson(values);
        try {
            Files.write(
                    path,
                    Collections.singletonList(json),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            Throwables.throwIfUnchecked(e);
        }
    }

}