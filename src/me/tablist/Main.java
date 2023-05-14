package me.tablist;

import org.bukkit.plugin.java.JavaPlugin;
import java.nio.file.StandardCopyOption;
import org.bukkit.entity.Player;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import java.io.File;
import java.net.URL;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		if (!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			getServer().getConsoleSender().sendMessage("§e[!] §cPROTOCOLLIB NOT FOUND!");
			downloadProtocolLib();
			return;
		}
		
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		
		File configFile = new File(getDataFolder(), "config.yml");
		
		if (!configFile.exists()) {
			saveResource("config.yml", false);
		}
		
		Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(this::updateLocation), 0L, 20L);
		
		PacketReader.register();
	}
	
	@Override
	public void onDisable() {
		saveConfig();
	}
	
	private void downloadProtocolLib() {
		try {
			String protocolLibURL = "https://github.com/dmulloy2/ProtocolLib/releases/download/5.0.0/ProtocolLib.jar";
			String path = "plugins/ProtocolLib.jar";
			URL url = new URL(protocolLibURL);
			
			try (InputStream inputStream = url.openStream()) {
				Files.copy(inputStream, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
			}
			
			Bukkit.reload();
			getServer().getConsoleSender().sendMessage("§e[!] §aPROTOCOLLIB DOWNLOADED SUCCESSFULLY!");
		}
		catch (Exception e) {
			getServer().getConsoleSender().sendMessage("§e[!] §cFAILED TO DOWNLOAD PROTOCOLLIB!");
			getPluginLoader().disablePlugin(this);
		}
	}
	
	private void updateLocation(Player player) {
		player.setPlayerListName(player.getDisplayName() + getLocationString(player));
	}
	
	private String getLocationString(Player player) {
		Location location = player.getLocation();
		String world = getWorld(location.getWorld().getEnvironment().name());
		String locationString = String.format(" [%s %d %d %d]", world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
		return locationString;
	}
	
	private String getWorld(String name) {
		String worldName = name.toLowerCase();
		
		switch (worldName) {
			case "normal": {
				return "WORLD";
			}
			
			case "end": {
				return "END";
			}
			
			case "nether": {
				return "NETHER";
			}
			
			default: {
				return worldName.substring(0, 1).toUpperCase() + worldName.substring(1);
			}
		}
	}
}