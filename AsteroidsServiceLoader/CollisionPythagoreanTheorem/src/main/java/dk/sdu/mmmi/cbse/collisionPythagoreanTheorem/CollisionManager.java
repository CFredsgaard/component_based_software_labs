package dk.sdu.mmmi.cbse.collisionPythagoreanTheorem;


import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.playersystem.Player;

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

        collision(gameData, world);
        //collisionDetection(gameData, world);
    }



    private void collision(GameData gameData, World world) {
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
                System.out.println("ASTEROID COLLISION \n");
                // Remove player ship
                // world.removeEntity(playerShip);
            }
        }

        // PlayerShip and EnemyShip Collision
        for (Entity enemyShip : enemyShips) {
            if (isColliding(playerShip, enemyShip)) {
                System.out.println("Enemy Ship Collision \n");
                // Remove both entities from game
                // world.removeEntity(playerShip);
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

        // If the distance between the two entities is less than the sum of their radii they are colliding
        if (distance < (entityA.getRadius() + entityB.getRadius())) {
            return true;
        }

        return false;
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
     * @param gameData
     * @param world
     */
    private void collisionDetection(GameData gameData, World world) {
        // Get the player ship
        Entity playerShip = getPlayerEntity(world);

        // If the player ship is no longer in the game, return
        if (playerShip == null) {
            return;
        }

        // Get the x,y coordinates of the player ship
        PositionPart playerShipPosition = playerShip.getPart(PositionPart.class);
        float playerX = playerShipPosition.getX();
        float playerY = playerShipPosition.getY();

        // Get all entities in the game
        ArrayList<Entity> enemyEntities = new ArrayList<>(world.getEntities());
        ArrayList<Entity> asteroids = new ArrayList<>(world.getEntities(Asteroid.class));

        // Check for collisions between player and enemyEntities (Asteroids, bullets, and enemy ships)
        handlePlayerCollision(playerShip, playerX, playerY, enemyEntities, world);




        //TODO: Detect collision between player and enemy ship DONE

        //TODO: Detect collision between player and bullets (dont care who shot the bullet)

        //TODO: Detect collision between enemy ship and bullets (MAYBE COMBINED WITH PLAYER SHIP)
    }

    private Entity getPlayerEntity(World world) {
        List<Entity> playerList = new ArrayList<>(world.getEntities(Player.class));

        if (playerList.isEmpty()) {
            return null;
        }

        return playerList.get(0);
    }

    private void handlePlayerCollision(Entity playerShip, float playerX, float playerY, ArrayList<Entity> enemyEntities, World world){
        // Enemy entity position uninitialized
        PositionPart enemyPosition;
        float enemyX, enemyY;

        // Distance uninitialized, used to hold distance between two entities
        double distance;

        for (Entity enemy : enemyEntities) {
            enemyPosition = enemy.getPart(PositionPart.class);
            enemyX = enemyPosition.getX();
            enemyY = enemyPosition.getY();

            // Calculate distance between player ship and asteroid
            distance = Math.sqrt(Math.pow(playerX - enemyX, 2)) + Math.sqrt(Math.pow(playerY - enemyY, 2));

            // If the distance between the two entities is less than the sum of their radii they are colliding
            if (distance < (playerShip.getRadius() + enemy.getRadius())) {

                if (enemy instanceof Enemy) {
                    System.out.println("\n********** Ship collided with enemy ship **********\n ");
                    //world.removeEntity(playerShip);
                    //world.removeEntity(enemy);
                }
                if (enemy instanceof Asteroid) {
                    System.out.println("\n********** Ship collided with asteroid **********\n ");
                    //world.removeEntity(playerShip);
                }
                //System.out.println("\n********** SHIT **********\n ");
            }
        }
    }
}
