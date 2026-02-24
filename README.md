
# 📦 Spring Batch V2 – Procesamiento por Lotes (CSV → MySQL → CSV)

## 📌 Descripción

Proyecto desarrollado con **Spring Boot + Spring Batch** que procesa un archivo CSV de empleados, transforma los datos y:

1. Inserta los registros procesados en MySQL.
2. Genera un archivo CSV final con el salario total.

Este proyecto demuestra el uso de:

* Job y Step
* Reader, Processor, Writer
* Chunk-oriented processing
* JobRepository
* RunIdIncrementer
* Configuración automática con Spring Boot

---

# 🏗 Arquitectura

```
STEP 1
empleados.csv → Processor → MySQL (empleados_procesados)

STEP 2
MySQL → Processor → reporte-empleados.csv
```

### Tecnologías

* Java 17
* Spring Boot 3.x
* Spring Batch
* MySQL
* Maven

---

# 📂 Estructura del Proyecto

```
src/main/java/com/academia/batch
│
├── BatchConfig.java
├── Empleado.java
├── EmpleadoReporte.java
├── EmpleadoProcessor.java
├── ReporteProcessor.java
└── SpringBatchV2Application.java

src/main/resources
│
├── empleados.csv
└── application.properties
```

---

# ⚙️ Configuración de Base de Datos

## Crear base de datos

```sql
CREATE DATABASE academia;
USE academia;
```

## Crear tabla de negocio

```sql
CREATE TABLE empleados_procesados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(50) NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    bono DECIMAL(10,2) NOT NULL
);
```

Las tablas `BATCH_*` se crean automáticamente.

---

# ⚙️ application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/academia
spring.datasource.username=alumno
spring.datasource.password=alumno123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.batch.jdbc.initialize-schema=always
spring.batch.job.enabled=true
```

---

# 📄 Archivo de Entrada

Ubicación:

```
src/main/resources/empleados.csv
```

Formato:

```csv
nombre,departamento,salario
Juan Perez,Ventas,25000
Maria Lopez,TI,35000
...
```

---

# ▶️ Ejecución

## Desde IDE

Ejecutar:

```
SpringBatchV2Application.java
```

## Desde consola

```bash
mvn clean install
mvn spring-boot:run
```

---

# 🔄 Flujo del Procesamiento

## STEP 1

* Lee CSV
* Convierte nombre a mayúsculas
* Calcula bono (10%)
* Inserta en MySQL

## STEP 2

* Lee desde MySQL
* Calcula salarioTotal = salario + bono
* Genera `reporte-empleados.csv`

---

# 📊 Verificación

Consultar datos procesados:

```sql
SELECT * FROM empleados_procesados;
```

Consultar ejecución del Job:

```sql
SELECT * FROM BATCH_JOB_EXECUTION;
SELECT step_name, read_count, write_count
FROM BATCH_STEP_EXECUTION;
```

---

# 🧠 Conceptos Aplicados

* Job → Trabajo completo
* Step → Fase del proceso
* ItemReader → Lectura de datos
* ItemProcessor → Transformación
* ItemWriter → Escritura
* chunk(3) → Procesamiento por bloques
* JobRepository → Metadatos y control de ejecución
* RunIdIncrementer → Permite re-ejecuciones

---

# 🎯 Objetivo Académico

Este proyecto tiene como finalidad comprender:

* Procesamiento por lotes empresarial
* Control transaccional
* Separación de responsabilidades
* Arquitectura robusta con Spring Batch

---
