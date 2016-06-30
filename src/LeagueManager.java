import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import util.Prompter;

import java.util.*;

public class LeagueManager {
    static Player[] players = Players.load();
    static Set<Player> availablePlayers = new TreeSet<>(Arrays.asList(players));
    static List<Team> teams = new ArrayList<>();

    public static void main(String[] args) {
        System.out.printf("There are currently %d registered players.%n", players.length);
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

    private static void showCoachMenu() {
        Team team = teamSelect();
        Menu coachMenu = new Menu();
        coachMenu.addMenuItem("Roster", () -> roster(team));
        coachMenu.addMenuItem("Back", LeagueManager::showMainMenu);
        coachMenu.show();
    }


    private static void showOrganizerMenu() {
        Menu organizerMenu = new Menu();
        organizerMenu.addMenuItem("Create Team", LeagueManager::createTeam);
        if (teams.size() > 0) {
            organizerMenu.addMenuItem("Add Player", LeagueManager::addPlayer);
            organizerMenu.addMenuItem("Delete Player", LeagueManager::removePlayer);
            organizerMenu.addMenuItem("Height Report", LeagueManager::teamReport);
            organizerMenu.addMenuItem("League Balance Report", LeagueManager::leagueBalanceReport);
            organizerMenu.addMenuItem("Back", LeagueManager::showMainMenu);
        }
        organizerMenu.show();
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

    private static void addPlayer() {
        Team team = teamSelect();
        Player player = playerSelect(availablePlayers);
        team.addPlayer(player);
        availablePlayers.remove(player);
        System.out.printf("%n%s has been added to team %s%n", player, team);
        showOrganizerMenu();
    }

    private static void removePlayer() {
        Team team = teamSelect();
        Set<Player> players = team.getPlayers();
        if (players.size() > 0) {
            Player player = playerSelect(players);
            team.removePlayer(player);
            availablePlayers.add(player);
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
    }

    private static Player playerSelect(Set<Player> players) {
        List<Player> playerList = new ArrayList<>(players);
        listPlayers(players);
        int playerNumber;
        do {
            playerNumber = Prompter.promptInt("Player> ");
        } while (playerNumber == 0 && playerNumber <= playerList.size());
        return playerList.get(playerNumber -1);
    }

    private static Team teamSelect() {
        int i = 1;
        for (Team team: teams) {
            System.out.printf("%d %s%n", i++, team);
        }
        int teamNumber = 0;
        do {
            teamNumber = Prompter.promptInt("Team>");
        } while (teamNumber < 1 || teamNumber > teams.size());
        return teams.get( teamNumber - 1);
    }

    private static void teamReport() {
        int height = 0;
        Team team = teamSelect();
        if (team.getPlayers().size() > 0) {
            Set<Player> players = team.getPlayers();
            for (Player player : players) {
                height += player.getHeightInInches();
            }
            int avgHeight = height / players.size();
            System.out.printf("%nThe average height for team %s is %d inches%n%n", team, avgHeight);
        }
        showOrganizerMenu();
    }

    private static void leagueBalanceReport() {
        for (Team team : teams) {
            int experiencedPlayers = 0;
            int inExperiencedPlayers = 0;
            HashMap<Integer, Integer> heightCounts = new HashMap<>();
            for (Player player : team.getPlayers()) {
                if (player.isPreviousExperience()) {
                    experiencedPlayers++;
                } else {
                    inExperiencedPlayers++;
                }
                int height = player.getHeightInInches();
                if (heightCounts.containsKey(height)) {
                    heightCounts.put(height, heightCounts.get(height) + 1);
                } else {
                    heightCounts.put(height, 1);
                }
            }
            System.out.printf("Team %s:%nExperienced Players: %d%nInexperienced Players: %d%n", team, experiencedPlayers, inExperiencedPlayers);
            System.out.printf("%nHeight Report%n");
            heightCounts.forEach((height, count) -> System.out.printf("%s\": %d players%n", height, count));
            System.out.println();
        }
        showOrganizerMenu();
    }

    private static void roster(Team team) {
        listPlayers(team.getPlayers());
        showMainMenu();
    }
}
