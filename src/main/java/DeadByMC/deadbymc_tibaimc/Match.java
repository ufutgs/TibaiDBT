package DeadByMC.deadbymc_tibaimc;

import java.io.File;




import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Match 
{
  private main plugin;
  private ArrayList<UUID>player_list = new ArrayList<UUID>();
  private ArrayList<String>position = new ArrayList<String>();
  private Map<UUID, survivor> survivorMap = new HashMap<UUID, survivor>();
  private ArrayList<UUID>survivor_list= new ArrayList<UUID>();
  private ArrayList<UUID>Dead_survivor_list = new ArrayList<UUID>();
  private ArrayList<UUID>escape_survivor_list = new ArrayList<UUID>();
  private UUID killer_player =null;
  private killer killer = null;
  private double match_num=0;
  private HashMap<Location, Double> generator_and_progression = new HashMap<Location, Double>();
  private int finished_generator = 0;
  private ArrayList<Integer> pre_killer_number=new ArrayList<Integer>(); // index of player who want to be killer , used for random pick who become killer
  private BukkitTask task = null;
  private String win_or_lose ="";
  private ArrayList<World>map = new ArrayList<World>();
  private  float[][] survivorspawnlocation = {{-65,79,-24},{-120,79,-70},{-51,79,-70},{-19,73,-66},{-15,79,-19},{-24,79,-38},{-40,79,-102},{-98,84,-93},{-107,81,-56},{-79,82,-25},{-17,79,-29},{-14,79,-84}};
  private float[][] killerspawnlocation = {{-51,68,-71},{-50,68,-66},{-87,84,-54},{-87,94,-72},{-94,94,-56},{-95,94,-92}};
  private ArrayList<Location> doorLocations = new ArrayList<Location>();
  public Match(main plugin)
  {
	  this.plugin = plugin;
  }
  
  public Match()
  {
	  
  }
  // execute when a player enter the match
  public void add_player(UUID player, double index , String position) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException {
	  FixedMetadataValue match_nuMetadataValue = new FixedMetadataValue(plugin, index);
	Bukkit.getPlayer(player).setMetadata("match_num", match_nuMetadataValue);
	player_list.add(player);
	this.position.add(position);
	if(position.equals(ChatColor.RED+"Killer"))
	{Bukkit.getScheduler().runTaskLater(plugin,()->{pre_killer_number.add(this.position.size()-1);}, 0);}
	match_num=index;
	if(player_list.size()==2)
	{
		for(UUID iPlayer : player_list)
		{
			Bukkit.getPlayer(iPlayer).sendMessage(ChatColor.YELLOW+"The game room is full. Game will start in 10 seconds.");
		}
		task=Bukkit.getScheduler().runTaskLater(plugin,()->{try {preparation_state();} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | SecurityException e) {e.printStackTrace();
		}}, 200);
	}
	else {
		for(UUID iPlayer : player_list)
		{
			Bukkit.getPlayer(iPlayer).sendMessage(ChatColor.YELLOW+"Someone has enter the game room . ("+player_list.size()+"/2)");
		}

	}
}
  // get match number tagged on the player
  public double get_match_num()
  {
	  return match_num;}
  
  public ArrayList<UUID> get_player_list() {
	return player_list;}

public String get_win_or_lose()
{return win_or_lose;}

public killer get_killer()
{return killer;}

public UUID get_killer_player()
{return killer_player;}

public World get_campfire()
{return map.get(0);}

public ArrayList<UUID> get_survivorList() {
	return survivor_list;
}

