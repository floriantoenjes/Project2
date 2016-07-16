package com.floriantoenjes.league.manager;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.floriantoenjes.presentation.Menu;
import com.floriantoenjes.util.Prompter;

import java.util.*;
import java.util.stream.Collectors;

public class LeagueManager {
    private static Player[] players = Players.load();
    private static Set<Player> availablePlayers = new TreeSet<>(Arrays.asList(players));
    private static Set<Player> waitingList = new LinkedHashSet<>();
    private static List<Team> teams = new ArrayList<>();

    public static void main(String[] args) {
        System.out.printf("There are currently %d registered players.%n%n", players.length);
        createMockData();
        showMainMenu();
    }

    private static void showMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.addMenuItem("Organizer", LeagueManager::showOrganizerMenu);
        if (teams.size() > 0) {
            mainMenu.addMenuItem("Coach", LeagueManager::showCoachMenu);
        }
        mainMenu.addMenuItem("Exit", () -> {
            System.out.println("Exiting...");
            System.exit(0);
        });
        mainMenu.show();
    }

    private static void showOrganizerMenu() {
        Menu organizerMenu = new Menu();
        organizerMenu.addMenuItem("Create Team", LeagueManager::createTeam);
        if (teams.size() > 0) {
            organizerMenu.addMenuItem("Add Player to Team", LeagueManager::addPlayer);
            organizerMenu.addMenuItem("Remove Player from Team", LeagueManager::removePlayer);
            organizerMenu.addMenuItem("Height Report for Team", LeagueManager::teamReport);
            organizerMenu.addMenuItem("League Balance Report", LeagueManager::leagueBalanceReport);
            organizerMenu.addMenuItem("Add Player to Waitinglist", LeagueManager::addPlayerToWaitingList);
            organizerMenu.addMenuItem("Remove Player from League", LeagueManager::removePlayerFromLeague);
            organizerMenu.addMenuItem("Build Fair Teams", LeagueManager::buildFairTeams);
            organizerMenu.addMenuItem("Back", LeagueManager::showMainMenu);
        }
        organizerMenu.show();
    }

    private static void showCoachMenu() {
        Team team = teamSelect();
        Menu coachMenu = new Menu();
        coachMenu.addMenuItem("Roster", () -> roster(team));
        coachMenu.addMenuItem("Back", LeagueManager::showMainMenu);
        coachMenu.show();
    }

    private static void createTeam() {
        if (teams.size() < availablePlayers.size()) {
            String teamName;
            do {
                teamName = Prompter.prompt("Team Name> ");
            } while (teamName.isEmpty());
            String coach;
            do {
                coach = Prompter.prompt("Coach Name> ");
            } while (coach.isEmpty());
            teams.add(new Team(teamName, coach));
            Collections.sort(teams);
            System.out.printf("Team %s created.%n%n", teamName);
        }
        showOrganizerMenu();
    }

    private static void addPlayerToTeam(Team team, Player player) {
        team.addPlayer(player);
        availablePlayers.remove(player);
    }

    private static void addPlayer() {
        Team team = teamSelect();
        if (team.getPlayers().size() < Team.MAX_PLAYERS && availablePlayers.size() > 0) {
            Player player = playerSelect(availablePlayers);
            addPlayerToTeam(team, player);
            System.out.printf("%n%s has been added to team %s%n%n", player, team);
        } else if (!(availablePlayers.size() > 0)){
            System.out.printf("There are currently no players available.%n%n");
        } else {

            System.out.printf("Team %s already has %d players%n%n", team, Team.MAX_PLAYERS);
        }
        showOrganizerMenu();
    }

    private static void removePlayerFromTeam(Team team, Player player) {
        team.removePlayer(player);
        availablePlayers.add(player);
    }

    private static void removePlayer() {
        Team team = teamSelect();
        Set<Player> players = team.getPlayers();
        if (players.size() > 0) {
            Player player = playerSelect(players);
            removePlayerFromTeam(team, player);
            System.out.printf("%n%s has been removed from team %s%n%n", player, team);
        }
        showOrganizerMenu();
    }

    private static void listPlayers(Set<Player> players) {
        List<Player> playerList = new ArrayList<>(players);
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            String experienced = (player.isPreviousExperience()) ? "Yes" : "No";
            System.out.printf("%d %s | Height: %d | Experienced: %s %n", i+1, player, player.getHeightInInches(), experienced);
        }
        System.out.println();
    }

    private static Player playerSelect(Set<Player> players) {
        List<Player> playerList = new ArrayList<>(players);
        listPlayers(players);
        int playerNumber;
        do {
            playerNumber = Prompter.promptInt("Player> ") -1;
        } while (playerNumber < 0 || playerNumber >= playerList.size());
        return playerList.get(playerNumber);
    }

    private static Team teamSelect() {
        int i = 1;
        for (Team team: teams) {
            System.out.printf("%d %s%n", i++, team);
        }
        int teamNumber;
        do {
            teamNumber = Prompter.promptInt("Team>");
        } while (teamNumber < 1 || teamNumber > teams.size());
        System.out.println();
        return teams.get(teamNumber - 1);
    }

    private static void teamReport() {
        Team team = teamSelect();
        if (team.getPlayers().size() > 0) {
            int height = 0;
            int avgHeight;
            Set<Player> small = new TreeSet<>();
            Set<Player> average = new TreeSet<>();
            Set<Player> big = new TreeSet<>();
            Set<Player> players = team.getPlayers();

            for (Player player : players) {
                int tmpHeight = player.getHeightInInches();
                if (tmpHeight < 39) {
                    small.add(player);
                } else if (tmpHeight > 38 && tmpHeight < 44) {
                    average.add(player);
                } else {
                    big.add(player);
                }
                height += player.getHeightInInches();
            }
            avgHeight = height / players.size();
            System.out.printf("%nThe average height for team %s is %d inches%n%n", team, avgHeight);
            System.out.printf("%d Small:%n", small.size());
            small.forEach( p ->
                System.out.printf("%s - %d\"%n", p.toString(), p.getHeightInInches())
            );
            System.out.printf("%d Average:%n", average.size());
            average.forEach( p ->
                System.out.printf("%s - %d\"%n", p.toString(), p.getHeightInInches())
            );
            System.out.printf("%d Big:%n", big.size());
            big.forEach( p ->
                System.out.printf("%s - %d\"%n", p.toString(), p.getHeightInInches())
            );
        } else {
            System.out.println("There are no players in this team.");
        }
        System.out.println();
        showOrganizerMenu();
    }

    private static void leagueBalanceReport() {
        for (Team team : teams) {
            int experiencedPlayers = 0;
            int inexperiencedPlayers = 0;
            HashMap<Integer, Integer> heightCounts = new HashMap<>();
            Set<Player> players = team.getPlayers();
            for (Player player : players) {
                if (player.isPreviousExperience()) {
                    experiencedPlayers++;
                } else {
                    inexperiencedPlayers++;
                }
                int height = player.getHeightInInches();
                if (heightCounts.containsKey(height)) {
                    heightCounts.put(height, heightCounts.get(height) + 1);
                } else {
                    heightCounts.put(height, 1);
                }
            }
            double ratioExperienced = 100d / players.size() * experiencedPlayers;
            double ratioInexperienced = 100d / players.size() * inexperiencedPlayers;
            System.out.printf("Team %s:%n" +
                    "Experienced Player Ratio: %.2f%%%n" +
                    "Experienced Players: %d" +
                    "%nInexperienced Player ratio: %.2f%%%n" +
                    "Inexperienced Players: %d%n",
                    team, ratioExperienced, experiencedPlayers, ratioInexperienced, inexperiencedPlayers);
            System.out.println();
        }
        showOrganizerMenu();
    }

    private static void roster(Team team) {
        if (team.getPlayers().size() > 0) {
            listPlayers(team.getPlayers());
        } else {
            System.out.printf("Team %s currently has no players.%n", team);
        }
        showMainMenu();
    }

    private static void addPlayerToWaitingList() {
        System.out.println("Enter Player data");
        String firstName = Prompter.prompt("First Name> ");
        String lastName = Prompter.prompt("Last Name> ");;
        int heightInInches = Prompter.promptInt("Height in Inches> ");
        boolean isPreviousExperience = Prompter.promptForYes("Has Experience Y(es)/(N)o> ");
        Player player = new Player(firstName, lastName, heightInInches, isPreviousExperience);
        waitingList.add(player);
        System.out.printf("%n%s has been added to the waiting list.%n%n", player);
        showOrganizerMenu();
    }

    private static void removePlayerFromLeague() {
        if (availablePlayers.size() > 0) {
            Player player = playerSelect(availablePlayers);
            availablePlayers.remove(player);
            System.out.printf("%s has been removed from the league%n", player);
            if (waitingList.iterator().hasNext()) {
                Player newPlayer = waitingList.iterator().next();
                waitingList.remove(player);
                availablePlayers.add(newPlayer);
                System.out.printf("%s has been added to the league%n", newPlayer);
            }
            System.out.println();
        } else {
            System.out.println("All players are assigned to teams!");
        }
        showOrganizerMenu();
    }

    private static Set<Player> getExperiencedPlayers() {
        return availablePlayers.stream().filter(Player::isPreviousExperience).collect(Collectors.toSet());
    }

    private static Set<Player> getInexperiencedPlayers() {
        return availablePlayers.stream().filter(p -> !p.isPreviousExperience()).collect(Collectors.toSet());
    }

    private static void buildFairTeams() {
        Set<Player> experiencedPlayers = getExperiencedPlayers();
        Set<Player> inExperiencedPlayers = getInexperiencedPlayers();
        if (experiencedPlayers.size() % teams.size() == 0) {
            Iterator<Team> teamIterator = teams.iterator();
            for (Player ePlayer : experiencedPlayers) {
                if (!teamIterator.hasNext()) {
                    teamIterator = teams.iterator();
                }
                Team team = teamIterator.next();
                if (team.getPlayers().size() < Team.MAX_PLAYERS) {
                    addPlayerToTeam(team, ePlayer);
                }
            }
            teamIterator = teams.iterator();
            for (Player iPlayer : inExperiencedPlayers) {
                if (!teamIterator.hasNext()) {
                    teamIterator = teams.iterator();
                }
                Team team = teamIterator.next();
                if (team.getPlayers().size() < Team.MAX_PLAYERS) {
                    addPlayerToTeam(team, iPlayer);
                }
            }
            System.out.println("Fair teams have been build");
        } else {
            System.out.println("Not able to automatically build fair teams");
        }
        showOrganizerMenu();
    }

    public static void createMockData() {
        Team testTeam1 = new Team("Dolphins", "James");
        Team testTeam2 = new Team("Spacers", "Bill");
        Team testTeam3 = new Team("Djangos", "Freud");
        teams.add(testTeam1);
        teams.add(testTeam2);
        teams.add(testTeam3);
    }
}