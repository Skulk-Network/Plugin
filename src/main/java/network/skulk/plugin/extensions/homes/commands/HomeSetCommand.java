package network.skulk.plugin.extensions.homes.commands;

import network.skulk.plugin.extensions.homes.Home;
import network.skulk.plugin.extensions.homes.HomesExtension;
import network.skulk.wrapper.BaseCommand;
import org.bukkit.entity.Player;

import static network.skulk.utils.MiniMessageHelper.sendMessage;

public final class HomeSetCommand extends BaseCommand<HomesExtension> {
    public HomeSetCommand(final HomesExtension extension) {
        super(extension);
    }

    @Override
    protected void init() {
        this.name = "home-set";
        this.playerOnly = true;
        this.maxArgs = 1;
        this.minArgs = 0;
    }

    @Override
    protected boolean execute(final Player player, final String[] args) {
        final var playerHomes = this.getExtension().getHomes().get(player.getName());
        final var playerLocation = player.getLocation();

        final String homeName;

        if (args.length == 0) {
            for (final Home home : playerHomes) {
                // Player already has a home named 'home'
                if (home.name().equalsIgnoreCase("home")) {
                    sendMessage(player, "red", '!', "You already have a default home. You need to delete that home and set it again.");
                    return true;
                }
            }

            homeName = "home";
        } else {
            homeName = args[0];
        }

        for (final Home home : playerHomes) {
            final var existingHomeName = home.name();

            if (existingHomeName.equalsIgnoreCase(homeName)) {
                sendMessage(player, "red", '!', "You already have a home named <b><0></b>.", existingHomeName);
                return true;
            }
        }

        if (homeName.length() >= 16) {
            sendMessage(player, "red", '!', "Home names can't be longer than 16 characters.");
            return true;
        }

        if (playerHomes.size() >= 16) {
            sendMessage(player, "red", '!', "You have the maximum amount of homes allowed (16).");
            return true;
        }

        playerHomes.add(new Home(homeName, playerLocation));

        sendMessage(player, "green", '✓', "Successfully created a home named <b><0></b>.", homeName);

        return true;
    }
}
