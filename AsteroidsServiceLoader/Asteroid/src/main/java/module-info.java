import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module dk.sdu.mmmi.cbse.asteroidsystem {
    requires dk.sdu.mmmi.cbse.common;
    requires dk.sdu.mmmi.cbse.common.asteroids;

    provides IGamePluginService with dk.sdu.mmmi.cbse.asteroidsystem.AsteroidPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.asteroidsystem.AsteroidControlSystem;
}