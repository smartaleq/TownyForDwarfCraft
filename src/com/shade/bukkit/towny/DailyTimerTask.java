package com.shade.bukkit.towny;

public class DailyTimerTask extends TownyTimerTask {
	public DailyTimerTask(TownyUniverse universe) {
		super(universe);
		// TODO Auto-generated constructor stub
	}

	private TownyUniverse universe;

	@Override
	public void run() {
		// Collect taxes
		try {
			universe.collectTownTaxes();
			universe.collectNationTaxes();
		} catch (IConomyException e) {
		}

		// Automatically delete old residents
		if (universe.getSettings().isDeletingOldResidents())
			for (Resident resident : universe.getResidents())
				if (System.currentTimeMillis() - resident.getLastOnline() > universe.getSettings().getMaxInactivePeriod()) {
					// TODO: Delete resident
				}

	}

}
