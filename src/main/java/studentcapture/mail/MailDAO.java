package studentcapture.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Class:       MailDAO
 * <p/>
 *
 * A class for accessing data required to send emails.
 *
 * Author:      Isak Hjelt, Emil Vannebäck
 * cs-user:     dv14iht, c13evk
 * Date:        5/18/16
 */
@Repository
public class MailDAO {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Method to return a list with all assignmentID
     * @return a List with assignmentIDs
     */
    public Optional<List<String>> getAssignmentID(){
        List<String> assIDList;
        try {
            assIDList = jdbcTemplate.queryForList(getAssignmentIDQuery(),String.class);
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(assIDList);
    }

    /**
     * Gets the start date from a specific assignment
     * @param assID AssignmentID ass String
     * @return Start date as String
     */
    public Optional<Date> getStartDateFromAssignment(String assID){
        Date date;
        try {
            date = jdbcTemplate.queryForObject(getStartDateQuery(), new Object[]{assID}, Date.class);
        } catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(date);

    }



    /**
     * Gets the courseID from a specific
     * @param assID String with assID
     * @return CourseID as String
     */
    public Optional<String> getCourseIDFromAssignment(String assID){
        String courseID;
        try {
            courseID = jdbcTemplate.queryForObject(getCourseIDQuery(), new Object[]{assID}, String.class);
        } catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(courseID);
    }

    /**
     * Gets a list with all the participants email from a specific course.
     * @param courseID as String
     * @return List with emails as String
     */
    public Optional<List<String>> getPraticipantsEmails(String courseID){
        List<String> emailList;
        try{
            emailList = jdbcTemplate.queryForList(getEmailsQuery(), new Object[]{courseID}, String.class);
        } catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(emailList);
    }

    public Optional<List<String>> getNotificationList(){
        List<String> notificationList;
        Date currentDate = new Date();
        try{
            notificationList = jdbcTemplate.queryForList(getNotificationQuery(),
                    new Object[]{new Timestamp(currentDate.getTime())}, String.class);
        } catch (IncorrectResultSizeDataAccessException e){
            System.out.println("ex");
            return Optional.empty();
        } catch (DataAccessException e1){
            e1.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(notificationList);
    }

    public void insertNotification(int assID, Date date){
        Timestamp timestamp = new Timestamp(date.getTime());
        try {
            if(getNotificationList().get().isEmpty()){
                jdbcTemplate.update(insertToEmptyTableQuery(),assID,timestamp);
            } else {
                jdbcTemplate.update(updateNotificationQuery(), timestamp, assID);
                jdbcTemplate.update(insertNotificationQuery(), assID, timestamp, assID);
            }
        } catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    /**
     * SQL-query for getting the participants emails.
     * @return SQL-query
     */
    private String getEmailsQuery() {
        return "SELECT Users.email "+
                "FROM Users JOIN Participant ON Users.userID = Participant.userID "+
                "WHERE courseID = ?;";
    }

    /**
     * SQL-query for getting all assignmentIDs
     * @return SQL-query
     */
    private String getAssignmentIDQuery(){
        return "SELECT assignmentID FROM assignment;";
    }

    /**
     * SQL-Query for returning a specific start date
     * @return SQL-query
     */
    private String getStartDateQuery(){
        return "SELECT startdate FROM assignment WHERE assignmentID=?;";
    }

    /**
     * SQL-query for returning a courseID from a assignment.
     * @return SQL-query
     */
    private String getCourseIDQuery(){
        return "SELECT courseID FROM assignment WHERE assignmentID=?;";
    }

    private String getNotificationQuery() {
        return "SELECT AssignmentID FROM MailScheduler WHERE (NotificationDate <= ?);";
    }

    private String updateNotificationQuery(){
        return "UPDATE MailScheduler SET notificationDate=? WHERE assignmentID = ?;";
    }

    private String insertNotificationQuery(){
        return "INSERT INTO MailScheduler(assignmentID,notificationDate) " +
                "SELECT ?, ? FROM MailScheduler "+
                "WHERE NOT EXISTS (SELECT assignmentID FROM MailScheduler WHERE assignmentID=?);";
    }

    private String insertToEmptyTableQuery(){
        return "INSERT INTO MailScheduler VALUES(?,?);";
    }
}
