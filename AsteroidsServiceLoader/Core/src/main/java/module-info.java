module Core {
    requires Common;
    requires CommonAsteroids;
    requires CommonEnemy;
    requires CommonPlayer;
    requires CommonBullet;
    requires java.desktop;
    requires com.badlogic.gdx;
    requires gdx.backend.lwjgl; // This fixed the issue with clean install not finding libgdx package

    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}