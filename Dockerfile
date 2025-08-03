# Usa Tomcat 9 con Java 8 como base
FROM tomcat:9-jdk8

# Elimina las apps por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia el archivo .war generado por NetBeans
COPY dist/CRUD-MVC-JAVA.war /usr/local/tomcat/webapps/ROOT.war

# Expón el puerto que usará Render
EXPOSE 8080

# Comando que arranca Tomcat dentro del contenedor
CMD ["catalina.sh", "run"]
