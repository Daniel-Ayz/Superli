package HumanResources.BusinessLayer.EmployeeModule;

public class BankInformation {
    private String bankName;
    private String bankAccountNumber;

    public BankInformation(String bankName, String bankAccountNumber) {
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String toString() {
        return "Bank Name: " + bankName + ", Bank Account Number: " + bankAccountNumber;
    }

    public boolean equals(BankInformation bankInformation) {
        if (this.bankName.equals(bankInformation.bankName) && this.bankAccountNumber.equals(bankInformation.bankAccountNumber)) {
            return true;
        }
        return false;
    }
}
