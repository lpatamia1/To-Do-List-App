# === Retro To-Do List App ===
# Java build & test automation
# =============================

JFLAGS = -cp .:lib/gson-2.10.1.jar
JC = javac
JVM = java
SRC = src
BIN = bin

# Default target
all: run

# 🔧 Compile source files into /bin
compile:
	@mkdir -p $(BIN)
	$(JC) $(JFLAGS) -d $(BIN) $(SRC)/*.java

# ▶️ Run app after compile
run: compile
	$(JVM) $(JFLAGS):$(BIN) todo

# 🧹 Clean compiled files
clean:
	@rm -rf $(BIN)/*.class

# 🧪 Run tests using JUnit 5
test:
	@mkdir -p $(BIN)
	$(JC) -cp .:lib/gson-2.10.1.jar:lib/junit-platform-console-standalone-1.10.2.jar -d $(BIN) $(SRC)/*.java tests/*.java
	$(JVM) -jar lib/junit-platform-console-standalone-1.10.2.jar -cp .:lib/gson-2.10.1.jar:$(BIN) --scan-classpath

# 🧮 Generate JaCoCo coverage data
coverage:
	@mkdir -p $(BIN)
	$(JC) -cp .:lib/gson-2.10.1.jar:lib/junit-platform-console-standalone-1.10.2.jar -d $(BIN) $(SRC)/*.java tests/*.java
	$(JVM) -javaagent:lib/jacocoagent.jar=destfile=jacoco.exec -jar lib/junit-platform-console-standalone-1.10.2.jar -cp .:lib/gson-2.10.1.jar:$(BIN) --scan-classpath
	@echo "\n🧾 Coverage data saved to jacoco.exec"

# 📊 Generate human-readable HTML report
htmlreport:
	@java -jar lib/jacococli.jar report jacoco.exec \
		--classfiles $(BIN) \
		--sourcefiles $(SRC) \
		--html coverage-report \
		--name "To-Do List App Coverage"
	@echo "\n📊 HTML coverage report generated at coverage-report/index.html"
# =============================