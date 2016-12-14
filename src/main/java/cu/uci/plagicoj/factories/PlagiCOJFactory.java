/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.factories;

import cu.uci.plagicoj.PlagiCOJResult;
import cu.uci.plagicoj.detectors.Detector;
import cu.uci.plagicoj.detectors.DetectorMatch;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.detectors.SourceCodeDetectorResult;
import cu.uci.plagicoj.detectors.impl.DetectorMatchImpl;
import cu.uci.plagicoj.detectors.impl.DetectorResultImpl;
import cu.uci.plagicoj.detectors.impl.SourceCodeDetectorResultImpl;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.impl.PlagiCOJResultImpl;
import java.util.List;
import org.antlr.runtime.Token;

public class PlagiCOJFactory {

    
    public static PlagiCOJResult createPlagiCOJResult(List<DetectorResult> detectorResults, double dictum) {
        return PlagiCOJResultImpl.create(detectorResults,dictum);
    }
    
    public static PlagiCOJResult createPlagiCOJResult(PlagiCOJResult.SpecialResult flag) {
        return PlagiCOJResultImpl.create(flag);
    }
    
    public static DetectorResult createDetectorResult(Detector origin,double dictum){
        return DetectorResultImpl.create(origin, dictum);
    }
    
    public static SourceCodeDetectorResult createSourceCodeDetectorResult(SourceCode sourceSourceCode, SourceCode destinationSourceCode, Detector origin, double dictum, List<DetectorMatch> matches, List<Token> sourceTokens, List<Token> destinationTokens) {
        return SourceCodeDetectorResultImpl.create(sourceSourceCode, destinationSourceCode,origin, dictum, matches, sourceTokens, destinationTokens);
    }

    public static DetectorMatch createDetectorMatch(int sourceIndex, int destinationIndex, int length) {
        return DetectorMatchImpl.create(sourceIndex, destinationIndex, length);
    }

    
}
