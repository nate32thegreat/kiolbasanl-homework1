package com.cybr406.post.problems;

import com.cybr406.post.PostApplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PostApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("pagination")
public class PostControllerPaginationProblems {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SuppressWarnings("unchecked")
    public void testPagination() throws Exception {
        String response = mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

        Map<String, Object> page = objectMapper.readValue(response, typeReference);
        assertNotNull(page);

        Map<String, Object> pageable = (Map) page.get("pageable");
        assertNotNull(pageable);
        assertTrue((boolean)page.get("first"));
        assertEquals(2, page.get("totalPages"));
        assertEquals(25, page.get("totalElements"));
        assertEquals(20, page.get("size"));
        assertEquals(0, page.get("number"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPaginationParams() throws Exception {
        String response = mockMvc.perform(get("/posts?size=10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

        Map<String, Object> page = objectMapper.readValue(response, typeReference);
        assertNotNull(page);

        assertTrue((boolean)page.get("first"));
        assertEquals(3, page.get("totalPages"));
        assertEquals(25, page.get("totalElements"));
        assertEquals(10, page.get("size"));

        response = mockMvc.perform(get("/posts?size=10&page=1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        page = objectMapper.readValue(response, typeReference);
        assertNotNull(page);

        assertFalse((boolean)page.get("first"));
        assertEquals(3, page.get("totalPages"));
        assertEquals(25, page.get("totalElements"));
        assertEquals(10, page.get("size"));
        assertEquals(1, page.get("number"));
    }

    @Test
    public void testSinglePost() throws Exception {
        String response = mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

        Map<String, Object> post = objectMapper.readValue(response, typeReference);
        assertNotNull(post);
        assertEquals("anonymous", post.get("author"));
        assertEquals("What do you call a belt made out of watches? A waist of time.", post.get("content"));

        response = mockMvc.perform(get("/posts/25"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        post = objectMapper.readValue(response, typeReference);
        assertNotNull(post);
        assertEquals("anonymous", post.get("author"));
        assertEquals("A man walks into a zoo, the only animal was a dog. It was a shitzu.", post.get("content"));

        mockMvc.perform(get("/posts/999"))
                .andExpect(status().isNotFound());
    }

}
