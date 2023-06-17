package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

public class TreatmentWindow extends Window{

    private int supplierId;
    private int shipmentId;
    public TreatmentWindow(FactoryService factoryService, int supplierId, int shipmentId) {
        super(factoryService);
        this.supplierId = supplierId;
        this.shipmentId = shipmentId;
    }
    
    @Override
    public void open() {
        while(active) {
            print("Select treatment:\n" +
                    "1) Dropping or switching destination\n" +
                    "2) switching truck\n" +
                    "3) dropping items");

            try{
                int treatment = Integer.parseInt(getInput());
                if(treatment == 1 || treatment == 2 || treatment == 3) {
                    Response response = factoryService.getDeliveryHandlerService().handleError(shipmentId, treatment, supplierId);
                    if(!response.hasError()){
                        print("Treatment was applied successfully");
                        this.close();
                    }
                    else
                        print(response.getMessage());
                }
                else
                    print("invalid number");
            }
            catch(Exception ex) { print("invalid input"); }
        }
    }


}
