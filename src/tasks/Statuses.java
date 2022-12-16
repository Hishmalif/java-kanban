package tasks;

public enum Statuses {
    NEW("NEW"), IN_PROGRESS("IN_PROGRESS"), DONE("DONE");
    private final String name;

    Statuses(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Statuses getStatus(final String name) {
        return Statuses.valueOf(name.trim().toUpperCase());
    }
}