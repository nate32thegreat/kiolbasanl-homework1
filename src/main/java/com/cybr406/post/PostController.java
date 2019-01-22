package com.cybr406.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PostController
{
    @Autowired
    PostRepository postRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new PostValidator());
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> demo(@Valid @RequestBody Post post) {
        return new ResponseEntity<>(postRepository.save(post), HttpStatus.CREATED);
    }

    @GetMapping("/posts")
    public Page <Post> testPagination (Pageable pageable)
    {
        return postRepository.findAll(pageable);
    }


    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getSinglePost(@PathVariable Long id)
    {
        Optional <Post> result =  postRepository.findById(id);

        if (result.isPresent())
        {
            return new ResponseEntity<Post>(result.get(), HttpStatus.OK);        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

}
