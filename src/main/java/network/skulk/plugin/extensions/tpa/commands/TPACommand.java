package network.skulk.plugin.extensions.tpa.commands;

import network.skulk.plugin.constants.Message;
import network.skulk.plugin.extensions.tpa.TPAExtension;
import network.skulk.plugin.pdts.StringListIncludesPDT;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public final class TPACommand implements CommandExecutor {
    private final @NotNull TPAExtension extension;

    public TPACommand(@NotNull final TPAExtension extension) {
        this.extension = extension;
        extension.plugin.registerCommand(this, "tpa");
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.ONLY_PLAYERS_CAN_USE_THIS_COMMAND);
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        final String playerName = player.getName();

        String targetName = args[0];

        if (targetName.equalsIgnoreCase(playerName)) {
            player.sendRichMessage(Message.TPA_TO_SELF_NOT_ALLOWED);
            return true;
        }

        final Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            player.sendRichMessage(Message.PLAYER_NOT_ONLINE);
            return true;
        }

        targetName = target.getName();

        final int targetIsIgnoring = target.getPersistentDataContainer().getOrDefault(extension.TPA_IGNORES_KEY, new StringListIncludesPDT(playerName), 0);

        if (targetIsIgnoring == 1) {
            player.sendRichMessage(Message.X_IGNORING_YOU.formatted(targetName));
            return true;
        }

        if (targetIsIgnoring == 2) {
            player.sendRichMessage(Message.X_IGNORING_ALL.formatted(targetName));
            return true;
        }

        final HashSet<String> targetIncomingRequests = extension.tpaRequests.computeIfAbsent(targetName, k -> new HashSet<>());

        if (targetIncomingRequests.contains(playerName)) {
            player.sendRichMessage(Message.TPA_REQUEST_TO_X_ALREADY_EXISTS.formatted(targetName));
            return true;
        }

        targetIncomingRequests.add(playerName);

        final String targetNameFinal = targetName;

        // FIXME: Kill this task when the TPA request is cancelled.
        extension.plugin.runLater(1200, () -> {
            if (targetIncomingRequests.contains(playerName)) {
                targetIncomingRequests.remove(playerName);
                player.sendRichMessage(Message.TPA_REQUEST_TO_X_EXPIRED.formatted(targetNameFinal));
                target.sendRichMessage(Message.TPA_REQUEST_X_SENT_TO_YOU_EXPIRED.formatted(playerName));
            }
        });

        player.sendRichMessage(Message.TPA_REQUEST_SENT_TO_X.formatted(targetName));
        target.sendRichMessage(Message.TPA_REQUEST_SENT_BY_X.formatted(playerName, playerName, playerName));

        return true;
    }
}
