package simplf;

class Environment {
    public AssocList assocList;
    public final Environment outer;  // Changed from 'enclosing' to 'outer'

    // Constructor for global environment
    Environment() {
        this.assocList = null;
        this.outer = null;  // Set outer to null for global
    }

    // Constructor for local environments
    Environment(Environment outer) {
        this.assocList = null;
        this.outer = outer;  // Assign outer
    }

    // Constructor with AssocList
    Environment(AssocList assocList, Environment outer) {
        this.assocList = assocList;
        this.outer = outer;  // Assign outer
    }

    // Define a new variable in the current environment
    Environment define(Token varToken, String name, Object value) {
        AssocList newAssocList = new AssocList(name, value, this.assocList);
        this.assocList = newAssocList;
        return new Environment(newAssocList, this); // Create a new environment with the updated association list
    }

    // Assign a value to an existing variable
    void assign(Token name, Object value) {
        for (Environment env = this; env != null; env = env.outer) {  // Use outer here
            for (AssocList assoc = env.assocList; assoc != null; assoc = assoc.next) {
                if (assoc.name.equals(name.lexeme)) {
                    assoc.value = value;
                    return;
                }
            }
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'");
    }

    // Retrieve a variable's value
    public Object get(Token name) {
        for (AssocList assoc = this.assocList; assoc != null; assoc = assoc.next) {
            if (assoc.name.equals(name.lexeme)) {
                return assoc.value;
            }
        }
        // If not found, check the outer environment if it exists
        if (this.outer != null) {
            return this.outer.get(name);  // Use outer here
        }
        // If the variable is not found, throw an error
        throw new RuntimeException("Undefined variable '" + name.lexeme + "'");
    }


}
