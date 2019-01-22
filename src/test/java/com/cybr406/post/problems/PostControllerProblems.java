package com.cybr406.post.problems;

import com.cybr406.post.PostApplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = PostApplication.class)
@AutoConfigureMockMvc
public class PostControllerProblems {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String content = "{ \"author\": \"test author\", \"content\": \"test content\" }";

    private String generateContent(int length) {
        String buffer = "";
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(2);
            buffer += r == 0 ? " " : "a";
        }
        return buffer;
    }

    @Test
    public void testPostControllerExists() {
        try {
            Class clazz = Class.forName("com.cybr406.post.PostController");
            List<Annotation> annotations = asList(clazz.getAnnotations());

            Annotation annotation = annotations.stream()
                    .filter(a -> a.annotationType().getName().equals("org.springframework.web.bind.annotation.RestController"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull("You must add the @RestController annotation to the PostController.", annotation);
        } catch (ClassNotFoundException e) {
            fail("Create a PostController object to manage posts.");
        }
    }

    @Test
    public void testPostValidatorExists() {
        try {
            Class clazz = Class.forName("com.cybr406.post.PostValidator");

            Class<?>[] interfaces = clazz.getInterfaces();
            assertEquals(
                    "PostValidator must implement exactly 1 interface (Validator).",
                    1,
                    interfaces.length);
            assertEquals(
                    "PostValidator must implement Validator",
                    "org.springframework.validation.Validator",
                    interfaces[0].getName());

        } catch (ClassNotFoundException e) {
            fail("Create a PostController object to manage posts.");
        }
    }

    @Test
    public void testPostCreation() throws Exception {
        for (int i = 0; i < 2; i++) {
            String response = mockMvc.perform(post("/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
            Map<String, Object> post = objectMapper.readValue(response, typeReference);

            // The id of the post should be auto-generated, and increment up with each new post.
            assertEquals(i + 1, post.get("id"));
            assertEquals("test author", post.get("author"));
            assertEquals("test content", post.get("content"));
        }
    }

    @Test
    public void testLargeContent() throws Exception {
        // You will need to annotate the Post's content field to handle large values.
        Map<String, Object> post = new HashMap<>();
        post.put("author", "test");
        post.put("content", generateContent(1024));

        mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(post)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testPostValidation() throws Exception {
        String emptyAuthor = "{ \"author\": \"\", \"content\": \"test content\" }";
        String missingAuthor = "{ \"content\": \"test content\" }";
        String emptyContent = "{ \"author\": \"test author\", \"content\": \"\" }";
        String missingContent = "{ \"author\": \"test author\" }";

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyAuthor))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingAuthor))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyContent))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingContent))
                .andExpect(status().isBadRequest());

    }

}
