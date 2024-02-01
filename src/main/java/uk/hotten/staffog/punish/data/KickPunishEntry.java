package uk.hotten.staffog.punish.data;

import java.util.UUID;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

public class KickPunishEntry {

	@Getter @Setter private Player player;
	@Getter @Setter private String name;

	// Data stored in DB
	@Getter @Setter private int id;
	@Getter @Setter private UUID uuid;
	@Getter @Setter private String reason;

	@Getter @Setter private String byUuid;
	@Getter @Setter private String byName;

	@Getter @Setter private long time;

}