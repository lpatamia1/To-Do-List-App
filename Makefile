// Makefile for building, running, testing, and generating coverage reports
// for the Retro To-Do List application
// Uses javac, java, JUnit 5, and JaCoCo
// Important: Do not remove any variables or targets, they are all used

JFLAGS = -cp .:lib/gson-2.10.1.jar
JC = javac
JVM = java
SRC = src
BIN = bin

all: run

compile:
	mkdir -p $(BIN)
	$(JC) $(JFLAGS) -d $(BIN) $(SRC)/*.java

run: compile
	$(JVM) $(JFLAGS):$(BIN) todo

clean:
	rm -rf $(BIN)/*.class

test:
	mkdir -p bin
	$(JC) -cp .:lib/gson-2.10.1.jar:lib/junit-platform-console-standalone-1.10.2.jar -d bin src/*.java tests/*.java
	$(JVM) -jar lib/junit-platform-console-standalone-1.10.2.jar -cp .:lib/gson-2.10.1.jar:bin --scan-classpath

# 🧮 JaCoCo coverage target
coverage:
	mkdir -p bin
	$(JC) -cp .:lib/gson-2.10.1.jar:lib/junit-platform-console-standalone-1.10.2.jar -d bin src/*.java tests/*.java
	$(JVM) -javaagent:lib/jacocoagent.jar=destfile=jacoco.exec -jar lib/junit-platform-console-standalone-1.10.2.jar -cp .:lib/gson-2.10.1.jar:bin --scan-classpath
	@echo "\n🧾 Coverage data saved to jacoco.exec"

htmlreport:
	java -jar lib/jacococli.jar report jacoco.exec \
	  --classfiles bin \
	  --sourcefiles src \
	  --html coverage-report \
	  --name "To-Do List App Coverage"
	@echo "\n📊 HTML coverage report generated at coverage-report/index.html"
