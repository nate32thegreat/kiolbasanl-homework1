package com.cybr406.post;

import com.cybr406.post.problems.DatabaseSetupProblems;
import com.cybr406.post.problems.PostControllerPaginationProblems;
import com.cybr406.post.problems.PostControllerProblems;
import com.cybr406.post.problems.SetupProblems;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SetupProblems.class,
        DatabaseSetupProblems.class,
        PostControllerProblems.class,
        PostControllerPaginationProblems.class
})
public class HomeworkTestSuite {

}
