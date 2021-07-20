package berg.lucas.dropmultiplier;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is the listener of the plugin.
 */
public class EventListener implements Listener {

    private final Random random = new Random();
    private final Map<Material, Double> targets = new HashMap<>();
    private final JavaPlugin plugin;
    private int multiplier = 1;

    /**
     * Initialise this with the plugin.
     *
     * @param plugin the plugin.
     *
     * @throws IllegalArgumentException if plugin is null;
     * @effects initialise this with the plugin.
     */
    public EventListener(@NotNull JavaPlugin plugin) {
        Preconditions.checkNotNull(plugin, "plugin cannot be null.");
        this.plugin = plugin;
    }

    /**
     * Sets the multiplier.
     *
     * @param multiplier the multiplier.
     *
     * @modifies this
     * @throws IllegalArgumentException if multiplier < 0;
     * @effects sets the multiplier.
     */
    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Sets the target blocks.
     *
     * @param targets the target blocks.
     *
     * @modifies this
     * @throws NullPointerException if targets is null;
     * @throws IllegalArgumentException if a probability is not between 0 and 1.
     * @effects sets the target blocks.
     */
    public void setTargets(@NotNull Map<Material, Double> targets) throws NullPointerException, IllegalArgumentException {
        Preconditions.checkNotNull(targets, "targets cannot be null.");
        targets.values().forEach(probability ->
                Preconditions.checkArgument(
                        0d <= probability && probability <= 1d,
                        "A probability must be between 0 and 1."
                )
        );
        this.targets.putAll(targets);
    }

    /**
     * Clears the target blocks.
     *
     * @modifies this
     * @effects clears the target blocks.
     */
    public void clear() {
        this.targets.clear();
    }

    /**
     * Listen to a block break.
     *
     * @param event the event.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        // Return if unwanted block
        if (!this.targets.containsKey(block.getType())) return;

        // Check if the block has already been placed
        if (block.hasMetadata("AlreadyPlaced")) return;

        event.setDropItems(false);

        // Get the info
        Double probability = this.targets.get(block.getType());
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack tool = player.getItemInUse();

        // Gets the drop
        Collection<ItemStack> items;
        if (tool == null) {
            items = block.getDrops();
        } else {
            items = block.getDrops(tool, player);
        }

        // Loop over the items
        for (ItemStack item : items) {
            // Multiply the amount if needed
            int amount;
            if (random.nextDouble() < probability) {
                amount = item.getAmount() * this.multiplier;
            } else {
                amount = item.getAmount();
            }

            // Set the new amount
            item.setAmount(amount);

            // Drop
            world.dropItemNaturally(block.getLocation(), item);
        }
    }

    /**
     * Listen to a block place.
     *
     * @param event the event.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        // Return if unwanted block
        if (!this.targets.containsKey(block.getType())) return;

        // Sets the metadata if the block doesn't have one yet
        if (!block.hasMetadata("AlreadyPlaced")) {
            block.setMetadata("AlreadyPlaced", new FixedMetadataValue(this.plugin, true));
        }
    }
}
