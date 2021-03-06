package studentcapture.assignment;

import org.junit.Before;
import org.junit.Test;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.assertEquals;


public class AssignmentModelTest extends StudentCaptureApplicationTests {

    private AssignmentModel assignmentModel;

    @Before
    public void setUp() {
        AssignmentVideoIntervall videoIntervall =
                new AssignmentVideoIntervall();
        AssignmentDateIntervalls assignmentIntervalls =
                new AssignmentDateIntervalls();
        assignmentModel = new AssignmentModel();

        videoIntervall.setMinTimeSeconds(120);
        videoIntervall.setMaxTimeSeconds(300);
        assignmentIntervalls.setStartDate("2016-01-22 15:00:00");
        assignmentIntervalls.setEndDate("2016-01-24 10:00:00");
        assignmentIntervalls.setPublishedDate("2016-01-22 15:00:00");
        assignmentModel.setTitle("Test");
        assignmentModel.setDescription("Info");
        assignmentModel.setVideoIntervall(videoIntervall);
        assignmentModel.setAssignmentIntervall(assignmentIntervalls);
        assignmentModel.setScale(GradeScale.U_O_K_G.toString());
        assignmentModel.setRecap("Recap");
    }

    @Test
    public void titleShouldBeTest() {
        assertEquals("Test", assignmentModel.getTitle());
    }

    @Test
    public void titleShouldBeTest1() {
        assignmentModel.setTitle("Test1");
        assertEquals("Test1", assignmentModel.getTitle());
    }

    @Test
    public void descriptionShouldBeInfo() {
        assertEquals("Info", assignmentModel.getDescription());
    }

    @Test
    public void descriptionShouldBeTest1() {
        assignmentModel.setDescription("Test1");
        assertEquals("Test1", assignmentModel.getDescription());
    }

    @Test
    public void recapShouldBeRecap() {
        assertEquals("Recap", assignmentModel.getRecap());
    }

    @Test
    public void recapShouldBeTest1() {
        assignmentModel.setRecap("Test1");
        assertEquals("Test1", assignmentModel.getRecap());
    }

    @Test
    public void gradeScaleShouldBeRightScale() {
        assertEquals(GradeScale.U_O_K_G.toString(), assignmentModel.getScale());
    }

    @Test
    public void gradeScaleShouldBeU_O_K_G() {
        assignmentModel.setScale("U_O_K_G");
        assertEquals("U_O_K_G", assignmentModel.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionBecauseInvalidGradeScale() {
        assignmentModel.setScale("InvalidGradeScale");
    }

    @Test
    public void CourseIDShouldBe1200() {
        assignmentModel.setCourseID(1200);
        assertEquals(1200, assignmentModel.getCourseID());
    }

    @Test
    public void CourseIDShouldBe2() {
        assignmentModel.setCourseID(2);
        assertEquals(2, assignmentModel.getCourseID());
    }

    @Test
    public void minTimeSecondsShouldBe120() {
        assertEquals(120, assignmentModel.getVideoIntervall()
                .getMinTimeSeconds());
    }

    @Test
    public void minTimeSecondsShouldBe150() {
        assignmentModel.getVideoIntervall().setMinTimeSeconds(150);
        assertEquals(150, assignmentModel.getVideoIntervall()
                .getMinTimeSeconds());
    }

    @Test
    public void maxTimeSecondsShouldBe300() {
        assertEquals(300, assignmentModel.getVideoIntervall()
                .getMaxTimeSeconds());
    }

    @Test
    public void maxTimeSecondsShouldBe290() {
        assignmentModel.getVideoIntervall().setMaxTimeSeconds(290);
        assertEquals(290, assignmentModel.getVideoIntervall()
                .getMaxTimeSeconds());
    }

}
