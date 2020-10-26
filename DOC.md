# Documentation
## Building and execution
The project uses Gradle as a build system.
You can build a __jar__ file with __shadowJar__ task.
Or execute the code with __runJar__ task
inside your IDE or in the terminal with __gradlew__.

## Usage
### Build mode
Main mode that builds an index file for the specified text file.
The index file is needed to perform queries in other modes.

These files are specified with flags:
```
-i, --index Output index file
-f, --file Input text file
```
For more detailed description consult `--help`.

Consider following example as a quick start:
```bash
java -jar build/libs/runnable-1.0-SNAPSHOT.jar build -i index.i -f data/voyna_i_mir.txt
```

### Top mode
Find most used words including all its word forms.

All the parameters are specified with flags:
```
-i, --index Index file
-n, --number, -c, --count The number of top words to find
-m, --min Minimum length of words
```
Specifying minimum length of words can be 
useful to filter out prepositions and other auxiliary words.

For more detailed description consult `--help`.

Consider following example as a quick start:
```bash
java -jar build/libs/runnable-1.0-SNAPSHOT.jar top -i index.i -m 3 -n 10
```

### Lines mode
Find all lines on which the specified word and its word forms are mentioned.

All the parameters are specified with flags:
```
-i, --index Index file
-f, --file Input text file
-w, --word Queried word
```

The original text is not stored inside the index, so to output lines
it is required to specify text file again

For more detailed description consult `--help`.

Consider following example as a quick start:
```bash
java -jar build/libs/runnable-1.0-SNAPSHOT.jar lines -i index.i -f data/voyna_i_mir.txt -w "безухов"
```

### Word mode
Analyze usage of the specified word.

All the parameters are specified with flags:
```
-i, --index Index file
-w, --word Queried word
```

The analyzed properties are the number of
entries, used word forms and indices of pages
containing one of the word forms.

For more detailed description consult `--help`.

Consider following example as a quick start:
```bash
java -jar build/libs/runnable-1.0-SNAPSHOT.jar word -i index.i -w "бричка"
```