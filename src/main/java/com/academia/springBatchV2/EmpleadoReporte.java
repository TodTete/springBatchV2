package com.academia.springBatchV2;

public class EmpleadoReporte {

	private String nombre;
	private String departamento;
	private double salario;
	private double bono;
	private double salarioTotal;

	public EmpleadoReporte() {
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	public double getBono() {
		return bono;
	}

	public void setBono(double bono) {
		this.bono = bono;
	}

	public double getSalarioTotal() {
		return salarioTotal;
	}

	public void setSalarioTotal(double salarioTotal) {
		this.salarioTotal = salarioTotal;
	}

	@Override
	public String toString() {
		return nombre + " | " + departamento +
				" | Salario: " + salario +
				" | Bono: " + bono +
				" | Total: " + salarioTotal;
	}
}
