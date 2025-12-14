package price.change;

/**
 * Класс обслуживания
 */
public enum FareCondition {
    BUSINESS("Business"),
    ECONOMY("Economy"),
    COMFORT("Comfort");

    private final String value;

    FareCondition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
