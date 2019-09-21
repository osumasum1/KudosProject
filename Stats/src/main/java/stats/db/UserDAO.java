package stats.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.google.common.base.Optional;
import com.sun.tools.sjavac.Log;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import stats.core.User;

public class UserDAO extends AbstractDAO<User>{
	
	public UserDAO(SessionFactory factory) {
		super(factory);
	}
	
	@SuppressWarnings("unchecked")
	public boolean updateKudosAmount(int kudosAmount, int userId) {
		try
        {
			Query<User> query = namedQuery("kudos.core.User.update.kudosAmount");
			query.setParameter("kudosAmount", kudosAmount);
			query.setParameter("userId", userId);
			
			query.executeUpdate();
             
            return true;
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
            return false;
        }
		
		
	}
	////////////////////////////////////////////////////////
	
	
	public Optional<User> findById(long id){
		//Optional.of(get(id));
		return Optional.fromNullable(get(id));
		//return Optional.ofNullable(get(id));
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findAll() {
        return list(namedQuery("kudos.core.User.findAll"));
    }
	
	@SuppressWarnings("unchecked")
	public List<User> findByFirstNameNickname(String firstName, String nickname) {
		Query<User> query = namedQuery("kudos.core.User.findByNickNameFirstName");
		query.setParameter("firstName", firstName);
		query.setParameter("nickname", nickname);
		return query.getResultList();
    }
	
	@SuppressWarnings("unchecked")
	public List<User> findAllSimple() {
		List<Object[]> results = list(namedQuery("kudos.core.User.simpleUsers"));
		List<User> resp = new ArrayList<User>();
		for (Object[] array : results) {
			resp.add(new User((long)array[0], (String)array[1], (int)array[2]));
		}
		
		return resp;
    }
	
	@SuppressWarnings("unchecked")
	public List<User> findUsersByUsernameAndPassword(String username, String password){
		Query<User> query = namedQuery("kudos.core.User.usernamePassword");
		query.setParameter("username", username);
		query.setParameter("password", password);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public boolean insertUser(String nickname, String firstName, String lastName, String username, String password) {
		try
        {
			Query<User> query = namedQuery("kudos.core.User.insertUser");
			query.setParameter(0, nickname);
			query.setParameter(1, firstName);
			query.setParameter(2, lastName);
			query.setParameter(3, username);
			query.setParameter(4, password);
			
			query.executeUpdate();
             
            return true;
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
            return false;
        }
		
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean deleteUserById(long id) {
		try
        {
			Query<User> query = namedQuery("kudos.core.User.deleteUserbyId");
			query.setParameter(0, id);
			
			query.executeUpdate();
             
            return true;
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
            return false;
        }
		
		
	}

}


