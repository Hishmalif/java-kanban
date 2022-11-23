package tests;

public enum ResultTest {
    TEST_PASSED("PASSED"),TEST_FAILED("FAILED");

    private final String type;
    private ResultTest(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
