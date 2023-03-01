package dk.sdu.mmmi.cbse.asteroidsystem;
import dk.sdu.mmmi.cbse.common.data.Entity;
public class Asteroid extends Entity{

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
