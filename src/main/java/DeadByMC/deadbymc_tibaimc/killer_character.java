package DeadByMC.deadbymc_tibaimc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;


public class killer_character {
	private killer_character_enum killer_character_enum;
	private Plugin plugin;
	private long time = System.currentTimeMillis();
	private boolean check= true;
	private ItemStack killer_item_ItemStack;
	// Constructor. checking the characterName is inside the enum or not, and get the method by the character Name.
	public killer_character(String characterName,Plugin plugin) 
	{
		for (killer_character_enum every_character_enum : DeadByMC.deadbymc_tibaimc.killer_character_enum.values())
		{
			if(every_character_enum.getvalue()==characterName)
			{
				killer_character_enum = every_character_enum;
				this.plugin = plugin;
				switch (characterName) {
				case "Huntress":
					killer_item_ItemStack =new ItemStack(Material.ARROW,1);
					break;
				case "Shadow":
					killer_item_ItemStack = new ItemStack(Material.COOKIE,1);
					break;
				case "Doctor":
					ItemStack posion_potionItemStack = new ItemStack(Material.LINGERING_POTION,1);
					PotionMeta potionMeta = (PotionMeta) posion_potionItemStack.getItemMeta();
					potionMeta.addCustomEffect(PotionEffectType.getByName("POISON").createEffect(10, 1), true);
					posion_potionItemStack.setItemMeta(potionMeta);
					killer_item_ItemStack =posion_potionItemStack;
					break;
				case "VoidWalker":
				{
					killer_item_ItemStack =new ItemStack(Material.ENDER_PEARL,1);
				}
				default:
					break;
				}
				break;
			}
		}
	}
	public killer_character()
	{
		
	}
	public killer_character_enum getKiller_enum()
	{
		return this.killer_character_enum;
	}
	// execute the character skill method.
	public void do_character_skill(String action,Player user,Player anotherPlayer , ArrayList<Player> survivor_list) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	{
		try {
			this.getClass().getMethod(killer_character_enum.getvalue()+"_"+action).invoke(this,user,anotherPlayer,survivor_list);
		} catch (NoSuchMethodException e) {
			return;
		} 
	}
	
	// Huntress
	public void Huntress_init(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		Inventory inventory = killer_player.getInventory();
		ItemStack bowItemStack = new ItemStack(Material.BOW, 1);
		ItemStack arrowItemStack = new ItemStack(killer_item_ItemStack);
		arrowItemStack.setAmount(3);
		inventory.setItem(1, bowItemStack);
		inventory.setItem(2, arrowItemStack);
	}
	public void Huntress_onhit(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		if(killer_player.getInventory().contains(Material.ARROW, 3)) {}
		else {
			killer_player.getInventory().addItem(killer_item_ItemStack);}	
			killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(3, 200));
		return;
	}
	
//   RTA
	public void RTA_onhit(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		if(killer_player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
		{
			int amp = killer_player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier();
			if(amp==5 || amp>5)
			{
				killer_player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				killer_player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(5, 5));
			}
			else {
				killer_player.removePotionEffect(PotionEffectType.getByName("INCREASE_DAMAGE"));
				killer_player.addPotionEffect(PotionEffectType.getByName("INCREASE_DAMAGE").createEffect(5, amp+1));
			}
		}
	}
	
	//shadow 
	public Runnable Shadow_init(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{if(check){	
		check = false;
		killer_player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(1000000, 1));
		killer_player.addPotionEffect(PotionEffectType.SPEED.createEffect(1000000, 1));
		killer_player.getInventory().setItem(2,killer_item_ItemStack);
		return null;}
	else if(System.currentTimeMillis()-time>=10000){
		killer_player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(1000000, 1));
		killer_player.addPotionEffect(PotionEffectType.SPEED.createEffect(1000000, 1));
		return null;
	}
	return null;
	}
	public void Shadow_onhit(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		killer_player.removePotionEffect(PotionEffectType.INVISIBILITY);
		killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(3, 200));
		Bukkit.getScheduler().runTaskLater(this.plugin, ()->{try{killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(6, 2));}catch (Exception e) {return;}}, 60);
		Bukkit.getScheduler().runTaskLater(this.plugin, ()->{try{Shadow_init(killer_player,null,null);}catch (Exception e) {return;}},200);
	}
	public void Shadow_eat(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		for(Player i : survivor_list)
		{
			i.addPotionEffect(PotionEffectType.GLOWING.createEffect(10, 1));
		}
		Bukkit.getScheduler().runTaskLater(this.plugin, ()->{try{killer_player.getInventory().setItem(2,killer_item_ItemStack);}catch (Exception e) {return;}}, 400);
	}
	
	
	//doctor
	public void Doctor_init(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		ItemStack potionItemStack = new ItemStack(killer_item_ItemStack);
		potionItemStack.setAmount(2);
		killer_player.getInventory().addItem(potionItemStack);
	}
	
	public void Doctor_onhit(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(3, 200));
		survivor.addPotionEffect(PotionEffectType.POISON.createEffect(3, 1));
	}
	public void Doctor_throw(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		Bukkit.getScheduler().runTaskLater(plugin,()->{try{killer_player.getInventory().addItem(killer_item_ItemStack);}catch (Exception e) {return;}}, 400);
	}
	
	
	//voidwalker
	public void VoidWalker_init(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		Inventory inventory = killer_player.getInventory();
		ItemStack ender_pearlItemStack = new ItemStack(killer_item_ItemStack);
		ender_pearlItemStack.setAmount(2);
		inventory.setItem(2, ender_pearlItemStack);
	}
	public void VoidWalker_onhit(Player killer_player,Player survivor, ArrayList<Player> survivor_list){killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(3, 200));}
	
	public void VoidWalker_teleport(Player killer_player,Player survivor, ArrayList<Player> survivor_list)
	{
		Location playerLocation = killer_player.getLocation();
		for(Player i : survivor_list)
		{
			Location survivor_location = i.getLocation();
			double distance = Math.pow(Math.pow(Math.abs(playerLocation.getX())-Math.abs(survivor_location.getX()), 2)+Math.pow(Math.abs(playerLocation.getY())-Math.abs(survivor_location.getY()), 2),0.5);
			if(distance<=20)
			{
				i.addPotionEffect(PotionEffectType.SLOW.createEffect(3, 1));
				i.addPotionEffect(PotionEffectType.GLOWING.createEffect(3, 1));
			}
			Bukkit.getScheduler().runTaskLater(plugin, ()->{try{killer_player.getInventory().addItem(killer_item_ItemStack);}catch (Exception e) {return;}}, 300);
		}
	}
}
