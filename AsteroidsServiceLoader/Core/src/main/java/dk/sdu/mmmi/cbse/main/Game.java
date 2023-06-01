package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
//import dk.sdu.mmmi.cbse.common.util.SPILocator;
import dk.sdu.mmmi.cbse.managers.GameInputProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

public class Game
        implements ApplicationListener {

    private static OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    private final GameData gameData = new GameData();
    private World world = new World();

    // Before using Spring framework and ModuleConfig
    //private List<IEntityProcessingService> entityProcessors = new ArrayList<>();
    //private List<IPostEntityProcessingService> postEntityProcessors = new ArrayList<>();

    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postEntityProcessors;

    // Used inside ModuleConfig for Spring framework to work
    public Game (List<IGamePluginService> gamePluginServices, List<IEntityProcessingService> entityProcessors, List<IPostEntityProcessingService> postEntityProcessors) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessors = entityProcessors;
        this.postEntityProcessors = postEntityProcessors;
    }

    @Override
    public void create() {

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        shapeRenderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(
                new GameInputProcessor(gameData)
        );

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(gameData, world);
        }
    }

    @Override
    public void render() {

        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();

        draw();

        gameData.getKeys().update();
    }

    private void update() {
        // Update
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        // Post update
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            shapeRenderer.setColor(0, 1, 0, 1);

            // Gives them thick lines by filling in the rectangles
            // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            float[] shapeX = entity.getShapeX();
            float[] shapeY = entity.getShapeY();

            for (int i = 0, j = shapeX.length - 1;
                    i < shapeX.length;
                    j = i++) {

                shapeRenderer.rectLine(shapeX[i], shapeY[i], shapeX[j], shapeY[j], 5);
                //shapeRenderer.line(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);
            }

            shapeRenderer.end();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        //return SPILocator.locateAll(IGamePluginService.class); // Before changing to JPMS
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        //return SPILocator.locateAll(IEntityProcessingService.class); // Before changing to JPMS
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
    
    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        //return SPILocator.locateAll(IPostEntityProcessingService.class); // Before changing to JPMS
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
