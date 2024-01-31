package uk.hotten.staffog.punish.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickPunishEntry {

    public String getName() {
		return name;
	}

	@Getter @Setter private Player player;
    public void setPlayer(Player player) {
		this.player = player;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Player getPlayer() {
		return player;
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

	public void setByUuid(String byUuid) {
		this.byUuid = byUuid;
	}

	public void setByName(String byName) {
		this.byName = byName;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Getter @Setter private String name;

    // Data stored in DB
    @Getter @Setter private int id;
    @Getter @Setter private UUID uuid;
    @Getter @Setter private String reason;

    @Getter @Setter private String byUuid;
    @Getter @Setter private String byName;

    @Getter @Setter private long time;

}
