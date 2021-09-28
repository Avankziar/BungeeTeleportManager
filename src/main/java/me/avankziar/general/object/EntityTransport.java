package main.java.me.avankziar.general.object;

public class EntityTransport
{
	public class TargetAccess
	{
		private String targetUUID;
		private String accessUUID;
		
		public TargetAccess(String targetUUID, String accessUUID)
		{
			setTargetUUID(targetUUID);
			setAccessUUID(accessUUID);
		}

		public String getTargetUUID()
		{
			return targetUUID;
		}

		public void setTargetUUID(String targetUUID)
		{
			this.targetUUID = targetUUID;
		}

		public String getAccessUUID()
		{
			return accessUUID;
		}

		public void setAccessUUID(String accessUUID)
		{
			this.accessUUID = accessUUID;
		}
	}
	
	public class Ticket
	{
		private String playerUUID;
		private int actualAmount;
		private int totalBuyedAmount;
		private double spendedMoney;
		
		public Ticket(String playerUUID, int actualAmount, int totalBuyedAmount, double spendedMoney)
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
}