import dk.sdu.mmmi.cbse.common.bullet.IBulletSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module Bullet {
    requires Common;
    requires CommonBullet;

    provides IGamePluginService with dk.sdu.mmmi.cbse.bullet.BulletPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.bullet.BulletControlSystem;
    provides IBulletSPI with dk.sdu.mmmi.cbse.bullet.BulletControlSystem;
}