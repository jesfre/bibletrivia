package com.blogspot.jesfre.bibletrivia;

import com.blogspot.jesfre.bibletrivia.config.AsyncSyncConfiguration;
import com.blogspot.jesfre.bibletrivia.config.EmbeddedSQL;
import com.blogspot.jesfre.bibletrivia.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BibletriviaApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
