package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.api.gps.TeleportationManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * A {@link WaypointCreateEvent} is called when a {@link Player} creates a new waypoint.
 * Either manually or through dying with an emergency transmitter.
 *
 * @author TheBusyBiscuit
 * @see GPSNetwork
 * @see TeleportationManager
 */
public class WaypointCreateEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Location location;
    private String name;

    private final boolean deathpoint;
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public WaypointCreateEvent(Player player, String name, Location location) {
        super(player);

        Validate.notNull(location, "Location must not be null!");
        Validate.notNull(name, "Name must not be null!");

        this.location = location;
        this.name = name;
        this.deathpoint = name.startsWith("player:death ");
    }

    /**
     * This returns the {@link Location} of the waypoint that should be created.
     *
     * @return The {@link Location} of this waypoint
     */
    public Location getLocation() {
        return location;
    }

    /**
     * This sets the {@link Location} of the waypoint.
     * The {@link Location} may never be null!
     *
     * @param loc The {@link Location} to set
     */
    public void setLocation(Location loc) {
        Validate.notNull(loc, "Cannot set the Location to null!");
        this.location = loc;
    }

    /**
     * This returns the name of the waypoint.
     *
     * @return The name of this waypoint
     */
    public String getName() {
        return name;
    }

    /**
     * This sets the name of the waypoint to the given argument.
     *
     * @param name The name for this waypoint
     */
    public void setName(String name) {
        Validate.notEmpty(name, "The name of a waypoint must not be empty!");
        this.name = name;
    }

    /**
     * This method returns whether this waypoint was created by an Emergency Transmitter.
     * This should mean that our {@link Player} has died.
     *
     * @return Whether this is a deathpoint
     */
    public boolean isDeathpoint() {
        return deathpoint;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}