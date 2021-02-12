package DeadByMC.deadbymc_tibaimc;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagDouble;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;

public class main extends JavaPlugin {
	 public HashMap<killer_character_enum, ItemStack> weaponMap = new HashMap<killer_character_enum, ItemStack>();
	 public HashMap<String, ItemStack> itemMap = new HashMap<String, ItemStack>();
	 public HashMap<String, Integer>item_price_list = new HashMap<String, Integer>();
	 private String[] itemStrings= {"Potion Of Hope","Potion Of Savior","Octane certified Drug","Potion Of Shit","Face The Death","Potion Of STOP","Enemy Chaser","Potion of Survive","Potion Of Big Brain","Potion Of Bruh"};
	 private int[] itemprice = {300,500,200,300,400,300,250,400,700,100};
	 public Inventory Inventory = Bukkit.createInventory(null, 27,"Choose your role you wished");
	 public Inventory itemInventory = Bukkit.createInventory(null, 54,"Item Shop");
	 public ItemStack key = new ItemStack(Material.STICK);
    @Override
    public void onEnable()
    {
    	
    	
    	this.getCommand("killeritem").setExecutor(new KillerCommandExecutor(this));
	 getServer().getPluginManager().registerEvents(new MatchListener(this), this);
	 Killer_weapon_generation();
	 survivor_item_generation();
	 ItemStack itemStack = new ItemStack(Material.LANTERN);
	 ItemMeta itemMeta = itemStack.getItemMeta();
	 itemMeta.setDisplayName(ChatColor.GREEN+"Survivor");
	 itemMeta.setLocalizedName(ChatColor.GREEN+"Survivor");
	 itemStack.setItemMeta(itemMeta);
	 Inventory.setItem(11,itemStack );
	 ItemStack itemStack1 = new ItemStack(Material.IRON_SWORD);
	 ItemMeta itemMeta1 = itemStack1.getItemMeta();
	 itemMeta1.setDisplayName(ChatColor.RED+"Killer");
	 itemMeta1.setLocalizedName(ChatColor.RED+"Killer");
	 itemStack1.setItemMeta(itemMeta1);
	 Inventory.setItem(15,itemStack1 );
	 ItemMeta keyItemMeta = key.getItemMeta();
	 keyItemMeta.setLocalizedName(ChatColor.YELLOW+"Key");
	 keyItemMeta.setDisplayName(ChatColor.YELLOW+"Key");
	 key.setItemMeta(keyItemMeta);
	 for(int i =0; i<itemStrings.length;i++)
	 {
		 item_price_list.put(itemStrings[i], itemprice[i]);
	 }
	 itemStrings=null;
	 itemprice=null;
//    createCustomConfig();
    
    }
   
    @Override
    public void onDisable()
    {
    	
	}
    
    public void Killer_weapon_generation() {
    	ItemStack weaponItemStack = null;
     	  double dmg = 0;
           for(killer_character_enum every_character_enum : DeadByMC.deadbymc_tibaimc.killer_character_enum.values())
           {
         	  switch (every_character_enum.getvalue()) {
     		case "Huntress":
     			weaponItemStack = new ItemStack(Material.IRON_AXE);
     			ItemMeta axeItemMeta = weaponItemStack.getItemMeta();
     			axeItemMeta.setDisplayName(ChatColor.RED+"Blund Axe");
     			weaponItemStack.setItemMeta(axeItemMeta);
     			dmg = 3;
     			break;
     		case "RTA":
     			weaponItemStack = new ItemStack(Material.IRON_SHOVEL);
     			ItemMeta shovelItemMeta = weaponItemStack.getItemMeta();
     			shovelItemMeta.setDisplayName(ChatColor.RED+"Lightweight Shovel");
     			weaponItemStack.setItemMeta(shovelItemMeta);
     			dmg = 1;
     			break;
     		case "Doctor":
     			weaponItemStack = new ItemStack(Material.BLAZE_ROD);
     			ItemMeta blaze_rodItemMeta = weaponItemStack.getItemMeta();
     			blaze_rodItemMeta.setDisplayName(ChatColor.RED+"Scalpel");
     			weaponItemStack.setItemMeta(blaze_rodItemMeta);
     			dmg = 2;
     			break;
     		case "Shadow":
     			weaponItemStack = new ItemStack(Material.IRON_SWORD);
     			ItemMeta swordItemMeta = weaponItemStack.getItemMeta();
     			swordItemMeta.setDisplayName(ChatColor.RED+"Sharp Sword");
     			weaponItemStack.setItemMeta(swordItemMeta);
     			dmg = 5;
     			break;
     		case "VoidWalker":
     			weaponItemStack = new ItemStack(Material.IRON_AXE);
     			ItemMeta axeItemMeta2 = weaponItemStack.getItemMeta();
     			axeItemMeta2.setDisplayName(ChatColor.RED+"Rusted Axe");
     			weaponItemStack.setItemMeta(axeItemMeta2);
     			dmg = 3;
     			break;
     		default:
     			break;
     		}
     	    net.minecraft.server.v1_16_R3.ItemStack nmStack = CraftItemStack.asNMSCopy(weaponItemStack);
     		NBTTagCompound compound = (nmStack.hasTag()) ? nmStack.getTag() : new NBTTagCompound();
     		NBTTagList modList = new NBTTagList();
     		NBTTagCompound nbttag = new NBTTagCompound();
     		nbttag.set("AttributeName",NBTTagString.a("generic.attack_damage"));
     		nbttag.set("Name",NBTTagString.a("generic.attack_damage"));
     		nbttag.set("Amount",NBTTagDouble.a(dmg));
     		nbttag.set("Operation",NBTTagInt.a(0));
     		nbttag.set("UUIDLeast",NBTTagInt.a(894654));
     		nbttag.set("UUIDMost",NBTTagInt.a(2872));
     		nbttag.set("Slot", NBTTagString.a("mainhand"));
     		modList.add(nbttag);
     		compound.set("AttributeModifiers", modList);
     		nmStack.setTag(compound);
     		weaponMap.put(every_character_enum, CraftItemStack.asBukkitCopy(nmStack));
           }
	}
    
