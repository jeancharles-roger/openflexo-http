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

package org.openflexo.http.connector.fml.editionaction;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.type.ParameterizedTypeImpl;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.fml.AbstractActionScheme;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.http.connector.model.HttpVirtualModelInstance;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;

@ModelEntity(isAbstract = true)
public interface HttpRequestBehaviour extends AbstractActionScheme {

	String RETURNED_FLEXO_CONCEPT_KEY = "returnedFlexoConcept";
	String RETURNED_FLEXO_CONCEPT_URI_KEY = "returnedFlexoConceptURI";
	String MULTIPLE_KEY = "multiple";


	@Getter(RETURNED_FLEXO_CONCEPT_URI_KEY) @XMLAttribute
	String getReturnedFlexoConceptURI();

	@Setter(RETURNED_FLEXO_CONCEPT_URI_KEY)
	void setReturnedFlexoConceptURI(String flexoConceptURI);

	@Getter(RETURNED_FLEXO_CONCEPT_KEY)
	FlexoConcept getReturnedFlexoConcept();

	@Setter(RETURNED_FLEXO_CONCEPT_KEY)
	void setReturnedFlexoConcept(FlexoConcept flexoConcept);

	@Getter(value = MULTIPLE_KEY, defaultValue = "false") @XMLAttribute
	boolean isMultiple();

	@Setter(MULTIPLE_KEY)
	void setMultiple(boolean multiple);


	Object execute(HttpVirtualModelInstance modelInstance, BindingEvaluationContext context) throws Exception;


	abstract class HttpRequestBehaviourImpl extends AbstractActionSchemeImpl implements HttpRequestBehaviour {

		private FlexoConcept flexoConcept;

		@Override
		public Type getReturnType() {
			FlexoConceptInstanceType type = FlexoConceptInstanceType.getFlexoConceptInstanceType(getReturnedFlexoConcept());
			return isMultiple() ? new ParameterizedTypeImpl(List.class, type)  : type;
		}

		@Override
		public ActionSchemeActionType getActionFactory(FlexoConceptInstance fci) {
			return new ActionSchemeActionType(this, fci) {
				@Override
				public ActionSchemeAction makeNewAction(
						FlexoConceptInstance focusedObject, Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor
				) {
					return new HttpRequestBehaviourAction(this, focusedObject, globalSelection, editor);
				}
			};
		}

		@Override
		public FlexoConcept getReturnedFlexoConcept() {
			String flexoConceptURI = getReturnedFlexoConceptURI();
			if (flexoConcept == null && flexoConceptURI != null && !isDeserializing() && getServiceManager() != null) {
				try {
					TechnologyAdapterService adapterService = getServiceManager().getTechnologyAdapterService();
					FMLTechnologyAdapter technologyAdapter = adapterService.getTechnologyAdapter(FMLTechnologyAdapter.class);
					this.flexoConcept = technologyAdapter.getViewPointLibrary().getFlexoConcept(flexoConceptURI, false);
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Can't find virtual model '"+ flexoConceptURI +"'", e);
				}
			}
			return flexoConcept;
		}

		@Override
		public void setReturnedFlexoConcept(FlexoConcept flexoConcept) {
			FlexoConcept oldVirtualModel = this.flexoConcept;
			if (oldVirtualModel != flexoConcept) {
				this.flexoConcept = flexoConcept;
				getPropertyChangeSupport().firePropertyChange(RETURNED_FLEXO_CONCEPT_KEY, oldVirtualModel, flexoConcept);
				setReturnedFlexoConceptURI(flexoConcept != null ? flexoConcept.getURI() : null);
			}
		}
	}
}