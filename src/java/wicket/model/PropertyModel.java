/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.model;

import wicket.Component;

/**
 * A PropertyModel is used to dynamically access a model using an <a
 * href="www.ognl.org">Ognl expression </a>.
 * <p>
 * For example, take the following bean:
 * 
 * <pre>
 * public class Person
 * {
 * 	private String name;
 * 
 * 	public String getName()
 * 	{
 * 		return name;
 * 	}
 * 
 * 	public void setName(String name)
 * 	{
 * 		this.name = name;
 * 	}
 * }
 * </pre>
 * 
 * We could construct a label that dynamically fetches the name property of the
 * given person object like this:
 * 
 * <pre>
 *              Person person = getSomePerson();
 *              ...
 *              add(new Label(&quot;myLabel&quot;, person, &quot;name&quot;);
 * </pre>
 * 
 * Where 'myLabel' is the name of the component, and 'name' is the Ognl
 * expression to get the name property.
 * </p>
 * <p>
 * In the same fashion, we can create form components that work dynamically on
 * the given model object. For instance, we could create a text field that
 * updates the name property of a person like this:
 * 
 * <pre>
 *              add(new TextField(&quot;myTextField&quot;, person, &quot;name&quot;);
 * </pre>
 * 
 * </p>
 * <p>
 * To force Ognl to convert to a specific type, you can provide constructor
 * argument 'propertyType'.if that is set, that type is used for conversion
 * instead of the type that is figured out by Ognl. This can be especially
 * usefull for when you have a generic property (like Serializable myProp) that
 * you want to be converted to a narrower type (e.g. an Integer). Ognl sees an
 * incomming string being compatible with the target property, and will then
 * bypass the converter. Hence, to force myProp being converted to and from and
 * integer, propertyType should be set to Integer.
 * </p>
 * 
 * @see wicket.model.IModel
 * @see wicket.model.Model
 * @see wicket.model.AbstractDetachableModel
 * 
 * @author Chris Turner
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class PropertyModel extends AbstractPropertyModel implements INestedModel
{
	/** Serial Version ID. */
	private static final long serialVersionUID = -3136339624173288385L;

	/** Ognl expression for property access. */
	private final String expression;

	/** The model. */
	private final IModel model;

	/**
	 * if this is set, this type is used for conversion instead of the type that
	 * is figured out by Ognl. This can be especially usefull for when you have
	 * a generic property (like Serializable myProp) that you want to be
	 * converted to a narrower type (e.g. an Integer). Ognl sees an incomming
	 * string being compatible with the target property, and will then bypass
	 * the converter. Hence, to force myProp being converted to and from and
	 * integer, propertyType should be set to Integer.
	 */
	private final Class propertyType;

	/**
	 * Construct with an IModel object and a Ognl expression that works on the
	 * given model. Additional formatting will be used depending on the
	 * configuration setting.
	 * 
	 * @param model
	 *            the wrapper
	 * @param expression
	 *            Ognl expression for property access
	 */
	public PropertyModel(final IModel model, final String expression)
	{
		this(model, expression, null);
	}

	/**
	 * Construct with an IModel object and a Ognl expression that works on the
	 * given model. Additional formatting will be used depending on the
	 * configuration setting.
	 * 
	 * @param model
	 *            the wrapper
	 * @param expression
	 *            Ognl expression for property access
	 * @param propertyType
	 *            the type to be used for conversion instead of the type that is
	 *            figured out by Ognl. This can be especially usefull for when
	 *            you have a generic property (like Serializable myProp) that
	 *            you want to be converted to a narrower type (e.g. an Integer).
	 *            Ognl sees an incomming string being compatible with the target
	 *            property, and will then bypass the converter. Hence, to force
	 *            myProp being converted to and from and integer, propertyType
	 *            should be set to Integer.
	 */
	public PropertyModel(final IModel model, final String expression, Class propertyType)
	{
		if (model == null)
		{
			throw new IllegalArgumentException("Model parameter must not be null");
		}

		this.model = model;
		this.expression = expression;
		this.propertyType = propertyType;
	}

	/**
	 * Gets the model on which the Ognl expressions are applied. The expression
	 * will actually not be applied on the instance of IModel, but (naturally)
	 * on the wrapped model object or more accurate, the object that results
	 * from calling getObject on the instance of IModel.
	 * 
	 * @return The model on which the Ognl expressions are applied.
	 */
	public final IModel getNestedModel()
	{
		return model;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[PropertyModel model = " + model + ", expression = " + expression + ", object = "
				+ getObject(null) + "]";
	}
	
	/**
	 * @see wicket.model.AbstractDetachableModel#onAttach()
	 */
	protected void onAttach()
	{
	}
	
	/**
	 * @see AbstractDetachableModel#onDetach()
	 */
	protected final void onDetach()
	{
		super.onDetach();
		model.detach();
	}

	/**
	 * @see wicket.model.AbstractPropertyModel#ognlExpression(wicket.Component)
	 */
	protected String ognlExpression(Component component)
	{
		return expression;
	}

	/**
	 * @see wicket.model.AbstractPropertyModel#modelObject(Component)
	 */
	protected Object modelObject(final Component component)
	{
		return model.getObject(component);
	}

	/**
	 * @see wicket.model.AbstractPropertyModel#propertyType(wicket.Component)
	 */
	protected Class propertyType(Component component)
	{
		return propertyType;
	}
}