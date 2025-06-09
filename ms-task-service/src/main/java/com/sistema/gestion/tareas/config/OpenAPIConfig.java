package com.sistema.gestion.tareas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {


	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components().addRequestBodies("multipartRequestBody",
						new RequestBody().content(new Content().addMediaType(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE,
								new MediaType().schema(new Schema().type("object").addProperties("file",
										new Schema().type("string").format("binary")))))))
				.info(new Info()
						.title("API REST Task.ES")
						.description("Documentación de la API REST para el mantenimiento de los Task")
						.version("1.0.0")
						.contact(new Contact()
								.name("Task C.A.")
								.url("https://www.Task.com/contactenos")));
	}

}