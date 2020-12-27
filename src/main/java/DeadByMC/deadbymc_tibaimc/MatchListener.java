package DeadByMC.deadbymc_tibaimc;

import java.lang.reflect.InvocationTargetException;



import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
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
		 if( e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&e.getClickedBlock().getState() instanceof Sign )
		 {
			 Sign sign = (Sign) e.getClickedBlock();
			 String name= sign.getLine(1);
			 name = ChatColor.stripColor(name);
			 switch (name) {
			case "Join Game":
				e.getPlayer().openInventory(plugin.Inventory);
				break;
			case "Huntress":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "Huntress");
			case "RTA":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "RTA");
			case "Shadow":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "Shadow");
			case "Doctor":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "Doctor");
			case "VoidWalker":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "VoidWalker");
			default:
				break;
			}
		 }
		 else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&e.getItem()==null&&e.getClickedBlock().getType().equals(Material.WARPED_BUTTON))
		 {
			 Powerable button = (Powerable)e.getClickedBlock().getBlockData();
			 if(!button.isPowered())
			 {
				 Location location = e.getClickedBlock().getLocation();
				 if(e.getPlayer().getLocation().getBlockX()==location.getBlockX()&&e.getPlayer().getLocation().getBlockY()==location.getBlockY()&&e.getPlayer().getLocation().getBlockZ()==location.getBlockZ())
				 {
//					 matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).repairing(e.getClickedBlock().getRelative(e.getBlockFace().getOppositeFace(),1).getLocation() , e.getPlayer(),button);
					 e.getPlayer().sendTitle("bruh", "bruh", 20, 20, 20);
					 return;
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
						
					} 
					e.getWhoClicked().teleport((Bukkit.getWorld("campfire").getSpawnLocation()));// teleport player to waiting room 
			 }
		 }
	 }
	 
	 @EventHandler
	 public void OnDamageEnable(EntityDamageEvent e)
	 {
		 if(e.getEntity() instanceof Player && e.getCause()!=DamageCause.ENTITY_ATTACK)
		 {
			 Player player = (Player) e.getEntity();
			 if(player.hasMetadata("match_num")&&matches.get(player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {}
			 else {e.setCancelled(true);}
		 }return;
	 }
	 @EventHandler
	 public void OnHitSurvivor(EntityDamageByEntityEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	 {
		 if(e.getDamager() instanceof Player && e.getEntity() instanceof Player)
		 {
			 Player killer_player = (Player)e.getDamager();
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {
				 Match match = matches.get(killer_player.getMetadata("match_num").get(0).asDouble());
				 match.killer_skill_execution("onhit", (Player)e.getEntity());
			 }
		}
		 return;
	 }
	 
	 @EventHandler
	 public void OnKillerComsuming(PlayerItemConsumeEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	 {
			 Player killer_player = e.getPlayer();
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {
				 Match match = matches.get(killer_player.getMetadata("match_num").get(0).asDouble());
				 match.killer_skill_execution("eat", null);
			 }
		 return;
	 }
	 
	 @EventHandler 
	 public void OnKillerThrow(ProjectileHitEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	 {
		 if(e.getEntity() instanceof Player)
		 {
			 Player killer_player = (Player)e.getEntity();
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {
				 Match match = matches.get(killer_player.getMetadata("match_num").get(0).asDouble());
				 match.killer_skill_execution("throw", null);
			 }
		}
		 return;
	 }
	 
	 @EventHandler 
	 public void OnKillerTeleport(PlayerTeleportEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	 {
			 Player killer_player = e.getPlayer();
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {
				 Match match = matches.get(killer_player.getMetadata("match_num").get(0).asDouble());
				 match.killer_skill_execution("teleport", null);
			 }
		 return;
	 }
	 
	 @EventHandler
	 public void OnDragInventory(InventoryDragEvent e)
	 {
		 if(e.getInventory() == e.getWhoClicked().getInventory())
		 {
			 return;
		 }
		 else {e.setCancelled(true);return;}
	 }
	 
	  @EventHandler
	  public void PlayerQuit(PlayerQuitEvent e)
	  {
		  matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).exit_match(e.getPlayer());return;
	  }
	 
}
