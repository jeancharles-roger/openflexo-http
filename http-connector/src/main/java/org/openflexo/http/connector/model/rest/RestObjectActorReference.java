/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
 * developed at Openflexo.
 * 
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
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
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

package org.openflexo.http.connector.model.rest;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.http.connector.model.HttpObjectActorReference;
import org.openflexo.http.connector.rm.HttpVirtualModelInstanceResource;
import org.openflexo.http.connector.rm.rest.RestVirtualModelInstanceResource;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;

/**
 * Implements {@link ActorReference} for {@link FlexoObject} as modelling elements.<br>
 * 
 * @author sylvain
 * 
 * @param <T>
 */
@ModelEntity
@ImplementationClass(RestObjectActorReference.RestObjectActorReferenceImpl.class)
@XMLElement
public interface RestObjectActorReference extends HttpObjectActorReference<RestFlexoConceptInstance> {

	abstract class RestObjectActorReferenceImpl extends HttpObjectActorReferenceImpl<RestFlexoConceptInstance>
			implements RestObjectActorReference {

		private static final Logger logger = FlexoLogger.getLogger(RestObjectActorReference.class.getPackage().toString());

		@Override
		protected RestFlexoConceptInstance retrieveModellingElement() {
			HttpVirtualModelInstanceResource<?> httpVMIResource = (HttpVirtualModelInstanceResource<?>) getServiceManager()
					.getResourceManager().getResource(getResourceURI());
			if (httpVMIResource instanceof RestVirtualModelInstanceResource) {
				RestVirtualModelInstance restVMI = ((RestVirtualModelInstanceResource) httpVMIResource).getVirtualModelInstance();
				VirtualModel vm = httpVMIResource.getVirtualModel();
				FlexoConcept concept = vm.getFlexoConcept(getFlexoConceptURI());
				// TODO: find the container !!!
				return restVMI.makeFlexoConceptInstance(getKey(), restVMI, concept);
			}
			return null;
		}

	}
}
