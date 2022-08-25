package manager.implementation;

public enum NamesOfColumn {
    ID("id"),
    TYPE("type"),
    NAME("name"),
    STATUS("status"),
    DESCRIPTION("description"),
    EPIC("epic"),
    DURATION("duration"),
    START_TIME("startTime");

    private String description;

    NamesOfColumn (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
