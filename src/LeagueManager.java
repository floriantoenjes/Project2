import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.util.*;

public class LeagueManager {
    static Player[] players = Players.load();
    static List<Player> allPlayers = new ArrayList<>(Arrays.asList(players));
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
            organizerMenu.addMenuItem("Add Player", LeagueManager::addPlayer);
            organizerMenu.addMenuItem("Delete Player", LeagueManager::removePlayer);
            organizerMenu.addMenuItem("Back", LeagueManager::showMainMenu);
        }
        organizerMenu.show();
    }

    private static void createTeam() {
        System.out.print("Team Name> ");
        String team = scanner.nextLine();
        System.out.print("Coach Name> ");
        String coach = scanner.nextLine();

        teams.add(new Team(team, coach));
        Collections.sort(teams);
        System.out.printf("Team %s created.%n", team);
        showOrganizerMenu();
    }

    private static void removePlayer() {
        Team team = teamSelect();
        List<Player> players = team.getPlayers();
        if (players.size() > 0) {
            Player player = playerSelect(players);
            team.removePlayer(player);
            System.out.printf("%s has been removed from team %s", player, team);
        }



        showOrganizerMenu();
    }

    private static void addPlayer() {
        Team team = teamSelect();
        Player player = playerSelect(allPlayers);
        team.addPlayer(player);
        System.out.printf("%s has been added to team %s", player, team);

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

    private static Player playerSelect(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            System.out.printf("%d %s%n", i+1, players.get(i));
        }
        System.out.print("Player> ");
        // ToDo: Error Handling
        return players.get(scanner.nextInt() -1);
    }

}
