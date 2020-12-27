package DeadByMC.deadbymc_tibaimc;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class KillerCommandExecutor implements CommandExecutor {
private main plugin;
  public KillerCommandExecutor(main plugin ) 
{	//this.plugin = plugin; // Store the plugin in situations where you need it.
  this.plugin = plugin;
}
public boolean  onCommand(CommandSender sender, Command cmd, String label, String[] args){
	String[] cmdStrings = cmd.getName().split(" ");
	if(cmdStrings[0].equalsIgnoreCase("killeritem")&&cmdStrings[1]!=""&&cmdStrings.length==2)
	{
		ItemStack paper = new ItemStack(Material.PAPER);
		ItemMeta paperItemMeta = paper.getItemMeta();
		switch (cmdStrings[1]) {
		case "huntress":
			paperItemMeta.setDisplayName(ChatColor.GREEN+"Huntress");
			String[] loreStrings = {ChatColor.DARK_GRAY+"She will watch you to Death...",
					ChatColor.WHITE+"As a huntress, her job is to make sure her prey would not run away.",
					ChatColor.WHITE+"With the combination of axe and bow, she can deal lots of damage in split second.",
					ChatColor.YELLOW+"Skill:",
					ChatColor.YELLOW+"when hit survivor, you gain one arrow, maximum 3 arrow in your inventory."};
			paperItemMeta.setLore(Arrays.asList(loreStrings));
			break;
		case"RTA":
			paperItemMeta.setDisplayName(ChatColor.GREEN+"RTA");
			String[] loreStrings1 = {ChatColor.DARK_GRAY+"Won't stop until you die...",
					ChatColor.WHITE+"A man with a butterfly knife, with incredible speed.",
					ChatColor.WHITE+"As a pevert, he enjoys in killing people, each hit was harder than previous hit.",
					ChatColor.YELLOW+"Skill:",
					ChatColor.YELLOW+"when hit survivor, you would not get debuff, refresh or add 1 stack of Strength for 5 seconds, max 5 stacks."};
			paperItemMeta.setLore(Arrays.asList(loreStrings1));break;
		case"Shadow":
			paperItemMeta.setDisplayName(ChatColor.GREEN+"Shadow");
			String[] loreStrings11 = {ChatColor.DARK_GRAY+"The fear inside your heart...",
					ChatColor.WHITE+"An unkown creature, with the ability of hiding in reality.",
					ChatColor.WHITE+"hiding outside the reality make him become faster, while appear in reality make him slower.",
					ChatColor.YELLOW+"Skill:",
					ChatColor.YELLOW+"when hit survivor, you get slowness 1 for 10 seconds.after not hitting survivor for 10secodns, you hide away along with speed 1 buff.",
					ChatColor.YELLOW+"Every 20s , you will get one cookie, max 1 cookie. ate it will allow you see survivor position for 10 seconds.",
					};
			paperItemMeta.setLore(Arrays.asList(loreStrings11));break;
		case"Doctor":
			paperItemMeta.setDisplayName(ChatColor.GREEN+"Doctor");
			String[] loreStrings111 = {ChatColor.DARK_GRAY+"knowledge is weapon to kill people...",
					ChatColor.WHITE+"A famous doctor, with a honor award on inventing poison substance",
					ChatColor.WHITE+"whether are his poison potions or his scalpel, he will make you poisoned in everyway.",
					ChatColor.YELLOW+"Skill:",
					ChatColor.YELLOW+"when hit survivor,survivor gains poison 1 for 3secodns.",
					ChatColor.YELLOW+"Every 20s , you will get one lingering potion of poison.",};
			paperItemMeta.setLore(Arrays.asList(loreStrings111));break;
		case"VoidWalker":
			paperItemMeta.setDisplayName(ChatColor.GREEN+"VoidWalker");
			String[] loreStrings1111 = {ChatColor.DARK_GRAY+"....",
					ChatColor.WHITE+"A",
					ChatColor.WHITE+"whether are his poison potions or his scalpel, he will make you poisoned in everyway.",
					ChatColor.YELLOW+"Skill:",
					ChatColor.YELLOW+"Every 20s , you will get one ender pearl, max 2 ender pearls. Apply slowness to survivor within terror radius when landing.",};
			paperItemMeta.setLore(Arrays.asList(loreStrings1111));break;
		default:
			break;
		}
		paper.setItemMeta(paperItemMeta);
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			player.getInventory().addItem(paper);
			return true;
		}
		
	}
	else {Player player = (Player) sender;
	player.sendMessage("wrong input!!!");}
	return false;
}

}
