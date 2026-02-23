package com.academia.springBatchV2;

import org.springframework.batch.item.ItemProcessor;

public class EmpleadoProcessor implements ItemProcessor<Empleado, Empleado> {

	@Override
	public Empleado process(Empleado empleado) {
		empleado.setNombre(empleado.getNombre().toUpperCase());
		empleado.setBono(empleado.getSalario() * 0.10);
		return empleado;
	}
}