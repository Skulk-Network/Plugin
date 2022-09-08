package network.skulk.wrapper;

import org.bukkit.event.Listener;

public abstract class BaseListener<E extends BaseExtension> implements Listener {
    private E extension;

    public final void init(final E extension) {
        this.extension = extension;
        this.extension.getPlugin().registerListener(this);
    }

    protected final E getExtension() {
        return this.extension;
    }
}
