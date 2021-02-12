package DeadByMC.deadbymc_tibaimc;

import java.util.ArrayList;
import java.util.UUID;

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
					ItemStack glowpotionItemStack = new ItemStack(Material.POTION,1);
					PotionMeta glowPotionMeta = (PotionMeta) glowpotionItemStack.getItemMeta();
					glowPotionMeta.addCustomEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(200, 1), true);
					glowpotionItemStack.setItemMeta(glowPotionMeta);
					killer_item_ItemStack =glowpotionItemStack;
					break;
				case "Doctor":
					ItemStack posion_potionItemStack = new ItemStack(Material.LINGERING_POTION,1);
					PotionMeta potionMeta = (PotionMeta) posion_potionItemStack.getItemMeta();
					potionMeta.addCustomEffect(PotionEffectType.getByName("POISON").createEffect(200, 1), true);
					potionMeta.addCustomEffect(PotionEffectType.SLOW.createEffect(20, 2), true);
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
	public void clear_item() {
		killer_item_ItemStack=null;
	}

	// Huntress
	public void Huntress_init(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Inventory inventory = Bukkit.getPlayer(killer_player_UUID).getInventory();
		ItemStack bowItemStack = new ItemStack(Material.BOW, 1);
		ItemStack arrowItemStack = new ItemStack(killer_item_ItemStack);
		arrowItemStack.setAmount(3);
		inventory.setItem(1, bowItemStack);
		inventory.setItem(2, arrowItemStack);
	}
	public void Huntress_onhit(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Player killer_player = Bukkit.getPlayer(killer_player_UUID);
		if(killer_player.getInventory().contains(Material.ARROW, 3)) {}
		else {
			killer_player.getInventory().addItem(killer_item_ItemStack);}	
			killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(60, 200));
		return;
	}
	
//   RTA
	public void RTA_onhit(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Player killer_player = Bukkit.getPlayer(killer_player_UUID);
		if(killer_player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
		{
			int amp = killer_player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier();
			if(amp==2 || amp>2)
			{
				killer_player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				killer_player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100, 2));
			}
			else {
				killer_player.removePotionEffect(PotionEffectType.getByName("INCREASE_DAMAGE"));
				killer_player.addPotionEffect(PotionEffectType.getByName("INCREASE_DAMAGE").createEffect(100, amp+1));
			}
		}
	}
	
	//shadow 
	public Runnable Shadow_init(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{Player killer_player = Bukkit.getPlayer(killer_player_UUID);
		if(check){	
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
	public void Shadow_onhit(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Player killer_player = Bukkit.getPlayer(killer_player_UUID);
		killer_player.removePotionEffect(PotionEffectType.INVISIBILITY);
		killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(30, 200));
		Bukkit.getScheduler().runTaskLater(this.plugin, ()->{try{killer_player.addPotionEffect(PotionEffectType.SLOW.createEffect(120, 2));}catch (Exception e) {e.printStackTrace();}}, 60);
		Bukkit.getScheduler().runTaskLater(this.plugin, ()->{try{Shadow_init(killer_player_UUID,null,null);}catch (Exception e) {e.printStackTrace();}},200);
	}
	public void Shadow_eat(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		for(UUID i : survivor_list)
		{
			Bukkit.getPlayer(i).addPotionEffect(PotionEffectType.GLOWING.createEffect(200, 1));
		}
		Bukkit.getScheduler().runTaskLater(this.plugin, ()->{try{Bukkit.getPlayer(killer_player_UUID).getInventory().setItem(2,killer_item_ItemStack);}catch (Exception e) {e.printStackTrace();}}, 400);
	}
	
	
	//doctor
	public void Doctor_init(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		ItemStack potionItemStack = new ItemStack(killer_item_ItemStack);
		potionItemStack.setAmount(2);
		Bukkit.getPlayer(killer_player_UUID).getInventory().addItem(potionItemStack);
	}
	
	public void Doctor_onhit(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Bukkit.getPlayer(killer_player_UUID).addPotionEffect(PotionEffectType.SLOW.createEffect(60, 200));
		survivor.addPotionEffect(PotionEffectType.POISON.createEffect(60, 1));
	}
	public void Doctor_throw(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Bukkit.getScheduler().runTaskLater(plugin,()->{try{Bukkit.getPlayer(killer_player_UUID).getInventory().addItem(killer_item_ItemStack);}catch (Exception e) {e.printStackTrace();}}, 400);
	}
	
	
	//voidwalker
	public void VoidWalker_init(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Inventory inventory = Bukkit.getPlayer(killer_player_UUID).getInventory();
		ItemStack ender_pearlItemStack = new ItemStack(killer_item_ItemStack);
		ender_pearlItemStack.setAmount(2);
		inventory.setItem(2, ender_pearlItemStack);
	}
	public void VoidWalker_onhit(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list){Bukkit.getPlayer(killer_player_UUID).addPotionEffect(PotionEffectType.SLOW.createEffect(60, 200));}
	
	public void VoidWalker_teleport(UUID killer_player_UUID,Player survivor, ArrayList<UUID> survivor_list)
	{
		Location playerLocation = Bukkit.getPlayer(killer_player_UUID).getLocation();
		for(UUID k: survivor_list)
		{
			Player i = Bukkit.getPlayer(k);
			Location survivor_location = i.getLocation();
			double distance = Math.pow(Math.pow(Math.abs(playerLocation.getX())-Math.abs(survivor_location.getX()), 2)+Math.pow(Math.abs(playerLocation.getY())-Math.abs(survivor_location.getY()), 2),0.5);
			if(distance<=20)
			{
				i.addPotionEffect(PotionEffectType.SLOW.createEffect(60, 1));
				i.addPotionEffect(PotionEffectType.GLOWING.createEffect(60, 1));
			}
			Bukkit.getScheduler().runTaskLater(plugin, ()->{try{Bukkit.getPlayer(killer_player_UUID).getInventory().addItem(killer_item_ItemStack);}catch (Exception e) {e.printStackTrace();}}, 300);
		}
	}
}
