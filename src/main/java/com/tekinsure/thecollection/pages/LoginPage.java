package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.model.ui.UserLogin;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

/**
 * The Login pages
 */
public class LoginPage extends BasePage {

    private UserLogin userLogin = new UserLogin();


    public LoginPage() {

        Form form = new Form("form");
        setMarkupContainer(form);
        add(form);

        setAddCommonComponents(false);

        addTextField("username", new PropertyModel(userLogin, "username"));
        addTextField("password", new PropertyModel(userLogin, "password"));

        form.add(new Button("login") {
            @Override
            public void onSubmit() {
                IAuthenticationStrategy strategy = getApplication().getSecuritySettings()
                        .getAuthenticationStrategy();

                if (signIn(userLogin.getUsername(), userLogin.getPassword()))
                {
                    strategy.save(userLogin.getUsername(), userLogin.getPassword());
                    onSignInSucceeded();
                }
                else
                {
                    onSignInFailed();
                    strategy.remove();
                }
            }
        });
    }

    /**
     * @see org.apache.wicket.Component#onBeforeRender()
     */
    @Override
    protected void onBeforeRender()
    {
        // logged in already?
        if (isSignedIn() == false)
        {
            IAuthenticationStrategy authenticationStrategy = getApplication().getSecuritySettings()
                    .getAuthenticationStrategy();
            // get username and password from persistence store
            String[] data = authenticationStrategy.load();

            if ((data != null) && (data.length > 1))
            {
                // try to sign in the user
                if (signIn(data[0], data[1]))
                {
                    userLogin.setUsername(data[0]);
                    userLogin.setPassword(data[1]);

                    // logon successful. Continue to the original destination
                    continueToOriginalDestination();
                    // Ups, no original destination. Go to the home page
                    throw new RestartResponseException(getSession().getPageFactory().newPage(
                            getApplication().getHomePage()));
                }
                else
                {
                    // the loaded credentials are wrong. erase them.
                    authenticationStrategy.remove();
                }
            }
        }

        // don't forget
        super.onBeforeRender();
    }

    /**
     * Sign in user if possible.
     *
     * @param username
     *            The username
     * @param password
     *            The password
     * @return True if signin was successful
     */
    private boolean signIn(String username, String password)
    {
        return AuthenticatedWebSession.get().signIn(username, password);
    }

    /**
     * @return true, if signed in
     */
    private boolean isSignedIn()
    {
        return AuthenticatedWebSession.get().isSignedIn();
    }

    /**
     * Called when sign in failed
     */
    protected void onSignInFailed()
    {
        // Try the component based localizer first. If not found try the
        // application localizer. Else use the default
        error(getLocalizer().getString("signInFailed", this, "Sign in failed"));
    }

    /**
     * Called when sign in was successful
     */
    protected void onSignInSucceeded()
    {
        // If login has been called because the user was not yet logged in, than continue to the
        // original destination, otherwise to the Home page
        continueToOriginalDestination();
        setResponsePage(getApplication().getHomePage());
    }

}
