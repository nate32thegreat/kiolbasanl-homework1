package com.cybr406.post.problems;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.cybr406.post.problems.TestUtil.testFieldGettersAndSetters;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("setup")
@SpringBootTest(classes = SetupProblemsApplication.class)
@AutoConfigureMockMvc
public class DatabaseSetupProblems {

    @Test
    public void testPostIsEntity() {
        try {
            Class clazz = Class.forName("com.cybr406.post.Post");
            List<Annotation> annotations = Arrays.asList(clazz.getAnnotations());

            Annotation entityAnnotation = annotations.stream()
                    .filter(a -> a.annotationType().getName().equals("javax.persistence.Entity"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull("You must add the @Entity annotation to the Post object.", entityAnnotation);
        } catch (ClassNotFoundException e) {
            fail("Create a Post object to represent a basic blog post.");
        }
    }

    @Test
    public void testPostIdentifier() throws Exception {
        try {
            Class clazz = Class.forName("com.cybr406.post.Post");

            // Post needs a unique, numerical field to distinguish one post from another in the database.
            testFieldGettersAndSetters(clazz, Long.class, "id", "getId", "setId");

            Field id = clazz.getDeclaredField("id");
            List<Annotation> annotations = Arrays.asList(id.getAnnotations());
            List<String> annotationNames = annotations.stream()
                    .map(a -> a.annotationType().getName())
                    .collect(Collectors.toList());
            assertTrue("The id field should be annotated with @Id", annotationNames.contains("javax.persistence.Id"));
            assertTrue("The id field should be annotated with @GeneratedValue", annotationNames.contains("javax.persistence.GeneratedValue"));
        } catch (ClassNotFoundException e) {
            fail("Create a Post object to represent a basic blog post.");
        }
    }

    @Test
    public void testPostRepositoryInterfaceExists() {
        try {
            Class clazz = Class.forName("com.cybr406.post.PostRepository");
            assertTrue(String.format("%s should be an interface, not a class.", clazz.getName()), clazz.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Create a PostRepository interface to manage Post objects in the database.");
        }
    }

    @Test
    public void testPostRepositoryInterfaceConfiguration() {
        try {
            Class clazz = Class.forName("com.cybr406.post.PostRepository");

            // Verify the correct interface is extended from
            Class superclass = Class.forName("org.springframework.data.jpa.repository.JpaRepository");
            assertTrue("PostRepository should extend JpaRepository", superclass.isAssignableFrom(clazz));

            // Verify the correct generic type arguments are used when extending
            List<Type> types = Arrays.asList(clazz.getGenericInterfaces());
            assertEquals(1, types.size());
            ParameterizedType parameterizedType = (ParameterizedType) types.get(0);
            List<Type> args = Arrays.asList(parameterizedType.getActualTypeArguments());
            assertEquals(2, args.size());
            assertEquals("The first type arg must be Post.",
                    "com.cybr406.post.Post",
                    args.get(0).getTypeName());
            assertEquals("The second type arg must be Long.",
                    "java.lang.Long",
                    args.get(1).getTypeName());

            // Verify the correct annotation is used.
            List<Annotation> annotations = Arrays.asList(clazz.getAnnotations());
            Annotation repository = annotations.stream()
                    .filter(a -> a.annotationType().getName().equals("org.springframework.stereotype.Repository"))
                    .findFirst()
                    .orElse(null);
            assertNotNull("PostRepository must be annotated with @Repository", repository);
        } catch (ClassNotFoundException e) {
            fail("Create a PostRepository interface to manage Post objects in the database.");
        }
    }

}
