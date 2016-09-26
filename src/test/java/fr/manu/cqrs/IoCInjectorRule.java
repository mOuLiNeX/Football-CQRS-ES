package fr.manu.cqrs;

import org.junit.rules.ExternalResource;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class IoCInjectorRule extends ExternalResource {

	private Injector injector;

	public <T> T getInstance(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	protected void before() throws Throwable {
		injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {

			}
		});
	}

	@Override
	protected void after() {
		injector = null;
	};

};