    public void survivor_item_generation() 
    {
		ItemStack itemStack= null;
		int slot = 1;
		for(String iString : itemStrings)
		{
			switch (iString) {
			case "Potion Of Hope":
				itemStack = new ItemStack(Material.POTION);
				PotionMeta PotionMeta = (PotionMeta) itemStack.getItemMeta();
				PotionMeta.addCustomEffect(PotionEffectType.REGENERATION.createEffect(190, 2), true);
				PotionMeta.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta);
				slot = 10;
				break;
			case "Potion Of Savior":
				itemStack = new ItemStack(Material.SPLASH_POTION);
				PotionMeta PotionMeta1 = (PotionMeta) itemStack.getItemMeta();
				PotionMeta1.addCustomEffect(PotionEffectType.REGENERATION.createEffect(190, 2), true);
				PotionMeta1.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta1);
				slot = 12;	
				break;
			case"Octane certified Drug":
				itemStack = new ItemStack(Material.POTION);
				PotionMeta PotionMeta11 = (PotionMeta) itemStack.getItemMeta();
				PotionMeta11.addCustomEffect(PotionEffectType.SPEED.createEffect(200, 1), true);
				PotionMeta11.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta11);
				slot = 14;
				break;
			case"Potion Of Shit":
				itemStack = new ItemStack(Material.SPLASH_POTION);
				PotionMeta PotionMeta2 = (PotionMeta) itemStack.getItemMeta();
				PotionMeta2.addCustomEffect(PotionEffectType.BLINDNESS.createEffect(100, 2), true);
				PotionMeta2.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta2);		
				slot = 16;
				break;
			case"Face The Death":
				itemStack = new ItemStack(Material.WOODEN_SWORD);
				itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName(iString);
				itemStack.setItemMeta(itemMeta);
				slot = 30;
				break;
			case "Potion Of STOP":
				itemStack = new ItemStack(Material.SPLASH_POTION);
				PotionMeta PotionMeta21 = (PotionMeta) itemStack.getItemMeta();
				PotionMeta21.addCustomEffect(PotionEffectType.SLOW.createEffect(100, 200), true);
				PotionMeta21.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta21);
				slot = 19;
				break;
			case "Enemy Chaser":
				itemStack = new ItemStack(Material.SNOWBALL, 16);
				slot =21;
				break;
			case "Potion of Survive":
				itemStack = new ItemStack(Material.POTION);
				PotionMeta PotionMeta31 = (PotionMeta) itemStack.getItemMeta();
				PotionMeta31.addCustomEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(180, 1), true);
				PotionMeta31.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta31);
				slot = 23;
				break;
			case "Potion Of Big Brain":
				itemStack = new ItemStack(Material.POTION);
				PotionMeta PotionMeta311 = (PotionMeta) itemStack.getItemMeta();
				PotionMeta311.addCustomEffect(PotionEffectType.INVISIBILITY.createEffect(200, 1), true);
				PotionMeta311.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta311);
				slot = 25;
				break;
			case "Potion Of Bruh":
				itemStack = new ItemStack(Material.SPLASH_POTION);
				PotionMeta PotionMeta3111 = (PotionMeta) itemStack.getItemMeta();
				PotionMeta3111.addCustomEffect(PotionEffectType.LEVITATION.createEffect(200, 2), true);
				PotionMeta3111.setDisplayName(iString);
				itemStack.setItemMeta(PotionMeta3111);
				slot = 28;
				break;


			default:
				break;
			}
			itemInventory.setItem(slot,itemStack);
	}
//    	try {
//    		getCustomConfig().save(savingFile);
//    	} catch (IOException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
    }
}

