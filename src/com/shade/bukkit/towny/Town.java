package com.shade.bukkit.towny;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.shade.bukkit.wallgen.WallSection;
import com.shade.bukkit.wallgen.Walled;

public class Town extends TownBlockOwner implements Walled {
	private List<Resident> residents = new ArrayList<Resident>();
	private List<Resident> assistants = new ArrayList<Resident>();
	private List<WallSection> wallSections = new ArrayList<WallSection>();
	private Resident mayor;
	private int bonusBlocks, taxes, plotPrice;
	private Nation nation;
	private boolean isPVP, hasMobs;
	private String townBoard;
	private TownBlock homeBlock;
	private TownyWorld world;
	private Location spawn;

	public Town(String name) {
		setName(name);
		permissions.allyBuild = true;
		permissions.allyDestroy = true;
		permissions.residentBuild = true;
		permissions.residentDestroy = true;
	}

	public Resident getMayor() {
		return mayor;
	}

	public void setTaxes(int taxes) {
		this.taxes = taxes;
	}

	public int getTaxes() {
		return taxes;
	}

	public void setMayor(Resident mayor) throws TownyException {
		if (!hasResident(mayor))
			throw new TownyException("Mayor doesn't belong to town.");
		this.mayor = mayor;
	}

	public Nation getNation() throws NotRegisteredException {
		if (hasNation())
			return nation;
		else
			throw new NotRegisteredException(
					"Town doesn't belong to any nation.");
	}

