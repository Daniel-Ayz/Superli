package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;

public class Control {
    public void start(boolean init) {
        if(init)
            new MenuWindow((FactoryService.getInstance()).randomInit()).open();
        else
            new MenuWindow(FactoryService.getInstance()).open();
    }
}
