package cu.uci.plagicoj.impl;

import cu.uci.plagicoj.PlagiCOJResult;
import java.util.*;
import cu.uci.plagicoj.detectors.DetectorResult;

/**
 *
 * @author Leandro González Vallejo
 */
public class PlagiCOJResultImpl implements PlagiCOJResult {
    private final List<DetectorResult> detectorResults;
    private Double dictum;
    private SpecialResult flag;
   
    public PlagiCOJResultImpl(){
        detectorResults = new ArrayList<>();
        this.flag = SpecialResult.NORMAL;
    }
    
    private PlagiCOJResultImpl(List<DetectorResult> detectorResults,double dictum) {
        this.detectorResults = detectorResults;
        this.dictum = dictum;
        this.flag = SpecialResult.NORMAL;
    }
    
    private PlagiCOJResultImpl(SpecialResult flag) {
        this();
        dictum = -1.0;
        this.flag = flag;
    }
    
    public static PlagiCOJResult create(List<DetectorResult> detectorResults, double dictum) {
        return new PlagiCOJResultImpl(detectorResults,dictum);
    }
    
    public static PlagiCOJResult create(SpecialResult flag) {
        return new PlagiCOJResultImpl(flag);
    }

    
    @Override
    public List<DetectorResult> getDetectorResults() {
        return detectorResults;
    }
    
    @Override
    public Double getDictum() {
        return dictum;
    }
    
    public void setDictum(Double dictum) {
        this.dictum = dictum;
    }

    @Override
    public SpecialResult getFlag() {
        return this.flag;
    }
}
