package dk.sdu.mmmi.cbse.asteroidsystem;

public enum AsteroidLife {
    GIANT_LIFE(8),
    LARGE_LIFE(6),
    MEDIUM_LIFE(4),
    SMALL_LIFE(1);

    private int life;
    AsteroidLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }
}
