package simplf;

class Environment {
    public AssocList assocList;
    public final Environment outer;  

    Environment() {
        this.assocList = null;
        this.outer = null;  
    }

    Environment(Environment outer) {
        this.assocList = null;
        this.outer = outer;  
    }

    Environment(AssocList assocList, Environment outer) {
        this.assocList = assocList;
        this.outer = outer; 
    }

    Environment define(Token varToken, String name, Object value) {
        AssocList newAssocList = new AssocList(name, value, this.assocList);
        this.assocList = newAssocList;
        return new Environment(newAssocList, this); 
    }

    void assign(Token name, Object value) {
        for (Environment env = this; env != null; env = env.outer) {  
            for (AssocList assoc = env.assocList; assoc != null; assoc = assoc.next) {
                if (assoc.name.equals(name.lexeme)) {
                    assoc.value = value;
                    return;
                }
            }
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'");
    }

    public Object get(Token name) {
        for (AssocList assoc = this.assocList; assoc != null; assoc = assoc.next) {
            if (assoc.name.equals(name.lexeme)) {
                return assoc.value;
            }
        }

        if (this.outer != null) {
            return this.outer.get(name);  
        }

        throw new RuntimeException("Undefined variable '" + name.lexeme + "'");
    }


}
