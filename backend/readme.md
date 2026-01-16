Requisitos previos



1. Instalar Java JDK 21

 	Ir a: https://www.oracle.com/java/technologies/downloads/#java21

 	Descargar JDK 21 – Windows x64 Installer

2\. Verificar Java

 	Abrir una terminal y ejecutar java -version

 	Debe mostrar algo como java version "21"

3\. Verificar Maven Wrapper

 	mvnw

 	mvnw.cmd

 	.mvn/

4\. Abrir un terminal en backend y ejecutar

 	.\\mvnw spring-boot:run

5\. En consola debe aparecer algo como:
Tomcat started on port 8080

 	Started SentimentApiApplication

6\. Verficacion rápida en navegador web

 	http://localhost:8080/actuator/health

7\. Debe mostrar algo como

 	{"status":"UP"}



Utilización de H2 Console



1. El backend debe estar corriendo
2. Abrir H2 Console en el navegador: http://localhost:8080/h2-console
3. Llenar los campos de conexión:

&nbsp;	JDBC URL: jdbc:h2:mem:sentimentdb

&nbsp;	User Name: sa

&nbsp;	Password: vacio

&nbsp;	Luego presionar Connect

4\. Una vez conectado ejecutar este query para ver los resultados:

&nbsp;	SELECT \* FROM PREDICTIONS ORDER BY ID DESC;



