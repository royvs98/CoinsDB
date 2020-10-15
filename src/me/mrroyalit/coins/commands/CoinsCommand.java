package me.mrroyalit.coins.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrroyalit.coins.Main;
import me.mrroyalit.coins.managers.CurrencyManager;
import me.mrroyalit.coins.utils.Utils;

public class CoinsCommand implements CommandExecutor {

	public Main plugin;
	
	public CoinsCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("currencymanager.use")) {
			if(args.length == 0) {
				// /currency
				sender.sendMessage(Utils.chat("&e/coins <add|get|set|remove|list> [player] [amount]"));
				return true;
			} else if(args.length == 1) {
				// /currency <add|set|remove|get>
				if(args[0].equalsIgnoreCase("add")) {
					sender.sendMessage(Utils.chat("&e/coins add <player> <amount>"));
					return true;
				} else if(args[0].equalsIgnoreCase("set")) {
					sender.sendMessage(Utils.chat("&e/coins set <player> <amount>"));
					return true;
				} else if(args[0].equalsIgnoreCase("remove")) {
					sender.sendMessage(Utils.chat("&e/coins remove <player> <amount>"));
					return true;
				} else if(args[0].equalsIgnoreCase("get")) {
					sender.sendMessage(Utils.chat("&e/coins get <player>"));
					return true;
				} else if(args[0].equalsIgnoreCase("list")) {
					CurrencyManager manager = new CurrencyManager(plugin);
					
					sender.sendMessage(Utils.chat("&2Hier de lijst met alle spelers en hun coins!"));
					sender.sendMessage(Utils.chat("&6============================================"));
					manager.listPlayersAndCoins(sender.getName());
					sender.sendMessage(Utils.chat("&6============================================"));
				} else {
					sender.sendMessage(Utils.chat("&e/coins <add|get|set|remove|list> [player] [amount]"));
				}
			} else if(args.length == 2) {				
				// /currency get <player>
				@SuppressWarnings("deprecation")
				OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
				
				CurrencyManager manager = new CurrencyManager(plugin);
				
				if(args[0].equalsIgnoreCase("get")) {
					int coins = manager.getPlayerCurrency(p);
					if(coins == 1) {
						sender.sendMessage(Utils.chat("&e" + p.getName() + " &ahas a balance of &6" + coins + " &acoin"));
					}else {
						sender.sendMessage(Utils.chat("&e" + p.getName() + " &ahas a balance of &6" + coins + " &acoins"));
					}
				} else {
					sender.sendMessage(Utils.chat("&e/coins <add|set|remove> " + p.getName() + " <amount>"));
				}
			} else if(args.length == 3) {
				// /currency <add|set|remove> <player> <amount>
				// run the full code here
				@SuppressWarnings("deprecation")
				OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
				int amount = Integer.parseInt(args[2]);
				
				CurrencyManager manager = new CurrencyManager(plugin);
				
				if(args[0].equalsIgnoreCase("add")) {
					if(p != null) {
						manager.addCurrencyToPlayer(p, amount);

						sender.sendMessage(Utils.chat("&aYou have succesfully added &6" + amount + "&a coins to the account of &e" + p.getName() + "."));
					}
				} else if(args[0].equalsIgnoreCase("set")) {
					if(p != null) {
						manager.setPlayerCurrency(p, amount);
						if(p.getUniqueId().toString() != null) {
							sender.sendMessage(Utils.chat("&aYou have succesfully set &e" + p.getName() + "'s &a coins to &6" + amount + "."));
						}else {
							sender.sendMessage(Utils.chat("&ePlayer &c" + p.getName() + "&e could not be found!"));
						}
					}
				} else if(args[0].equalsIgnoreCase("remove")) {
					if(p != null) {
						manager.removeCurrencyFromPlayer(p, amount);
						if(amount == 1) {
							sender.sendMessage(Utils.chat("&aYou have succesfully removed &6" + amount + "&a coin from the account of &e" + p.getName() + "."));
						}else {
							sender.sendMessage(Utils.chat("&aYou have succesfully removed &6" + amount + "&a coins from the account of &e" + p.getName() + "."));
						}
					}			
				} else if(args[0].equalsIgnoreCase("get")) {
					sender.sendMessage(Utils.chat("&e/coins get <player>"));
					return true;
				}else if(args[0].equalsIgnoreCase("pay")) {
					
					Player commandsender = (Player) sender;
					
					if(p != null) {
						manager.removeCurrencyFromPlayer(commandsender, amount);
						manager.addCurrencyToPlayer(p, amount);
						if(amount == 1) {
							sender.sendMessage(Utils.chat("&aYou have succesfully payed &6" + amount + "&a coin to &e" + p.getName() + "."));
						}else {
							sender.sendMessage(Utils.chat("&aYou have succesfully payed &6" + amount + "&a coins to &e" + p.getName() + "."));
						}
					}			
				}
			} else if(args.length > 3) {
				sender.sendMessage(Utils.chat("&e/coins <add|get|set|remove|list> [player] [amount]"));
			}
		} else {
			if(args.length == 3){
				@SuppressWarnings("deprecation")
				OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
				int amount = Integer.parseInt(args[2]);
				
				Player commandsender = (Player) sender;
				
				CurrencyManager manager = new CurrencyManager(plugin);
				if(args[0].equalsIgnoreCase("pay")) {
					if(p != null) {
						manager.removeCurrencyFromPlayer(commandsender, amount);
						manager.addCurrencyToPlayer(p, amount);
						if(amount == 1) {
							sender.sendMessage(Utils.chat("&aYou have succesfully payed &6" + amount + "&a coin to &e" + p.getName() + "."));
						}else {
							sender.sendMessage(Utils.chat("&aYou have succesfully payed &6" + amount + "&a coins to &e" + p.getName() + "."));
						}
					}			
				}
			}else {

				OfflinePlayer p = (Player) sender;
				
				CurrencyManager manager = new CurrencyManager(plugin);
				
				int coins = manager.getPlayerCurrency(p);
				if(coins == 1) {
					sender.sendMessage(Utils.chat("&aYou have &6" + coins + " &acoin"));
				}else {
					sender.sendMessage(Utils.chat("&aYou have &6" + coins + " &acoins"));
				}
			}			
		}
		
		return false;
	}
	
}
