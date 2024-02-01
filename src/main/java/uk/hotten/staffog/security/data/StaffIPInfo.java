package uk.hotten.staffog.security.data;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class StaffIPInfo {

	@Getter private int id;
	@Getter private UUID uuid;
	@Getter private String ip;
	@Getter @Setter private boolean initial;
	@Getter @Setter private boolean panelAcknowledged;
	@Getter @Setter private boolean panelVerified;
	@Getter @Setter private boolean gameVerified;

	public StaffIPInfo(int id, UUID uuid, String ip, boolean initial, boolean panelAcknowledged, boolean panelVerified, boolean gameVerified) {

		this.id = id;
		this.uuid = uuid;
		this.ip = ip;
		this.initial = initial;
		this.panelAcknowledged = panelAcknowledged;
		this.panelVerified = panelVerified;
		this.gameVerified = gameVerified;

	}

	public StaffIPInfo(int id, UUID uuid, String ip) {

		this.id = id;
		this.uuid = uuid;
		this.ip = ip;
		this.initial = false;
		this.panelVerified = false;
		this.gameVerified = false;

	}

}
