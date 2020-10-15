package me.mrroyalit.coins.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.mrroyalit.coins.Main;
import me.mrroyalit.coins.utils.Utils;

public class OnJoinEvent implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		try {
			ResultSet rs = Main.prepareStatement("SELECT COUNT(UUID) FROM player_data WHERE UUID = '" + player.getUniqueId().toString() + "';").executeQuery();
			rs.next();
			
			if(rs.getInt(1) == 0) {
				Main.prepareStatement("INSERT INTO player_data(UUID, Coins) VALUES('" + player.getUniqueId().toString() + "', '0');").executeUpdate();
				System.out.println("New player added!");
			}else {
				System.out.println("Player already exists");
			}
			
		} catch(SQLException x) {
			x.printStackTrace();
		}
	}
	
}
