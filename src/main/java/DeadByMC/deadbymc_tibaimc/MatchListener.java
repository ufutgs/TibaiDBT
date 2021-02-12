package DeadByMC.deadbymc_tibaimc;

import java.io.IOException;



import java.lang.reflect.InvocationTargetException;



import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;


public final class MatchListener  implements Listener{
	private main plugin;
	private HashMap<Double, Match> matches = new HashMap<Double, Match>();
	  public MatchListener(main plugin) {
		this.plugin = (main) plugin;
	}
	  @EventHandler
	  public void OnPlayerLogin(PlayerJoinEvent e)
	  {
		  e.getPlayer().teleport(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
		  e.getPlayer().setGameMode(GameMode.ADVENTURE);
			 for(PotionEffect i: e.getPlayer().getActivePotionEffects())
			 { e.getPlayer().removePotionEffect(i.getType());}
			 e.getPlayer().getInventory().clear();
	  }

	  @EventHandler
	  public void saturation(FoodLevelChangeEvent e)
	  {
		  e.setCancelled(true);
	  }
	  @EventHandler
	  public void disableHealthGeneration(EntityRegainHealthEvent e)
	  {
		  if(e.getEntity() instanceof Player&&!e.getRegainReason().equals(RegainReason.MAGIC_REGEN))
		  {
			  Player player = (Player) e.getEntity();
			  if(player.hasMetadata("match_num"))
			  {  e.setCancelled(true);  }
		  }
	  }
	  
	 @EventHandler
	 public void OnClickMerchant(PlayerInteractAtEntityEvent e)
	 {
		 if(!(e.getRightClicked() instanceof Player)&&e.getRightClicked().getCustomName().equals("merchant")&&matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="preparation")
		 {
			 e.getPlayer().openInventory(plugin.itemInventory);
		 }
	 }
	 @EventHandler
	 public void player_interact_event(PlayerInteractEvent e) throws IllegalArgumentException, IOException
	 {
		 if( e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&e.getClickedBlock().getState() instanceof Sign )
		 {
			 Sign sign = (Sign) e.getClickedBlock().getState();
			 String name= sign.getLine(1);
			 name = ChatColor.stripColor(name);
			 switch (name) {
			case "Join Game":
				e.getPlayer().openInventory(plugin.Inventory);
				break;
			case "Huntress":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "Huntress");break;
			case "RTA":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "RTA");break;
			case "Shadow":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "Shadow");break;
			case "Doctor":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "Doctor");break;
			case "VoidWalker":
				matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).choose_killer(e.getPlayer(), "VoidWalker");break;
			default:
				break;
			}
		 }
		 else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&e.getItem()==null&&e.getClickedBlock().getType().equals(Material.WARPED_BUTTON)&&matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).get_killer_player()!=e.getPlayer().getUniqueId())
		 {
			 Powerable button = (Powerable)e.getClickedBlock().getBlockData();
			 if(!button.isPowered())
			 {
				 Location location = e.getClickedBlock().getLocation();
				 if(e.getPlayer().getLocation().getBlockX()==location.getBlockX()&&e.getPlayer().getLocation().getBlockY()==location.getBlockY()&&e.getPlayer().getLocation().getBlockZ()==location.getBlockZ())
				 {
					 boolean have_key =false;
					 if(e.getPlayer().getInventory().getItemInMainHand().equals(plugin.key))
					 {have_key=true;;}
					 boolean qte=matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).repairing(e.getClickedBlock().getRelative(e.getBlockFace().getOppositeFace(),2).getLocation(), e.getPlayer(),button, have_key);
					 if(qte)
					 {e.setCancelled(true);}
					 return;
				 }
			 }
		 }
		 else if(e.getAction().equals(Action.PHYSICAL)&&e.getClickedBlock().getType().equals(Material.WARPED_PRESSURE_PLATE)&&matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).get_killer_player()!=e.getPlayer().getUniqueId())
		 {
			 matches.get(e.getPlayer().getMetadata("match_num").get(0).asDouble()).survivor_escape(e.getPlayer());
			
		 }
	 }
	
	
	 @EventHandler
	 public void OnclickInventory(InventoryClickEvent e) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException , IOException
	 {
		 if(e.getClickedInventory()==plugin.Inventory&&e.getCurrentItem()!=null)
		 {
			 if(e.getCurrentItem().getItemMeta().getLocalizedName().equals(ChatColor.GREEN+"Survivor")||e.getCurrentItem().getItemMeta().getLocalizedName().equals(ChatColor.RED+"Killer"))
			 {
				 World campfire=null;
				 boolean check = false;
				 double index = System.currentTimeMillis();
					if(matches.size()==0)
					{
						Match match = new Match(plugin);
						match.add_player(e.getWhoClicked().getUniqueId(),index,e.getCurrentItem().getItemMeta().getLocalizedName());
						matches.put(index, match);
						e.getWhoClicked().setMetadata("match_num", new FixedMetadataValue(plugin, index));
						match.copyWorld("campfire", index);
						campfire=match.get_campfire();
					}
					else {
						for (Match iMatch : matches.values())
						{
							if(iMatch.get_player_list().size()<5)
							{
								iMatch.add_player(e.getWhoClicked().getUniqueId(),iMatch.get_match_num(),e.getCurrentItem().getItemMeta().getLocalizedName());
								check = true;
								e.getWhoClicked().setMetadata("match_num", new FixedMetadataValue(plugin, iMatch.get_match_num()));
								campfire=iMatch.get_campfire();
								break;
							}
						}
						if (!check)
						{
							Match match = new Match(plugin);
							match.add_player(e.getWhoClicked().getUniqueId(),index,e.getCurrentItem().getItemMeta().getLocalizedName());
							matches.put(index, match);
							e.getWhoClicked().setMetadata("match_num", new FixedMetadataValue(plugin, index));
							match.copyWorld("campfire", index);
							campfire=match.get_campfire();
						}
					}
					e.getWhoClicked().teleport(campfire.getSpawnLocation());// teleport player to waiting room 
			 }
			 e.setCancelled(true);
		 }
		// survivor buy item 
		 else if(e.getClickedInventory() == plugin.itemInventory&& e.getCurrentItem()!=null)
		 {
			 if(e.getWhoClicked().hasMetadata("match_num"))
			 {
				boolean buy_state= matches.get(e.getWhoClicked().getMetadata("match_num").get(0).asDouble()).buyitem(e.getWhoClicked().getUniqueId(),e.getCurrentItem().getItemMeta().getDisplayName());
				if(buy_state)
				{
					e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
				}
			 }
			 e.setCancelled(true);
		 }
	 }

	
	 @EventHandler
	 public void OnDamageEnable(EntityDamageEvent e) throws IllegalArgumentException, IOException
	 {
		 if(e.getEntity() instanceof Player)
		 {
			 Player player = (Player) e.getEntity();
			 if(player.hasMetadata("match_num")&&matches.get(player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {if(player.getHealth() - e.getFinalDamage()<=0)
				 {
				 	player.setGameMode(GameMode.SPECTATOR);
				 	player.sendTitle("You Die !", "Just run only , bruh.", 10, 10, 10);
				 	matches.get(player.getMetadata("match_num").get(0).asDouble()).survivor_die(player);
				 	 if(matches.get(player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ended")
					  {Bukkit.getScheduler().runTaskLater(plugin,()->{matches.remove(player.getMetadata("match_num").get(0).asDouble());}, 220);}  
				  e.setCancelled(true);
				 }
			 }
			 else {e.setCancelled(true);}
		 }
		 else {e.setCancelled(true);}
	 }
	 @EventHandler
	 public void OnHitSurvivor(EntityDamageByEntityEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, IOException
	 {
		 if(e.getEntity() instanceof Player)
		 {Player killer_player=null;
		 Player survivorPlayer = (Player)e.getEntity();
			 if(e.getDamager() instanceof Player)
			 {
				  killer_player = (Player)e.getDamager();
			 }
			 else if(e.getDamager() instanceof Arrow)
			 {
				  Projectile arrow = (Projectile)e.getDamager();
				  killer_player=(Player)arrow.getShooter();
			}
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player.getUniqueId()&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {
				 Match match = matches.get(killer_player.getMetadata("match_num").get(0).asDouble());
				 match.killer_skill_execution("onhit", (Player)e.getEntity());
				 if(survivorPlayer.getHealth() - e.getFinalDamage()<=0)
				 {
					 survivorPlayer.setGameMode(GameMode.SPECTATOR);
					 survivorPlayer.sendTitle("You Die !", "Just run only , bruh.", 10, 10, 10);
				 	matches.get(survivorPlayer.getMetadata("match_num").get(0).asDouble()).survivor_die(survivorPlayer);
				 	 if(matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ended")
					  {Bukkit.getScheduler().runTaskLater(plugin,()->{matches.remove(killer_player.getMetadata("match_num").get(0).asDouble());}, 220);}  
				  e.setCancelled(true);
				 }
			 }
			 else if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_survivorList().contains(killer_player.getUniqueId())&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing") 
			 {
				if(matches.get(survivorPlayer.getMetadata("match_num").get(0).asDouble()).get_killer_player()==survivorPlayer.getUniqueId())
				{return;}
				else {e.setCancelled(true);}
			 }
			 else {e.setCancelled(true);}
		}
		 else {
			 e.setCancelled(true);
		 }
	 }
	 
	 @EventHandler
	 public void OnKillerComsuming(PlayerItemConsumeEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	 {
			 Player killer_player = e.getPlayer();
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player.getUniqueId()&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
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
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player.getUniqueId()&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {
				 Match match = matches.get(killer_player.getMetadata("match_num").get(0).asDouble());
				 match.killer_skill_execution("throw", null);
			 }
		}
	 }
	 
	 @EventHandler 
	 public void OnKillerTeleport(PlayerTeleportEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	 {
			 Player killer_player = e.getPlayer();
			 if(killer_player.hasMetadata("match_num")&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_killer_player()==killer_player.getUniqueId()&&matches.get(killer_player.getMetadata("match_num").get(0).asDouble()).get_win_or_lose()=="ongoing")
			 {
				 Match match = matches.get(killer_player.getMetadata("match_num").get(0).asDouble());
				 match.killer_skill_execution("teleport", null);
			 }
		 return;
	 }
	 
	 @EventHandler
	 public void OnDragInventory(InventoryDragEvent e)
	 {
		 if(e.getInventory() == plugin.Inventory||e.getInventory()==plugin.itemInventory)
		 {
			 e.setCancelled(true);
		 }
		
	 }
	 
	  @EventHandler
	  public void PlayerQuit(PlayerQuitEvent e) throws IllegalArgumentException, IOException
	  {
		  if(e.getPlayer().hasMetadata("match_num"))
		  {
			  double match_num = e.getPlayer().getMetadata("match_num").get(0).asDouble();
			  Match match =matches.get(match_num);
			  match.exit_match(e.getPlayer());
			  if(match.get_win_or_lose()=="ended")
			  {Bukkit.getScheduler().runTaskLater(plugin,()->{matches.remove(match_num);}, 220);}  
		  }
		  return;
	  }
	 
}
