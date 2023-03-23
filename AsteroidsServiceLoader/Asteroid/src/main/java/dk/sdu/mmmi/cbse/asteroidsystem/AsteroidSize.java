package dk.sdu.mmmi.cbse.asteroidsystem;

public enum AsteroidSize {
    GIANT_SIZE("GIANT_SIZE"),
    LARGE_SIZE("LARGE_SIZE"),
    MEDIUM_SIZE("MEDIUM_SIZE"),
    SMALL_SIZE("SMALL_SIZE");

    private String size;

    AsteroidSize(String size) {
        this.size = size;
    }

    public String getAsteroidSize() {
        return size;
    }
}
