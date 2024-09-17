package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.h2.Comment;
import ru.otus.hw.service.MigrationService;

@Configuration
public class CommentJobConfig {
    public static final int CHUNK_SIZE = 3;

    public static final String LIBRARY_MIGRATION_JOB = "libraryMigrationJob";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public JpaPagingItemReader<Comment> commentReader() {
        return new JpaPagingItemReaderBuilder<Comment>()
                .name("commentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Comment c")
                .pageSize(3)
                .build();
    }

    @Bean
    public ItemProcessor<Comment, ru.otus.hw.model.mongo.Comment> commentProcessor(MigrationService migrationService) {
        return migrationService::migrateComment;
    }

    @Bean
    public MongoItemWriter<ru.otus.hw.model.mongo.Comment> commentWriter() {
        return new MongoItemWriterBuilder<ru.otus.hw.model.mongo.Comment>()
                .template(mongoTemplate)
                .collection("Comment")
                .build();
    }

    @Bean
    public Step transformCommentStep(JpaPagingItemReader<Comment> commentReader,
                                    ItemProcessor<Comment, ru.otus.hw.model.mongo.Comment> commentProcessor,
                                    MongoItemWriter<ru.otus.hw.model.mongo.Comment> commentWriter) {
        return new StepBuilder("transformCommentStep", jobRepository)
                .<Comment, ru.otus.hw.model.mongo.Comment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .build();
    }
}
