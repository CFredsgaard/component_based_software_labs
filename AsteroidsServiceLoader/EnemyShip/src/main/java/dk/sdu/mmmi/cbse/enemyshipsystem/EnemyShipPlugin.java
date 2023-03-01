package dk.sdu.mmmi.cbse.enemyshipsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.ArrayList;
import java.util.Random;

public class EnemyShipPlugin implements IGamePluginService {

    private ArrayList<Entity> enemyShips = new ArrayList<>();

    public EnemyShipPlugin() {}

    /**
     * Creates the enemy ships using the gameData and adds them to the world
     * @param gameData information about the game environment
     * @param world    contains the entities in the game
     */
    @Override
    public void start(GameData gameData, World world) {
        Entity enemyShip1 = createEnemyShip(gameData);
        Entity enemyShip2 = createEnemyShip(gameData);

        world.addEntity(enemyShip1);
        world.addEntity(enemyShip2);

        enemyShips.add(enemyShip1);
        enemyShips.add(enemyShip2);
    }

    private Entity createEnemyShip(GameData gameData) {
        Random random = new Random();

        // Create moving part variables
        float deceleration = 0;
        float acceleration = random.nextInt(200) + 50;
        float maxSpeed = random.nextInt(200) + 50;
        float rotationSpeed = random.nextInt(10);

        // Create position part variables
        float x = gameData.getDisplayWidth() / (float) (random.nextInt(5)+1); //Random number
        float y = gameData.getDisplayHeight() / 4f;
        float radians = (float) Math.PI / 2;

        // Create Life part variables
        int life = 5;

        // Create the enemy ship and add the parts
        Entity enemyShip = new Enemy();
        enemyShip.add(new MovingPart(deceleration, acceleration, maxSpeed, rotationSpeed));
        enemyShip.add(new PositionPart(x,y,radians));
        enemyShip.add(new LifePart(life));

        // Give the ship a radius
        enemyShip.setRadius(10);

        // return the enemy ship
        return enemyShip;
    }

    /**
     * Removes all enemy ships in the game
     *
     * @param gameData information about the game environment
     * @param world    contains the entities in the game
     */
    @Override
    public void stop(GameData gameData, World world) {
        for (Entity enemyShip : enemyShips) {
            world.removeEntity(enemyShip);
        }
    }
}
