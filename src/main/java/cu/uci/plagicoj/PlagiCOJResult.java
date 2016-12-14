/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cu.uci.plagicoj;

import cu.uci.plagicoj.detectors.DetectorResult;
import java.util.List;

/**
 *
 * @author leandro
 */
public interface PlagiCOJResult {

    List<DetectorResult> getDetectorResults();

    Double getDictum();
    
    SpecialResult getFlag();
    
    public enum SpecialResult {        
      NORMAL, SAME_LANGUAGE, SAME_USER , SAME_SUBMISSION
    }
    
}
