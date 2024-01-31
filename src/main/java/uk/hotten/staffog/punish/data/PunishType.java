package uk.hotten.staffog.punish.data;

import lombok.Getter;

public enum PunishType {

	BAN("staffog_ban", "banned"),
	MUTE("staffog_mute", "muted");

	public String getTable() {
		return table;
	}

	public String getBroadcastMessage() {
		return broadcastMessage;
	}

	@Getter private String table;
	@Getter private String broadcastMessage;

	private PunishType(String table, String broadcastMessage) {
		this.table = table;
		this.broadcastMessage = broadcastMessage;
	}

}
