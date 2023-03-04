package dk.sdu.mmmi.cbse.asteroidsystem;
public class Asteroid extends dk.sdu.mmmi.cbse.common.asteroids.Asteroid {

    private AsteroidSize asteroidSize;


    /***
     * Create an asteroid with a size from the Enum
     * @param asteroidSize
     */
    public Asteroid(AsteroidSize asteroidSize) {

        this.asteroidSize = asteroidSize;

    }

    public AsteroidSize getAsteroidSize() {
        return asteroidSize;
    }
}
