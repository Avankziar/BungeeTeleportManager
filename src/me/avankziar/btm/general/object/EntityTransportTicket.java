package me.avankziar.btm.general.object;

public class EntityTransportTicket
{	
	private String playerUUID;
	private int actualAmount;
	private int totalBuyedAmount;
	private double spendedMoney;
	
	public EntityTransportTicket(String playerUUID, int actualAmount, int totalBuyedAmount, double spendedMoney)
	{
		setPlayerUUID(playerUUID);
		setActualAmount(actualAmount);
		setTotalBuyedAmount(totalBuyedAmount);
		setSpendedMoney(spendedMoney);
	}

	public String getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(String playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public int getActualAmount()
	{
		return actualAmount;
	}

	public void setActualAmount(int actualAmount)
	{
		this.actualAmount = actualAmount;
	}

	public int getTotalBuyedAmount()
	{
		return totalBuyedAmount;
	}

	public void setTotalBuyedAmount(int totalBuyedAmount)
	{
		this.totalBuyedAmount = totalBuyedAmount;
	}

	public double getSpendedMoney()
	{
		return spendedMoney;
	}

	public void setSpendedMoney(double spendedMoney)
	{
		this.spendedMoney = spendedMoney;
	}
}