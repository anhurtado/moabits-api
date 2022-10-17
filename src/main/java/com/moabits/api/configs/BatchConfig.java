package com.moabits.api.configs;

import com.moabits.api.entities.Movement;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
// @EnableScheduling
public class BatchConfig {
    @Bean
    public Job job(
        JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory,
        ItemReader<Movement> itemReader,
        ItemProcessor<Movement, Movement> itemProcessor,
        ItemWriter<Movement> itemWriter) {
        Step step = stepBuilderFactory
            .get("movement-step")
            .<Movement, Movement>chunk(100000)
            .reader(itemReader)
            .processor(itemProcessor)
            .writer(itemWriter)
            .build();

        return jobBuilderFactory.get("movement-load")
            .incrementer(new RunIdIncrementer())
            .start(step)
            .build();
    }

    @Bean
    public FlatFileItemReader<Movement> itemReader() {
        FlatFileItemReader<Movement> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/csv/MOCK_DATA.csv"));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Movement> lineMapper() {
        DefaultLineMapper<Movement> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("accountNumber", "amount", "clientId", "movementTypeId", "balance");

        BeanWrapperFieldSetMapper<Movement> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Movement.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
}
