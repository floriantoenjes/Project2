import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.util.*;

public class LeagueManager {
    static Player[] players = Players.load();
    static List<Player> availablePlayers = new ArrayList<>(Arrays.asList(players));
    static ArrayList<Team> teams = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.printf("There are currently %d registered players.%n", players.length);
        // Your code here!
        showMainMenu();
    }

    private static void showMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.addMenuItem("Organizer", LeagueManager::showOrganizerMenu);
        mainMenu.addMenuItem("Coach", LeagueManager::showCoachMenu);
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
        coachMenu.show();
    }

    private static void showOrganizerMenu() {
        Menu organizerMenu = new Menu();
        organizerMenu.addMenuItem("Create Team", LeagueManager::createTeam);
        if (teams.size() > 0) {
            organizerMenu.addMenuItem("Add Player", LeagueManager::addPlayer);
            organizerMenu.addMenuItem("Delete Player", LeagueManager::removePlayer);
            organizerMenu.addMenuItem("Height Report", LeagueManager::heightReport);
            organizerMenu.addMenuItem("League Balance Report", LeagueManager::leagueBalanceReport);
            organizerMenu.addMenuItem("Back", LeagueManager::showMainMenu);
        }
        organizerMenu.show();
    }

    private static void createTeam() {
        if (teams.size() == 0 || availablePlayers.size() % teams.size() != 0) {
            System.out.print("Team Name> ");
            String team = scanner.nextLine();
            System.out.print("Coach Name> ");
            String coach = scanner.nextLine();

            teams.add(new Team(team, coach));
            Collections.sort(teams);
            System.out.printf("Team %s created.%n", team);
        }
        showOrganizerMenu();
    }

    private static void removePlayer() {
        Team team = teamSelect();
        List<Player> players = team.getPlayers();
        if (players.size() > 0) {
            Player player = playerSelect(players);
            team.removePlayer(player);
            availablePlayers.add(player);
            System.out.printf("%s has been removed from team %s", player, team);
        }

        showOrganizerMenu();
    }

    private static void addPlayer() {
        Team team = teamSelect();
        Player player = playerSelect(availablePlayers);
        team.addPlayer(player);
        availablePlayers.remove(player);
        System.out.printf("%s has been added to team %s%n", player, team);

        showOrganizerMenu();
    }

    private static Team teamSelect() {
        int i = 1;
        for (Team team: teams) {
            System.out.printf("%d %s%n", i, team);
        }
        System.out.print("Player> ");
        // ToDo: Error Handling
        return teams.get(scanner.nextInt() - 1);
    }

    private static void listPlayers(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String experienced = (player.isPreviousExperience()) ? "Yes" : "No";
            System.out.printf("%d %s | Height: %d | Experienced: %s %n", i+1, player, player.getHeightInInches(), experienced);
        }
    }

    private static Player playerSelect(List<Player> players) {
        listPlayers(players);
        System.out.print("Player> ");
        // ToDo: Error Handling
        return players.get(scanner.nextInt() -1);
    }

    private static void heightReport() {
        int height = 0;
        Team team = teamSelect();
        if (team.getPlayers().size() > 0) {
            List<Player> players = team.getPlayers();
            for (Player player : players) {
                height += player.getHeightInInches();
            }
            int avgHeight = height / players.size();
            System.out.printf("The average height for team %s is %d inches%n", team, avgHeight);
        }
        showOrganizerMenu();
    }

    private static void leagueBalanceReport() {
        int experiencedPlayers = 0;
        int inExperiencedPlayers = 0;

        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if (player.isPreviousExperience()) {
                    experiencedPlayers++;
                } else {
                    inExperiencedPlayers++;
                }
            }
        }

        System.out.printf("Experienced Players: %d%nInexperienced Players: %d%n", experiencedPlayers, inExperiencedPlayers);
        showOrganizerMenu();
    }

    private static void roster(Team team) {
        listPlayers(team.getPlayers());
    }

}
