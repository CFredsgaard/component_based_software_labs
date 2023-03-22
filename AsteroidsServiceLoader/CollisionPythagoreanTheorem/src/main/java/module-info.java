import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module CollisionPythagoreanTheorem {
    // TODO: Refactor code to remove the coupling on the entities
    requires Common;
    requires CommonAsteroids;
    requires CommonEnemy;
    //requires dk.sdu.mmmi.cbse.playersystem;
    //requires dk.sdu.mmmi.cbse.asteroidsystem;

    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionPythagoreanTheorem.CollisionManager;
}