package tk.skulk.plugin.wrapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static org.jetbrains.annotations.ApiStatus.OverrideOnly;
import static tk.skulk.plugin.helpers.MiniMessageHelper.sendMessage;

public abstract class BaseCommand <E extends BaseExtension> implements CommandExecutor, TabCompleter {
    private final @NotNull E extension;

    private String name;
    private String description;
    private String usage;
    private boolean playerOnly = true;
    private int minArgs = 0;
    private int maxArgs = 0;
    private @Nullable String permission = null;

    public BaseCommand(final @NotNull E extension) {
        this.extension = extension;
        this.init();

        final var plugin = extension.getPlugin();
        final var command = plugin.getCommand(this.name);

        if (command == null) {
            plugin.reportError(
                "The command '%s' could not be registered because it was not included in the plugin.yml.".formatted(
                    this.name));
            return;
        }

        command.setExecutor(this);
        command.setDescription(this.description);
        command.setUsage(this.usage);
        command.setPermission(this.permission);
    }

    protected @NotNull E getExtension() {
        return this.extension;
    }

    protected void setName(final @NotNull String name) {
        this.name = name;
    }

    protected void setDescription(final @NotNull String description) {
        this.description = description;
    }

    protected void setUsage(final @NotNull String usage) {
        this.usage = usage;
    }

    @SuppressWarnings("SameParameterValue")
    protected void setPlayerOnly(final boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    @SuppressWarnings("SameParameterValue")
    protected void setMinArgs(final int minArgs) {
        this.minArgs = minArgs;
    }

    @SuppressWarnings("SameParameterValue")
    protected void setMaxArgs(final int maxArgs) {
        this.maxArgs = maxArgs;
    }

    @SuppressWarnings("SameParameterValue")
    protected void setPermission(final @Nullable String permission) {
        this.permission = permission;
    }

    // aliases, playerOnly, marArgs and minArgs will be set here.
    @OverrideOnly
    protected void init() {
    }

    @OverrideOnly
    protected @Nullable ArrayList<String> tabComplete(
        final @NotNull Player player,
        final @NotNull String[] args
    ) {
        return null;
    }

    @OverrideOnly
    protected boolean execute(final @NotNull Player player) {
        sendMessage(
            player,
            "red",
            '!',
            "You might have overriden the wrong method... <gold>(player)</gold>"
        );
        return true;
    }

    @OverrideOnly
    protected boolean execute(final @NotNull Player player, final @NotNull String[] args) {
        sendMessage(
            player,
            "red",
            '!',
            "You might have overriden the wrong method... <gold>(player, args[])</gold>"
        );
        return true;
    }

    @OverrideOnly
    protected boolean execute(final @NotNull CommandSender sender) {
        sendMessage(
            sender,
            "red",
            '!',
            "You might have overriden the wrong method... <gold>(sender)</gold>"
        );
        return true;
    }

    @OverrideOnly
    protected boolean execute(final @NotNull CommandSender sender, final @NotNull String[] args) {
        sendMessage(
            sender,
            "red",
            '!',
            "You might have overriden the wrong method... <gold>(sender, args[])</gold>"
        );
        return true;
    }

    @OverrideOnly
    protected @Nullable ArrayList<String> tabComplete(
        final @NotNull CommandSender sender,
        final @NotNull String[] args
    ) {
        return null;
    }

    @Override
    public final @Nullable ArrayList<String> onTabComplete(
        final @NotNull CommandSender sender,
        final @NotNull Command command,
        final @NotNull String label,
        final @NotNull String[] args
    ) {
        if (this.playerOnly) {
            return tabComplete((Player) sender, args);
        }
        else {
            return tabComplete(sender, args);
        }
    }

    @Override
    public final boolean onCommand(
        final @NotNull CommandSender sender,
        final @NotNull Command command,
        final @NotNull String label,
        final @NotNull String[] args
    ) {
        if (this.playerOnly && !(sender instanceof Player)) {
            sendMessage(sender, "red", '!', "This command can only be used by players.");
            return true;
        }

        if (args.length > this.maxArgs || args.length < this.minArgs) {
            return false;
        }

        if (this.playerOnly) {
            final var player = (Player) sender;
            if (maxArgs == 0) {
                return execute(player);
            }
            return execute(player, args);
        }

        if (maxArgs == 0) {
            return execute(sender);
        }

        return execute(sender, args);
    }
}
