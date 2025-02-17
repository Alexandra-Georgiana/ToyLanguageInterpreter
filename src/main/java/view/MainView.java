package view;

import controller.Controller;

public class MainView {
    Controller controller;
    
    public MainView(Controller controller) {
        this.controller = controller;
    }

    public void run() {
        try {
            controller.allStep();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
        
}
