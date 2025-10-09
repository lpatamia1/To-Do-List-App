JFLAGS = -cp .:lib/gson-2.10.1.jar
JC = javac
JVM = java

all: run

compile:
	$(JC) $(JFLAGS) todo.java Task.java ColorText.java LocalDateAdapter.java

run: compile
	$(JVM) $(JFLAGS) todo

clean:
	rm -f *.class
