# Makefile — place at paoj-2026/
SRC_BASE := src/com/pao/laboratory10
OUT_DIR  := out
TEST_SRC := src/com/pao/test
LIB      := lib/java-diff-utils-4.15.jar

DIFFUTILS_URL := https://repo1.maven.org/maven2/io/github/java-diff-utils/java-diff-utils/4.15/java-diff-utils-4.15.jar

.PHONY: all ex1 ex2 clean

$(LIB):
	mkdir -p lib
	curl -L -o $(LIB) $(DIFFUTILS_URL)

ex1: compile-ex1
	java -cp $(OUT_DIR):$(LIB) com.pao.laboratory10.exercise1.Checker

ex2: compile-ex2
	java -cp $(OUT_DIR):$(LIB) com.pao.laboratory10.exercise2.Checker

compile-ex1: $(LIB)
	mkdir -p $(OUT_DIR)
	find $(TEST_SRC) $(SRC_BASE)/exercise1 -name "*.java" | xargs javac -cp $(LIB) -d $(OUT_DIR)

compile-ex2: $(LIB)
	mkdir -p $(OUT_DIR)
	find $(TEST_SRC) $(SRC_BASE)/exercise1 $(SRC_BASE)/exercise2 -name "*.java" | xargs javac -cp $(LIB) -d $(OUT_DIR)

clean:
	rm -rf $(OUT_DIR)

clean-all: clean
	rm -rf lib
