package com.cybr406.post.problems;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.cybr406.post.problems.TestUtil.testFieldGettersAndSetters;
import static org.junit.Assert.fail;

/**
 * This test checks that the project has been properly prepared and basic starter classes have been added.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles("setup")
@SpringBootTest(classes = SetupProblemsApplication.class)
@AutoConfigureMockMvc
public class SetupProblems {

    @Test
    public void testH2DependencyAdded() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            fail("You need to add the h2 dependency to your gradle file.");
        }
    }

    @Test
    public void testJPADependencyAdded() {
        try {
            Class.forName("javax.persistence.Entity");
        } catch (ClassNotFoundException e) {
            fail("You need to add the JPA dependency to your gradle file.");
        }
    }

    @Test
    public void testPostExists() {
        try {
            Class.forName("com.cybr406.post.Post");
        } catch (ClassNotFoundException e) {
            fail("Create a Post object to represent a basic blog post.");
        }
    }

    @Test
    public void testPostFieldsAndMethods() {
        try {
            Class clazz = Class.forName("com.cybr406.post.Post");
            testFieldGettersAndSetters(clazz, String.class, "author", "getAuthor", "setAuthor");
            testFieldGettersAndSetters(clazz, String.class, "content", "getContent", "setContent");
        } catch (ClassNotFoundException e) {
            fail("Create a Post object to represent a basic blog post.");
        }
    }

}
