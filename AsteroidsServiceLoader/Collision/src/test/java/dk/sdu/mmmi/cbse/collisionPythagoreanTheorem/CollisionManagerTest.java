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

    final PositionPart positionPart_A = new PositionPart(100, 100, 10);
    final PositionPart positionPart_B = new PositionPart(150, 150, 10);

    /**
     * Runs before every test
     * Responsible for instantiating the collisionManager to be tested
     * Makes world return empty lists for the different entity types
     */
    @BeforeEach
    void setUp() {
        collisionManager = new CollisionManager();

        when(world.getEntities(Asteroid.class)).thenReturn(asteroidsList);
        when(world.getEntities(Bullet.class)).thenReturn(bulletList);
        when(world.getEntities(Enemy.class)).thenReturn(enemyList);
        when(world.getEntities(Player.class)).thenReturn(playerList);
    }

    /**
     * Runs after every test
     * Responsible for resetting the world and the lists
     */
    @AfterEach
    void tearDown() {
        reset(world);
        asteroidsList.clear();
        playerList.clear();
        bulletList.clear();
        enemyList.clear();
    }

    public Entity mockPlayerWithPositionAndLife(PositionPart positionPart) {
        Entity player = mock(Player.class);
        when(player.getPart(PositionPart.class)).thenReturn(positionPart);
        lenient().when(player.getPart(LifePart.class)).thenReturn(new LifePart(10));
        when(player.getRadius()).thenReturn(5f);
        playerList.add(player);
        return player;
    }

    public Entity mockEnemyWithPositionAndLife(PositionPart positionPart) {
        Entity enemy = mock(Enemy.class);
        when(enemy.getPart(PositionPart.class)).thenReturn(positionPart);
        lenient().when(enemy.getPart(LifePart.class)).thenReturn(new LifePart(10));
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
        lenient().when(asteroid.getPart(LifePart.class)).thenReturn(new LifePart(10));
        when(asteroid.getRadius()).thenReturn(10f);
        asteroidsList.add(asteroid);
        return asteroid;
    }

    @Test
    void bullet_asteroid_same_position_colliding() {
        // Player needs to be present for the game to process other collisions
        mockPlayerWithPositionAndLife(positionPart_B);

        // Entities that are colliding
        Entity bullet = mockBulletWithPosition(positionPart_A);
        Entity asteroid = mockAsteroidWithPositionAndLife(positionPart_A);

        collisionManager.process(gameData, world);

        // Check method to remove bullet has been called
        verify(world, times(1)).removeEntity(bullet);

        // Check asteroid accesses its LifePart
        verify(asteroid, times(1)).getPart(LifePart.class);
    }

    @Test
    void bullet_player_same_position_colliding() {
        Entity bullet = mockBulletWithPosition(positionPart_A);
        Entity player = mockPlayerWithPositionAndLife(positionPart_A);

        collisionManager.process(gameData, world);

        verify(world, times(1)).removeEntity(bullet);
        verify(player, times(1)).getPart(LifePart.class);
    }

    @Test
    void bullet_enemy_same_position_colliding() {
        mockPlayerWithPositionAndLife(positionPart_A);

        // Create Bullet and Enemy at same position
        Entity bullet = mockBulletWithPosition(positionPart_B);
        Entity enemy = mockEnemyWithPositionAndLife(positionPart_B);

        collisionManager.process(gameData, world);

        verify(world, times(1)).removeEntity(bullet);
        verify(enemy, times(1)).getPart(LifePart.class);
    }

    @Test
    void bullet_bullet_same_position_not_colliding() {
        mockPlayerWithPositionAndLife(positionPart_A);

        // Create two bullets with the same position
        Entity bullet_1 = mockBulletWithPosition(positionPart_B);
        Entity bullet_2 = mockBulletWithPosition(positionPart_B);

        collisionManager.process(gameData, world);

        verify(world, times(0)).removeEntity(bullet_1);
        verify(world, times(0)).removeEntity(bullet_2);
    }

    /**
     * Check that player and asteroid can collide
     */
    @Test
    void player_asteroid_at_same_position_colliding(){
        Entity player = mockPlayerWithPositionAndLife(positionPart_A);
        Entity asteroid = mockAsteroidWithPositionAndLife(positionPart_A);

        collisionManager.process(gameData, world);

        verify(world, times(1)).removeEntity(player);
        verify(asteroid, times(1)).getPart(LifePart.class);
    }

    /**
     * Test that the collision between player and enemy is detected
     */
    @Test
    void player_enemy_at_same_position_colliding() {
        Entity player = mockPlayerWithPositionAndLife(positionPart_A);
        Entity enemy = mockEnemyWithPositionAndLife(positionPart_A);

        collisionManager.process(gameData, world);

        // verify that enemy and player are removed when they collide
        verify(world, times(1)).removeEntity(enemy);
        verify(world, times(1)).removeEntity(player);
    }

    /**
     * Check that enemy and player does not collide when they are at different positions
     */
    @Test
    void player_enemy_at_different_positions_not_colliding() {
        Entity player = mockPlayerWithPositionAndLife(positionPart_A);
        Entity enemy = mockEnemyWithPositionAndLife(positionPart_B);

        collisionManager.process(gameData, world);
        verify(world, times(0)).removeEntity(player);
        verify(world, times(0)).removeEntity(enemy);
    }

    @Test
    void enemy_asteroid_at_same_position_not_colliding() {
        mockPlayerWithPositionAndLife(positionPart_A);

        Entity enemy = mockEnemyWithPositionAndLife(positionPart_B);
        Entity asteroid = mockAsteroidWithPositionAndLife(positionPart_B);

        collisionManager.process(gameData,world);

        verify(world,times(0)).removeEntity(enemy);
        verify(world, times(0)).removeEntity(asteroid);
    }

    @Test
    void enemy_enemy_at_same_position_not_colliding() {
        mockPlayerWithPositionAndLife(positionPart_A);

        Entity enemy_1 = mockEnemyWithPositionAndLife(positionPart_B);
        Entity enemy_2 = mockEnemyWithPositionAndLife(positionPart_B);

        collisionManager.process(gameData, world);

        verify(world, times(0)).removeEntity(enemy_1);
        verify(world, times(0)).removeEntity(enemy_2);
    }

    @Test
    void asteroid_asteroid_at_same_position_not_colliding() {
        mockPlayerWithPositionAndLife(positionPart_A);

        Entity asteroid_1 = mockAsteroidWithPositionAndLife(positionPart_B);
        Entity asteroid_2 = mockAsteroidWithPositionAndLife(positionPart_B);

        collisionManager.process(gameData, world);

        verify(world, times(0)).removeEntity(asteroid_1);
        verify(world, times(0)).removeEntity(asteroid_2);
    }

    @Test
    void no_collision_when_sum_of_entities_radii_equals_distance_between_entities_center() {
        Entity player = mockPlayerWithPositionAndLife(new PositionPart(10, 10, 0));
        Entity enemy = mockEnemyWithPositionAndLife(new PositionPart(30, 10, 0));

        when(player.getRadius()).thenReturn(10f);
        when(enemy.getRadius()).thenReturn(10f);

        collisionManager.process(gameData, world);

        verify(world, times(0)).removeEntity(player);
        verify(world, times(0)).removeEntity(enemy);
    }

    /**
     * Checking that no collision is happening when the entities are exactly not touching.
     * Here distance between entities center is 1 pixel larger than the sum of the entities radii.
     */
    @Test
    void no_collision_when_sum_of_entities_radii_is_one_pixel_smaller_than_distance_between_entities_center() {
        Entity player = mockPlayerWithPositionAndLife(new PositionPart(10, 10, 0));
        Entity enemy = mockEnemyWithPositionAndLife(new PositionPart(31, 10, 0));

        when(player.getRadius()).thenReturn(10f);
        when(enemy.getRadius()).thenReturn(10f);

        collisionManager.process(gameData, world);

        verify(world, times(0)).removeEntity(player);
        verify(world, times(0)).removeEntity(enemy);
    }

    /**
     * Checking that collision is happening when the entities are exactly 1 pixel overlapping.
     * Here distance between entities center is 1 pixel smaller than the sum of the entities radii.
     */
    @Test
    void collision_when_sum_of_entities_radii_is_one_pixel_larger_than_distance_between_entities_center() {
        Entity player = mockPlayerWithPositionAndLife(new PositionPart(10, 10, 0));
        Entity enemy = mockEnemyWithPositionAndLife(new PositionPart(29, 10, 0));

        when(player.getRadius()).thenReturn(10f);
        when(enemy.getRadius()).thenReturn(10f);

        collisionManager.process(gameData, world);

        verify(world, times(1)).removeEntity(player);
        verify(world, times(1)).removeEntity(enemy);
    }



    /**
     * Check that the CollisionManager does not crash when there is no player in the game
     */
    @Test
    void game_runs_without_player_present() {
        collisionManager.process(gameData, world);

        // Check that player list is empty
        assertTrue(playerList.isEmpty());
    }

    /**
     * Check that the CollisionManager does not crash if player is the only entity in the game
     */
    @Test
    void game_runs_with_only_player_present() {
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