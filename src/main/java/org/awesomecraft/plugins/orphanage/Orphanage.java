package org.awesomecraft.plugins.orphanage;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Orphanage extends JavaPlugin {
    /*
    * Thanks to 
    * 
    *
    */ 
    public Map<World, Location> orphanage = new HashMap<World, Location>(); //<World, Orphanage Location>
    public Map<Player, Player> mothers = new HashMap<Player, Player>(); //<Mother, Orphan>
    public Map<Player, Boolean> isOrphan = new HashMap<Player, Boolean>(); //<Orphan, isOrphan>
    public void onDisable() {
        System.out.println(this + " is now disabled!");
        try {
            SLAPI.save(mothers, "mothers.bin");
        } catch (Exception ex) {
            
        }
        try {
            SLAPI.save(isOrphan, "orphans.bin");
        } catch (Exception ex) {
            
        }
        try {
            SLAPI.save(orphanage, "orphanage.bin");
        }
        catch(Exception ex) {
            
        }
    }

    public void onEnable() {
        getCommand("adopt").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
                if(args.length == 1) {
                    if(cs instanceof Player) {
                        try {
                    Player target = getServer().getPlayer(args[0]);
                    Player sender = (Player) cs;
                    sender.sendMessage(ChatColor.GREEN + "You are now the parent of " + target.getDisplayName() + "!");
                    target.sendMessage(ChatColor.GREEN + "You have been adopted by " + sender.getDisplayName() + "!");
                    mothers.put(sender, target);
                    isOrphan.put(target, Boolean.FALSE);
                        }
                        catch(NullPointerException npe) {
                            cs.sendMessage(ChatColor.RED + "Player not found.");
                        }
                    }
                    else{
                        cs.sendMessage(ChatColor.RED + "You must be ingame to use this command.");
                    }
                            
                }
                return true;
            }
        });
        getCommand("abandon").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
                if(cs instanceof Player) {
                Player target = getServer().getPlayer(args[0]);
                Player sender = (Player) cs;
                if(mothers.containsValue(target) && mothers.containsKey(sender)) {
                    mothers.put(null, null);
                    isOrphan.put(target, true);
                    target.sendMessage(ChatColor.RED + "You are an orphan again.");
                    sender.sendMessage(ChatColor.RED + "You abandoned your orphan on the streets.");
                } else {
                sender.sendMessage(ChatColor.RED + "You aren't the parent of that person!");
                }  
                }
                return true;
            
        }});
        getCommand("setorphanage").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
                if(cs instanceof Player) {
                    Player sender = (Player) cs;
                    orphanage.put(sender.getWorld(), sender.getLocation());
                    sender.sendMessage(ChatColor.GREEN + "Set an orphanage at your current location.");
                }
                else {
                    cs.sendMessage(ChatColor.RED + "You must be ingame to use this command.");
                }
                return true;
            }
        });
        getCommand("orphanage").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
                if(cs instanceof Player) {
                    Player sender = (Player) cs;
                    if(orphanage.get(sender.getWorld()) != null) {
                        Location tele = orphanage.get(sender.getWorld()); 
                        sender.teleport(tele);
                    
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "An orphanage hasn't been set yet.");
                    }
                            
                    
                }
                else {
                  cs.sendMessage(ChatColor.RED + "You must be ingame to use this command.");   
                }
                
                return true;
            }
        });
        
        try {
            mothers = (Map<Player, Player>)SLAPI.load("mothers.bin");
        } catch (Exception ex) {
            
        }
        try {
            isOrphan = (Map<Player, Boolean>)SLAPI.load("orphans.bin");
        } catch (Exception ex) {
            
        }
        try {
            orphanage = (Map<World, Location>)SLAPI.load("orphanage.bin");
        }
        catch(Exception ex) {
            
        }
        

        
        System.out.println(this + " is now enabled!");
    }
}
