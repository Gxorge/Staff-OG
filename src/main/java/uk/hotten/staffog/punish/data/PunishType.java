package uk.hotten.staffog.punish.data;

import lombok.Getter;

public enum PunishType {

    BAN("staffog_ban", "banned"),
    MUTE("staffog_mute", "muted");

    @Getter private String table;
    @Getter private String broadcastMessage;

    private PunishType(String table, String broadcastMessage) {
        this.table = table;
        this.broadcastMessage = broadcastMessage;
    }

}
