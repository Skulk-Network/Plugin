package network.skulk.plugin.extensions.homes.commands;

import net.kyori.adventure.text.Component;
import network.skulk.plugin.extensions.homes.Home;
import network.skulk.plugin.extensions.homes.HomesExtension;
import network.skulk.wrapper.BaseCommand;
import org.bukkit.entity.Player;

import static network.skulk.utils.MiniMessageFormat.*;

public final class HomeListCommand extends BaseCommand<HomesExtension> {

    @Override
    protected void init() {
        this.name = "home-list";
        this.playerOnly = true;
        this.maxArgs = 0;
        this.minArgs = 0;
    }

    @Override
    protected boolean execute(final Player player) {
        final var playerHomes = this.getExtension().getHomes().get(player.getName());

        if (playerHomes.isEmpty()) {
            sendMessage(player, "orange", '!', "You don't have any homes! You can set one by doing <b>/home-set myHomeName</b>");
            return true;
        }

        final var component = Component.text().append(
                makeMessage("orange", '!', "All homes:")
        );

        for (final Home home : playerHomes) {
            final var l = home.location();

            component.append(fmt("<b><gray>-></gray></b> <color:#ffae1a><0> (X: %.0f, Y: %.0f, Z: %.0f)</color>".formatted(l.getX(), l.getY(), l.getZ()), home.name()));
        }

        player.sendMessage(component);

        return true;
    }
}