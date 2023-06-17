package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

import java.util.HashMap;
import java.util.Map;

public class OrderWindow extends Window{
    public OrderWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        Map<Integer, Map<Integer, Integer>> stockOrder = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> supplyOrder = new HashMap<>();

        boolean done1 = false;
        print("Initialize your stock order:");
        while(!done1) {
            print("Enter store id, -1 to stop:");
            int storeId = Integer.parseInt(getInput());


            if (storeId == -1)
                break;

            boolean done2 = false;
            print("Enter items to order, -1 to stop:");
            while (!done2 && storeId != -1) {
                print("Enter item id:");
                int itemId = Integer.parseInt(getInput());

                if (itemId == -1) {
                    done2 = true;
                }
                else {
                    print("Enter amount:");
                    int amount = Integer.parseInt(getInput());

                    if (!stockOrder.containsKey(storeId)) {
                        stockOrder.put(storeId, new HashMap<>());
                    }
                    stockOrder.get(storeId).put(itemId, amount);
                }
            }

            if (storeId == -1) {
                done1 = true;
            }
        }

        done1 = false;
        print("Initialize your supply order:");
        while(!done1) {
            print("Enter provider id, -1 to stop:");
            int providerId = Integer.parseInt(getInput());

            if (providerId == -1)
                break;

            boolean done2 = false;
            print("Enter items to order, -1 to stop:");
            while (!done2 && providerId != -1) {
                print("Enter item id:");
                int itemId = Integer.parseInt(getInput());

                if (itemId == -1) {
                    done2 = true;
                }
                else {
                    print("Enter amount:");
                    int amount = Integer.parseInt(getInput());

                    if (!supplyOrder.containsKey(providerId)) {
                        supplyOrder.put(providerId, new HashMap<>());
                    }
                    supplyOrder.get(providerId).put(itemId, amount);
                }
            }

            if (providerId == -1) {
                done1 = true;
            }
        }

        Response response = factoryService.getDeliveryHandlerService().handleOrder(stockOrder, supplyOrder);
        if(response.hasError())
            print(response.getMessage());
        else {
            print("Order created successfully:");
            print(response.getData().toString());
        }

        this.close();
    }
}
