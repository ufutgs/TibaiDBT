package DeadByMC.deadbymc_tibaimc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class killer {
	private Player user;
	private UUID player;
	private killer_character character=null;
	private main plugin;
	private BukkitTask task;
	public killer(UUID user, main plugin)
	{
		this.player = user;
		this.plugin = plugin;
	}
	public killer()
	{
		
	}
	public killer_character get_character()
	{return character;}
	public BukkitTask gettask()
	{	return task;}
	public void choose_character(String character)
	{
		this.character= new killer_character(character,plugin);
	}
	public void Start_match() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		user=Bukkit.getPlayer(player);
		Inventory inventory = user.getInventory();
		inventory.clear();
		inventory.setItem(0, plugin.weaponMap.get(character.getKiller_enum()));
		user.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(1000000, 6));
		killer_skill_execution("init",null,null);
		task=Bukkit.getScheduler().runTaskTimer(plugin,()->{Bukkit.getPlayer(player).getWorld().playSound(Bukkit.getPlayer(player).getLocation(),Sound.ENTITY_WITHER_AMBIENT, (float)0.1, (float)1);}, 1, 10);
	}
	public void killer_skill_execution(String action,Player anotherPlayer, ArrayList<UUID> survivor_list) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	{
		try {
			character.getClass().getMethod(character.getKiller_enum().getvalue()+"_"+action,UUID.class,Player.class, new ArrayList<UUID>().getClass()).invoke(character,player,anotherPlayer,survivor_list);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} 
	}
}
