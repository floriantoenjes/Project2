import java.util.ArrayList;
import java.util.Scanner;

public class Menu  {
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    public void show() {
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, menuItems.get(i));
        }
        System.out.print("Option? > ");
        int selection = (new Scanner(System.in)).nextInt() - 1;
        menuItems.get(selection).execute();
    }

    public void addMenuItem(String name, Runnable r) {
        menuItems.add(new MenuItem(name, r));
    }
}
