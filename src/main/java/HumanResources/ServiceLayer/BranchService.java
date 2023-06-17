package HumanResources.ServiceLayer;

import HumanResources.BusinessLayer.BranchModule.BranchFacade;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;

public class BranchService {
    private BranchFacade branchFacade;

    public BranchService(BranchFacade branchFacade) {
        this.branchFacade = branchFacade;
    }

    public Response isBranchExist(int id) {
        try{
            return new Response(branchFacade.isBranchExist(id), true);
        } catch (Exception e) {
            return new Response(e.getMessage(), false);
        }
    }

    public Response addBranch(int id, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime) {
        try{
            branchFacade.addBranch(id, morningStartTime, morningEndTime, nightStartTime, nightEndTime);
            return new Response("Branch added successfully", true);
        } catch (Exception e) {
            return new Response(e.getMessage(), false);
        }
    }

    public Response getAllBranches() {
        try{
            return new Response(branchFacade.getAllBranches(), true);
        } catch (Exception e) {
            return new Response(e.getMessage(), false);
        }
    }

    // TODO: implement
    public Response getFinishTime(int branchId, ShiftType shiftType) {
        try{
            return new Response(branchFacade.getFinishTime(branchId,shiftType), true);
        } catch (Exception e) {
            return new Response(e.getMessage(), false);
        }
    }

    // TODO: implement
    public Response getStartTime(int branchId, ShiftType shiftType) {
        try{
            return new Response(branchFacade.getStartTime(branchId,shiftType), true);
        } catch (Exception e) {
            return new Response(e.getMessage(), false);
        }
    }
}
