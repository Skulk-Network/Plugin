package network.skulk.plugin.extensions.tpa;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import network.skulk.helpers.FileHelper;
import network.skulk.plugin.Plugin;
import network.skulk.plugin.extensions.tpa.commands.*;
import network.skulk.plugin.extensions.tpa.listeners.PlayerQuitListener;
import network.skulk.singletons.Singletons;
import network.skulk.utils.NestedPlayerMap;
import network.skulk.wrapper.BaseExtension;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.UUID;

public final class TPAExtension extends BaseExtension {
    @Override
    protected void initCommands() {
        new TPAAcceptCommand(this);
        new TPACancelCommand(this);
        new TPACommand(this);
        new TPAIgnoreAllCommand(this);
        new TPAIgnoreCommand(this);
        new TPAListIgnoredCommand(this);
        new TPARejectCommand(this);
    }

    @Override
    protected void initListeners() {
        new PlayerQuitListener(this);
    }

    // Vs want to TPA to K.
    private final NestedPlayerMap<BukkitTask> tpaRequests = new NestedPlayerMap<>();
    private final File tpaIgnoresFile = new File(this.getPlugin().getDataFolder(), "tpaIgnores.yml");
    private Multimap<UUID, String> tpaIgnores;
    private FileWriter tpaIgnoresFileWriter;

    public TPAExtension(final Plugin extension) {
        super(extension);
    }

    public HashMap<Player, HashMap<Player, BukkitTask>> getTpaRequests() {
        return this.tpaRequests;
    }

    @Override
    protected void onEnableHook() throws Exception {
        final var plugin = this.getPlugin();
        final var yaml = Singletons.getYaml();

        FileHelper.createFile(this.tpaIgnoresFile);

        this.tpaIgnores = yaml.load(new FileInputStream(this.tpaIgnoresFile));

        if (this.tpaIgnores == null) {
            this.tpaIgnores = HashMultimap.create();
        }

        this.tpaIgnoresFileWriter = new FileWriter(this.tpaIgnoresFile);
        plugin.runRepeatingAsync(30 * 60 * 20, () -> {
            try {
                yaml.dump(this.tpaIgnores, this.tpaIgnoresFileWriter);
            } catch (final Exception error) {
                plugin.reportError("There was an error while trying to save the TPA ignores:", error);
            }
        });
    }

    @Override
    protected void onDisableHook() {
        Singletons.getYaml().dump(this.tpaIgnores, this.tpaIgnoresFileWriter);
    }

    public Multimap<UUID, String> getTpaIgnores() {
        return this.tpaIgnores;
    }
}
