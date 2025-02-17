package model.Commands;

import controller.Controller;

public class RunExampleCommand extends Command{
    private final Controller controller;

    public RunExampleCommand(String key, String desc, Controller controller){
        super(key, desc);
        this.controller = controller;
    }

    @Override
    public void execute(){
        try {
            this.controller.allStep();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
