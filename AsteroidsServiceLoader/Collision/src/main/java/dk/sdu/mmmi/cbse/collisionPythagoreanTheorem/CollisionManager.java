package dk.sdu.mmmi.cbse.collisionPythagoreanTheorem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager implements IPostEntityProcessingService {
    /**
     * Processes the entities
     *
     * @param gameData information about the game environment
     * @param world    contains the entities in the game
     */
    @Override
    public void process(GameData gameData, World world) {
        collision(world);
    }


    /***
     * Collision detection based on the pythagorean theorem.
     * Collision is checked between all entities, and handled different depending on what is colliding.
     * Player collisions with:
     *  - Asteroids destroys the player ship
     *  - Bullets reduces the player ship's life
     *  - Enemy ships destroys both ships
     * Asteroid collisions with:
     *  - Other asteroids, nothing happens
     *  - Enemy ships, nothing happens
     *  - Bullets reduces the asteroids' life (with low life splits to two smaller asteroids)
     *  - Player, the player dies
     * Enemy ship collisions with:
     *  - Asteroids nothing happens
     *  - Player ship destroys both ships
     *  - Bullet reduces the enemy ships' life
     *  - Enemy ships nothing happens
     *
     * @param world
     */
    private void collision(World world) {
        List<Entity> playerList = new ArrayList<>(world.getEntities(Player.class));

        // Stop if the Player is no longer in the game, no reason to check for collisions
        if (playerList.isEmpty()) {
            return;
        }

        Entity playerShip = playerList.get(0);

        // Get all the entities into separate lists
        ArrayList<Entity> asteroids = new ArrayList<>(world.getEntities(Asteroid.class));
        ArrayList<Entity> enemyShips = new ArrayList<>(world.getEntities(Enemy.class));
        ArrayList<Entity> bullets = new ArrayList<>(world.getEntities(Bullet.class));

        // Check collision between all relevant entities

        // PlayerShip and Asteroid Collision
        for (Entity asteroid : asteroids) {
            if (isColliding(playerShip, asteroid)) {
                System.out.println("ASTEROID AND PLAYER COLLISION \n");
                // Remove player ship
                world.removeEntity(playerShip);

                LifePart asteroidLife = asteroid.getPart(LifePart.class);
                asteroidLife.setLife(asteroidLife.getLife() - 1);
            }
        }

        // PlayerShip and EnemyShip Collision
        for (Entity enemyShip : enemyShips) {
            if (isColliding(playerShip, enemyShip)) {
                System.out.println("ENEMY AND PLAYER COLLISION\n");
                // Remove both entities from game
                world.removeEntity(playerShip);
                world.removeEntity(enemyShip);
            }
        }

        // Bullet colliding with PlayerShip, EnemyShip or Asteroid
        for (Entity bullet : bullets) {
            // Player Ship and Bullet
            if (isColliding(playerShip, bullet)) {
                // Change playerShip life
                LifePart playerShipLife = playerShip.getPart(LifePart.class);
                playerShipLife.setLife(playerShipLife.getLife() - 1);
                // Remove bullet
                world.removeEntity(bullet);
            }
            // Enemy Ship and Bullet
            for (Entity enemyShip : enemyShips) {
                if (isColliding(enemyShip, bullet)){
                    // Reduce enemyShip life
                    LifePart enemyShipLife = enemyShip.getPart(LifePart.class);
                    enemyShipLife.setLife(enemyShipLife.getLife() - 1);
                    // Remove bullet
                    world.removeEntity(bullet);
                }
            }

            // Asteroid and Bullet
            for (Entity asteroid : asteroids) {
                if (isColliding(asteroid, bullet)) {
                    // Reduce asteroid life
                    LifePart asteroidLife = asteroid.getPart(LifePart.class);
                    asteroidLife.setLife(asteroidLife.getLife() - 1);
                    // Remove bullet
                    world.removeEntity(bullet);
                }
            }
        }
    }

    private boolean isColliding(Entity entityA, Entity entityB) {
        // Get x and y coordinates from the entities positions
        PositionPart aPosition = entityA.getPart(PositionPart.class);
        float aX = aPosition.getX();
        float aY = aPosition.getY();

        PositionPart bPosition = entityB.getPart(PositionPart.class);
        float bX = bPosition.getX();
        float bY = bPosition.getY();

        // Calculate distance between the two entities
        double distance = Math.sqrt(Math.pow(aX - bX, 2)) + Math.sqrt(Math.pow(aY - bY, 2));

        float radiusA = entityA.getRadius();
        float radiusB = entityB.getRadius();

        float radiiSum = entityA.getRadius() + entityB.getRadius();

        // If the distance between the two entities is less than the sum of their radii they are colliding
        return distance < (entityA.getRadius() + entityB.getRadius());
    }
}
