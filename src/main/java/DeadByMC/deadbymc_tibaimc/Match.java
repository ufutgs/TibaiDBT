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
			iPlayer.sendMessage(ChatColor.YELLOW+"Game will start in 10 seconds.");
		}
		task=Bukkit.getScheduler().runTaskLater(plugin,preparation_state(), 200);
	}
}
  // get match number tagged on the player
  public double get_match_num()
  {
	  return match_num;
  }
  
  public ArrayList<Player> get_player_list() {
	return player_list;
}

 
  
  // clear player's state
//  public void clear(Player player)
//  {
//	  if(player!=killer_player)
//	  {   survivor_list.remove(player);
//	  	  survivorMap.remove(player);
//		  player.sendMessage(ChatColor.BLUE+"Clear Successfully !"); return;
//	  }
//	  else if (player == killer_player) {
//		killer_player = null;
//		killer = null;
//		player.sendMessage(ChatColor.BLUE+"Clear Successfully !"); return;
//	}
//  }
//  
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
	  if(player != killer_player)
	  { if (survivor_list.contains(player)) {
			survivor_list.remove(player);survivorMap.remove(player);
		}
	  }
	  else {killer_player=null;killer=null;}
	  if(this.position.get(player_list.indexOf(player))=="Killer")
	  {pre_killer_number.remove(player_list.indexOf(player));}
	  this.position.remove(player_list.indexOf(player));
	  player.removeMetadata("match_num",plugin);
	  return;
  }
  
 public Runnable preparation_state() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException
 {
	 task=null;
	 int killer_index=0;
	if(pre_killer_number.size()>=1)
	{
		 killer_index = (int) (Math.random()*(pre_killer_number.size()-1));}
	else {
		killer_index = pre_killer_number.get(0);}
	killer_player = player_list.get(pre_killer_number.get(killer_index));
	for(Player iPlayer : player_list)
	{
		iPlayer.sendTitle(ChatColor.RED+" Killer is " + killer_player.getDisplayName(), ChatColor.DARK_RED+"GLHF", 10, 10, 10);
		Bukkit.getScheduler().runTaskLater(plugin, ()->{iPlayer.sendTitle(ChatColor.YELLOW+" 1-min prepration Start! ", ChatColor.DARK_RED+"prepared yourself!", 10, 10, 10);}, 30);
	}
	Bukkit.getScheduler().runTaskLater(plugin, start_match(), 1230);
	return null; 
 }
 
  public Runnable start_match() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		killer.Start_match();
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
		 player.sendTitle(null, ChatColor.YELLOW+"REPAIR SUCCESSFULLY!! ",1,10,5);
		 finished_generator+=1;
		 for(int i =1;i<5;i++)
		 {
			 Powerable redstonelamp = (Powerable)location.clone().add(0, i, 0).getBlock().getBlockData();
			 redstonelamp.setPowered(true);}
		 if(finished_generator==5)
		 {
			 for(Player player1 : player_list)
			 {
				 player1.sendTitle(ChatColor.RED+"GENERATOR HAS BEEN REPAIRED!!", ChatColor.WHITE+"survivor may proceed to the gate!!!", 1, 20, 10);
			 }
			 //open the door using command block 
		 }
		 return; 
	 }
	 else { generator_and_progression.replace(location, generator_and_progression.get(location)+repair_rate);}
	 player.sendTitle(null, ChatColor.WHITE+"repairing... progression: "+repair_rate *100+"%" , 1, 10, 5);
	
	  
  }
}
