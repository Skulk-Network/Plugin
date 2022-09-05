package network.skulk.plugin.extensions.homes;

import network.skulk.plugin.Plugin;
import network.skulk.plugin.extensions.BaseExtension;
import network.skulk.plugin.extensions.homes.commands.HomeAddCommand;
import network.skulk.plugin.extensions.homes.commands.HomeCommand;
import network.skulk.plugin.extensions.homes.commands.HomeDeleteCommand;
import network.skulk.plugin.extensions.homes.commands.HomeListCommand;
import network.skulk.plugin.extensions.homes.listeners.TabCompleteEventListener;
import org.bukkit.NamespacedKey;

public class HomesExtension implements BaseExtension {
    public final NamespacedKey HOMES_KEY;
    public final Plugin plugin;

    public HomesExtension(final Plugin plugin) {
        this.plugin = plugin;
        HOMES_KEY = new NamespacedKey(plugin, "homes");
    }

    public void onEnable() {
        new HomeCommand(this);
        new HomeDeleteCommand(this);
        new HomeListCommand(this);
        new HomeAddCommand(this);

        new TabCompleteEventListener(this);
    }

    public void onDisable() {

    }
}
