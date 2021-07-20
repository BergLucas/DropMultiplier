package berg.lucas.dropmultiplier;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is the core of the plugin.
 */
public class DropMultiplier extends JavaPlugin {

    private final EventListener eventListener = new EventListener(this);

    @Override
    public void onEnable() {
        this.getCommand("dropmultiplier").setExecutor(new PluginCommand(this));
        this.getServer().getPluginManager().registerEvents(eventListener, this);
        this.saveDefaultConfig();
        try {
            this.readConfig();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable(){
    }

    /**
     * Reads the config.
     *
     * @modifies this
     * @throws InvalidConfigurationException if the config is invalid;
     * @effects reads the config.
     */
    public void readConfig() throws InvalidConfigurationException {
        FileConfiguration config = this.getConfig();

        // Get the multiplier or set to 2 by default
        int multiplier = config.getInt("Multiplier", 2);

        Map<Material, Double> target = new HashMap<>();

        // Check if the Blocks key exists
        ConfigurationSection blocks = config.getConfigurationSection("Blocks");
        if (blocks != null) {
            // Convert the string in Material and get their probabilities
            for (Map.Entry<String, Object> entry : blocks.getValues(false).entrySet()) {
                Material material = Material.valueOf(entry.getKey());
                if (!(entry.getValue() instanceof Double probability)) throw new InvalidConfigurationException("A probability must be a Double.");
                target.put(material, probability);
            }
        }

        // Sets the targets and the multiplier
        this.eventListener.clear();
        try {
            this.eventListener.setMultiplier(multiplier);
            this.eventListener.setTargets(target);
        } catch (IllegalArgumentException e) {
            throw new InvalidConfigurationException(e.getMessage());
        }
    }
}
