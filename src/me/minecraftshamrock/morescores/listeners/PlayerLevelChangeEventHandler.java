package me.minecraftshamrock.morescores.listeners;

import java.util.Map.Entry;

import me.minecraftshamrock.morescores.MSObjectiveType;
import me.minecraftshamrock.morescores.MoreScores;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public final class PlayerLevelChangeEventHandler implements Listener {

	@EventHandler(priority=EventPriority.MONITOR)
	public final void onPlayerLevelChange(final PlayerLevelChangeEvent event) {
		for(final Entry<String, MSObjectiveType> entry : MoreScores.getInstance().getObjectives().entrySet()) {
			if(entry.getValue() == MSObjectiveType.XP_LEVEL) {
				final Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
				final Objective o = board.getObjective(entry.getKey());
				if(o == null) {
					MoreScores.getInstance().getLogger().warning("The objective " + entry.getKey() + " has been removed!");
					MoreScores.getInstance().removeObjective(entry.getKey());
					return;
				}
				final Score s = o.getScore(event.getPlayer().getName());
				s.setScore(event.getNewLevel());
			}
		}
	}
	
}
