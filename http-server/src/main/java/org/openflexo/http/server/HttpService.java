package org.openflexo.http.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openflexo.connie.DataBinding;
import org.openflexo.http.server.connie.ConnieHandler;
import org.openflexo.http.server.json.JsonSerializer;

/**
 * HTTP Service for OpenFlexo
 */
public class HttpService {

	private static Logger logger = Logger.getLogger(HttpService.class.getPackage().getName());

	private final int port;
	private final String host;

	private final Vertx vertx = Vertx.vertx();
	private final HttpServerOptions serverOptions;

	private HttpServer server = null;

	private final List<ObjectFinder> finders = new ArrayList<>();

	private long lastHeapPrint = -1L;

	public static class Options {
		public int port = 8080;

		public String host = "localhost";

	}

	public HttpService(Options options) {
		this.port = options.port;
		this.host = options.host;
		serverOptions = new HttpServerOptions();
		serverOptions.setCompressionSupported(true);
	}

	private void printHeapHandler(RoutingContext context) {
		long nanoTime = System.nanoTime();
		if ((nanoTime - lastHeapPrint) > (20 * 1_000_000_000L)) {
			lastHeapPrint = nanoTime;
			long totalMemory = Runtime.getRuntime().totalMemory();
			logger.info("[HEAP] Total used memory: " + totalMemory + " bytes (" + (totalMemory / 1024 / 1024) + " mb)");
		}

		context.next();
	}

	@SuppressWarnings("unchecked")
	public void initialize() {
		DataBinding.setDefaultCachingStrategy(DataBinding.CachingStrategy.NO_CACHING);

		Router router = Router.router(vertx);

		// gets REST services
		ServiceLoader<RouteService> restServices = ServiceLoader.load(RouteService.class);

		// initializes REST services
		List<RouteService<Object>> initializedServices = new ArrayList<>();
		for (RouteService<Object> routeService : restServices) {
			String name = routeService.getClass().getName();
			try {
				logger.log(Level.INFO, "Initializing REST service " + name);
				routeService.initialize(this, null);
				initializedServices.add(routeService);

			} catch (Exception e) {
				logger.log(Level.SEVERE, "Unable to initialize REST service " + name, e);
			}
		}

		// adds routes for initialized service
		for (RouteService routeService : initializedServices) {
			routeService.addRoutes(vertx, router);
		}

		router.get().handler(LoggerHandler.create());
		router.get().handler(this::printHeapHandler);
		router.get().handler(CorsHandler.create(".*"));

		// adds server.log content with no caching
		StaticHandler logsFileHandler = StaticHandler.create("logs");
		logsFileHandler.setIndexPage("server.log");
		logsFileHandler.setAlwaysAsyncFS(true);
		logsFileHandler.setCacheEntryTimeout(1);
		logsFileHandler.setCachingEnabled(false);
		router.get("/logs").handler(logsFileHandler);

		// adds static content
		StaticHandler staticHandler = StaticHandler.create();
		router.get("/*").handler(staticHandler);

		server = vertx.createHttpServer(serverOptions);
		server.requestHandler(router::accept);
		server.websocketHandler(new ConnieHandler(new ObjectFinder() {
			@Override
			public <T> T find(Class<T> type, String resourceId, String objectId) {
				return null;
			}
		}, new JsonSerializer()));

		logger.info("Starting HTTP Server on " + host + ":" + port);
		server.listen(port, host);
	}


	public void stop() {
		server.close();
	}

}
