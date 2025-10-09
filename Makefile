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
