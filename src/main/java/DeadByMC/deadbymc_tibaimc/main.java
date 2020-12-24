package DeadByMC.deadbymc_tibaimc;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagDouble;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;

public class main extends JavaPlugin {
	 public HashMap<killer_character_enum, ItemStack> weaponMap = new HashMap<killer_character_enum, ItemStack>();
	 public Inventory Inventory = Bukkit.createInventory(null, 27,"Choose your role you wished");
    @Override
    public void onEnable()
    {
    	
//	this.getCommand("bigpeepee").setExecutor(new bigpepeCommandExecutor(this));
//	this.getCommand("wccs-menu").setExecutor(new MainMenuCommandExectuor(this));
	 getServer().getPluginManager().registerEvents(new MatchListener(this), this);
	 ItemStack itemStack = new ItemStack(Material.LANTERN);
	 ItemMeta itemMeta = itemStack.getItemMeta();
	 itemMeta.setDisplayName(ChatColor.GREEN+"Survivor");
	 itemMeta.setLocalizedName(ChatColor.GREEN+"Survivor");
	 itemStack.setItemMeta(itemMeta);
	 ItemStack itemStack1 = new ItemStack(Material.IRON_SWORD);
	 ItemMeta itemMeta1 = itemStack1.getItemMeta();
	 itemMeta1.setDisplayName(ChatColor.RED+"Killer");
	 itemMeta1.setLocalizedName(ChatColor.RED+"Killer");
	 itemStack1.setItemMeta(itemMeta1);
	 Inventory.setItem(15,itemStack1 );
//    createCustomConfig();
    
    }
    public void Weapon() {
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
    @Override
    public void onDisable()
    {
    	
	}
//    	try {
//    		getCustomConfig().save(savingFile);
//    	} catch (IOException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
    }

