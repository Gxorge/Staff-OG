package uk.hotten.staffog.punish;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.TimeUtils;

public class PunishEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PunishManager.getInstance().checkNameToUuid(event.getPlayer().getName(), event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onAsyncPreJoin(AsyncPlayerPreLoginEvent event) {
        PunishEntry entry = PunishManager.getInstance().checkActivePunishment(PunishType.BAN, event.getUniqueId());

        if (entry == null)
            return;

        if (entry.checkDurationOver()) {
            PunishManager.getInstance().removePunishment(entry.getId(), "Expired", "");
            return;
        }

        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        event.setKickMessage(ChatColor.RED + "You are banned for " + TimeUtils.formatMillisecondTime(entry.calculateRemaining()) + "\nReason: " + ChatColor.WHITE + entry.getReason());
    }

}
