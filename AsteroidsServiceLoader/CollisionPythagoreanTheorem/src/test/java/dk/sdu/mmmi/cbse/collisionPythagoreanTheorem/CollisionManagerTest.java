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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 *  Test for collision
 */
@ExtendWith(MockitoExtension.class)
class CollisionManagerTest {
    @Mock
    GameData gameData;

    @Mock
    World world;




    // Lists of entities
    List<Entity> asteroidsList = new ArrayList<>();
    List<Entity> bulletList = new ArrayList<>();
    List<Entity> enemyList = new ArrayList<>();
    List<Entity> playerList = new ArrayList<>();

    CollisionManager collisionManager;

    final PositionPart positionPart_1 = new PositionPart(100, 100, 10);
    final PositionPart positionPart_2 = new PositionPart(150, 150, 10);

    @BeforeEach
    void setUp() {
        collisionManager = new CollisionManager();

        when(world.getEntities(Asteroid.class)).thenReturn(asteroidsList);
        when(world.getEntities(Bullet.class)).thenReturn(bulletList);
        when(world.getEntities(Enemy.class)).thenReturn(enemyList);
        when(world.getEntities(Player.class)).thenReturn(playerList);
    }

    @AfterEach
    void tearDown() {
        reset(world);
        asteroidsList.clear();
        playerList.clear();
        bulletList.clear();
        enemyList.clear();
    }

    public Entity mockPlayerWithPosition(PositionPart positionPart) {
        Entity player = mock(Player.class);
        when(player.getPart(PositionPart.class)).thenReturn(positionPart);
        when(player.getRadius()).thenReturn(5f);
        playerList.add(player);
        return player;
    }

    public Entity mockEnemyWithPosition(PositionPart positionPart) {
        Entity enemy = mock(Enemy.class);
        when(enemy.getPart(PositionPart.class)).thenReturn(positionPart);
        when(enemy.getRadius()).thenReturn(5f);
        enemyList.add(enemy);
        return enemy;
    }

    public Entity mockBulletWithPosition(PositionPart positionPart) {
        Entity bullet = mock(Bullet.class);
        when(bullet.getPart(PositionPart.class)).thenReturn(positionPart);
        when(bullet.getRadius()).thenReturn(1f);
        bulletList.add(bullet);
        return bullet;
    }

    public Entity mockAsteroidWithPositionAndLife(PositionPart positionPart) {
        Entity asteroid = mock(Asteroid.class);
        when(asteroid.getPart(PositionPart.class)).thenReturn(positionPart);
        when(asteroid.getPart(LifePart.class)).thenReturn(new LifePart(10));
        when(asteroid.getRadius()).thenReturn(10f);
        asteroidsList.add(asteroid);
        return asteroid;
    }

    @Test
    void bulletAsteroidColliding() {}

    @Test
    void bulletPlayerColliding() {}

    @Test
    void bulletEnemyColliding() {}

    @Test
    void bulletBulletSamePositionNotColliding() {
        mockPlayerWithPosition(positionPart_2);

        // Create two bullets with the same position
        Entity bullet_1 = mockBulletWithPosition(positionPart_1);
        Entity bullet_2 = mockBulletWithPosition(positionPart_1);

        collisionManager.process(gameData, world);

        verify(world, times(0)).removeEntity(bullet_1);
        verify(world, times(0)).removeEntity(bullet_2);
    }

    /**
     * Check that player and asteroid can collide
     */
    @Test
    void playerAsteroidColliding(){
        Entity player = mockPlayerWithPosition(positionPart_1);
        Entity asteroid = mockAsteroidWithPositionAndLife(positionPart_1);

        collisionManager.process(gameData, world);

        verify(world, times(1)).removeEntity(player);
        verify(asteroid, times(1)).getPart(LifePart.class);
    }

    /**
     * Test that the collision between player and enemy is detected
     */
    @Test
    void playerEnemyAtSamePositionColliding() {
        Entity player = mockPlayerWithPosition(positionPart_1);
        Entity enemy = mockEnemyWithPosition(positionPart_1);

        collisionManager.process(gameData, world);

        // verify that enemy and player are removed when they collide
        verify(world, times(1)).removeEntity(enemy);
        verify(world, times(1)).removeEntity(player);
    }

    /**
     * Check that enemy and player does not collide when they are far from each other
     */
    @Test
    void playerEnemyAtDifferentPositionsNotColliding() {
        Entity player = mockPlayerWithPosition(positionPart_1);
        Entity enemy = mockEnemyWithPosition(positionPart_2);

        collisionManager.process(gameData, world);
        verify(world, times(0)).removeEntity(player);
        verify(world, times(0)).removeEntity(enemy);
    }

    /**
     * Check that the collisionManager does not crash when there is no player in the game
     */
    @Test
    void noPlayerPresent() {
        collisionManager.process(gameData, world);

        // Check that player list is empty
        assertTrue(playerList.isEmpty());
    }

    /**
     * Check that the collisionManager does not crash if player is the only entity
     */
    @Test
    void onlyPlayerPresent() {
        Entity player = mock(Player.class);
        playerList.add(player);

        collisionManager.process(gameData, world);

        // assert that player list is not empty
        assertFalse(playerList.isEmpty());

        // check that all other entity lists are empty
        assertTrue(enemyList.isEmpty());
        assertTrue(asteroidsList.isEmpty());
        assertTrue(bulletList.isEmpty());
    }
}