#!/bin/bash
# Script to create a new Helidon microservice from the template

# Check if service name was provided
if [ $# -eq 0 ]; then
    echo "Please provide a service name (e.g. './create-service.sh video-service')"
    exit 1
fi

SERVICE_NAME=$1
PACKAGE_NAME=$(echo $SERVICE_NAME | sed 's/-service//')
GROUP_ID="com.xeon2035"
ARTIFACT_ID=$SERVICE_NAME
MAIN_CLASS="${GROUP_ID}.${PACKAGE_NAME}.Main"
BASE_PACKAGE_DIR="src/main/java/com/xeon2035/${PACKAGE_NAME}"
RESOURCE_DIR="src/main/resources"

# Create project structure
mkdir -p $SERVICE_NAME/$BASE_PACKAGE_DIR/utils
mkdir -p $SERVICE_NAME/$RESOURCE_DIR

# Create pom.xml
cat > $SERVICE_NAME/pom.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>io.helidon.applications</groupId>
        <artifactId>helidon-mp</artifactId>
        <version>4.2.0</version>
        <relativePath/>
    </parent>

    <groupId>${GROUP_ID}</groupId>
    <artifactId>${ARTIFACT_ID}</artifactId>
    <name>\${project.artifactId}</name>

    <properties>
        <mainClass>${MAIN_CLASS}</mainClass>
    </properties>

    <dependencies>
        <dependency>
            <groupId>io.helidon.microprofile.bundles</groupId>
            <artifactId>helidon-microprofile</artifactId>
        </dependency>
    </dependencies>

    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.wav</include>
                    <include>**/*.mp4</include>
                    <include>**/*.jpg</include>
                    <include>**/*.png</include>
                    <include>**/*.gif</include>
                    <include>**/*.webp</include>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-libs</id>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>io.smallrye</groupId>
                <artifactId>jandex-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>make-index</id>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Create AudioServiceServer.java
cat > $SERVICE_NAME/$BASE_PACKAGE_DIR/AudioServiceMain.java << EOF
package ${GROUP_ID}.${PACKAGE_NAME};

import ${GROUP_ID}.${PACKAGE_NAME}.utils.ClearTerminal;
import ${GROUP_ID}.${PACKAGE_NAME}.utils.LogUtil;
import io.helidon.microprofile.server.Server;

import java.io.IOException;
import java.util.logging.Logger;

public final class Main {

    private static final Logger LOGGER = LogUtil.getLogger(Main.class);

    private Main() {
    }

    public static void main(final String[] args) throws IOException {
        // Start the server
        Server server = startServer();

        // Log the server startup and URL
        LOGGER.info("Server started on http://localhost:" + server.port());

        LOGGER.info(String.format("\\nEndpoints available: \\n" +
                        "- ${PACKAGE_NAME}: http://localhost:%d/${PACKAGE_NAME}/{filename}",
                server.port()));
    }
        
    static Server startServer() {
        ClearTerminal.clearTerminal();

        return Server.builder().port(8080)
                .build().start();
    }
}
EOF

# Create RestApplication.java
cat > $SERVICE_NAME/$BASE_PACKAGE_DIR/RestApplication.java << EOF
package ${GROUP_ID}.${PACKAGE_NAME};

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("/")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
                ${PACKAGE_NAME^}Resource.class
                // Add other resources as needed
        );
    }
}
EOF

# Create Resource.java
cat > $SERVICE_NAME/$BASE_PACKAGE_DIR/${PACKAGE_NAME^}Resource.java << EOF
package ${GROUP_ID}.${PACKAGE_NAME};

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

@Path("/${PACKAGE_NAME}")
@RequestScoped
public class ${PACKAGE_NAME^}Resource {
    private static final Logger logger = Logger.getLogger(${PACKAGE_NAME^}Resource.class.getName());

    @GET
    @Path("/{fileName}")
    @Produces("${PACKAGE_NAME}/file") // Change this to appropriate content type
    public Response get${PACKAGE_NAME^}(@PathParam("fileName") String fileName) {
        logger.info("Request received for ${PACKAGE_NAME}: " + fileName);
        
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("${PACKAGE_NAME}/" + fileName);
            
            if (inputStream == null) {
                logger.warning("File not found: " + fileName);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("File not found: " + fileName)
                        .build();
            }
            
            byte[] data = inputStream.readAllBytes();
            inputStream.close();
            
            logger.info("File found: " + fileName + ", size: " + data.length + " bytes");
            
            return Response.ok(data)
                    .type("${PACKAGE_NAME}/file") // Change to appropriate content type
                    .build();
        } catch (IOException e) {
            logger.severe("Error loading file: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading file: " + e.getMessage())
                    .build();
        }
    }
}
EOF

# Create ClearTerminal.java
cat > $SERVICE_NAME/$BASE_PACKAGE_DIR/utils/ClearTerminal.java << EOF
package ${GROUP_ID}.${PACKAGE_NAME}.utils;

public class ClearTerminal {
    public static void clearTerminal() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Ignore exceptions
        }
    }
}
EOF

# Create LogUtil.java
cat > $SERVICE_NAME/$BASE_PACKAGE_DIR/utils/LogUtil.java << EOF
package ${GROUP_ID}.${PACKAGE_NAME}.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(Level.INFO);
        
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("[%s] %s: %s%n",
                        record.getLevel(),
                        record.getLoggerName(),
                        record.getMessage());
            }
        });
        
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        
        return logger;
    }
}
EOF

# Create Dockerfile
cat > $SERVICE_NAME/Dockerfile << EOF
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/${ARTIFACT_ID}.jar /app/app.jar
COPY src/main/resources /app/resources

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
EOF

# Create docker-compose.yml
cat > $SERVICE_NAME/docker-compose.yml << EOF
version: '3.8'

services:
  ${SERVICE_NAME}:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    volumes:
      - ./src/main/resources:/app/resources
    restart: unless-stopped
EOF

# Create resource directory
mkdir -p $SERVICE_NAME/$RESOURCE_DIR/$PACKAGE_NAME

echo "Created microservice template for $SERVICE_NAME"
echo "To use it:"
echo "  1. cd $SERVICE_NAME"
echo "  2. Add your ${PACKAGE_NAME} files to $RESOURCE_DIR/$PACKAGE_NAME"
echo "  3. Update the content type in ${PACKAGE_NAME^}Resource.java"
echo "  4. Build with: mvn clean package"
echo "  5. Run with: java -jar target/${ARTIFACT_ID}.jar"
echo "  6. Or use Docker: docker-compose up -d"
