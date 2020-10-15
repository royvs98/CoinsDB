package me.mrroyalit.coins.placeholder;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mrroyalit.coins.Main;
import me.mrroyalit.coins.managers.CurrencyManager;

public class Placeholder extends PlaceholderExpansion{

	public Main plugin;
	
	public Placeholder(Main plugin) {
		this.plugin = plugin;
	}
	
    public boolean persist(){
        return true;
    }
	
    public boolean canRegister(){
        return true;
    }
	
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	public String getIdentifier() {
		return "royalcoins";
	}

	public String getVersion() {
		return plugin.getDescription().getVersion();
	}
	
	 public String onPlaceholderRequest(Player player, String identifier){
		 
		CurrencyManager manager = new CurrencyManager(plugin);
		
        if(player == null){
            return "";
        }

        // %royalcoins_coins%
        if(identifier.equals("coins")){
        	int coins = manager.getPlayerCurrency(player);
        	String coin = Integer.toString(coins);
            return plugin.getConfig().getString("placeholder1", coin);
        }
 
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%) 
        // was provided
        return null;
    }

}
