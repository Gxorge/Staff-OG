package uk.hotten.staffog.punish.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import uk.hotten.staffog.utils.TimeUtils;

import java.util.*;

public class PunishEntry {

    // None DB data
    @Getter @Setter private PunishType type;
    @Getter @Setter private Player player;
    @Getter @Setter private String name;

    // Data stored in DB
    @Getter @Setter private long id;
    @Getter @Setter private UUID uuid;
    @Getter @Setter private String reason;

    @Getter @Setter private String byUuid;
    @Getter @Setter private String byName;

    @Getter @Setter private String removedUuid;
    @Getter @Setter private String removedName;
    @Getter @Setter private String removedReason;
    @Getter @Setter private long removedTime;

    @Getter @Setter private long time;
    @Getter @Setter private long until;

    @Getter @Setter private boolean active;

    public PunishEntry(PunishType type) {
        this.type = type;
    }

    public long calculateDuration() {
        if (until == -1)
            return -1;

        return until - time;
    }

    public long calculateRemaining() {
        if (until == -1)
            return -1;

        return until - System.currentTimeMillis();
    }

    public boolean checkDurationOver() {
        if (until == -1)
            return false;

        return (calculateRemaining() <= 0);
    }

    public String calculateUntilDate() {
        if (until == -1)
            return "forever";

        Date date = new Date(until);
        return TimeUtils.notificationTimeFormat.format(date);
    }
}
