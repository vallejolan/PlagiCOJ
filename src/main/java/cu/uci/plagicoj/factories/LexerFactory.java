/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.factories;

import cu.uci.plagicoj.lexers.impl.JavaLexer;
import cu.uci.plagicoj.lexers.impl.PythonLexer;
import cu.uci.plagicoj.lexers.impl.PascalLexer;
import cu.uci.plagicoj.lexers.impl.CppLexer;
import cu.uci.plagicoj.lexers.impl.CSharpLexer;
import cu.uci.plagicoj.lexers.impl.CLexer;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;

/**
 *
 * @author Leandro
 */
public class LexerFactory {

    public static CppLexer CreateCppLexer(String sourceCode) {
        return new CppLexer(new ANTLRStringStream(sourceCode));
    }

    public static CLexer CreateCLexer(String sourceCode) {
        return new CLexer(new ANTLRStringStream(sourceCode));
    }

    public static JavaLexer CreateJavaLexer(String sourceCode) {
        return new JavaLexer(new ANTLRStringStream(sourceCode));
    }

    public static PythonLexer CreatePythonLexer(String sourceCode) {
        return new PythonLexer(new ANTLRStringStream(sourceCode));
    }

    public static PascalLexer CreatePascalLexer(String sourceCode) {
        return new PascalLexer(new ANTLRStringStream(sourceCode));
    }

    public static CSharpLexer CreateCSharpLexer(String sourceCode) {
        return new CSharpLexer(new ANTLRStringStream(sourceCode));
    }
    
    public static CommonTokenStream CreateCommonTokenStream(Lexer lexer){
        
        return new CommonTokenStream(lexer);
        
    }
    
}
