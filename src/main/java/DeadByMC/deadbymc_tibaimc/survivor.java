package DeadByMC.deadbymc_tibaimc;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class survivor 
{
	private UUID survivor;
	private int money=1000;
	private boolean QTE=false;
	private double QTE_percentage = 0.1;
	private main plugin;
	private BukkitTask task =null;
	public survivor(UUID survivor,main plugin)
	{
		this.survivor = survivor;
		this.plugin=plugin;
	}
	public boolean get_QTE() {
		return this.QTE;
	}
	public boolean buyitem(String itemString,int price) {
		if(money-price<0)
		{
			Bukkit.getPlayer(survivor).sendMessage(ChatColor.RED+"You do not have any more Money!");
			Bukkit.getPlayer(survivor).playSound(Bukkit.getPlayer(survivor).getLocation(),Sound.ENTITY_WITHER_SPAWN, (float)0.1, (float)1);
			return false;
		}
		else {
			money -= price;
			Bukkit.getPlayer(survivor).playSound(Bukkit.getPlayer(survivor).getLocation(),Sound.BLOCK_ANVIL_USE, (float)0.1, (float)1);
			Bukkit.getPlayer(survivor).sendMessage(ChatColor.YELLOW+"You have bought :" +ChatColor.GREEN+itemString);
			Bukkit.getPlayer(survivor).closeInventory();
			return true;
		}
	}

	public double on_reparing(Powerable button ,boolean have_key)
	{
		Player survivorPlayer = Bukkit.getPlayer(survivor);
		if(QTE==true)
		{
			if(have_key)
			{
				Bukkit.getScheduler().cancelTask(task.getTaskId());
				survivorPlayer.getInventory().remove(plugin.key);
				QTE=false;
				QTE_percentage=0.1;
				survivorPlayer.sendMessage(ChatColor.YELLOW+"Success!! + 8% repairing progression");
				survivorPlayer.playSound(survivorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float)0.1, (float)1);
				return 0.08;	
			}
			else {
				Bukkit.getScheduler().cancelTask(task.getTaskId());
				repairing_falied();
				return 0;
			}
		}
		else {
			double repair_rate = Math.floor((Math.random()*(0.04)+0.01)*100)/100;
			QTE_percentage+= repair_rate;
			QTE = qte_chance();
			if (QTE ==true)
			{
				int key_slot = (int)(Math.random()*9);
				ItemStack key_slotItemStack = survivorPlayer.getInventory().getItem(key_slot);
				if(key_slotItemStack!=null&&key_slotItemStack.getType()!=Material.AIR)
				{
					survivorPlayer.getInventory().remove(key_slotItemStack);
					survivorPlayer.getInventory().setItem(key_slot, plugin.key);
					survivorPlayer.getInventory().addItem(key_slotItemStack);
				}
				else {survivorPlayer.getInventory().setItem(key_slot, plugin.key);}
				survivorPlayer.sendTitle(ChatColor.RED+"QTE TIME !", ChatColor.WHITE+"Press the button in 2 second!!", 5, 10, 5);
				task = Bukkit.getScheduler().runTaskLater(plugin, ()->{try{repairing_falied();}catch (Exception e) {return;
				}}, 40);
			}
			survivorPlayer.playSound(survivorPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, (float)0.1,(float) 1.0);
			return (repair_rate);}
	}
	

	private void repairing_falied() {
		if(QTE==true)
		{
			Player player = Bukkit.getPlayer(survivor);
			player.getInventory().remove(plugin.key);
			player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, (float)0.5, (float)1);
			player.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
			player.addPotionEffect(PotionEffectType.SLOW.createEffect(60, 200));
			player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(60, 1));
			player.addPotionEffect(PotionEffectType.GLOWING.createEffect(100, 1));
			QTE_percentage=0.01;
			QTE=false;
			return;
		}
	}
	

	private boolean qte_chance()
	{
		double random = Math.random()*(0.5-0.1)+0.1;
		if(random<=QTE_percentage)
		{return true;}
		else {return false;}
	}
}
