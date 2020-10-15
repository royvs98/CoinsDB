package me.mrroyalit.coins.managers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import me.mrroyalit.coins.Main;
import me.mrroyalit.coins.utils.Utils;

public class CurrencyManager {

	public Main plugin;

	public CurrencyManager(Main plugin) {
		this.plugin = plugin;
	}
	
		
	/*
	 * Create add, remove, set and get methods
	 */
	
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

	public void setPlayerCurrency(OfflinePlayer p, int amount) {
		try {
			Main.prepareStatement("UPDATE player_data SET COINS='" + amount + "' WHERE UUID='" + p.getUniqueId().toString() + "';").executeUpdate();
		} catch(SQLException x) {
			x.printStackTrace();
		}
	}

	public int getPlayerCurrency(OfflinePlayer p) {
		try {
			ResultSet rs = Main.prepareStatement("SELECT * FROM player_data WHERE UUID = '" + p.getUniqueId().toString() + "';").executeQuery();
			rs.next();
			int coins = rs.getInt("COINS");
			
			if(p.getUniqueId().toString() != null) {
				return coins;
			}else {
				Bukkit.getServer().getPlayer(p.getName()).sendMessage(Utils.chat("&ePlayer &c" + p.getName() + "&e could not be found!"));
			}
		} catch(SQLException x) {
			x.printStackTrace();
		}
		return 0;
	}
	
	public void listPlayersAndCoins(String sender) {
		
		Player p = Bukkit.getPlayer(sender);
		
		try {
			ResultSet rs = Main.prepareStatement("SELECT * FROM player_data;").executeQuery();
			
			while(rs.next()) {
				String uuid = rs.getString("UUID");
				String url = "https://api.mojang.com/user/profiles/"+uuid.replace("-", "")+"/names";
		        try {
		            @SuppressWarnings("deprecation")
		            String nameJson = IOUtils.toString(new URL(url));           
		            JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
		            String playerSlot = nameValue.get(nameValue.size()-1).toString();
		            JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
		            
					int coins = rs.getInt("COINS");
					if(coins == 1) {
						p.sendMessage(Utils.chat("&a" + nameObject.get("name").toString() + ": &6" + coins + " &acoin"));
					}else {
						p.sendMessage(Utils.chat("&a" + nameObject.get("name").toString() + ": &6" + coins + " &acoins"));
					}
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
			}
		} catch(SQLException x) {
			x.printStackTrace();
		}
	}

}
