package studentcapture.feedback.controller;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by c12osn on 2016-04-21.
 */
public class FeedBackControllerTest {
    @Test
    public void shouldReturnGradeAndFeedback(){
        FeedbackController controller = new FeedbackController();
        Grade obj  = controller.handleFeedbackRequestFromStudent("Anna", "PVT", "deltenta 1");

        Grade grade = new Grade("vg", "ditt svar var ok men det sög");

        assertEquals(obj, grade);
    }
}