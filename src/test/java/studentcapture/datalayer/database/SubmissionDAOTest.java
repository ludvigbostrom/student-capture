package studentcapture.datalayer.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by c13arm on 2016-05-12.
 */
public class SubmissionDAOTest extends StudentCaptureApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Autowired
    SubmissionDAO submissionDAO;

    /**
     * Inserts dummy data to test against h2Database
     */
    @Before
    public void setUp() {
        String sql1 = "INSERT INTO Users VALUES (1, null, 'mkyong', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword');";
        String sql2 = "INSERT INTO Users VALUES (2, null, 'alex', 'abcd', 'defg', 'alex@yahoo.com', 'SecretPassword');";
        String sql3 = "INSERT INTO Users VALUES (3, null, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword');";
        String sql4 = "INSERT INTO Course VALUES ('PVT',2016, 'VT', '1234', 'ABC', null, true);";
        String sql5 = "INSERT INTO Assignment VALUES (1, 'PVT', 'OU1', '2016-05-13 10:00:00', '2016-05-13 12:00:00', 60, 180, null, 'XYZ');";
        String sql6 = "INSERT INTO Submission VALUES (1, 1, null, '2016-05-13 11:00:00', null, null, null, null);";
        String sql7 = "INSERT INTO Submission VALUES (1, 3, null, '2016-05-13 11:00:00', 'MVG', 2, null, null);";
        String sql8 = "INSERT INTO Participant VALUES (3, 'PVT', 'Teacher');";

        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
        jdbcMock.update(sql3);
        jdbcMock.update(sql4);
        jdbcMock.update(sql5);
        jdbcMock.update(sql6);
        jdbcMock.update(sql7);
        jdbcMock.update(sql8);
    }

    /**
     * Remove all content from the database
     */
    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Users;";
        String sql2 = "DELETE FROM Course;";
        String sql3 = "DELETE FROM Assignment;";
        String sql4 = "DELETE FROM Submission;";
        String sql5 = "DELETE FROM Participant;";

        jdbcMock.update(sql5);
        jdbcMock.update(sql4);
        jdbcMock.update(sql3);
        jdbcMock.update(sql2);
        jdbcMock.update(sql1);
    }

    /**
     * Tests that the insertion of test data works
     */
    @Test
    public void databaseSetUpTest() {
        String sql = "SELECT * FROM Submission WHERE assignmentID = 1 AND studentID = 1";

        //Getting values from table Submission
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcMock.queryForObject(sql, new SubmissionDAOWrapper());

        assertEquals("1",info.get("AssignmentId"));
        assertEquals("1",info.get("StudentId"));
        assertEquals(null,info.get("StudentPublishConsent"));
        assertEquals("2016-05-13 11:00:00.0",info.get("SubmissionDate"));
    }

    /**
     * Tests the correct insertion of values from method setGrade
     */
    @Test
    public void setGradeValues() {
        Submission submission = new Submission(1,1);
        Grade grade = new Grade("vg", 3);
        submission.setCourseID("PVT");
        submissionDAO.setGrade(submission, grade);

        String sql = "SELECT * FROM Submission WHERE assignmentID = 1 AND studentID = 1";

        //Getting values from table Submission
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcMock.queryForObject(sql, new SubmissionDAOWrapper());

        assertEquals("1",info.get("AssignmentId"));
        assertEquals("1",info.get("StudentId"));
        assertEquals(null,info.get("StudentPublishConsent"));
        assertEquals("vg",info.get("Grade"));
        assertEquals("3",info.get("TeacherId"));
    }

    /**
     * Checks the returnvalue from an insertion with correct values
     */
    @Test
    public void gradeExistingAssignment() {
        Submission submission = new Submission(1,1);
        Grade grade = new Grade("vg", 3);
        submission.setCourseID("PVT");

        boolean returnValue = submissionDAO.setGrade(submission, grade);

        assertTrue(returnValue);
    }

    /**
     * Checks the returnvalue from an insertion with incorrect values
     */
    @Test
    public void gradeNonExistingAssignment() {
        Submission submission = new Submission(2,1);
        Grade grade = new Grade("vg", 3);
        submission.setCourseID("PVT");

        boolean returnValue = submissionDAO.setGrade(submission, grade);

        assertFalse(returnValue);
    }

    /**
     * Checks if the teacher trying to set a grade exists in the table, in this test the teacher does not exist and the test should return false
     */
    @Test
    public void nonExistingTeacherSetsGrade() {
        Submission submission = new Submission(1,1);
        Grade grade = new Grade("vg", 2);
        submission.setCourseID("PVT");

        boolean returnValue = submissionDAO.setGrade(submission, grade);

        assertFalse(returnValue);
    }

    /**
     * Teacher cannot publish non-graded feedback
     */
    @Test
    public void publishNonGradedFeedback() {
        Submission submission = new Submission(1,1);
        submission.setCourseID("PVT");
        boolean returnValue = submissionDAO.publishFeedback(submission, true);

        assertFalse(returnValue);
    }

    /**
     * Teacher publishes feedback
     */
    @Test
    public void publishFeedback() {
        Submission submission = new Submission(1,1);
        Grade grade = new Grade("vg", 3);
        submission.setCourseID("PVT");
        submission.setGrade(grade);

        boolean returnValue = submissionDAO.publishFeedback(submission, true);

        assertTrue(returnValue);
    }

    /**
     * Retrieving ungraded grade, only time should be returned
     */
    @Test
    public void getGradeShouldReturnNullOnGradeAndTeacher() {
        Submission model = createSubmissionModel(1,1);
        Map result = submissionDAO.getGrade(model);
        assertEquals(null, result.get("grade"));
        assertEquals(null, result.get("teacher"));
        assertEquals("2016-05-13 11:00:00.0", result.get("time"));
        assertEquals(null, result.get("error"));
    }

    /**
     * Retrieve graded grade, everything should be returned
     * (not error message)
     */
    @Test
    public void getGradeShouldReturnAllValues() {
        Submission submission = createSubmissionModel(1,3);
        Map result = submissionDAO.getGrade(submission);
        assertEquals("MVG", result.get("grade"));
        assertEquals("abcd defg", result.get("teacher"));
        assertEquals("2016-05-13 11:00:00.0", result.get("time"));
        assertEquals(null, result.get("error"));
    }

    /**
     * No rows in the tables has the values sent into getGrade,
     * error message returned
     */
    @Test
    public void getGradeShouldHandleNonExistingIds() {
        Submission submission = createSubmissionModel(3546,124);
        Map result = submissionDAO.getGrade(submission);
        assertEquals(null, result.get("grade"));
        assertEquals(null, result.get("teacher"));
        assertEquals(null, result.get("time"));
        assertEquals("The given parameters does not have an entry in the database",
                result.get("error"));
    }

    private Submission createSubmissionModel(int assignmentID, int studentID) {
        Submission submission = new Submission();
        submission.setAssignmentID(assignmentID);
        submission.setStudentID(studentID);
        return submission;
    }

    /**
     *  Used to collect user information, and return a hashmap.
     */
    protected class SubmissionDAOWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            HashMap<String,String> info = new HashMap();
            info.put("AssignmentId",rs.getString("AssignmentId"));
            info.put("StudentId",rs.getString("StudentId"));
            info.put("StudentPublishConsent",rs.getString("StudentPublishConsent"));
            info.put("SubmissionDate",rs.getString("SubmissionDate"));
            info.put("Grade",rs.getString("Grade"));
            info.put("TeacherId",rs.getString("TeacherId"));
            info.put("PublishStudentSubmission",rs.getString("PublishStudentSubmission"));
            return info;
        }
    }
}
