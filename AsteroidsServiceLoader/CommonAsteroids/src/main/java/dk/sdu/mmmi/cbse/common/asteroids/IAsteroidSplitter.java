package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 *
 * @author corfixen
 *
 * This interface is not being used
 */
public interface IAsteroidSplitter {
    void createSplitAsteroid(Entity entity, GameData gameData, World world);
}
