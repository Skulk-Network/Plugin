package network.skulk.plugin.extensions.homes.commands;

import network.skulk.plugin.extensions.homes.HomesExtension;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class HomeDeleteCommand implements CommandExecutor {
    private final @NotNull HomesExtension extension;

    public HomeDeleteCommand(@NotNull HomesExtension extension) {
        this.extension = extension;
        extension.plugin.registerCommand(this, "home-delete");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }
}