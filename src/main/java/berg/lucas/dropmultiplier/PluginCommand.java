package berg.lucas.dropmultiplier;

import com.google.common.base.Preconditions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

/**
 * This class allows the usage of the dropmultiplier commands.
 */
public class PluginCommand implements CommandExecutor {

    private final DropMultiplier plugin;

    /**
     * Initialise this to the plugin.
     *
     * @param plugin the plugin.
     *
     * @throws IllegalArgumentException if plugin is null;
     * @effects initialise this to the plugin.
     */
    public PluginCommand(@NotNull DropMultiplier plugin) {
        Preconditions.checkArgument(plugin != null, "plugin cannot be null.");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Display information
        if (args.length == 0) {
            sender.sendMessage(String.format("DropMultiplier v%s", this.plugin.getDescription().getVersion()));
            return true;
        }
        // Reload the config
        else if (args.length == 1 && args[0].equals("reload") && sender.hasPermission("dropmultiplier.reload")) {
            this.plugin.reloadConfig();
            try {
                this.plugin.readConfig();
                sender.sendMessage("Config reloaded.");
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
                sender.sendMessage(e.getMessage());
            }
            return true;
        }
        // Invalid command
        else {
            return false;
        }
    }
}
