package com.jadventure.game.navigation;

import com.jadventure.game.items.Item;
import com.jadventure.game.entities.NPC;
import com.jadventure.game.monsters.Monster;
import com.jadventure.game.QueueProvider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The location class mostly deals with getting and setting variables.
 * It also contains the method to print a location's details.
 */
public class Location implements ILocation {
    private Coordinate coordinate;
    private String title;
    private String description;
    private LocationType locationType;
    private int dangerRating;
    private ArrayList<String> items;
    private ArrayList<String> npcs;
    private ArrayList<Monster> monsters = new ArrayList<Monster>();

    public Location() {

    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public int getDangerRating() {
        return dangerRating;
    }

    public void setDangerRating(int dangerRating) {
        this.dangerRating = dangerRating;
    }

    // It checks each direction for an exit and adds it to the exits hashmap if it exists.
    public Map<Direction, ILocation> getExits() {
        Map<Direction, ILocation> exits = new HashMap<Direction, ILocation>();
        ILocation borderingLocation;
        for(Direction direction: Direction.values()) {
            borderingLocation = LocationManager.getLocation(getCoordinate().getBorderingCoordinate(direction));
            if (borderingLocation != null) {
                exits.put(direction, borderingLocation);
            }
        }
        return exits;
    }

    public void setItems(ArrayList items) {
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        for (String itemId : this.items) {
            Item itemName = new Item(itemId);
            items.add(itemName);
        }
        return items;
    }

    public void setNPCs(ArrayList npcs) {
        this.npcs = npcs;
    }

    public ArrayList<NPC> getNPCs() {
        ArrayList<NPC> npcs = new ArrayList<NPC>();
        for (String npcID : this.npcs) {
            NPC npc = new NPC(npcID);
            npcs.add(npc);
        }
        return npcs;
    }
   
    public void addMonster(Monster monster) {
        if (monster != null) {
            ArrayList<Monster> list = this.monsters;
            list.add(monster);
            this.monsters = list;
        }
    }

    public void removeMonster(Monster monster) {
        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).equals(monster)) {
                monsters.remove(i);
            }
        }
    }

    public ArrayList<Monster> getMonsters() {
        return this.monsters;
    }

    public void removePublicItem(String itemID) {
        ArrayList<String> items = this.items;
        items.remove(itemID);
        setItems(items);
    }

    public void addPublicItem(String itemID) {
        ArrayList<String> items = this.items;
        items.add(itemID);
        setItems(items);
    }

    public void print() {
        QueueProvider.offer(getTitle() + ":");
        QueueProvider.offer(getDescription());
        ArrayList<Item> publicItems = getItems();
        if (!publicItems.isEmpty()) {
            QueueProvider.offer("Items:");
            for (Item item : publicItems) {
                QueueProvider.offer("    "+item.getName());
            }
        }
        ArrayList<NPC> npcs = getNPCs();
        if (!npcs.isEmpty()) {
            QueueProvider.offer("NPCs:");
            for (NPC npc : npcs) {
                QueueProvider.offer("   "+npc.getName());
            }
        }
        QueueProvider.offer("");
        for (Map.Entry<Direction,ILocation> direction : getExits().entrySet()) {
		QueueProvider.offer(direction.getKey().getDescription() + ": ");
    		QueueProvider.offer("    " + direction.getValue().getDescription());
        }
        QueueProvider.offer("");
    }
}
