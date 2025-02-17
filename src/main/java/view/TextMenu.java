package view;

import model.Commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;




public class TextMenu {
    private final Map<String, Command> commands;
    
    public TextMenu(){
        commands = new HashMap<>();
    }

    public void addCommand(Command c){
        this.commands.put(c.getKey(), c);
    }

    private void printMenu() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            if (!entry.getKey().equals("11")) {
                String line = String.format("%4s, %s", entry.getKey(), entry.getValue().getDescription());
                System.out.println(line);
            }
        }
        if (commands.containsKey("11")) {
            Command command11 = commands.get("11");
            String line = String.format("%4s, %s", command11.getKey(), command11.getDescription());
            System.out.println(line);
        }
    }

    public void show(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            printMenu();
            System.out.println("Input the option: ");
            String key = scanner.nextLine();
            Command com = commands.get(key);
            if (com == null){
                System.out.println("Invalid option");
            }
            else{
                com.execute();
        }
    }
    }
}
