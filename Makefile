JAVAC=javac
JAVA=java

CLASSES=$(patsubst src/%.java,bin/%.class,src/OS1Assignment.java)

all: $(CLASSES)

bin/%.class: src/%.java | bin
	$(JAVAC) -d bin $<

bin:
	mkdir bin

clean:
	rm -rf bin/*

run: $(CLASSES)
	$(JAVA) -cp bin src.OS1Assignment $(ARGS)