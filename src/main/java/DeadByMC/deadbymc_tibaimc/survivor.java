package DeadByMC.deadbymc_tibaimc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class survivor 
{
	private Player survivor;
	private int money=1000;
	private boolean QTE=false;
	private double QTE_percentage = 0.1;
	private main plugin;
	public survivor(Player survivor,main plugin)
	{
		this.survivor = survivor;
		this.plugin=plugin;
	}
	public void deduce_money(int price) {
		if(money-price<0)
		{
			survivor.sendMessage(ChatColor.RED+"You do not have any more Money!");
			return;
		}
		else {
			money -= price;
		}
	}

	public double on_reparing(Powerable button)
	{
		if(QTE==true)
		{
			QTE=false;
			QTE_percentage=0.1;
			survivor.sendTitle(null, ChatColor.YELLOW+"Success!! + 8% repairing progression", 3, 10, 3);
			return 0.8;
		}
		else {
			double repair_rate = Math.random()*(0.05-0.01)+0.01;
			QTE_percentage+= repair_rate;
			QTE = qte_chance();
			if (QTE ==true)
			{
				button.setPowered(false);
				survivor.sendTitle(ChatColor.RED+"QTE TIME !", ChatColor.WHITE+"Press the button in one second!!", 1, 5, 1);
				Bukkit.getScheduler().runTaskLater(plugin, ()->{try{repairing_falied();}catch (Exception e) {return;
				}}, 20);
			}
			return (repair_rate);
		}
	}
	

	private Runnable repairing_falied() {
		if(QTE==true)
		{
			survivor.addPotionEffect(PotionEffectType.SLOW.createEffect(3, 200));
			survivor.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(3, 1));
			survivor.addPotionEffect(PotionEffectType.GLOWING.createEffect(10, 1));
			return null;
		}
		else {return null;}
	}
	

	private boolean qte_chance()
	{
		double random = Math.random()*(0.5-0.1)+0.1;
		if(random<=QTE_percentage)
		{return true;}
		else {return false;}
	}
}
