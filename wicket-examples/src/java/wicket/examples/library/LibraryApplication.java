/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.library;

import wicket.Component;
import wicket.ISessionFactory;
import wicket.RestartResponseAtInterceptPageException;
import wicket.Session;
import wicket.authorization.Action;
import wicket.authorization.IAuthorizationStrategy;
import wicket.examples.WicketExampleApplication;
import wicket.settings.Settings;

/**
 * WicketServlet class for example.
 * @author Jonathan Locke
 */
public final class LibraryApplication extends WicketExampleApplication
{
    /**
     * Constructor.
     */
    public LibraryApplication()
    {
    }
    
	/**
	 * @see wicket.examples.WicketExampleApplication#init()
	 */
	protected void init()
	{
        getExceptionSettings().setThrowExceptionOnMissingResource(false);
		getRequestCycleSettings().setRenderStrategy(Settings.REDIRECT_TO_RENDER);
		getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy()
		{
			public boolean authorizeAction(Component component, Action action)
			{
				return true;
			}
		
			public boolean authorizeInstantiation(Class componentClass)
			{
				if(AuthenticatedWebPage.class.isAssignableFrom(componentClass))
				{
			        // Is user signed in?
			        if (((LibrarySession)Session.get()).isSignedIn())
			        {
			            // okay to proceed
			            return true;
			        }
			        
			        // Force sign in
			        throw new RestartResponseAtInterceptPageException(SignIn.class);
				}
				return true;
			}
		});
	}
    
    
    /**
	 * @see wicket.protocol.http.WebApplication#getSessionFactory()
	 */
	public ISessionFactory getSessionFactory()
	{
        return new ISessionFactory()
        {
            public Session newSession()
            {
                return new LibrarySession(LibraryApplication.this);
            }
        };
	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class getHomePage()
	{
		return Home.class;
	}
}
