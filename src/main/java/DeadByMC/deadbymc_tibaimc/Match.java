package DeadByMC.deadbymc_tibaimc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Match 
{
  private main plugin;
  private ArrayList<Player>player_list = new ArrayList<Player>();
  private ArrayList<String>position = new ArrayList<String>();
  private Map<Player, survivor> survivorMap = new HashMap<Player, survivor>();
  private ArrayList<Player>survivor_list= new ArrayList<Player>();
  private Player killer_player =null;
  private killer killer = null;
  private double match_num=0;
  private HashMap<Location, Double> generator_and_progression = new HashMap<Location, Double>();
  private int finished_generator = 0;
  private ArrayList<Integer> pre_killer_number=new ArrayList<Integer>();;
  private BukkitTask task = null;
  private String win_or_lose ="";
  public Match(main plugin)
  {
	  this.plugin = plugin;
  }
  
  public Match()
  {
	  
  }
  // execute when a player enter the match
  public void add_player(Player player, double index , String position) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException {
	player.setMetadata("match_num", new FixedMetadataValue(plugin, index));
	player_list.add(player);
	this.position.add(position);
	if(position=="Killer")
	{pre_killer_number.add(player_list.indexOf(player));}
	match_num=index;
	if(player_list.size()==5)
	{
		for(Player iPlayer : player_list)
		{
			iPlayer.sendMessage(ChatColor.YELLOW+"The game room is full. Game will start in 10 seconds.");
		}
		task=Bukkit.getScheduler().runTaskLater(plugin,preparation_state(), 200);
	}
	else {
		for(Player iPlayer : player_list)
		{
			iPlayer.sendMessage(ChatColor.YELLOW+"Someone has enter the game room . ("+player_list.size()+"/5)");
		}

	}
}
  // get match number tagged on the player
  public double get_match_num()
  {
	  return match_num;}
  
  public ArrayList<Player> get_player_list() {
	return player_list;}

public String get_win_or_lose()
{return win_or_lose;}

public killer get_killer()
{return killer;}

public Player get_killer_player()
{return killer_player;}

public void killer_skill_execution(String action, Player anotherPlayer) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
{
	killer.killer_skill_execution(action, anotherPlayer, survivor_list);
	}


 public void choose_killer(Player player, String killer_charac)
 {
	 if(player == killer_player)
	 { killer.choose_character(killer_charac);}
 }
 
  // clear all stuff related to disconnected player from this match
  public void exit_match(Player player)
  {
	  if(task!=null)
	  {
		  Bukkit.getScheduler().cancelTask(task.getTaskId());
		  player_list.remove(player); 
		  for (Player iPlayer : player_list)
		  {iPlayer.sendMessage(ChatColor.YELLOW+"there is a player exit the match!! Please wait for next player in order to start the game!");}
	  }
	  else {player_list.remove(player); }
	  if(this.position.get(player_list.indexOf(player))=="Killer")
	  {pre_killer_number.remove(player_list.indexOf(player));}
	  else {this.position.remove(player_list.indexOf(player));}
	  player.removeMetadata("match_num",plugin); // remove match index tagged on player 
	  if(player != killer_player)
	  { if (survivor_list.contains(player)) {
			survivor_list.remove(player);survivorMap.remove(player);
			if(survivor_list.size()==0)
			{win_or_lose="killer_win";}}
	  }
	  else {
		 killer_player=null;
		 killer=null;
		 win_or_lose="survivor_win";}
	  if (win_or_lose!="ended")
	  {end_match();}
	  for(PotionEffect potionEffect : player.getActivePotionEffects())
	  {player.removePotionEffect(potionEffect.getType());}
	  return;
  }
  
 // executed when match is ended 
 public void end_match()
 {
	 if(win_or_lose=="survivor_win")
	 {for(Player iPlayer : player_list)
		 {
		 	iPlayer.sendTitle(ChatColor.YELLOW+"Survivor Win !!!!", "teleport will be proceed after 10 seconds", 10, 10, 10);
		 	exit_match(iPlayer);
		 }
	 Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,()->{for(Player iPlayer : player_list){iPlayer.teleport(Bukkit.getWorld("lobby").getSpawnLocation()); }}, 200);
	 win_or_lose="ended";
	 }
	 else if(win_or_lose=="killer_win")
	 {for(Player iPlayer : player_list)
	 {
		 	iPlayer.sendTitle(ChatColor.YELLOW+"Killer Win !!!!", "teleport will be proceed after 10 seconds", 10, 10, 10);
		 	exit_match(iPlayer);
	 }
	 Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,()->{for(Player iPlayer : player_list){iPlayer.teleport(Bukkit.getWorld("lobby").getSpawnLocation());}}, 200);
	 win_or_lose="ended";}
 }
 
 
 // executed when match is in preparation state , where survivor buying stuff, killer changing character
 public Runnable preparation_state() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException
 {
	 task=null;
	 win_or_lose="preparation";
	 int killer_index=0;
	if(pre_killer_number.size()>=1)
	{
		 killer_index = (int) (Math.random()*(pre_killer_number.size()-1));}
	else {
		killer_index = pre_killer_number.get(0);}
	killer_player = player_list.get(pre_killer_number.get(killer_index));
	killer = new killer(killer_player, plugin);
	for(Player iPlayer : player_list)
	{
		iPlayer.sendTitle(ChatColor.RED+" Killer is " + killer_player.getDisplayName(), ChatColor.DARK_RED+"GLHF", 5, 10, 5);
	}
	Bukkit.getScheduler().runTaskLater(plugin, ()->{if(win_or_lose=="survivor_win" || win_or_lose=="killer_win") {return;}for( Player iPlayer : player_list) {iPlayer.sendTitle(ChatColor.YELLOW+" 1-min prepration Start! ", ChatColor.DARK_RED+"prepared yourself!", 5, 10, 5);}}, 36);
	Bukkit.getScheduler().runTaskLater(plugin, ()->{if(win_or_lose=="survivor_win" || win_or_lose=="killer_win") {return;}for( Player iPlayer : player_list) {iPlayer.sendTitle("game is about to start!", null, 10, 20, 10);}}, 1172);
	Bukkit.getScheduler().runTaskLater(plugin, start_match(), 1272);
	return null; 
 }
 
 //executed when match is about to started
  public Runnable start_match() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
	  if (killer.get_character()==null)
	  {killer.choose_character("Huntress");}
		killer.Start_match();
		win_or_lose="ongoing";
		for (Player iPlayer : player_list)
		{
			iPlayer.addPotionEffect(PotionEffectType.SATURATION.createEffect(1000000, 1));
		}
		// teleport player to the map
		return null;
}
  
 //check generator location , check whether is repair finished completed or not 
  public void repairing(Location location,Player player, Powerable button)
  {
	  if(generator_and_progression.get(location)>=1.0)
	  {
		  return;
	  }
	  if(!generator_and_progression.containsKey(location))
	  {
		  generator_and_progression.put(location, 0.0);}
	  
	 double repair_rate = generator_and_progression.replace(location, survivorMap.get(player).on_reparing(button));
	 if( generator_and_progression.get(location)+repair_rate>=1)
	 {
		 generator_and_progression.replace(location, 1.0);
		 player.sendTitle(null, ChatColor.YELLOW+"REPAIR SUCCESSFULLY!! ",5,20,5);
		 finished_generator+=1;
		 for(int i =1;i<3;i++)
		 {
			 Powerable redstonelamp = (Powerable)location.clone().add(0, i, 0).getBlock().getBlockData();
			 redstonelamp.setPowered(true);}
		 if(finished_generator==5)
		 {
			 for(Player player1 : survivor_list)
			 {
				 player1.sendTitle(ChatColor.RED+"GENERATOR HAS BEEN REPAIRED!!", ChatColor.WHITE+"survivor may proceed to the gate!!!", 5, 20, 5);
			 }
			 //open the door using command block 
		 }
		 return; 
	 }
	 else { generator_and_progression.replace(location, generator_and_progression.get(location)+repair_rate);}
	 player.sendTitle(null, ChatColor.WHITE+"repairing... progression: "+repair_rate *100+"%" , 2, 10, 2);
	
	  
  }
}
