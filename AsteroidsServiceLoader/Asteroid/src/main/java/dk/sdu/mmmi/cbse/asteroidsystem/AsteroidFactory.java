package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;


public class AsteroidFactory {
    // Moving part data
    float deceleration, acceleration, maxSpeed, rotationSpeed = 0;

    // Life part data
    int life = 0;
    float expiration = 0;

    AsteroidFactory(){}

    /***
     * Creates an asteroid with the right size and associated moving part and life part values.
     * @param asteroid an asteroid with a size, but no moving or life part values.
     * @return asteroid with correct moving and life part values associated with the size of the asteroid
     */
    private Asteroid createAsteroidWithLifeAndMovementParts(Asteroid asteroid) {

        switch (asteroid.getAsteroidSize()) {
            case GIANT_SIZE -> {
                // MovingPart Data
                deceleration = 0;
                acceleration = 30;
                maxSpeed = 25;
                rotationSpeed = 0.1f;

                // LifePart Data
                life = AsteroidLife.GIANT_LIFE.getLife();
                expiration = 15.3f;

                asteroid.setRadius(30);
            }
            case LARGE_SIZE -> {
                // MovingPart Data
                deceleration = 0;
                acceleration = 40;
                maxSpeed = 50;
                rotationSpeed = 0.2f;

                // LifePart Data
                life = AsteroidLife.LARGE_LIFE.getLife();
                expiration = 15.3f;
                asteroid.setRadius(25);
            }
            case MEDIUM_SIZE -> {
                // MovingPart Data
                deceleration = 0;
                acceleration = 60;
                maxSpeed = 35;
                rotationSpeed = 0.3f;

                // LifePart Data
                life = AsteroidLife.MEDIUM_LIFE.getLife();
                expiration = 15.3f;
                asteroid.setRadius(15);
            }
            case SMALL_SIZE -> {
                // MovingPart Data
                deceleration = 0;
                acceleration = 65;
                maxSpeed = 65;
                rotationSpeed = 0.4f;

                // LifePart Data
                life = AsteroidLife.SMALL_LIFE.getLife();
                expiration = 15.3f;
                asteroid.setRadius(8);
            }
        }

        // Add the parts to the asteroid
        asteroid.add(new MovingPart(deceleration, acceleration, maxSpeed, rotationSpeed));
        asteroid.add(new LifePart(life));

        // Returns the asteroid with all the parts
        return asteroid;
    }

    private PositionPart randomPosition(GameData gameData) {
        // Position Part data, same for all sizes, creates a random start position for the asteroid
        float x = gameData.getDisplayWidth() / (float) Math.random() * 10;
        float y = gameData.getDisplayHeight() / (float) Math.random() * 10;
        float radians = (float) (Math.PI /Math.random());

        return new PositionPart(x,y,radians);
    }
    
    // Create a giant asteroid
    public Asteroid createGiantAsteroid(GameData gameData) {
        Asteroid asteroid = createAsteroidWithLifeAndMovementParts(new Asteroid(AsteroidSize.GIANT_SIZE));
        asteroid.add(randomPosition(gameData));
        return asteroid;
    }

    // Create a large asteroid
    public Asteroid createLargeAsteroid(GameData gameData) {
        Asteroid asteroid =  createAsteroidWithLifeAndMovementParts(new Asteroid(AsteroidSize.LARGE_SIZE));
        asteroid.add(randomPosition(gameData));
        return asteroid;
    }

    // Create a medium asteroid
    public Asteroid createMediumAsteroid(GameData gameData) {
        Asteroid asteroid = createAsteroidWithLifeAndMovementParts(new Asteroid(AsteroidSize.MEDIUM_SIZE));
        asteroid.add(randomPosition(gameData));
        return asteroid;
    }

    // Create a small asteroid
    public Asteroid createSmallAsteroid(GameData gameData) {
        Asteroid asteroid = createAsteroidWithLifeAndMovementParts(new Asteroid(AsteroidSize.SMALL_SIZE));
        asteroid.add(randomPosition(gameData));
        return asteroid;
    }
    
}
