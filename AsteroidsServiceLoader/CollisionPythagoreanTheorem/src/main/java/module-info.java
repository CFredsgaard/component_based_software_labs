import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module CollisionPythagoreanTheorem {
    // TODO: Refactor code to remove the coupling on the entities
    requires Common;
    requires CommonAsteroids;
    requires CommonEnemy;
    requires CommonPlayer;
    requires CommonBullet;

    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionPythagoreanTheorem.CollisionManager;
}