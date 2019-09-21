package stats.core;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Null;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(
			name = "kudos.core.User.findAll",
			query = "select p from User p"
	),
	@NamedQuery(
			name = "kudos.core.User.usernamePassword",
			query = "select u from User u where u.username=:username and u.password=:password"
	),
	
	@NamedQuery(
			name = "kudos.core.User.simpleUsers",
			
			query = "select p.id, p.nickname, p.kudosAmount from User p"
	),
	@NamedQuery(
			name = "kudos.core.User.findByNickNameFirstName",
			query = "select p from User p where p.nickname=:nickname and p.firstName=:firstName" 
	)
	//p.id, p.nickname, p.kudosAmount
	/*
	,
	
	@NamedNativeQuery(
			name = "kudos.core.User.insertUser",
			query = "INSERT INTO users (nickname, first_name, last_name, username, password) VALUES (?, ?, ?, ?, ?)"
	)
	*/
})

@SqlResultSetMappings({
	@SqlResultSetMapping(name="updateResult", columns = { @ColumnResult(name = "count")}),
	@SqlResultSetMapping(name="simpleUser", entities = {
			@EntityResult(
					entityClass = User.class,
					fields = {
							@FieldResult(name="id",column="id"),
							@FieldResult(name="nickname",column="nickname"),
							@FieldResult(name="kudosAmount",column="kudos_amount")

					}
			)
	})
})

@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "kudos.core.User.insertUser",
            query   =   "INSERT INTO users (nickname, first_name, last_name, username, password) VALUES (?, ?, ?, ?, ?)",
            resultSetMapping = "updateResult"
    ),
    @NamedNativeQuery(
            name    =   "kudos.core.User.deleteUserbyId",
            query   =   "Delete from users where id=?",
            resultSetMapping = "updateResult"
    ),
    @NamedNativeQuery(
            name    =   "kudos.core.User.getSimpleUser1",
            query   =   "select p.id, p.nickname, p.kudos_Amount from users as p",
            resultSetMapping = "simpleUser"
    )
    
    ,
    @NamedNativeQuery(
    		//UPDATE `users` SET `kudos_amount` = '1' WHERE `users`.`id` = 6;
            name    =   "kudos.core.User.update.kudosAmount",
            query   =   "UPDATE users SET kudos_amount=:kudosAmount WHERE id=:userId",
            resultSetMapping = "updateResult"
    )
})

@JsonInclude(Include.NON_NULL)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "kudos_amount")
	private int kudosAmount;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Transient
	private String kudosList;
	
	public User(String nickname, String firstName, String lastName, String username, String password) {
		this.nickname = nickname;
		this.firstName = firstName;
		this.lastName = lastName;	
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password) {	
		this.username = username;
		this.password = password;
	}
	public User(long id, String nickname,int kudosAmount) {
		this.id = id;
		this.nickname = nickname;
		this.kudosAmount = kudosAmount;
	}
	public User() {}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getKudosAmount() {
		return kudosAmount;
	}

	public void setKudosAmount(int kudosAmount) {
		this.kudosAmount = kudosAmount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getKudosList() {
		return kudosList;
	}

	public void setKudosList(String kudosList) {
		this.kudosList = kudosList;
	}
	
	
	
	
}
