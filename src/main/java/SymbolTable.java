import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Variable> table = new HashMap<>();

    public void declare(String name, String type, Object value) {
        if (exists(name)) {
            throw new RuntimeException("Variable ya declarada: " + name);
        }
        table.put(name, new Variable(type, value));
    }

    public void assign(String name, Object value) {
        if (!exists(name)) {
            throw new RuntimeException("Variable no declarada: " + name);
        }
        table.get(name).setValue(value);
    }

    public Object getValue(String name) {
        if (!exists(name)) {
            throw new RuntimeException("Variable no declarada: " + name);
        }
        return table.get(name).getValue();
    }

    public String getType(String name) {
        Variable var = table.get(name);
        return var != null ? var.getType() : null;
    }

    // Nuevo método para chequear existencia
    public boolean exists(String name) {
        return table.containsKey(name);
    }
}

class Variable {
    private String type;
    private Object value;

    public Variable(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() { return type; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
}
