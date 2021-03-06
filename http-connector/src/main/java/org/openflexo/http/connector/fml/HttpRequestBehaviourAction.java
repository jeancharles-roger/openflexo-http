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

package org.openflexo.http.connector.fml;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.foundation.fml.rt.action.AbstractActionSchemeAction;
import org.openflexo.http.connector.model.HttpVirtualModelInstance;

/**
 * Created by charlie on 21/03/2017.
 */
public class HttpRequestBehaviourAction
		extends AbstractActionSchemeAction<HttpRequestBehaviourAction, HttpRequestBehaviour<?, ?>, HttpVirtualModelInstance<?>> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(HttpRequestBehaviourAction.class.getPackage().getName());

	/**
	 * Constructor to be used with a factory
	 * 
	 * @param actionFactory
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	public HttpRequestBehaviourAction(HttpRequestBehaviourActionFactory actionFactory, HttpVirtualModelInstance<?> focusedObject,
			List<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(actionFactory, focusedObject, globalSelection, editor);
	}

	/**
	 * Constructor to be used for creating a new action without factory
	 * 
	 * @param flexoBehaviour
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	public HttpRequestBehaviourAction(HttpRequestBehaviour<?, ?> behaviour, HttpVirtualModelInstance<?> focusedObject,
			List<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(behaviour, focusedObject, globalSelection, editor);
	}

	/**
	 * Constructor to be used for creating a new action as an action embedded in another one
	 * 
	 * @param flexoBehaviour
	 * @param focusedObject
	 * @param globalSelection
	 * @param ownerAction
	 *            Action in which action to be created will be embedded
	 */
	public HttpRequestBehaviourAction(HttpRequestBehaviour<?, ?> behaviour, HttpVirtualModelInstance<?> focusedObject,
			List<VirtualModelInstanceObject> globalSelection, FlexoAction<?, ?, ?> ownerAction) {
		super(behaviour, focusedObject, globalSelection, ownerAction);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void doAction(Object context) throws FlexoException {
		try {
			returnedValue = ((HttpRequestBehaviour) getFlexoBehaviour()).execute((HttpVirtualModelInstance<?>) getFlexoConceptInstance(),
					this);
		} catch (FlexoException e) {
			throw e;
		} catch (Exception e) {
			throw new FlexoException(e);
		}
	}

}
