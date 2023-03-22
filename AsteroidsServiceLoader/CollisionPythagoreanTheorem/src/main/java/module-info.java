import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module dk.sdu.mmmi.cbse.collisionPythagoreanTheorem {
    // TODO: Refactor code to remove the coupling on the entities
    requires dk.sdu.mmmi.cbse.common;
    requires dk.sdu.mmmi.cbse.common.asteroids;
    requires dk.sdu.mmmi.cbse.common.enemy;
    requires dk.sdu.mmmi.cbse.playersystem;
    requires dk.sdu.mmmi.cbse.asteroidsystem;

    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionPythagoreanTheorem.CollisionManager;
}