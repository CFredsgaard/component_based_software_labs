package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.ArrayList;

public class AsteroidPlugin implements IGamePluginService {
    // List to hold all the asteroids in the game
    private ArrayList<Entity> asteroids = new ArrayList<>();

    // Add the asteroid to the game
    public AsteroidPlugin(){}

    /***
     * Creates the asteroids using the gameData and adds them to the world
     * @param gameData information about the game environment
     * @param world contains the entities in the game
     */
    @Override
    public void start(GameData gameData, World world) {
        AsteroidFactory asteroidFactory = new AsteroidFactory();

        // Create the asteroids as entities
        Entity giantAsteroid = asteroidFactory.createGiantAsteroid(gameData);
        Entity largeAsteroid = asteroidFactory.createLargeAsteroid(gameData);
        Entity mediumAsteroid = asteroidFactory.createMediumAsteroid(gameData);
        Entity smallAsteroid = asteroidFactory.createSmallAsteroid(gameData);

        // Add asteroids to the world
        world.addEntity(giantAsteroid);
        world.addEntity(largeAsteroid);
        world.addEntity(mediumAsteroid);
        world.addEntity(smallAsteroid);

        // Add entities to arraylist of asteroids
        asteroids.add(giantAsteroid);
        asteroids.add(largeAsteroid);
        asteroids.add(mediumAsteroid);
        asteroids.add(smallAsteroid);
    }

    /***
     * Removes all the asteroids from the world
     * @param gameData information about the game environment
     * @param world contains the entities in the game
     */
    @Override
    public void stop(GameData gameData, World world) {
        for (Entity asteroid : asteroids) {
            world.removeEntity(asteroid);
        }
    }
}
