package com.shade.bukkit.towny;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class TownyWorld extends TownyObject {
	private List<Town> towns = new ArrayList<Town>();

	private Hashtable<Coord, TownBlock> townBlocks = new Hashtable<Coord, TownBlock>();

	// TODO: private List<TownBlock> adminTownBlocks = new
	// ArrayList<TownBlock>();

	public TownyWorld(String name) {
		setName(name);
	}

	public List<Town> getTowns() {
		return towns;
	}

	public boolean hasTown(String name) {
		for (Town town : towns)
			if (town.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}

	public boolean hasTown(Town town) {
		return towns.contains(town);
	}

	public void addTown(Town town) throws AlreadyRegisteredException {
		if (hasTown(town)) {
			throw new AlreadyRegisteredException();
		} else {
			towns.add(town);
			town.setWorld(this);
		}
	}

	public TownBlock getTownBlock(Coord coord) throws NotRegisteredException {
		TownBlock townBlock = townBlocks.get(coord);
		if (townBlock == null)
			throw new NotRegisteredException();
		else
			return townBlock;
	}

	public void newTownBlock(int x, int z) throws AlreadyRegisteredException {
		newTownBlock(new Coord(x, z));
	}

	public TownBlock newTownBlock(Coord key) throws AlreadyRegisteredException {
		if (hasTownBlock(key))
			throw new AlreadyRegisteredException();
		townBlocks.put(key, new TownBlock(key.getX(), key.getZ(), this));
		return townBlocks.get(key);
	}

	public boolean hasTownBlock(Coord key) {
		return townBlocks.containsKey(key);
	}

	public TownBlock getTownBlock(int x, int z) throws NotRegisteredException {
		return getTownBlock(new Coord(x, z));
	}

	public Collection<TownBlock> getTownBlocks() {
		return townBlocks.values();
	}

	public void removeTown(Town town) throws NotRegisteredException {
		if (!hasTown(town)) {
			throw new NotRegisteredException();
		} else {
			towns.remove(town);
			try {
				town.setWorld(null);
			} catch (AlreadyRegisteredException e) {
			}
		}
	}

	public void removeTownBlocks(List<TownBlock> townBlocks) {
		for (TownBlock townBlock : townBlocks) {
			removeTownBlock(townBlock);
		}
	}

	public void removeTownBlock(TownBlock townBlock) {
		try {
			if (townBlock.hasResident())
				townBlock.getResident().removeTownBlock(townBlock);
		} catch (NotRegisteredException e) {
		}
		try {
			if (townBlock.hasTown()) {
				townBlock.getTown().removeTownBlock(townBlock);
			}
		} catch (NotRegisteredException e) {
		}

		removeTownBlock(townBlock.getCoord());
	}

	public void removeTownBlock(Coord coord) {
		townBlocks.remove(coord);
	}
}
