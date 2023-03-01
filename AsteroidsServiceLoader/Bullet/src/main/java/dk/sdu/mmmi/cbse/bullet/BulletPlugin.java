package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.ArrayList;

public class BulletPlugin implements IGamePluginService {

    //List of all bullets in the game
    ArrayList<Entity> playerBullets;
    ArrayList<Entity> enemyBullets;
    public BulletPlugin(){}

    /**
     * Start the game
     *
     * @param gameData information about the game environment
     * @param world    contains the entities in the game
     */
    @Override
    public void start(GameData gameData, World world) {
        // There are no bullets at the beginning of the game, these need to be created during the game
    }

    /*
    protected Entity createBullet(Entity ship) {
        // Create moving part variables
        float deceleration = 0;
        float acceleration = 1000000;
        float maxSpeed = 400;
        float rotationSpeed = 0;

        // Get the position part for the spaceship
        PositionPart shipPosition = ship.getPart(PositionPart.class);

        // Define the offset distance from the spaceship to the bullet
        int offsetDistance = 15;
        // Calculate the x and y components of the offset vector
        float offsetX = (float) (offsetDistance * Math.cos(shipPosition.getRadians()));
        float offsetY = (float) (offsetDistance * Math.sin(shipPosition.getRadians()));

        // Create position part variables for the bullet form the play ships position and the offset
        float bulletX = shipPosition.getX() + offsetX;
        float bulletY = shipPosition.getY() + offsetY;
        float radians = shipPosition.getRadians();

        // Create life part variables for the bullet
        int life = 1;
        float expiration = 10;

        // Create the bullet and add the parts
        Entity bullet = new Bullet();
        bullet.add(new MovingPart(deceleration, acceleration, maxSpeed, rotationSpeed));
        bullet.add(new PositionPart(bulletX, bulletY, radians));
        bullet.add(new LifePart(life, expiration));

        // Give the bullet a radius, used for determining collision
        bullet.setRadius(3);

        // Return the bullet
        return bullet;
    }

     */ //TODO: CREATE BULLET

    /**
     * Stops the game
     *
     * @param gameData information about the game environment
     * @param world    contains the entities in the game
     */
    @Override
    public void stop(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
            world.removeEntity(bullet);
        }
    }
}
