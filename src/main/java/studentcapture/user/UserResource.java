package studentcapture.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import studentcapture.login.ErrorFlags;
import studentcapture.usersettings.SettingsDAO;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by c12ton on 5/17/16.
 */
@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserDAO userDAO;


    @Autowired
    private SettingsDAO settingsDAO;

    /**
     * Get user object containing all related information of a user,
     * except user ID.
     * @param value for the user
     * @param flag 0 for getting user details by username
     *             1 for getting user details by userid.
     * @return user object with a httpStatus.OK if successful
     *         else HttpStatus.NOT_FOUND
     * @throws URISyntaxException 
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@RequestParam(value = "String")String value,
                                        @RequestParam(value = "int") int flag) throws URISyntaxException {

        User user = userDAO.getUser(value,flag);

        if(user == null) {
            URI uri = new URI("/login?failed");            
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Add a new user with given user details
     * @param
     * @return
     * @throws URISyntaxException 
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addUser(
            @RequestParam(value="firstname")           String firstName,
            @RequestParam(value="lastname")            String lastName,
            @RequestParam(value="email")               String email,
            @RequestParam(value="username")            String username,
            @RequestParam(value="password")            String password) throws URISyntaxException {
       
        User user = new User(username, firstName, lastName, email, encryptPassword(password), false);
        
        ErrorFlags status = userDAO.addUser(user);
        user = userDAO.getUser(username,0);

        //Redirect to /login after the registration process is complete
        URI uri;
        HttpHeaders httpHeaders = new HttpHeaders();

        //Get the correct status message
        if(status == ErrorFlags.NOERROR){
            uri = new URI("/login?" + status.toString());
            settingsDAO.setDefaultConfig(Integer.parseInt(user.getUserID()));

        }else{
            uri = new URI("/login?error=" + status.toString());
        }
        httpHeaders.setLocation(uri);

        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }

    /**
     * Update a existing user by given user object details.
     * @param user an instance of User, which will replace old user by username
     * @return HttpStatus.Ok if succesfull else HttpStatus.NOT_FOUND
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        boolean success = userDAO.updateUser(user);

        if(!success) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Encrypts password
     * @param password The input password
     * @return Encrypted password
     */
    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(11));
    }
}
