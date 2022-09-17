package network.skulk.plugin.extensions.tpa.commands;

import network.skulk.plugin.extensions.tpa.TPAExtension;
import network.skulk.wrapper.BaseCommand;
import org.bukkit.entity.Player;

import static network.skulk.helpers.MiniMessageHelper.sendMessage;

public final class TPAIgnoreAllCommand extends BaseCommand<TPAExtension> {
    @Override
    protected void init() {
        this.name = "tpa-ignore-all";
        this.playerOnly = true;
        this.maxArgs = 0;
        this.minArgs = 0;
    }

    public TPAIgnoreAllCommand(final TPAExtension extension) {
        super(extension);
    }

    @Override
    protected boolean execute(final Player player) {
        final var playerIgnores = this.getExtension().getTpaIgnores().get(player.getUniqueId());

        final String message;

        if (playerIgnores.contains("*")) {
            playerIgnores.remove("*");
            message = "You are now <b>not</b> ignoring everyone's TPA requests.";

        } else {
            playerIgnores.add("*");
            message = "You are now ignoring everyone's TPA requests.";
        }

        sendMessage(player, "green", '✓', message);

        return true;
    }
}
