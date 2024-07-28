# Use an official Maven image with OpenJDK
FROM maven:3.8.5-openjdk-8

# Set the working directory
WORKDIR /app

# Copy the Maven project files to the container
COPY . /app

# Build the Maven project
RUN mvn clean package

# Set the entry point to run the application
# Replace 'your-jar-file.jar' and 'com.yourpackage.YourMainClass' with the actual JAR file and main class of your project
ENTRYPOINT ["java", "-jar", "target/web-application-server-jar-with-dependencies.jar"]