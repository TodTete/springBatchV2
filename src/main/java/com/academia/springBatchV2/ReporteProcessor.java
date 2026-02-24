package com.academia.springBatchV2;

import org.springframework.batch.item.ItemProcessor;

public class ReporteProcessor implements ItemProcessor<Empleado, EmpleadoReporte> {

	@Override
	public EmpleadoReporte process(Empleado empleado) {
		EmpleadoReporte reporte = new EmpleadoReporte();
		reporte.setNombre(empleado.getNombre());
		reporte.setDepartamento(empleado.getDepartamento());
		reporte.setSalario(empleado.getSalario());
		reporte.setBono(empleado.getBono());
		reporte.setSalarioTotal(empleado.getSalario() + empleado.getBono());
		return reporte;
	}
}