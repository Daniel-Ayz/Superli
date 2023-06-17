package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;

import java.util.Scanner;

public abstract class Window {
    protected FactoryService factoryService;

    private Scanner scanner = new Scanner(System.in);

    protected boolean active = true;

    protected Window(FactoryService factoryService) {
        this.factoryService = factoryService;
    }

    protected void print(String message) {
        System.out.println(message);
    }

    protected String getInput() {
        return scanner.nextLine();
    }

    public abstract void open();

    protected void close() {
        active = false;
    }
}
