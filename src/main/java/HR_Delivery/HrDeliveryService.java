package HR_Delivery;

import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;
import delivery.backend.serviceLayer.FactoryService;

public class HrDeliveryService {
    public Response addBranch(int id, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime, String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        Response response1 = ServiceFactory.getInstance().getBranchService().isBranchExist(id);

        if(response1.isSuccess() && !(Boolean)response1.getData()){
            delivery.tools.Response response2 = FactoryService.getInstance().getDestinationService().addDestination(address, area, phone, contactName, northCoordinate, eastCoordinate, false);
            if(response2.hasError())
                return new Response("error at Delivery module: " + response2.getMessage(), false);
            return ServiceFactory.getInstance().getBranchService().addBranch(id, morningStartTime, morningEndTime, nightStartTime, nightEndTime);

        }
        return new Response("Branch already exists",false);
    }
}
