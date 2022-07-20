MAIN = DungeonController
JARS = ./jars/*
PACKAGE = graph

all:
	javac -cp .:$(JARS) $(PACKAGE)/*.java

run:
	java -cp .:$(JARS) $(PACKAGE).$(MAIN)

clear:
	rm $(PACKAGE)/*.class
