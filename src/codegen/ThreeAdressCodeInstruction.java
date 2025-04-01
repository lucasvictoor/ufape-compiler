package codegen;

public class ThreeAdressCodeInstruction {
    private String operation;
    private String arg1;
    private String arg2;
    private String result;

    public ThreeAdressCodeInstruction(String operation, String arg1, String arg2, String result) {
        this.operation = operation;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        if (arg2 == null) {
            return result + " = " + operation + " " + arg1;
        } else {
            return result + " = " + arg1 + " " + operation + " " + arg2;
        }    
    }
}
