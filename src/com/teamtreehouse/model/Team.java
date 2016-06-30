package com.teamtreehouse.model;

import java.util.ArrayList;
import java.util.TreeSet;

public class Team implements Comparable<Team>{
    private final String name;
    private String coach;
    TreeSet<Player> players = new TreeSet<>();

    public Team(String team, String coach) {
        this.name = team;
        this.coach = coach;
    }

    @Override
    public int compareTo(Team o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public String toString() {
        return name;
    }

    public TreeSet<Player> getPlayers() {
        return new TreeSet<>(players);
    }
}
