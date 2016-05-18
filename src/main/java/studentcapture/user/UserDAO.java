package studentcapture.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Repository
public class UserDAO {

    private final int GET_USER_BY_USERNAME = 0;
    private final int GET_USER_BY_ID = 1;


    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Add a new user to the User-table in the database.
     * @author Timmy Olsson
     *
     * @param user  instance that contains information of the user to be added.
     */
    public boolean addUser(User user) {

        String sql = "INSERT INTO users"
                + " (username, firstname, lastname, email, pswd)"
                + " VALUES (?, ?, ?, ?, ?)";
        //Preparing statement
        Object[] args = new Object[] {user.getUserName(),user.getfName(),
                                      user.getlName(),user.getEmail(),
                                      user.getPswd()};

        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                                      Types.VARCHAR,Types.VARCHAR};
        //Execute
        try {
            jdbcTemplate.update(sql, args,types);
        } catch (DataIntegrityViolationException e) {
			return false;
		}

		return true;
    }

    /**
     * Get user by username.
     * @author Timmy Olsson
     * @param value to be searched for in respect to  given flag
     * @param flag 0 returns user object by giving username
     *             1 returns user object by giving userID.
     *
     *
     * @return User object, that contains all related information to
     *          a user otherwise null will be returned.
     */
    public User getUser(String value, int flag) {

        String sql=null;
        Object[] args;
        int[] types;

        if(flag == GET_USER_BY_USERNAME) {
            args = new Object[]{value};
            types = new int[]{Types.VARCHAR};
            sql = "SELECT  * FROM users WHERE username = ?";
        } else if(flag == GET_USER_BY_ID) {
            args = new Object[]{Integer.parseInt(value)};
            types = new int[]{Types.INTEGER};
            sql = "SELECT  * FROM users WHERE userid = ?";
        } else {
            System.out.println("WRONG FLAG!!!!!!!!!!!!!!!!!!!!!!!!!");
            //Invalid flag
            return null;
        }

        User user = null;
        try {
            user = (User) jdbcTemplate.queryForObject(sql, args,types,
                    new UserWrapper());
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("FUCKING EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return  null;
        }

        return user;
    }

    /**
     * Updates user with username from user object.
     * @author Timmy Olsson
     *
     * @param user
     * @return true if update was successfull else false
     */
    public boolean updateUser(User user) {

        if(!userNameExist(user.getUserName())) {
            return false;
        }

        String sql = "UPDATE users SET firstname = ?, lastname = ?, email = ?," +
                " pswd = ? WHERE username = ?";


        Object[] args = {user.getfName(),user.getlName(),user.getEmail(),
                         user.getPswd(),user.getUserName()};
        int[] types = {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                       Types.VARCHAR};

        try{
            jdbcTemplate.update(sql, args,types);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Checks if given username already exists.
     * @author Timmy Olsson
     *
     * @param userName user name for user.
     * @return true if it exists else false
     */
    public boolean userNameExist(String userName) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users "
                + "WHERE  UserName = ?)";

        Object[] args = new Object[]{userName};
        int[] types = new int[]{Types.VARCHAR};

        return jdbcTemplate.queryForObject(sql,args,types,Boolean.class);
    }



	/**
     *  Used to collect user information, and return a hashmap.
     *  @author Timmy Olsson
     */
    protected class UserWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User(rs.getString("username"),rs.getString("firstname"),
                                 rs.getString("lastname"),rs.getString("email"),
                                 rs.getString("pswd"));
            user.setUserID(rs.getString("userid"));
            return user;
        }
    }
}