	public void setNation(Nation nation) throws AlreadyRegisteredException {
		if (nation == null) {
			this.nation = null;
			return;
		}
		if (this.nation == nation)
			return;
		if (hasNation())
			throw new AlreadyRegisteredException();
		this.nation = nation;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public List<Resident> getAssistants() {
		return assistants;
	}

	public boolean hasResident(String name) {
		for (Resident resident : residents)
			if (resident.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}

	public boolean hasResident(Resident resident) {
		return residents.contains(resident);
	}

	public boolean hasAssistant(Resident resident) {
		return assistants.contains(resident);
	}

	public void addResident(Resident resident)
			throws AlreadyRegisteredException {
		if (hasResident(resident))
			throw new AlreadyRegisteredException();
		else {
			residents.add(resident);
			resident.setTown(this);
		}
	}

	public void addAssistant(Resident resident)
			throws AlreadyRegisteredException {
		if (hasAssistant(resident))
			throw new AlreadyRegisteredException();
		else
			assistants.add(resident);
	}

	public boolean isMayor(Resident resident) {
		return resident == mayor;
	}

	public boolean hasNation() {
		return nation != null;
	}

	public int getNumResidents() {
		return residents.size();
	}

	public boolean isCapital() {
		return hasNation() ? nation.isCapital(this) : false;
	}

	public void setHasMobs(boolean hasMobs) {
		this.hasMobs = hasMobs;
	}

	public boolean hasMobs() {
		return hasMobs;
	}

	public void setPVP(boolean isPVP) {
		this.isPVP = isPVP;
	}

	public boolean isPVP() {
		return isPVP;
	}

	public void setTownBoard(String townBoard) {
		this.townBoard = townBoard;
	}

	public String getTownBoard() {
		return townBoard;
	}

	public void setBonusBlocks(int bonusBlocks) {
		this.bonusBlocks = bonusBlocks;
	}

	public int getBonusBlocks() {
		return bonusBlocks;
	}

	public void setHomeBlock(TownBlock homeBlock) throws TownyException {
		if (homeBlock == null) {
			this.homeBlock = null;
			return;
		}
		if (!hasTownBlock(homeBlock))
			throw new TownyException("Town has no claim over this town block.");
		this.homeBlock = homeBlock;
		try {
			setSpawn(spawn);
		} catch (TownyException e) {
			spawn = null;
		} catch (NullPointerException e) {
			// In the event that spawn is already null
		}
	}

	public TownBlock getHomeBlock() throws TownyException {
		if (hasHomeBlock())
			return homeBlock;
		else
			throw new TownyException("Town has not set a home block.");
	}

	public void setWorld(TownyWorld world) throws AlreadyRegisteredException {
		if (world == null) {
			this.world = null;
			return;
		}
		if (this.world == world)
			return;
		if (hasWorld())
			throw new AlreadyRegisteredException();
		else
			this.world = world;
	}

	public TownyWorld getWorld() {
		return world;
	}

	public boolean hasMayor() {
		return mayor != null;
	}
	
	public void removeResident(Resident resident) throws NotRegisteredException, EmptyTownException {
		if (!hasResident(resident))
			throw new NotRegisteredException();
		else {
			
			remove(resident);
			
			if (getNumResidents() == 0)
				try {
					clear();
					throw new EmptyTownException(this);
				} catch (EmptyNationException e) {
					throw new EmptyTownException(this, e);
				}
		}
	}
	private void removeAllResidents() {
		for (Resident resident : new ArrayList<Resident>(residents))
			remove(resident);
	}
	
	private void remove(Resident resident) {
		// TODO: Remove all plots of land owned in town.

		if (hasNation() && nation.hasAssistant(resident))
			try {
				nation.removeAssistant(resident);
			} catch (NotRegisteredException e) {
			}
		if (hasAssistant(resident))
			try {
				removeAssistant(resident);
			} catch (NotRegisteredException e) {
			}
			
		try {
			resident.setTown(null);
		} catch (AlreadyRegisteredException e) {
		}
		residents.remove(resident);
	}
	
	public void removeAssistant(Resident resident) throws NotRegisteredException {
		if (!hasAssistant(resident))
			throw new NotRegisteredException();
		else
			assistants.remove(resident);
	}

	public void setSpawn(Location spawn) throws TownyException {
		if (!hasHomeBlock())
			throw new TownyException("Home Block has not been set");
		Coord spawnBlock = Coord.parseCoord(spawn);
		if (homeBlock.getX() == spawnBlock.getX()
				&& homeBlock.getZ() == spawnBlock.getZ())
			this.spawn = spawn;
		else
			throw new TownyException("Spawn is not within the homeBlock.");
	}

	public Location getSpawn() throws TownyException {
		if (hasSpawn())
			return spawn;
		else
			throw new TownyException("Town has not set a spawn location.");
	}

	private boolean hasSpawn() {
		return spawn != null;
	}

	public boolean hasHomeBlock() {
		return homeBlock != null;
	}

	public void clear() throws EmptyNationException {
		//Cleanup
		removeAllResidents();
		mayor = null;
		residents = null;
		assistants = null;
		homeBlock = null;

		try {
			if (hasWorld()) {
				world.removeTownBlocks(getTownBlocks());
				world.removeTown(this);
			}
		} catch (NotRegisteredException e) {
		}
		if (hasNation())
			try {
				nation.removeTown(this);
			} catch (NotRegisteredException e) {
			}
	}

	private boolean hasWorld() {
		return world != null;
	}

	@Override
	public void removeTownBlock(TownBlock townBlock)
			throws NotRegisteredException {
		if (!hasTownBlock(townBlock))
			throw new NotRegisteredException();
		else {
			try {
				if (getHomeBlock() == townBlock)
					setHomeBlock(null);
			} catch (TownyException e) {
			}
			townBlocks.remove(townBlock);
		}
	}

	public void setPlotPrice(int plotPrice) {
		this.plotPrice = plotPrice;
	}

	public int getPlotPrice() {
		return plotPrice;
	}

	@Override
	public List<WallSection> getWallSections() {
		return wallSections;
	}

	@Override
	public void setWallSections(List<WallSection> wallSections) {
		this.wallSections = wallSections;

	}

	@Override
	public boolean hasWallSection(WallSection wallSection) {
		return wallSections.contains(wallSection);
	}

	@Override
	public void addWallSection(WallSection wallSection) {
		wallSections.add(wallSection);
	}

	@Override
	public void removeWallSection(WallSection wallSection) {
		wallSections.remove(wallSection);
	}

	public boolean isHomeBlock(TownBlock townBlock) {
		return hasHomeBlock() ? townBlock == homeBlock : false;
	}
}
