package DeadByMC.deadbymc_tibaimc;

import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;


public final class MatchListener  implements Listener{
	private main plugin;
	private HashMap<Double, Match> matches = new HashMap<Double, Match>();
	  public MatchListener(main plugin) {
		this.plugin = (main) plugin;
	}
	 @EventHandler
	 public void player_interact_event(PlayerInteractEvent e)
	 {
		 if(e.getClickedBlock().getState() instanceof Sign && e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		 {
			 Sign sign = (Sign) e.getClickedBlock();
			 String name= sign.getLine(1);
			 name = ChatColor.stripColor(name);
			 switch (name) {
			case "Join Game":
				e.getPlayer().openInventory(plugin.Inventory);
				break;
			case "Ready":
			{
				
			}
			default:
				break;
			}
		 }
		 else if (e.getItem()==null&&e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&e.getClickedBlock().getType().equals(Material.WARPED_BUTTON))
		 {
			 Powerable button = (Powerable)e.getClickedBlock().getBlockData();
			 if(!button.isPowered())
			 {
				 Location location = e.getClickedBlock().getLocation();
				 if(e.getPlayer().getLocation().getBlockX()==location.getBlockX()&&e.getPlayer().getLocation().getBlockY()==location.getBlockY()&&e.getPlayer().getLocation().getBlockZ()==location.getBlockZ())
				 {
					 matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).repairing(e.getClickedBlock().getRelative(e.getBlockFace().getOppositeFace(),1).getLocation() , e.getPlayer(),button);
				 }
			 }
		 }
	 }
	
	 @EventHandler
	 public void OnclickInventory(InventoryClickEvent e) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException
	 {
		 if(e.getClickedInventory()==plugin.Inventory&&e.getCurrentItem()!=null)
		 {
			 if(e.getCurrentItem().getItemMeta().getLocalizedName().equals(ChatColor.GREEN+"Survivor")||e.getCurrentItem().getItemMeta().getLocalizedName().equals(ChatColor.RED+"Killer"))
			 {
				 boolean check = false;
				 double index = System.currentTimeMillis();
					if(matches.size()==0)
					{
						Match match = new Match(plugin);
						match.add_player((Player)e.getWhoClicked(),index,ChatColor.stripColor(e.getCurrentItem().getItemMeta().getLocalizedName()));
						matches.put(index, match);
						e.getWhoClicked().setMetadata("match_num", new FixedMetadataValue(plugin, index));
					}
					else {
						for (Match iMatch : matches.values())
						{
							if(iMatch.get_player_list().size()<5)
							{
								iMatch.add_player((Player)e.getWhoClicked(),iMatch.get_match_num(),ChatColor.stripColor(e.getCurrentItem().getItemMeta().getLocalizedName()));
								check = true;
								e.getWhoClicked().setMetadata("match_num", new FixedMetadataValue(plugin, iMatch.get_match_num()));
								break;
							}
						}
						if (!check)
						{
							Match match = new Match(plugin);
							match.add_player((Player)e.getWhoClicked(),index,ChatColor.stripColor(e.getCurrentItem().getItemMeta().getLocalizedName()));
							matches.put(index, match);
							e.getWhoClicked().setMetadata("match_num", new FixedMetadataValue(plugin, index));
						}
						
					}  // teleport player to waiting room 
			 }
		 }
	 }
	 
	  
	 
}
