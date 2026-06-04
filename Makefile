# Makefile — place at paoj-2026/
SRC_BASE := src/com/pao/project
OUT_DIR  := out
TEST_SRC := src/com/pao/test

# Libraries
LIB_DIFF  := lib/java-diff-utils-4.15.jar
LIB_MYSQL := lib/mysql-connector-j-8.3.0.jar
CLASSPATH := $(OUT_DIR):$(LIB_DIFF):$(LIB_MYSQL)

DIFFUTILS_URL    := https://repo1.maven.org/maven2/io/github/java-diff-utils/java-diff-utils/4.15/java-diff-utils-4.15.jar
MYSQL_DRIVER_URL := https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar

.PHONY: all run compile copy-resources clean clean-all

all: compile copy-resources

# Download targets for dependencies
$(LIB_DIFF):
	mkdir -p lib
	curl -L -o $(LIB_DIFF) $(DIFFUTILS_URL)

$(LIB_MYSQL):
	mkdir -p lib
	curl -L -o $(LIB_MYSQL) $(MYSQL_DRIVER_URL)

# Compiles using a temporary sources file list to resolve inter-package dependencies natively
compile: $(LIB_DIFF) $(LIB_MYSQL)
	mkdir -p $(OUT_DIR)
	find $(TEST_SRC) $(SRC_BASE) -name "*.java" > sources.txt
	javac -cp "$(LIB_DIFF):$(LIB_MYSQL)" -sourcepath src -d $(OUT_DIR) @sources.txt
	@rm -f sources.txt

# Copies db.properties into out/ so the binary can find it at runtime
copy-resources:
	@if [ -d "$(SRC_BASE)/resources" ]; then \
		cp -r $(SRC_BASE)/resources/* $(OUT_DIR)/; \
	elif [ -d "resources" ]; then \
		cp -r resources/* $(OUT_DIR)/; \
	fi

# Boots your banking application
run: compile copy-resources
	java -cp $(CLASSPATH) com.pao.project.banking.Main

clean:
	rm -rf $(OUT_DIR)
	rm -f sources.txt

clean-all: clean
	rm -rf lib
