
package simplf;
 
import java.util.List;

class SimplfFunction implements SimplfCallable {
    private final Stmt.Function declaration;
    private final Environment closure;

    // Constructor to store the function declaration and the closure (environment)
    SimplfFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    // Method to call the function
    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        // Create a new environment for this function
        Environment environment = new Environment(closure);
        
        // Bind arguments to parameter names in the new environment
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(null, declaration.params.get(i).lexeme, args.get(i));
        }

        // Execute the function body
        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            return returnValue.value; // Return statement
        }

        return null; // No explicit return value
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}

class Return extends RuntimeException {
    public final Object value;

    public Return(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
