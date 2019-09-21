package users.api;
import users.core.User;

public class UsersApi {
	private User user;

	public UsersApi() {
		// Jackson deserialization
	}

	public UsersApi(int id, String nickname, String firstName, String lastName,int kudosAmount) {
		//user = new User(nickname, firstName, lastName);
	}
}
