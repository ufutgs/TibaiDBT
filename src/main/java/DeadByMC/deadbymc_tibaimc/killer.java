package DeadByMC.deadbymc_tibaimc;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class killer {
	private Player user;

	private killer_character character;
	private main plugin;
	public killer(Player user, main plugin)
	{
		this.user = user;
		this.plugin = plugin;
	}
	public killer()
	{
		
	}
	public void choose_character(String character)
	{
		this.character= new killer_character(character,plugin);
	}
	public void Start_match() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		Inventory inventory = user.getInventory();
		inventory.clear();
		inventory.setItem(0, plugin.weaponMap.get(character.getKiller_enum()));
		killer_skill_execution("init");
	}
	public void killer_skill_execution(String action) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	{
		this.character.do_character_skill(action,user);
	}
}
