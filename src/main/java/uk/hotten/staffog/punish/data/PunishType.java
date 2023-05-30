package uk.hotten.staffog.punish.data;

import lombok.Getter;
import org.jooq.TableLike;
import uk.hotten.staffog.data.jooq.Tables;

public enum PunishType {

    BAN(Tables.STAFFOG_BAN, "banned"),
    MUTE(Tables.STAFFOG_MUTE, "muted");

    @Getter private TableLike table;
    @Getter private String broadcastMessage;

    private PunishType(TableLike table, String broadcastMessage) {
        this.table = table;
        this.broadcastMessage = broadcastMessage;
    }

}
