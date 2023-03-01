package dk.sdu.mmmi.cbse.asteroidsystem;

public enum AsteroidSize {
    GIANT_SIZE("GIANT"),
    LARGE_SIZE("LARGE"),
    MEDIUM_SIZE("MEDIUM"),
    SMALL_SIZE("SMALL");

    private String size;

    AsteroidSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}
