package code.az.buytourproject.enums;

public enum CommandType {
    START("/start"),
    STOP("/stop");
    private final String value;
    CommandType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
