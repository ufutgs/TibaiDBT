package DeadByMC.deadbymc_tibaimc;

import java.util.HashMap;



public enum killer_character_enum {
	HUNTRESS("Huntress"),RTA("RTA"),SHADOW("Shadow"),DOCTOR("Doctor"),VOIDWALKER("VoidWalker");
	
	private String value = "";

	private killer_character_enum(String value)
	{
		this.value = value;
	}
	
	public String getvalue()
	{
		return this.value;
	}

}
