/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors;

import cu.uci.plagicoj.entities.SourceCode;
import java.util.List;
import org.antlr.runtime.Token;

/**
 *
 * @author Leandro
 */
public interface SourceCodeDetectorResult extends DetectorResult {

    public SourceCode getSourceSourceCode();

    public SourceCode getDestinationSourceCode() ;

    public List<DetectorMatch> getMatches() ;

    public List<Token> getSourceTokens() ;

    public List<Token> getDestinationTokens() ;
    
}
