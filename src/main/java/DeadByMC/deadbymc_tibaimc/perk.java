package DeadByMC.deadbymc_tibaimc;

public class perk {
	@SuppressWarnings("unused")
	private perk_enum perk_enum;
	
	public perk(String perkname) 
	{
		for (perk_enum every_perk_enum : DeadByMC.deadbymc_tibaimc.perk_enum.values())
		{
			if(every_perk_enum.getvalue()==perkname)
			{
				perk_enum = every_perk_enum;
				break;
			}
		}
	}
	public perk()
	{
		
	}
	
	// 1. all perk are passive skill . 
	//2. killer can only bring one perk.
	
}
