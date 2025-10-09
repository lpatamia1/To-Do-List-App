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
	$(JVM) -cp $(BIN):lib/gson-2.10.1.jar todo

clean:
	rm -rf $(BIN)/*.class

test:
	mkdir -p $(BIN)
	$(JC) -cp .:lib/gson-2.10.1.jar:lib/junit-platform-console-standalone-1.10.2.jar -d $(BIN) $(SRC)/*.java tests/*.java
	java -jar lib/junit-platform-console-standalone-1.10.2.jar -cp .:lib/gson-2.10.1.jar:$(BIN) --scan-classpath
