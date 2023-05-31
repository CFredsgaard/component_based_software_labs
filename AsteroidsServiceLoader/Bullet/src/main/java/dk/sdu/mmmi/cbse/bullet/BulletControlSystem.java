package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.IBulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.TimerPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, IBulletSPI {
    /**
     * Processes the entities
     *
     * @param gameData information about the game environment
     * @param world    contains the entities in the game
     */
    @Override
    public void process(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {

            PositionPart positionPart = bullet.getPart(PositionPart.class);
            MovingPart movingPart = bullet.getPart(MovingPart.class);
            TimerPart timerPart = bullet.getPart(TimerPart.class);
            movingPart.setUp(true);
            if (timerPart.getExpiration() < 0) {
                world.removeEntity(bullet);
            }
            timerPart.process(gameData, bullet);
            movingPart.process(gameData, bullet);
            positionPart.process(gameData, bullet);

            updateShape(bullet);
        }
    }

    private void updateShape(Entity bullet) {
        // Get the correct sized array for the bullet's x and y coordinates
        float[] shapeX = bullet.getShapeX();
        float[] shapeY = bullet.getShapeY();

        // Get the bullet's position part
        PositionPart positionPart = bullet.getPart(PositionPart.class);
        // Get x and y coordinates, and radians
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        shapeX[0] = (float) (x + Math.cos(radians));
        shapeY[0] = (float) (y + Math.sin(radians));

        float squareSize = 5.0f; // size of the bullet
        float angle = (float) (radians + Math.PI / 4); // 45 degrees in radians, used to rotate the bullet 45 degrees
        shapeX[1] = shapeX[0] + (float) (Math.cos(angle + Math.PI / 2) * squareSize);
        shapeY[1] = shapeY[0] + (float) (Math.sin(angle + Math.PI / 2) * squareSize);

        shapeX[2] = shapeX[1] + (float) (Math.cos(angle + Math.PI) * squareSize);
        shapeY[2] = shapeY[1] + (float) (Math.sin(angle + Math.PI) * squareSize);

        shapeX[3] = shapeX[2] + (float) (Math.cos(angle - Math.PI / 2) * squareSize);
        shapeY[3] = shapeY[2] + (float) (Math.sin(angle - Math.PI / 2) * squareSize);

        // Set the new shape
        bullet.setShapeX(shapeX);
        bullet.setShapeY(shapeY);
    }

    @Override
    public Entity createBullet(Entity ship) {
        // Get the position part for the spaceship
        PositionPart shipPosition = ship.getPart(PositionPart.class);

        // Create moving part variables
        float deceleration = 0;
        float acceleration = 1000000;
        float maxSpeed = 400;
        float rotationSpeed = 0;

        // Define the offset distance from the spaceship to the bullet
        int offsetDistance = 20;
        // Calculate the x and y components of the offset vector
        float offsetX = (float) (offsetDistance * Math.cos(shipPosition.getRadians()));
        float offsetY = (float) (offsetDistance * Math.sin(shipPosition.getRadians()));

        // Create position part variables for the bullet form the play ships position and the offset
        float bulletX = shipPosition.getX() + offsetX;
        float bulletY = shipPosition.getY() + offsetY;
        float radians = shipPosition.getRadians();

        // Create life part variable for the bullet
        int life = 1;

        // Create the bullet and add the parts
        Entity bullet = new Bullet();
        bullet.add(new MovingPart(deceleration, acceleration, maxSpeed, rotationSpeed));
        bullet.add(new PositionPart(bulletX, bulletY, radians));
        bullet.add(new LifePart(life));
        bullet.add(new TimerPart(1));

        // Give the bullet a radius, used for determining collision
        bullet.setRadius(3);

        // Return the bullet
        return bullet;
    }
}
