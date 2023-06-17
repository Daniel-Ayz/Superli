package HumanResources.Tests;

import HumanResources.BusinessLayer.BranchModule.BranchFacade;
import HumanResources.DataAcessLayer.BranchDAL.BranchDAO;
import HumanResources.ServiceLayer.BranchService;
import HumanResources.ServiceLayer.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchTest {
    BranchService bs;
    @BeforeEach
    void setUp(){
        bs = new BranchService(new BranchFacade());
    }

    @AfterEach
    @BeforeEach
    void clearDB(){
        new BranchDAO().deleteAll();
    }

    //test that adds a branch
    @Test
    void addBranch() {
        bs.addBranch(1,"7:00","15:00","15:00","23:00");
        assert bs.isBranchExist(1).isSuccess();
    }

    //test that adds two branches with the same id
    @Test
    void addBranchWithSameId() {
        bs.addBranch(1,"7:00","15:00","15:00","23:00");
        assert bs.isBranchExist(1).isSuccess();
        Response res = bs.addBranch(1,"8:00","15:00","15:00","22:00");
        assert !res.isSuccess();
    }
}