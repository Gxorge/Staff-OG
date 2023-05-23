package uk.hotten.staffog.tasks.data;

import lombok.Getter;
import uk.hotten.staffog.punish.data.PunishType;

import java.util.UUID;

public class UnpunishTask {

    @Getter private PunishType type;
    @Getter private int id;
    @Getter private String name;

    public UnpunishTask(String type, int id) {
        this.type = PunishType.valueOf(type);
        this.id = id;
    }

    public UnpunishTask() { }

}
