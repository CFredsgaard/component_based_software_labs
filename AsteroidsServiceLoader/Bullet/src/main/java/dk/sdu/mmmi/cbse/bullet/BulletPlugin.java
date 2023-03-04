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
