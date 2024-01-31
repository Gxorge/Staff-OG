package uk.hotten.staffog.punish.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import uk.hotten.staffog.utils.TimeUtils;

import java.util.*;

public class PunishEntry {

    public String getName() {
		return name;
	}

	public PunishType getType() {
		return type;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getRemovedUuid() {
		return removedUuid;
	}

	public String getRemovedName() {
		return removedName;
	}

	public String getRemovedReason() {
		return removedReason;
	}

	public long getRemovedTime() {
		return removedTime;
	}

	public Player getPlayer() {
		return player;
	}

	public void setRemovedTime(long removedTime) {
		this.removedTime = removedTime;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getReason() {
		return reason;
	}

	public String getByUuid() {
		return byUuid;
	}

	public String getByName() {
		return byName;
	}

	public long getTime() {
		return time;
	}

	public long getUntil() {
		return until;
	}

	public boolean isActive() {
		return active;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setRemovedUuid(String removedUuid) {
		this.removedUuid = removedUuid;
	}

	public void setRemovedName(String removedName) {
		this.removedName = removedName;
	}

	public void setRemovedReason(String removedReason) {
		this.removedReason = removedReason;
	}

	// None DB data
    @Getter @Setter private PunishType type;
    @Getter @Setter private Player player;
    @Getter @Setter private String name;

    // Data stored in DB
    @Getter @Setter private long id;
    @Getter @Setter private UUID uuid;
    @Getter @Setter private String reason;

    @Getter @Setter private String byUuid;
    @Getter @Setter private String byName;

    @Getter @Setter private String removedUuid;
    public void setName(String name) {
		this.name = name;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setByUuid(String byUuid) {
		this.byUuid = byUuid;
	}

	public void setByName(String byName) {
		this.byName = byName;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setUntil(long until) {
		this.until = until;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Getter @Setter private String removedName;
    @Getter @Setter private String removedReason;
    @Getter @Setter private long removedTime;

    @Getter @Setter private long time;
    @Getter @Setter private long until;

    @Getter @Setter private boolean active;

    public PunishEntry(PunishType type) {
        this.type = type;
    }

    public long calculateDuration() {
        if (until == -1)
            return -1;

        return until - time;
    }

    public long calculateRemaining() {
        if (until == -1)
            return -1;

        return until - System.currentTimeMillis();
    }

    public boolean checkDurationOver() {
        if (until == -1)
            return false;

        return (calculateRemaining() <= 0);
    }

    public String calculateUntilDate() {
        if (until == -1)
            return "forever";

        Date date = new Date(until);
        return TimeUtils.notificationTimeFormat.format(date);
    }
}
