package com.academia.springBatchV2;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.JobRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	// ===================== STEP 1: CSV → MySQL =====================

	@Bean
	public FlatFileItemReader<Empleado> leerCsv() {
		return new FlatFileItemReaderBuilder<Empleado>()
				.name("empleadoReader")
				.resource(new ClassPathResource("empleados.csv"))
				.delimited()
				.names("nombre", "departamento", "salario")
				.targetType(Empleado.class)
				.linesToSkip(1)
				.build();
	}

	@Bean
	public EmpleadoProcessor procesarEmpleado() {
		return new EmpleadoProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Empleado> escribirEnBd(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Empleado>()
				.sql("INSERT INTO empleados_procesados (nombre, departamento, salario, bono) " +
						"VALUES (:nombre, :departamento, :salario, :bono)")
				.dataSource(dataSource)
				.beanMapped()
				.build();
	}

	@Bean
	public Step paso1(JobRepository jobRepository,
					  PlatformTransactionManager transactionManager,
					  FlatFileItemReader<Empleado> leerCsv,
					  EmpleadoProcessor procesarEmpleado,
					  JdbcBatchItemWriter<Empleado> escribirEnBd) {

		return new StepBuilder("paso1", jobRepository)
				.<Empleado, Empleado>chunk(3, transactionManager)
				.reader(leerCsv)
				.processor(procesarEmpleado)
				.writer(escribirEnBd)
				.build();
	}

	// ===================== STEP 2: MySQL → CSV =====================

	@Bean
	public JdbcCursorItemReader<Empleado> leerDeBd(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<Empleado>()
				.name("empleadoDbReader")
				.dataSource(dataSource)
				.sql("SELECT nombre, departamento, salario, bono FROM empleados_procesados")
				.rowMapper((rs, rowNum) -> {
					Empleado empleado = new Empleado();
					empleado.setNombre(rs.getString("nombre"));
					empleado.setDepartamento(rs.getString("departamento"));
					empleado.setSalario(rs.getDouble("salario"));
					empleado.setBono(rs.getDouble("bono"));
					return empleado;
				})
				.build();
	}

	@Bean
	public ReporteProcessor procesarReporte() {
		return new ReporteProcessor();
	}

	@Bean
	public FlatFileItemWriter<EmpleadoReporte> escribirCsv() {
		return new FlatFileItemWriterBuilder<EmpleadoReporte>()
				.name("reporteWriter")
				.resource(new FileSystemResource("reporte-empleados.csv"))
				.headerCallback(writer ->
						writer.write("nombre,departamento,salario,bono,salario_total"))
				.delimited()
				.names("nombre", "departamento", "salario", "bono", "salarioTotal")
				.build();
	}

	@Bean
	public Step paso2(JobRepository jobRepository,
					  PlatformTransactionManager transactionManager,
					  JdbcCursorItemReader<Empleado> leerDeBd,
					  ReporteProcessor procesarReporte,
					  FlatFileItemWriter<EmpleadoReporte> escribirCsv) {

		return new StepBuilder("paso2", jobRepository)
				.<Empleado, EmpleadoReporte>chunk(3, transactionManager)
				.reader(leerDeBd)
				.processor(procesarReporte)
				.writer(escribirCsv)
				.build();
	}

	// ===================== JOB =====================

	@Bean
	public Job procesarEmpleadosJob(JobRepository jobRepository,
									Step paso1,
									Step paso2) {

		return new JobBuilder("procesarEmpleadosJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(paso1)
				.next(paso2)
				.build();
	}
}