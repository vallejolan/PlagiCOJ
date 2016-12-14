/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.utils;

import java.util.List;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Token;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.factories.LexerFactory;

import static cu.uci.plagicoj.utils.Language.*;

/**
 *
 * @author Leandro
 */
public class Utils {

    public static List<Token> getTokens(SourceCode sourceCode){
        Lexer lexer = null;
        switch (sourceCode.getLanguage()) {
            case C:
                lexer = LexerFactory.CreateCLexer(sourceCode.getCode());
                break;

            case Cpp:
                lexer = LexerFactory.CreateCppLexer(sourceCode.getCode());

                break;
            case Java:
                lexer = LexerFactory.CreateJavaLexer(sourceCode.getCode());

                break;
            case CSharp:
                lexer = LexerFactory.CreateCSharpLexer(sourceCode.getCode());

                break;
            case Python:
                lexer = LexerFactory.CreatePythonLexer(sourceCode.getCode());

                break;
            case Pascal:
                lexer = LexerFactory.CreatePascalLexer(sourceCode.getCode());
                break;
        }
        
        return LexerFactory.CreateCommonTokenStream(lexer).getTokens();
    }
}
