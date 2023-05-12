package uk.hotten.staffog.punish.data;

import lombok.Getter;

import java.util.UUID;

public class PunishEntry {

    @Getter private PunishType type;

    @Getter private UUID uuid;
    @Getter private String reason;

    @Getter private UUID byUuid;
    @Getter private String byName;

    @Getter private UUID removedUuid;
    @Getter private String removedName;

    @Getter private int time;
    @Getter private int until;

    @Getter private boolean active;

    public PunishEntry(PunishType type, UUID uuid, String reason, UUID byUuid, String byName, int time, int until, boolean active) {
        this.type = type;
        this.uuid = uuid;
        this.reason = reason;
        this.byUuid = byUuid;
        this.byName = byName;
        this.time = time;
        this.until = until;
        this.active = active;
    }

    public PunishEntry(PunishType type, UUID uuid, String reason, UUID byUuid, String byName, UUID removedUuid, String removedName, int time, int until, boolean active) {
        this.type = type;
        this.uuid = uuid;
        this.reason = reason;
        this.byUuid = byUuid;
        this.byName = byName;
        this.removedUuid = removedUuid;
        this.removedName = reason;
        this.time = time;
        this.until = until;
        this.active = active;
    }

}
