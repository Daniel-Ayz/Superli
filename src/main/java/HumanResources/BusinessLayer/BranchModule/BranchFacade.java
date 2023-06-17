package HumanResources.BusinessLayer.BranchModule;

import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.DataAcessLayer.BranchDAL.BranchDAO;
import HumanResources.ServiceLayer.Response;
import delivery.backend.businessLayer.destination.Provider;
import delivery.backend.businessLayer.destination.StoreImpl;
import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.destination.ProviderDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchFacade {
    private Map<Integer, Branch> branches;

    public BranchFacade() {
        branches = new HashMap<Integer, Branch>();
    }

    // adds a branch to the map with id and branch times
    public void addBranch(int id, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime){
        if(branches.get(id) == null && new BranchDAO().insert(id,morningStartTime,morningEndTime,nightStartTime,nightEndTime)) {
            branches.put(id, new Branch(id, morningStartTime, morningEndTime, nightStartTime, nightEndTime));
        }else{
            throw new IllegalArgumentException("A branch with such id already exists");
        }
    }

    public void loadData(){
        ArrayList<Branch> branchesDAL = new BranchDAO().selectAll();
        for(Branch branch: branchesDAL){
            branches.put(branch.getId(),branch);
        }
        if (!branches.containsKey(0)) {
            addBranch(0, "08:00", "21:00", "21:00", "22:00");
        }
    }

    public boolean isBranchExist(int id) {
        return branches.containsKey(id);
    }

    public Map<Integer, Branch> getAllBranches() {
        return branches;
    }

    public String getFinishTime(int branchId, ShiftType shiftType){
        if(isBranchExist(branchId)) {
            if(shiftType.equals(ShiftType.MORNING))
                return branches.get(branchId).getMorningEndTime();
            return branches.get(branchId).getNightEndTime();
        }else{
            throw new IllegalArgumentException("A branch with such id doesn't exist");
        }
    }

    public String getStartTime(int branchId, ShiftType shiftType) {
        if(isBranchExist(branchId)) {
            if(shiftType.equals(ShiftType.MORNING))
                return branches.get(branchId).getMorningStartTime();
            return branches.get(branchId).getNightStartTime();
        }else{
            throw new IllegalArgumentException("A branch with such id doesn't exist");
        }
    }
}
