package uk.hotten.staffog.punish.data;

import lombok.Getter;

public enum PunishType {

    BAN("staffog_ban"),
    MUTE("staffog_mute");

    @Getter private String table;

    private PunishType(String table) {
        this.table = table;
    }

}
