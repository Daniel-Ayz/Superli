package HumanResources.BusinessLayer.BranchModule;

public class Branch {
    private int id;
    private String morningStartTime;
    private String morningEndTime;
    private String nightStartTime;
    private String nightEndTime;


    public Branch(int id, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime) {
        this.id = id;
        this.morningStartTime = morningStartTime;
        this.morningEndTime = morningEndTime;
        this.nightStartTime = nightStartTime;
        this.nightEndTime = nightEndTime;
    }


    public int getId() {
        return id;
    }

    public String getMorningStartTime() {
        return morningStartTime;
    }

    public String getMorningEndTime() {
        return morningEndTime;
    }

    public String getNightStartTime() {
        return nightStartTime;
    }

    public String getNightEndTime() {
        return nightEndTime;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setMorningStartTime(String morningStartTime) {
        this.morningStartTime = morningStartTime;
    }

    public void setMorningEndTime(String morningEndTime) {
        this.morningEndTime = morningEndTime;
    }

    public void setNightStartTime(String nightStartTime) {
        this.nightStartTime = nightStartTime;
    }

    public void setNightEndTime(String nightEndTime) {
        this.nightEndTime = nightEndTime;
    }


    public String toString() {
        return "Branch id: " + id + ", Morning start time: " + morningStartTime + ", Morning end time: " + morningEndTime + ", Night start time: " + nightStartTime + ", Night end time: " + nightEndTime;
    }

    public boolean equals(Branch branch) {
        if (this.id == branch.id && this.morningStartTime == branch.morningStartTime && this.morningEndTime == branch.morningEndTime && this.nightStartTime == branch.nightStartTime && this.nightEndTime == branch.nightEndTime) {
            return true;
        }
        return false;
    }



}