public void killer_skill_execution(String action, Player anotherPlayer) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
{
	killer.killer_skill_execution(action, anotherPlayer, survivor_list);
	}


 public void choose_killer(Player player, String killer_charac)
 {
	 if(player.getUniqueId() == killer_player)
	 { killer.choose_character(killer_charac);
	 	player.sendMessage(ChatColor.YELLOW+"you have selected "+killer_charac);
	 }
 }
 
 
 public boolean buyitem(UUID survivorUuid,String itemString) {
	return survivorMap.get(survivorUuid).buyitem(itemString,plugin.item_price_list.get(itemString));
}
  // clear all stuff related to disconnected player from this match
  public void exit_match(Player player) throws IllegalArgumentException, IOException
  {
	  if(this.position.get(player_list.indexOf(player.getUniqueId()))=="Killer")
	  {pre_killer_number.remove(player_list.indexOf(player.getUniqueId()));}
	  this.position.remove(player_list.indexOf(player.getUniqueId()));
	  if(task!=null)
	  {
		  Bukkit.getScheduler().cancelTask(task.getTaskId());
		  player_list.remove(player.getUniqueId()); 
		  if(player_list.size()!=0)
		  {
			  for (UUID iPlayer : player_list)
			  {Bukkit.getPlayer(iPlayer).sendMessage(ChatColor.YELLOW+"there is a player exit the match!! Please wait for next player in order to start the game!");}  
		  }
		  else {end_match();return;}
	  }
	  else {player_list.remove(player.getUniqueId()); }
	  player.removeMetadata("match_num",plugin); // remove match index tagged on player 
	  if(player != Bukkit.getPlayer(killer_player)&&survivor_list.contains(player.getUniqueId()))
	  { 
		survivor_list.remove(player.getUniqueId());survivorMap.remove(player.getUniqueId());Dead_survivor_list.remove(player.getUniqueId());escape_survivor_list.remove(player.getUniqueId());
		
		if(survivor_list.size()==0)
		{win_or_lose="match_end";
		}
	  }
  	else {
  		Bukkit.getScheduler().cancelTask(killer.gettask().getTaskId());
		 killer_player=null;
		 killer=null;
		 win_or_lose="match_end";
		 }
	  if(win_or_lose=="match_end") {
		  end_match();
	  }
	  for(PotionEffect potionEffect : player.getActivePotionEffects())
	  {player.removePotionEffect(potionEffect.getType());}
	  return;
  }
  
 // executed when match is ended 
 public void end_match() throws IllegalArgumentException, IOException
 {
	 if(win_or_lose=="match_end")
	 {for(UUID iPlayer : player_list)
		 {
		 	Bukkit.getPlayer(iPlayer).sendTitle(ChatColor.YELLOW+"Match has ended !!!!", "teleport will be proceed after 10 seconds", 10, 10, 10);
		 	 Bukkit.getPlayer(iPlayer).removeMetadata("match_num",plugin);
		 }
	if(killer!=null){
		Bukkit.getScheduler().cancelTask(killer.gettask().getTaskId());
		killer.get_character().clear_item();
		}
	 Bukkit.getScheduler().runTaskLater(plugin,()->{for(UUID iPlayer : player_list){
		 Player player = Bukkit.getPlayer(iPlayer);
		 for(PotionEffect i: player.getActivePotionEffects())
		 { player.removePotionEffect(i.getType());}
		 player.getInventory().clear();
		 player.setGameMode(GameMode.ADVENTURE);
		 Bukkit.getPlayer(iPlayer).teleport(Bukkit.getWorld("lobby").getSpawnLocation()); 
		 }}, 200);
	 Bukkit.getScheduler().runTaskLater(plugin,()->{try {deleteWorld();} catch (IOException e) {e.printStackTrace();}}, 210);
	 win_or_lose="ended";
	 }
	
 }
 
 
 // executed when match is in preparation state , where survivor buying stuff, killer changing character
 public void preparation_state() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException
 {
	 task=null;
	 win_or_lose="preparation";
	 int killer_index=0;
	if(pre_killer_number.size()>1)
	{killer_index = (int) (Math.random()*(pre_killer_number.size()-1));}
	else {killer_index = 0;}
	//creating killer 
	killer_player = player_list.get(pre_killer_number.get(killer_index));
	killer = new killer(killer_player, plugin);
	// creating survivor list
	survivor_list = new ArrayList<UUID>(player_list);
	survivor_list.remove(player_list.get(pre_killer_number.get(killer_index)));
	for(UUID survivorUuid : survivor_list)
	{
		survivor survivor = new survivor(survivorUuid, plugin);
		survivorMap.put(survivorUuid, survivor);
	}
	for(UUID iPlayer : player_list)
	{
		Bukkit.getPlayer(iPlayer).sendTitle(ChatColor.RED+" Killer is " + Bukkit.getPlayer(killer_player).getDisplayName(), ChatColor.DARK_RED+"GLHF", 10, 40, 10);
	}
	Bukkit.getScheduler().runTaskLater(plugin, ()->{try{for( UUID iPlayer : player_list) {Bukkit.getPlayer(iPlayer).sendTitle(ChatColor.YELLOW+" 1-min prepration Start! ", ChatColor.DARK_RED+"prepared yourself!", 10, 30, 10);}}catch (Exception e) {return;}}, 36);
	Bukkit.getScheduler().runTaskLater(plugin, ()->{try{for( UUID iPlayer : player_list) {Bukkit.getPlayer(iPlayer).sendTitle("game is about to start!", null, 10, 20, 10);}}catch (Exception e) {return;}}, 1172);
	Bukkit.getScheduler().runTaskLater(plugin, ()->{try{start_match();}catch (Exception e) {e.printStackTrace();}}, 1272);
	Bukkit.getPlayer(killer_player).teleport(new Location(map.get(0),59, 71, 64));
	return; 
 }
 

 //executed when match is about to started
  public void start_match() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, IOException {
	  if (killer.get_character()==null)
	  {killer.choose_character("Huntress");}
		killer.Start_match();
		copyWorld("gameroom", match_num);
		for (UUID iPlayer : player_list)
		{
			Bukkit.getPlayer(iPlayer).addPotionEffect(PotionEffectType.SATURATION.createEffect(1000000, 1));
		}
		for(int i=0;i<survivor_list.size();i++)
		{
		 int randomnum = (int) (Math.random()*12);
		 Location spawnLocation = new Location(map.get(1),survivorspawnlocation[randomnum][0], survivorspawnlocation[randomnum][1], survivorspawnlocation[randomnum][2]);
		 Bukkit.getPlayer(survivor_list.get(i)).teleport(spawnLocation);
		}
		int randomnum = (int) (Math.random()*6);
		Location spawnLocation = new Location(map.get(1),killerspawnlocation[randomnum][0], killerspawnlocation[randomnum][1], killerspawnlocation[randomnum][2]);
		Bukkit.getPlayer(killer_player).teleport(spawnLocation);
		 for(UUID player : player_list)
		 {
			 Bukkit.getPlayer(player).addPotionEffect(PotionEffectType.LEVITATION.createEffect(5, -100));
			 Bukkit.getPlayer(player).addPotionEffect(PotionEffectType.SLOW.createEffect(5, 200));
			 Bukkit.getPlayer(player).sendTitle(ChatColor.GREEN+"May God Save You", ChatColor.WHITE+"POG", 10, 10, 10);
		 }
		 doorLocations.add(new Location(map.get(1),-62.5,79,-20));
		  doorLocations.add(new Location(map.get(1),-74.5,79,-110));
		win_or_lose="ongoing";
		return;
}
  
	 // copy campfire and gameroom for each match
	public void copyWorld(String worldname, Double index) throws IOException {
		File worldFile = Bukkit.getWorld(worldname).getWorldFolder();
		File copyFile = new File(worldFile.getParent(), worldname+index);
		 FileUtils.copyDirectory(worldFile, copyFile);
		 FileUtils.getFile(copyFile, "uid.dat").delete();
		 WorldCreator WorldCreator = new WorldCreator(worldname+index);
		 WorldCreator.createWorld();
		 map.add(Bukkit.getServer().createWorld(WorldCreator));
	} 
	// delete campfire and gameroom for each match
	public void deleteWorld() throws IOException {
		for(World worldname : map)
		{
		Bukkit.getServer().unloadWorld(worldname, false);
		File worldFile = worldname.getWorldFolder();
		FileUtils.deleteDirectory(worldFile);
		Bukkit.getConsoleSender().sendMessage("mv remove "+worldname.getName());}
		map=null;
		return;
	}
  
 //check generator location , check whether is repair finished completed or not 
  public boolean repairing(Location location,Player player, Powerable button,boolean have_key)
  {
	  double current_repair_rate;
	  if(!generator_and_progression.containsKey(location))
	  {
		  generator_and_progression.put(location, 0.0);
		   current_repair_rate = 0;}
	  else if(generator_and_progression.get(location)>=1.0)
	  {
		  return false;
	  }
	  else { current_repair_rate =  generator_and_progression.get(location);}
	  survivor survivor = survivorMap.get(player.getUniqueId());
	 double repair_rate = survivor.on_reparing(button,have_key);
	 survivorMap.put(player.getUniqueId(), survivor);
	 if( current_repair_rate+repair_rate>=1)
	 {
		 generator_and_progression.put(location, 1.0);
		 finished_generator+=1;
		 for(int i =1;i<3;i++)
		 {
			 Block block = location.clone().add(0, i, 0).getBlock();
			 BlockData lampBlockData = Material.SEA_LANTERN.createBlockData();
			 block.setBlockData(lampBlockData);}
		 if(finished_generator==1)
		 {
			 for(UUID player1 : survivor_list)
			 {
				 Bukkit.getPlayer(player1).sendTitle(ChatColor.RED+"GENERATOR HAS BEEN REPAIRED!!", ChatColor.WHITE+"survivor may proceed to the gate!!!", 5, 20, 5);
			 }
			 BlockData barrierBlockData = Material.AIR.createBlockData();
			 for (Location location2 : doorLocations)
			 {
				 location2.getBlock().setBlockData(barrierBlockData);
			 }
		 }
		 else 
		 {
			 for(UUID player1 : survivor_list)
			 {Bukkit.getPlayer(player1).sendMessage(ChatColor.YELLOW+"Generator has been repaired !");
			 } 
		 }
		 return false;
	 }
	 else {double nowvalue = Math.floor((current_repair_rate+repair_rate)*100);
		 generator_and_progression.put(location,nowvalue/100); 
	 player.sendMessage(ChatColor.WHITE+"repairing... progression: "+generator_and_progression.size()+"  "+nowvalue+"%");}
	if(survivor.get_QTE())
	{return true;}
	else {return false;}
  }
 
  public void survivor_die(Player player) throws IllegalArgumentException, IOException
  {
	  survivor_list.remove(player.getUniqueId());
	  Dead_survivor_list.add(player.getUniqueId());
	  player.setGameMode(GameMode.SPECTATOR);
	  for(UUID iPlayer : player_list)
	  {
		  Bukkit.getPlayer(iPlayer).sendMessage(ChatColor.RED+player.getDisplayName()+" has been killed !! ("+survivor_list.size()+" survivor left)");
	  }
	  if(survivor_list.size()==0)
	  {
		  win_or_lose = "match_end";end_match();
	  }
  }
  public void survivor_escape(Player player) throws IllegalArgumentException, IOException
  {
	  survivor_list.remove(player.getUniqueId());
	  escape_survivor_list.add(player.getUniqueId());
	  player.setGameMode(GameMode.SPECTATOR);
	  for(UUID iPlayer : player_list)
	  {
		  Bukkit.getPlayer(iPlayer).sendMessage(ChatColor.RED+player.getDisplayName()+" has escape !! ("+survivor_list.size()+" survivor left)");
	  }
	  if(survivor_list.size()==0)
	  {
		  win_or_lose = "match_end";end_match();
	  }
  }
}
