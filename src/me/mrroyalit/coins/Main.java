package me.mrroyalit.coins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrroyalit.coins.commands.CoinsCommand;
import me.mrroyalit.coins.events.OnJoinEvent;
import me.mrroyalit.coins.events.OnMobKillEvent;
import me.mrroyalit.coins.managers.CurrencyManager;
import me.mrroyalit.coins.placeholder.Placeholder;

public class Main extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new Placeholder(this).register();
		}

		registerConfig();
		registerEvents();
		registerManagers();
		registerCommands();
		registerListeners();
		
		try {
			databaseConnection();
			System.out.println("Connected to the database!");
			createTable();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private static Connection connection;
	private String host, database, username, password;
	private int port;
	
	public void databaseConnection() throws SQLException {
		
		// Hier haal je de gegevens op van de config en maak je verbinding naar de database
		
		host = getConfig().getString("host");
		port = getConfig().getInt("port");
		database = getConfig().getString("database");
		username = getConfig().getString("username");
		password = getConfig().getString("password");
		
		if(connection != null && !connection.isClosed()) {
			return;
		}
		
		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		
	}
	
	public static PreparedStatement prepareStatement(String query) {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(query);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return ps;
	}
	
	public void createTable() {
		
		try {
			ResultSet rs = Main.prepareStatement("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '" + getConfig().getString("database") + "' AND table_name = 'player_data'").executeQuery();
			rs.next();
			
			if(rs.getInt(1) == 0) {
				Main.prepareStatement("CREATE TABLE player_data (UUID varchar(255),COINS varchar(255), PRIMARY KEY(UUID))").executeUpdate();
			}else {
				System.out.println("Table already exists!");
			}
			
		} catch(SQLException x) {
			x.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
	}
	
	public void registerManagers() {
		//Register managers here
		new CurrencyManager(this);
	}
	
	public void registerCommands() {
		//Register managers here
		getCommand("coins").setExecutor(new CoinsCommand(this));
	}
	
	public void registerListeners() {
		//Register managers here
	}

	private void registerEvents() {
		// Register Events here
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new OnJoinEvent(), this);
		//pm.registerEvents(new OnMobKillEvent(), this);
	}
	
	public void registerConfig() {
		getConfig().addDefault("host", "localhost");
		getConfig().addDefault("port", 3306);
		getConfig().addDefault("database", "coins");
		getConfig().addDefault("username", "root");
		getConfig().addDefault("password", "");
		
		ConfigurationSection c = getConfig();
		getConfig().createSection("Prices");
		
		c.set("Prices.Zombie", "1");
		getConfig().options().copyDefaults(true);
		saveConfig();
	  }
}
