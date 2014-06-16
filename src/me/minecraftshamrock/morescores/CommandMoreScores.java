package me.minecraftshamrock.morescores;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandMoreScores implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 2) {
			sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.WHITE + "/morescores <add|remove> <name> [type]");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("remove")) {
			MoreScores.getInstance().removeObjective(args[1]);
			sender.sendMessage(ChatColor.GOLD + "Removed objective " + args[1] + ".");
		} else if(args[0].equalsIgnoreCase("add")) {
			if(args.length < 3) {
				sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.WHITE + "/morescores add <name> <type>");
				return true;
			}
			final MSObjectiveType type;
			try {
				 type = MSObjectiveType.valueOf(args[2]);
			} catch(final IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED + args[2] + " is no valid type. Valid types are:");
				for(final MSObjectiveType o : MSObjectiveType.values()) {
					sender.sendMessage(ChatColor.DARK_RED + " -> " + o.name());
				}
				return true;
			}
			MoreScores.getInstance().addObjective(args[1], type);
			sender.sendMessage(ChatColor.GOLD + "Added objective " + args[1] + " with type " + type.name() + ".");
		} else {
			sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.WHITE + "/morescores <add|remove> <name> [type]");
		}
		
		return true;
	}

}
