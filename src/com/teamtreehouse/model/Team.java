package com.teamtreehouse.model;

import java.util.ArrayList;

public class Team implements Comparable<Team>{
    private final String name;
    private String coach;
    ArrayList<Player> players = new ArrayList<>();

    public Team(String team, String coach) {
        this.name = team;
        this.coach = coach;
    }

    @Override
    public int compareTo(Team o) {
        return this.name.compareTo(o.name);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public String toString() {
        return name;
    }
}
