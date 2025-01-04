package com.DbsBank.Application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
// import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
// import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "The DBS Bank Application Project", description = "Java Springboot REST API", version = "v1.01"
// contact=@Contact(
// name="",
// email="",
// url=""),
// license=@License(
// name="",
// url=""),

), externalDocs = @ExternalDocumentation(description = "Demo Bank application"))
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
