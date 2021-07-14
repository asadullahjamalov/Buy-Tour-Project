package code.az.buytourproject.enums;

public enum OperationType {
    BUTTON("Button"),
    FREETEXT("FreeText");
    private final String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

