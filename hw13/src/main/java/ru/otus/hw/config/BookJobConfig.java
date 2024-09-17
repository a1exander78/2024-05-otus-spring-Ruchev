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
import ru.otus.hw.model.h2.Book;
import ru.otus.hw.service.MigrationService;

@Configuration
public class BookJobConfig {
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
    public JpaPagingItemReader<Book> bookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Book b")
                .pageSize(3)
                .build();
    }

    @Bean
    public ItemProcessor<Book, ru.otus.hw.model.mongo.Book> bookProcessor(MigrationService migrationService) {
        return migrationService::migrateBook;
    }

    @Bean
    public MongoItemWriter<ru.otus.hw.model.mongo.Book> bookWriter() {
        return new MongoItemWriterBuilder<ru.otus.hw.model.mongo.Book>()
                .template(mongoTemplate)
                .collection("Book")
                .build();
    }

    @Bean
    public Step transformBookStep(JpaPagingItemReader<Book> bookReader,
                                    ItemProcessor<Book, ru.otus.hw.model.mongo.Book> bookProcessor,
                                    MongoItemWriter<ru.otus.hw.model.mongo.Book> bookWriter) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<Book, ru.otus.hw.model.mongo.Book>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .build();
    }
}
