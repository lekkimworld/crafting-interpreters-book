

## Generate `Expr.java`
```
mvn package && JAR="target/jlox-1.0-SNAPSHOT.jar" && java -classpath $JAR com.craftinginterpreters.tool.GenerateAst /Users/mheisterberg/Programming/repos/crafting-interpreters-book/src/main/java/com/craftinginterpreters/lox
```

## Run `AstPrinter`
```
JAR="target/jlox-1.0-SNAPSHOT.jar" && java -classpath $JAR com.craftinginterpreters.lox.AstPrinter                            
```