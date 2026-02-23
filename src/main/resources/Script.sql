CREATE DATABASE academia;
USE academia;

CREATE TABLE empleados_procesados (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      nombre VARCHAR(100) NOT NULL,
                                      departamento VARCHAR(50) NOT NULL,
                                      salario DECIMAL(10,2) NOT NULL,
                                      bono DECIMAL(10,2) NOT NULL
);