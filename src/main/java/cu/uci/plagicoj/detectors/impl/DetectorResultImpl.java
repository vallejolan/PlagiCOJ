/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors.impl;

import cu.uci.plagicoj.detectors.Detector;
import cu.uci.plagicoj.detectors.DetectorResult;

/**
 *
 * @author Leandro
 */
public class DetectorResultImpl implements DetectorResult {

    protected double dictum;    
    private Detector origin;
    
    protected DetectorResultImpl(Detector origin, double dictum){
        this.dictum = dictum;
        this.origin = origin;
    }

    public static DetectorResultImpl create(Detector origin,double dictum) {
        return new DetectorResultImpl(origin,dictum);
    }
    
    @Override
    public double getDictum() {
        return dictum;
    }  

    @Override
    public Detector getOrigin() {
         return origin;
    }
}
