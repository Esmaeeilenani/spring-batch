package com.batch.batchdemo.jobs;

import com.batch.batchdemo.student.Student;
import com.batch.batchdemo.student.StudentInfo;
import com.batch.batchdemo.student.StudentInfoRepo;
import com.batch.batchdemo.student.StudentRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

@Component
public class StudentDataTransferJob {

    private final JobBuilder jobBuilder;

    private final StepBuilder stepBuilder;

    public StudentDataTransferJob(JobBuilder jobBuilder, StepBuilder stepBuilder) {
        this.jobBuilder = jobBuilder;
        this.stepBuilder = stepBuilder;
    }

    @Bean
    public RepositoryItemReader<Student> studentRepositoryItemReader(StudentRepo studentRepo) {
        RepositoryItemReader<Student> studentRepositoryItemReader = new RepositoryItemReader<>();
        studentRepositoryItemReader.setRepository(studentRepo);
        studentRepositoryItemReader.setMethodName("findAll");
        studentRepositoryItemReader.setPageSize(10);
        HashMap<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id",Sort.Direction.ASC);
        studentRepositoryItemReader.setSort(sort);
        return studentRepositoryItemReader;
    }


    @Bean
    public ItemWriter <StudentInfo> studentInfoItemWriter (StudentInfoRepo studentInfoRepo){
        RepositoryItemWriter<StudentInfo> studentInfoItemWriter = new RepositoryItemWriter<>();
        studentInfoItemWriter.setRepository(studentInfoRepo);
        return studentInfoItemWriter;
    }


    @Bean
    public ItemProcessor<Student,StudentInfo> processor(){
        return (student)->{
          StudentInfo studentInfo = new StudentInfo();
          studentInfo.setFirstName(student.getFirstName());
          studentInfo.setLastName(student.getLastName());
          studentInfo.setGpa(student.getGpa());
          return studentInfo;
        };
    }


    @Bean
    public Step step1(ItemReader reader, ItemWriter writer, ItemProcessor processor, PlatformTransactionManager transactionManager){
        return stepBuilder.chunk(10,transactionManager)
                .reader(reader)
                .writer(writer)
                .processor(processor)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step){
        return jobBuilder
                .repository(jobRepository)
                .start(step)
                .build();
    }

}
