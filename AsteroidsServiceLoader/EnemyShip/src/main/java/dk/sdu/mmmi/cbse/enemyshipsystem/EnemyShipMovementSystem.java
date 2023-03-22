package dk.sdu.mmmi.cbse.enemyshipsystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
//import dk.sdu.mmmi.cbse.common.util.SPILocator;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyShipMovementSystem implements IEntityProcessingService {


    /**
     * Processes the entities
     *
     * @param gameData information about the game environment
     * @param world    contains the entities in the game
     */
    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemyShip : world.getEntities(Enemy.class)) {
            // Get the movement part for the enemy ship
            MovingPart movingPart = enemyShip.getPart(MovingPart.class);

            Random random = new Random();

            // Create bullets from enemy ships
            if (random.nextInt(1000) < 10) {
                for (BulletSPI bullet : getBulletSPIs()) {
                    world.addEntity(bullet.createBullet(enemyShip, gameData));
                }
            }

            // Makes the movement turn more, because "Up" is only true 20% of the time
            boolean shouldMoveUp = random.nextDouble() < 0.8;

            movingPart.setUp(shouldMoveUp);
            movingPart.setRight(random.nextBoolean());
            movingPart.setLeft(random.nextBoolean());

            movingPart.process(gameData, enemyShip);

            updateShape(enemyShip);
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    // Update the position of the ship
    private void updateShape(Entity enemyShip) {
        // Get the correct sized array for the enemy ships x and y coordinates
        float[] shapeX = enemyShip.getShapeX();
        float[] shapeY = enemyShip.getShapeY();

        // Get the enemy's position part
        PositionPart positionPart = enemyShip.getPart(PositionPart.class);
        // Get x and y coordinates, and radians
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        // Set the position of the different corners in the enemy ship
        shapeX[0] = (float) (x + Math.cos(radians) * 10);
        shapeY[0] = (float) (y + Math.sin(radians) * 10);

        shapeX[1] = (float) (x + Math.cos(radians - 4 * Math.PI / 5) * 10);
        shapeY[1] = (float) (y + Math.sin(radians - 4 * Math.PI / 5) * 10);

        shapeX[2] = (float) (x + Math.cos(radians + Math.PI) * 7);
        shapeY[2] = (float) (y + Math.sin(radians + Math.PI) * 7);

        shapeX[3] = (float) (x + Math.cos(radians + 4 * Math.PI / 5) * 10);
        shapeY[3] = (float) (y + Math.sin(radians + 4 * Math.PI / 5) * 10);

        // Set the new shape
        enemyShip.setShapeX(shapeX);
        enemyShip.setShapeY(shapeY);
    }
}
