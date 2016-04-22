package studentcapture.feedback.controller;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by c12osn on 2016-04-21.
 */
public class GradeTest {

    @Test
    public void gradeFakeEqualsShouldBeTrue(){
        Grade grade1 = new Grade("g", "anna");
        Grade grade2 = new Grade("g", "anna");
        assertTrue(grade1.equals(grade2));
    }

    @Test
    public void gradeFakeEqualsShouldBeFalse(){
        Grade grade1 = new Grade("vg", "anna");
        Grade grade2 = new Grade("g", "anna");
        assertFalse(grade1.equals(grade2));
    }



}