package network.skulk.plugin.core.extensions.updatechecker.commands;

import network.skulk.plugin.core.extensions.updatechecker.UpdateCheckerExtension;
import network.skulk.plugin.wrapper.BaseCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static network.skulk.plugin.helpers.MiniMessageHelper.sendMessage;

// TODO
public final class UpdateCommand extends BaseCommand<UpdateCheckerExtension> {
    public UpdateCommand(final @NotNull UpdateCheckerExtension extension) {
        super(extension);
    }

    @Override protected void init() {
        this.setName("update-snp");
        this.setDescription("Updates the plugin");
        this.setUsage("/update-snp");
        this.setPlayerOnly(false);
        this.setMaxArgs(0);
        this.setMinArgs(0);
        this.setPermission("op");
    }

    // TODO
    @Override protected boolean execute(final @NotNull CommandSender sender) {
        sendMessage(sender, "orange", '!', "This command is not implemented yet.");
        return true;
    }
}