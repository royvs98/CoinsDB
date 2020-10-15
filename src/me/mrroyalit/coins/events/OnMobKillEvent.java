package me.mrroyalit.coins.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.mrroyalit.coins.Main;
import me.mrroyalit.coins.utils.Utils;

public class OnMobKillEvent implements Listener {
	/*
	 * Mob kill method and player death method
	 */
	@EventHandler
	public void onMobKill(EntityDeathEvent e) {
		Entity died = e.getEntity();
		Entity killer = e.getEntity().getKiller();
		
		if(killer instanceof Player) {
			Player p = (Player) killer;
			
			if(died.getType().equals(EntityType.ZOMBIE)) {
				addCurrencyToPlayer(p, 1);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c1&e coin!"));
			}else if(died.getType().equals(EntityType.SKELETON)) {
				addCurrencyToPlayer(p, 1);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c1&e coin!"));
			}else if(died.getType().equals(EntityType.SPIDER)) {
				addCurrencyToPlayer(p, 1);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c1&e coin!"));
			}else if(died.getType().equals(EntityType.CAVE_SPIDER)) {
				addCurrencyToPlayer(p, 1);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c1&e coin!"));
			}else if(died.getType().equals(EntityType.ENDERMAN)) {
				addCurrencyToPlayer(p, 2);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c2&e coins!"));
			}else if(died.getType().equals(EntityType.CREEPER)) {
				addCurrencyToPlayer(p, 3);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c3&e coins!"));
			}else if(died.getType().equals(EntityType.GHAST)) {
				addCurrencyToPlayer(p, 5);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c5&e coins!"));
			}else if(died.getType().equals(EntityType.PIG_ZOMBIE)) {
				addCurrencyToPlayer(p, 1);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c1&e coin!"));
			}else if(died.getType().equals(EntityType.WITHER_SKELETON)) {
				addCurrencyToPlayer(p, 2);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c2&e coins!"));
			}else if(died.getType().equals(EntityType.WITHER)) {
				addCurrencyToPlayer(p, 10);
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&eYou have received &c10&e coins!"));
			}else{
				return;
			}
		}else {
			return;
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Entity died = e.getEntity();
		
		if(died instanceof Player) {
			Player p = (Player) died;
				
			removeCurrencyFromPlayer(p, 5);
		}else {
			return;
		}
	}
	
	public void addCurrencyToPlayer(OfflinePlayer p, int amount) {
		try {
			ResultSet rs = Main.prepareStatement("SELECT * FROM player_data WHERE UUID = '" + p.getUniqueId().toString() + "';").executeQuery();
			rs.next();
			Integer coins = rs.getInt("COINS");
			Integer newcoins = coins + amount;
			Main.prepareStatement("UPDATE player_data SET COINS='" + newcoins + "' WHERE UUID='" + p.getUniqueId().toString() + "';").executeUpdate();
		} catch(SQLException x) {
			x.printStackTrace();
		}
	}

	public void removeCurrencyFromPlayer(OfflinePlayer p, int amount) {
		try {
			ResultSet rs = Main.prepareStatement("SELECT * FROM player_data WHERE UUID = '" + p.getUniqueId().toString() + "';").executeQuery();
			rs.next();
			Integer coins = rs.getInt("COINS");
			
			if(amount > coins) {
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&ePlayer &c" + p.getName() + "&e does not have enough coins!"));
			}else {
				Integer newcoins = coins - amount;
				if(p.getUniqueId().toString() != null) {
					Main.prepareStatement("UPDATE player_data SET COINS='" + newcoins + "' WHERE UUID='" + p.getUniqueId().toString() + "';").executeUpdate();
				}else {
					Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&ePlayer &c" + p.getName() + "&e could not be found!"));
				}
			}
		} catch(SQLException x) {
			x.printStackTrace();
		}
	}
}
