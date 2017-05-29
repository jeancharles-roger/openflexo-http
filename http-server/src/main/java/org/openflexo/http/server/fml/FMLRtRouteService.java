/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
 *
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either
 * version 1.1 of the License, or any later version ), which is available at
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 *
 * You can redistribute it and/or modify under the terms of either of these licenses
 *
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 *
 * See http://www.openflexo.org/license.html for details.
 *
 *
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 *
 */

package org.openflexo.http.server.fml;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.fml.rt.ViewLibrary;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.http.server.HttpService;
import org.openflexo.http.server.RouteService;
import org.openflexo.http.server.core.ta.TechnologyAdapterRouteComplement;
import org.openflexo.http.server.util.JsonUtils;

/**
 * Created by charlie on 08/02/2017.
 */
public class FMLRtRouteService implements TechnologyAdapterRouteComplement<FMLRTTechnologyAdapter> {

	private FMLRTTechnologyAdapter technologyAdapter;

	@Override
	public Class<FMLRTTechnologyAdapter> getTechnologyAdapterClass() {
		return FMLRTTechnologyAdapter.class;
	}

	@Override
	public void initialize(HttpService service, FMLRTTechnologyAdapter technologyAdapter) throws Exception {
		this.technologyAdapter = technologyAdapter;
	}

	public void addRoutes(Vertx vertx, Router router) {
		router.get("/library").produces(RouteService.JSON).handler(this::serveLibraryList);
	}


	private void serveLibraryList(RoutingContext context) {
		try {
			JsonArray result = new JsonArray();
			for (ViewLibrary served : technologyAdapter.getViewLibraries()) {
				Object vpJson = JsonUtils.getRepositoryDescription(served);
				result.add(vpJson);
			}
			context.response().end(result.encodePrettily());

		} catch (Exception e) {
			context.fail(e);
		}
	}

	@Override
	public Map<Class<? extends FlexoResource<?>>, String> getResourceRoots() {
		Map<Class<? extends FlexoResource<?>>, String> result = new HashMap<>();
		return result;
	}


	@Override
	public void complementRoot(String url, JsonObject object) {
		object.put("librariesUrl", url + "/libraries");
	}

	@Override
	public void complementResource(TechnologyAdapterResource resource, JsonObject object) {

	}
}
