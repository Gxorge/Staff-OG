package uk.hotten.staffog.punish;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.Message;
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

        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        event.setKickMessage(ChatColor.RED + "You have been banned for: \n" +
                ChatColor.WHITE + entry.getReason() + "\n\n" +
                ChatColor.RED + "Your ban " + (entry.getUntil() == -1 ? "does not expire." : "will expire in:\n"
                + ChatColor.WHITE + TimeUtils.formatMillisecondTime(entry.calculateRemaining())));
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        PunishEntry entry = PunishManager.getInstance().checkActivePunishment(PunishType.MUTE, event.getPlayer().getUniqueId());

        if (entry == null)
            return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Message.format(ChatColor.RED + "You have been muted for " + ChatColor.WHITE + entry.getReason()));
        event.getPlayer().sendMessage(Message.format(ChatColor.RED + "Your mute " + (entry.getUntil() == -1 ? "does not expire." : "will expire in: "
                + ChatColor.WHITE + TimeUtils.formatMillisecondTime(entry.calculateRemaining()))));
    }

}
