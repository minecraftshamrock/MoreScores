package me.minecraftshamrock.morescores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.minecraftshamrock.morescores.listeners.FoodLevelChangeEventHandler;
import me.minecraftshamrock.morescores.listeners.PlayerLevelChangeEventHandler;
import me.minecraftshamrock.morescores.org.mcstats.MetricsLite;
import net.minecraft.util.com.google.common.reflect.TypeToken;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoreScores extends JavaPlugin {

	private static MoreScores instance;
	
	private HashMap<String, MSObjectiveType> objectives;
	private Gson gson;
	
	public static final MoreScores getInstance() {
		return instance;
	}
	
	@Override
	public final synchronized void onEnable() {
		instance = this;
		final PluginDescriptionFile desc = this.getDescription();
		final Logger log = this.getLogger();
		log.info("Loading MoreScores " + desc.getVersion());
		
		final GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		builder.serializeSpecialFloatingPointValues();
		builder.disableHtmlEscaping();
		builder.setPrettyPrinting();
		gson = builder.create();
		objectives = loadObjectives();		
		
		this.getCommand("morescores").setExecutor(new CommandMoreScores());
		
		Bukkit.getPluginManager().registerEvents(new PlayerLevelChangeEventHandler(), this);
		Bukkit.getPluginManager().registerEvents(new FoodLevelChangeEventHandler(), this);
		
		try {
			final MetricsLite m = new MetricsLite(this);
			m.start();
		} catch(final IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public final synchronized void addObjective(final String name, final MSObjectiveType type) {
		objectives.put(name, type);
		saveObjectives(objectives);
	}
	
	public final synchronized void removeObjective(final String name) {
		objectives.remove(name);
		saveObjectives(objectives);
	}
	
	public final synchronized Map<String, MSObjectiveType> getObjectives() {
		return objectives;
	}
	
	@SuppressWarnings({ "unchecked", "serial" })
	private final synchronized  HashMap<String, MSObjectiveType> loadObjectives() {
		try {
			if(!this.getDataFolder().exists()) {
				this.getDataFolder().mkdir();
			}
			final File f = new File(this.getDataFolder(), "objectives.json");
			if(!f.exists()) {
				saveObjectives(new HashMap<String, MSObjectiveType>());
			}
			final BufferedReader in = new BufferedReader(new FileReader(f));
			final StringBuilder b = new StringBuilder();
			final String ls = System.lineSeparator();
			String line;
			while((line = in.readLine()) != null) {
				b.append(line);
				b.append(ls);
			}
			in.close();
			return (HashMap<String, MSObjectiveType>) gson.fromJson(b.toString(), new TypeToken<HashMap<String, MSObjectiveType>>() {}.getType());
		} catch(final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private final synchronized void saveObjectives(final HashMap<String, MSObjectiveType> objectives) {
		try {
			if(!this.getDataFolder().exists()) {
				this.getDataFolder().mkdir();
			}
			final File f = new File(this.getDataFolder(), "objectives.json");
			if(!f.exists()) {
				this.getLogger().info("Creating new objectives.json file.");
				f.createNewFile();
			}
			@SuppressWarnings("serial")
			final String json = gson.toJson(objectives, new TypeToken<HashMap<String, MSObjectiveType>>() {}.getType());
			final PrintWriter out = new PrintWriter(f);
			out.write(json);
			out.close();
			f.setLastModified(System.currentTimeMillis());
		} catch(final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
