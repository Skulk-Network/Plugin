package network.skulk.wrapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static network.skulk.utils.MiniMessageFormat.sendMessage;
import static org.jetbrains.annotations.ApiStatus.OverrideOnly;

public abstract class BaseCommand<E extends BaseExtension> implements CommandExecutor, TabCompleter {
    protected int minArgs;
    protected String[] aliases;
    protected boolean playerOnly;
    protected int maxArgs;
    protected String[] neededAnyPermissions = new String[0];

    private E extension;

    public final void create(final E extension) {
        this.extension = extension;
        this.init();
        this.extension.registerCommand(this);
    }

    protected E getExtension() {
        return this.extension;
    }

    // aliases, playerOnly, marArgs and minArgs will be set here.
    @OverrideOnly
    protected void init() {
    }

    @Nullable
    @OverrideOnly
    protected List<String> tabComplete(final CommandSender player, final String[] args) {
        return null;
    }

    @Nullable
    @OverrideOnly
    protected List<String> tabComplete(final Player player, final String[] args) {
        return null;
    }

    @OverrideOnly
    protected boolean execute(final CommandSender sender, final String[] args) {
        return false;
    }

    @OverrideOnly
    protected boolean execute(final Player player, final String[] args) {
        return false;
    }

    @OverrideOnly
    protected boolean execute(final CommandSender sender) {
        return false;
    }

    @OverrideOnly
    protected boolean execute(final Player player) {
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (this.playerOnly) {
            return tabComplete((Player) sender, args);
        } else {
            return tabComplete(sender, args);
        }
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (this.playerOnly && !(sender instanceof Player)) {
            sendMessage(sender, "red", '!', "This command can only be used by players.");
            return true;
        }

        for (final String perm : this.neededAnyPermissions) {
            if (!sender.hasPermission(perm)) {
                sendMessage(sender, "red", '!', "You can't use this command.");
                return true;
            }
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

    // Utility functions.

    public final BukkitTask runAfter(final long delay, final Runnable runnable) {
        return this.extension.runAfter(delay, runnable);
    }

    public final BukkitTask runAsync(final Runnable runnable) {
        return this.extension.runAsync(runnable);
    }
}
