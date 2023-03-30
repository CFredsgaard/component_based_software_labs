package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {

		/**** Main before using Spring framework

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Asteroids");
		config.setWindowSizeLimits(1000,600,1000,600);

		new Lwjgl3Application(new Game(), config);

		*/

		// Using Spring framework
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ModuleConfig.class);

		// Print the names of all the beans
		for (String beanName : annotationConfigApplicationContext.getBeanDefinitionNames()) {
			System.out.println("Bean Name: " + beanName);
		}

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Asteroids");
		config.setWindowSizeLimits(1000,600,1000,600);

		// Change new Game()
		new Lwjgl3Application(annotationConfigApplicationContext.getBean(Game.class), config);

	}

}
