package uk.hotten.staffog.tasks.data;

import lombok.Getter;
import uk.hotten.staffog.punish.data.PunishType;

import java.util.UUID;

public class UnpunishTask {

    @Getter private PunishType type;
    @Getter private int id;
    @Getter private UUID removedUuid;
    @Getter private String removedName;
    @Getter private String removedReason;
    @Getter private long removedTime;

    public UnpunishTask(String type, int id, String removedUuid, String removedName, String removedReason, long removedTime) {
        this.type = PunishType.valueOf(type);
        this.id = id;
        this.removedUuid = UUID.fromString(removedUuid);
        this.removedName = removedName;
        this.removedReason = removedReason;
        this.removedTime = removedTime;
    }

    public UnpunishTask() { }

}
