import javax.naming.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

/**
 * Searches an LDAP server (e.g., Active Directory) for group members. Reads connection
 * details and search criteria from "config.ini".  **WARNING:**  Insecure credential
 * storage.  Replace placeholders and secure before production use.  Requires ini4j.
 */

public class LdapSearch {

    public static void main(String[] args) throws IOException {
        String url = "ldap://<ldap_server_address>:<port>"; // Replace with your LDAP server and port
        System.out.println("The user.dir is located in the following directory: \n" + System.getProperty("user.dir"));

        Ini ini;
        ini = new Ini(new File("config.ini")); // Keep the config file approach
        java.util.prefs.Preferences prefs = new IniPreferences(ini);

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ini.get("ldap", "ldapUser")); // Get from config
        env.put(Context.SECURITY_CREDENTIALS, ini.get("ldap", "ldapPassword", String.class)); // Get from config

        String searchbase = "<search_base_dn>"; // Replace with your search base DN
        String searchfilter = "<search_filter>"; // Replace with your search filter

        LdapContext ctx;
        try {
            ctx = new InitialLdapContext(env, null);
            System.out.println("connected");
            System.out.println(ctx.getEnvironment());
           
            NamingEnumeration results = null;
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search(searchbase, searchfilter, controls);
            ctx.close();

            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute attr = attributes.get("member"); // Assuming you want the "member" attribute
                if (attr != null) { // Check if the attribute exists
                    for (int i = 0; i < attr.size(); i++) {
                        System.out.print(attr.get(i) + "\n");
                    }
                } else {
                    System.out.println("The specified attribute 'member' was not found for this entry.");
                }
            }
        } catch (AuthenticationNotSupportedException ex) {
            System.out.println("FATAL: The authentication is not supported by the server " +
                    "\n\tAuthenticationNotSupportedException\n\t" + ex.getMessage());
        } catch (AuthenticationException ex) {
            System.out.println("FATAL: Incorrect password or username " +
                    "\n\tAuthenticationException\n\t" + ex.getMessage());
        } catch (NamingException ex) {
            System.out.println("FATAL: An Error has occured whilst trying to create the context. " +
                    "\tNamingException\n\t" + ex.getMessage());
        }
    }
}
