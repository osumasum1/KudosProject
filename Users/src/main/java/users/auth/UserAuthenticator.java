package users.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.util.Sets;
import users.core.Session;
import users.core.User;
import users.db.UserDAO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UserAuthenticator implements Authenticator<BasicCredentials, Session> {
	private final UserDAO userDao;
	
	public UserAuthenticator(UserDAO userDao) {
		this.userDao = userDao;
	}
	/*
    /**
     * Valid users with mapping user -> roles
     */
    /*
	private static final Map<String, Set<String>> VALID_SESSIONS;

    static {
        final Map<String, Set<String>> validUsers = new HashMap<>();
        validUsers.put("guest", Collections.emptySet());
        validUsers.put("good-guy", Collections.singleton("BASIC_GUY"));
        validUsers.put("chief-wizard", Sets.of("ADMIN", "BASIC_GUY"));
        VALID_SESSIONS = Collections.unmodifiableMap(validUsers);
    }

    @Override
    public Optional<Session> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (VALID_SESSIONS.containsKey(credentials.getUsername()) && "secret".equals(credentials.getPassword())) {
            return Optional.of(new Session(credentials.getUsername(), VALID_SESSIONS.get(credentials.getUsername())));
        }
        return Optional.empty();
    }
    */
	
	@Override
    public Optional<Session> authenticate(BasicCredentials credentials) throws AuthenticationException {
		if(userDao.findUsersByUsernameAndPassword(credentials.getUsername(), credentials.getPassword()).isEmpty()==false)
			return Optional.of(new Session(credentials.getUsername()));
		/*
        if ("Password1!".equals(credentials.getPassword())) {
            return Optional.of(new Session(credentials.getUsername()));
        }
        */
        return Optional.empty();
    }
}
