package org.openflexo.http.server;

import org.openflexo.foundation.DefaultFlexoEditor;
import org.openflexo.foundation.DefaultFlexoServiceManager;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.logging.FlexoLoggingManager;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by charlie on 17/01/2017.
 */
public class OpenFlexoServer {

	public static class Options {

		public HttpService.Options serverOptions = new HttpService.Options();

		public boolean verbose = false;

		public boolean devMode = false;

		public String resourceCenterUri = "http//org.openflexo.server/resourceCenter";

	}

	public static FlexoServiceManager createServiceManager(Options options) {
		FlexoServiceManager manager = new DefaultFlexoServiceManager(null, options.devMode) {
			@Override
			protected FlexoEditor createApplicationEditor() {
				return new DefaultFlexoEditor(null, this);
			}

			@Override
			protected FlexoResourceCenterService createResourceCenterService() {
				FlexoResourceCenterService resourceCenterService = super.createResourceCenterService();
				resourceCenterService.addToResourceCenters(new DirectoryResourceCenter(new File("./rc"), options.resourceCenterUri, resourceCenterService));
				return resourceCenterService;
			}
		};

		TechnologyAdapterService technologyAdapterService = manager.getTechnologyAdapterService();
		technologyAdapterService.activateTechnologyAdapter(technologyAdapterService.getTechnologyAdapter(FMLTechnologyAdapter.class));
		technologyAdapterService.activateTechnologyAdapter(technologyAdapterService.getTechnologyAdapter(FMLRTTechnologyAdapter.class));
		return manager;
	}

	public static void main(String[] args) throws IOException {
		Options options = new Options();

		FlexoLoggingManager.initialize(-1, true, null, options.verbose ? Level.INFO : Level.WARNING, null);

		FlexoServiceManager manager = createServiceManager(options);
		manager.registerService(new HttpService(options.serverOptions));
	}
}