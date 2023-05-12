package uk.hotten.staffog.punish;

import lombok.Getter;

public class PunishmentManager {

    @Getter private static PunishmentManager instance;

    public PunishmentManager() {
        instance = this;
    }

}
