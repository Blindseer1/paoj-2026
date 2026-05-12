# Makefile — place at paoj/
SRC_BASE := src/com/pao/laboratory09
OUT_DIR  := out
TEST_SRC := src/com/pao/test
LIB      := lib/java-diff-utils-4.15.jar

.PHONY: all ex1 ex2 clean

ex1: compile-ex1
	java -cp $(OUT_DIR):$(LIB) com.pao.laboratory09.exercise1.Checker

ex2: compile-ex2
	java -cp $(OUT_DIR):$(LIB) com.pao.laboratory09.exercise2.Checker

compile-ex1:
	mkdir -p $(OUT_DIR)
	find $(TEST_SRC) $(SRC_BASE)/exercise1 -name "*.java" | xargs javac -cp $(LIB) -d $(OUT_DIR)

compile-ex2:
	mkdir -p $(OUT_DIR)
	find $(TEST_SRC) $(SRC_BASE)/exercise1 $(SRC_BASE)/exercise2 -name "*.java" | xargs javac -cp $(LIB) -d $(OUT_DIR)

clean:
	rm -rf $(OUT_DIR)
