package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SizePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.Random;

import static dk.sdu.mmmi.cbse.asteroidsystem.AsteroidLife.*;
import static dk.sdu.mmmi.cbse.asteroidsystem.AsteroidSize.*;

public class AsteroidControlSystem implements IEntityProcessingService, IAsteroidSplitter {

    Random random = new Random();
    AsteroidFactory asteroidFactory = new AsteroidFactory();
    @Override
    public void process(GameData gameData, World world) {
        // Get all the asteroids in the game
        ArrayList<Entity> asteroids = new ArrayList<>(world.getEntities(Asteroid.class));
        // Loop over all the asteroids
        for(Entity asteroid : asteroids) {

            handleAsteroidLifeStatus(asteroid, gameData, world);

            handleAsteroidMovement(asteroid, gameData);

            updateShape(asteroid);
        }
    }

    @Override
    public void createSplitAsteroid(Entity parentAsteroid, GameData gameData, World world) {
        // Parent Asteroid Position Data
        PositionPart parentAsteroidPosition = parentAsteroid.getPart(PositionPart.class);
        float parentX = parentAsteroidPosition.getX();
        float parentY = parentAsteroidPosition.getY();
        float parentRadians = parentAsteroidPosition.getRadians();

        // Parent Asteroid Size Data, get it from part and turn it into enum value
        SizePart parentAsteroidSize = parentAsteroid.getPart(SizePart.class);
        AsteroidSize size = AsteroidSize.valueOf(parentAsteroidSize.getSize());


        // Create the child asteroids and give them the parents position
        Entity childAsteroid1 = null;
        Entity childAsteroid2 = null;

        // Create two large asteroids
        if (size.equals(GIANT_SIZE)) {
            childAsteroid1 = asteroidFactory.createLargeAsteroid(gameData);
            childAsteroid2 = asteroidFactory.createLargeAsteroid(gameData);
        // Create two medium asteroids
        } else if (size.equals(LARGE_SIZE)) {
            childAsteroid1 = asteroidFactory.createMediumAsteroid(gameData);
            childAsteroid2 = asteroidFactory.createMediumAsteroid(gameData);
        // Create two medium asteroids
        } else if (size.equals(MEDIUM_SIZE)) {
            childAsteroid1 = asteroidFactory.createSmallAsteroid(gameData);
            childAsteroid2 = asteroidFactory.createSmallAsteroid(gameData);
        } else {
            return;
        }

        childAsteroid1.add(new PositionPart(parentX, parentY, parentRadians));
        childAsteroid2.add(new PositionPart(parentX, parentY, -parentRadians));

        // Add new child asteroids to the world
        world.addEntity(childAsteroid1);
        world.addEntity(childAsteroid2);

        // Remove the old asteroid
        world.removeEntity(parentAsteroid);
    }


    private void handleAsteroidLifeStatus(Entity asteroid, GameData gameData, World world) {
        // Get Life info
        LifePart asteroidLifePart = asteroid.getPart(LifePart.class);
        int asteroidLife = asteroidLifePart.getLife();

        // Get Size info
        SizePart asteroidSizePart = asteroid.getPart(SizePart.class);
        AsteroidSize size = AsteroidSize.valueOf(asteroidSizePart.getSize());

        if (asteroidLife <= 0) {
            world.removeEntity(asteroid);
        }
        else if (size == GIANT_SIZE && asteroidLife <= LARGE_LIFE.getLife()) {
            createSplitAsteroid(asteroid, gameData, world);
        } else if (size == LARGE_SIZE && asteroidLife <= MEDIUM_LIFE.getLife()) {
            createSplitAsteroid(asteroid, gameData, world);
        } else if (size == MEDIUM_SIZE && asteroidLife <= SMALL_LIFE.getLife()) {
            createSplitAsteroid(asteroid, gameData, world);
        }
    }

    private void handleAsteroidMovement(Entity asteroid, GameData gameData) {
        // Get the entity's moving part
        MovingPart movingPart = asteroid.getPart(MovingPart.class);

        // Makes the movement turn more, because "Up" is only true 20% of the time
        boolean shouldMoveUp = random.nextDouble() < 0.8;

        movingPart.setUp(shouldMoveUp);
        movingPart.setRight(random.nextBoolean());
        movingPart.setLeft(random.nextBoolean());

        movingPart.process(gameData, asteroid);
    }

    private void updateShape(Entity asteroid) {
        // Get the position part of the asteroid
        PositionPart positionPart = asteroid.getPart(PositionPart.class);

        // Get x and y coordinates, and radians
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        int corners = getNumberOfCorners(asteroid);

        // Create to float arrays representing x and y coordinates with the correct number of corners
        float[] shapeX = new float[corners];
        float[] shapeY = new float[corners];

        // Angle of the corner positions, starting from 0.
        float angle = 0;

        // Set the x and y values for all corners
        for (int i = 0; i < corners; i++) {
            shapeX[i] = x + (float) Math.cos(angle + radians) * (2 * corners);
            shapeY[i] = y + (float) Math.sin(angle + radians) * (2 * corners);
            angle += 2 * 3.1415f / corners;
        }

        // Set the shape of the asteroid to the new shapeX and shapeY arrays
        asteroid.setShapeX(shapeX);
        asteroid.setShapeY(shapeY);
    }

    private int getNumberOfCorners(Entity asteroid) {
        // Find the size of the asteroid
        SizePart asteroidSizePart = asteroid.getPart(SizePart.class);
        AsteroidSize size = AsteroidSize.valueOf(asteroidSizePart.getSize());

        // Size determines number of corners
        if (size.equals(GIANT_SIZE)) {
            return 15;
        }
        if (size.equals(LARGE_SIZE)) {
            return 8;
        }
        if (size.equals(MEDIUM_SIZE)) {
            return 6;
        }
        return 4;
    }
}
