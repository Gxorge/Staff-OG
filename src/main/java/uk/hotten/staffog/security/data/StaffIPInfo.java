package uk.hotten.staffog.security.data;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class StaffIPInfo {

	public void setGameVerified(boolean gameVerified) {
		this.gameVerified = gameVerified;
	}

	public int getId() {
		return id;
	}

	public boolean isPanelVerified() {

		return panelVerified;

	}

	@Getter private int id;
	@Getter private UUID uuid;
	@Getter private String ip;
	public UUID getUuid() {
		return uuid;
	}

	public String getIp() {
		return ip;
	}

	public boolean isInitial() {
		return initial;
	}

	public boolean isPanelAcknowledged() {
		return panelAcknowledged;
	}

	public boolean isGameVerified() {
		return gameVerified;
	}

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
