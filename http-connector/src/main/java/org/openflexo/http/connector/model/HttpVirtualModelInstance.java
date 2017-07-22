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

package org.openflexo.http.connector.model;

import java.util.logging.Logger;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.AbstractVirtualModelInstanceResource;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.http.connector.HttpTechnologyAdapter;
import org.openflexo.http.connector.model.HttpVirtualModelInstance.HttpVirtualModelInstanceImpl;
import org.openflexo.http.connector.rm.AccessPointResource;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.Setter;

/**
 * VirtualModel instance that represents a distant object set through an AccessPoint
 */
@ModelEntity(isAbstract = true)
@ImplementationClass(HttpVirtualModelInstanceImpl.class)
@Imports(@Import(HttpFlexoConceptInstance.class))
public interface HttpVirtualModelInstance extends VirtualModelInstance<HttpVirtualModelInstance, HttpTechnologyAdapter> {

	String ACCESS_POINT = "accessPoint";

	@Initializer
	void initialize(@Parameter(ACCESS_POINT) AccessPoint accessPoint, FlexoServiceManager serviceManager,
			ContentSupportFactory<?, ?> supportFactory);

	@Getter(ACCESS_POINT)
	AccessPoint getAccessPoint();

	@Setter(ACCESS_POINT)
	void setAccessPoint(AccessPoint accessPoint);

	// List<HttpFlexoConceptInstance> getFlexoConceptInstances(String path, String pointer, FlexoConcept concept);

	// HttpFlexoConceptInstance getFlexoConceptInstance(String url, String pointer, FlexoConcept concept);

	// public VirtualModelInstanceModelFactory makeVirtualModelInstanceModelFactory(FlexoServiceManager serviceManager)
	// throws ModelDefinitionException;

	public CloseableHttpClient getHttpclient();

	abstract class HttpVirtualModelInstanceImpl extends VirtualModelInstanceImpl<HttpVirtualModelInstance, HttpTechnologyAdapter>
			implements HttpVirtualModelInstance {

		@SuppressWarnings("unused")
		private static final Logger logger = FlexoLogger.getLogger(HttpVirtualModelInstance.class.getPackage().toString());

		private final CloseableHttpClient httpclient = HttpClients.createDefault();

		private ContentSupportFactory<?, ?> supportFactory;

		@Override
		public void initialize(AccessPoint accessPoint, FlexoServiceManager serviceManager, ContentSupportFactory<?, ?> supportFactory) {

			System.out.println("Hop, on initialise un HttpVirtualModelInstance");

			performSuperSetter(ACCESS_POINT, accessPoint);
			this.supportFactory = supportFactory;
		}

		@Override
		public CloseableHttpClient getHttpclient() {
			return httpclient;
		}

		public AccessPointResource getAccessPointResource() {
			return (AccessPointResource) this.getAccessPoint().getResource();
		}

		public AccessPointFactory getAccessPointFactory() {
			return getAccessPointResource().getFactory();
		}

		@Override
		public VirtualModel getVirtualModel() {
			return getContainerVirtualModelInstance().getVirtualModel();
		}

		@Override
		public VirtualModelInstance<?, ?> getContainerVirtualModelInstance() {
			AccessPointResource resource = getAccessPointResource();
			if (resource != null && resource.getContainer() instanceof AbstractVirtualModelInstanceResource) {
				return ((AbstractVirtualModelInstanceResource) resource.getContainer()).getVirtualModelInstance();
			}
			return null;
		}

		@Override
		public FlexoResourceCenter<?> getResourceCenter() {
			FlexoResource<AccessPoint> resource = getAccessPoint().getResource();
			return resource != null ? resource.getResourceCenter() : null;
		}

		@Override
		public String toString() {
			return "HttpVirtualModelInstance:" + super.toString();
		}

		public ContentSupportFactory<?, ?> getSupportFactory() {
			return supportFactory;
		}

		/*public HttpFlexoConceptInstance retrieveFlexoConceptInstance(FlexoConcept type, Map<?,?> values) {
			
		}*/
	}
}
