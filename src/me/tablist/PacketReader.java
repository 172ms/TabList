package me.tablist;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

@SuppressWarnings("deprecation")
public class PacketReader {
	private static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	
	public static void setPlayerListHeaderFooter(Player player, String header, String footer) {
		PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
		
		packetContainer.getChatComponents().write(0, WrappedChatComponent.fromText(header));
		packetContainer.getChatComponents().write(1, WrappedChatComponent.fromText(footer));
		
		try {
			protocolManager.sendServerPacket(player, packetContainer);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void register() {
		protocolManager.addPacketListener(new PacketAdapter(Main.getPlugin(Main.class), PacketType.Play.Server.PLAYER_INFO) {
			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packetContainer = event.getPacket();
				EnumWrappers.PlayerInfoAction action = packetContainer.getPlayerInfoAction().read(0);
				
				if (action == EnumWrappers.PlayerInfoAction.REMOVE_PLAYER) {
					String header = ChatColor.translateAlternateColorCodes('&',
							Main.getPlugin(Main.class).getConfig().getString("tablistTag-header"))
							.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
							.replace("%onlineMAX%", String.valueOf(Bukkit.getMaxPlayers()));
					
					String footer = ChatColor.translateAlternateColorCodes('&',
							Main.getPlugin(Main.class).getConfig().getString("tablistTag-footer"))
							.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
							.replace("%onlineMAX%", String.valueOf(Bukkit.getMaxPlayers()));
					
					Bukkit.getOnlinePlayers().forEach(player -> setPlayerListHeaderFooter(player, header, footer));
				}
				
				if (action == EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
					String header = ChatColor.translateAlternateColorCodes('&',
							Main.getPlugin(Main.class).getConfig().getString("tablistTag-header"))
							.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
							.replace("%onlineMAX%", String.valueOf(Bukkit.getMaxPlayers()));
					
					String footer = ChatColor.translateAlternateColorCodes('&',
							Main.getPlugin(Main.class).getConfig().getString("tablistTag-footer"))
							.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
							.replace("%onlineMAX%", String.valueOf(Bukkit.getMaxPlayers()));
					
					Bukkit.getOnlinePlayers().forEach(player -> setPlayerListHeaderFooter(player, header, footer));
				}
			}
		});
	}
}