module dk.sdu.mmmi.cbse.main {
    requires dk.sdu.mmmi.cbse.common;
    requires dk.sdu.mmmi.cbse.common.bullet;
    requires dk.sdu.mmmi.cbse.common.asteroids;
    requires dk.sdu.mmmi.cbse.common.enemy;
    requires dk.sdu.mmmi.cbse.common.player;
    requires java.desktop;
    requires com.badlogic.gdx;

    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}