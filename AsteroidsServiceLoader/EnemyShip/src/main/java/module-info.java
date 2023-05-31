import dk.sdu.mmmi.cbse.common.bullet.IBulletSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module EnemyShip {
    requires Common;
    requires CommonEnemy;
    requires CommonBullet;

    provides IGamePluginService with dk.sdu.mmmi.cbse.enemyshipsystem.EnemyShipPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.enemyshipsystem.EnemyShipMovementSystem;

    uses IBulletSPI;
}