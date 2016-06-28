import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.util.*;

public class LeagueManager {
    static Player[] players = Players.load();
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
        if (teams.size() > 1) {
            organizerMenu.addMenuItem("Add Player", LeagueManager::addPlayer);
            organizerMenu.addMenuItem("Delete Player", LeagueManager::removePlayer);
            organizerMenu.addMenuItem("Back", LeagueManager::showMainMenu);
        }
        organizerMenu.show();
    }

    private static void removePlayer() {
        teamSelect().removePlayer();
    }

    private static void addPlayer() {
        teamSelect().addPlayer(playerSelect());
        System.out.println("Player added");

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

    private static Player playerSelect() {
        for (int i = 0; i < players.length; i++) {
            System.out.printf("%d %s%n", i+1, players[i]);
        }
        System.out.print("Player> ");
        // ToDo: Error Handling
        return players[scanner.nextInt() - 1];
    }

    private static void createTeam() {
        System.out.print("Team Name> ");
        String team = scanner.nextLine();
        System.out.print("Coach Name> ");
        String coach = scanner.nextLine();

        teams.add(new Team(team, coach));
        System.out.printf("Team %s created.%n", team);
        showOrganizerMenu();
    }

}
