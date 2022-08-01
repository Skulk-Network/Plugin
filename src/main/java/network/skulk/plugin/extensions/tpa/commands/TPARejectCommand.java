package network.skulk.plugin.extensions.tpa.commands;

import network.skulk.plugin.constants.Message;
import network.skulk.plugin.extensions.tpa.TPAExtension;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public final class TPARejectCommand implements CommandExecutor {
    private final @NotNull TPAExtension extension;

    public TPARejectCommand(@NotNull TPAExtension extension) {
        this.extension = extension;
        extension.plugin.registerCommand(this, "tpa-reject");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.CONSOLE_NOT_ALLOWED);
            return true;
        } else if (args.length > 1) {
            return false;
        }

        String playerName = player.getName();
        HashSet<String> playerIncomingRequests = extension.tpaRequests.computeIfAbsent(playerName, k -> new HashSet<>());

        Player target;
        String targetName;

        if (args.length == 1) {
            targetName = args[0];
        } else {
            // No target was specified.
            if (playerIncomingRequests.size() == 1) {
                // The player has 1 incoming TPA request.
                targetName = playerIncomingRequests.iterator().next();
            } else if (playerIncomingRequests.size() == 0) {
                // The player has no incoming TPA requests.
                player.sendRichMessage(Message.NO_INCOMING_TPA_REQUESTS);
                return true;
            } else {
                // Multiple people want to TPA to the player.
                StringBuilder response = new StringBuilder()
                        .append(Message.ASD);

                for (String toReject : playerIncomingRequests) {
                    response.append(Message.DEF.formatted(toReject, toReject));
                }

                player.sendRichMessage(response.toString());
                return true;
            }
        }

        target = Bukkit.getPlayer(targetName);

        if (target == null || !target.isOnline()) {
            player.sendRichMessage(Message.PLAYER_OFFLINE);
            return true;
        } else if (!playerIncomingRequests.contains(targetName)) {
            player.sendRichMessage(Message.FOO.formatted(targetName));
            return true;
        }

        playerIncomingRequests.remove(targetName);

        player.sendRichMessage(Message.BAR.formatted(targetName));
        target.sendRichMessage(Message.BAZ.formatted(playerName));

        return true;
    }
}
