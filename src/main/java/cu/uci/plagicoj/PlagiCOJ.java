package cu.uci.plagicoj;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import cu.uci.plagicoj.detectors.Detector;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.factories.PlagiCOJFactory;

public class PlagiCOJ {

    protected List<Detector> detectors;
    protected Map<Class<? extends Detector>, Double> detectorWeights;
    
    private PlagiCOJ(List<Detector> detectors) {
        detectorWeights = new HashMap<>();
        this.detectors = detectors;
        
        for (Detector detector: detectors){
            detectorWeights.put(detector.getClass(), 1.0);
        }
    }
    
    public static PlagiCOJ create(Detector detector) {
        List<Detector> detectors = new ArrayList<>();
        detectors.add(detector);
        return new PlagiCOJ(detectors);
    }
    
    public static PlagiCOJ create(List<Detector> detectors) {
        return new PlagiCOJ(detectors);
    }
    
    public void addDetector(Detector detector){
        this.detectors.add(detector);
        detectorWeights.put(detector.getClass(), 1.0);
    }
    
    public PlagiCOJResult detectPlagiarism() {          
        List<DetectorResult> detectorResults = new ArrayList<>();
        for (int i = 0; i < detectors.size(); ++i) {
            DetectorResult plagiCOJDetectorResult = detectors.get(i).detectPlagiarism();
            detectorResults.add(plagiCOJDetectorResult);
        }
        
        double dictum = mergeDetectorDictums(detectorResults);
        return PlagiCOJFactory.createPlagiCOJResult(detectorResults,dictum);
    }

    private double mergeDetectorDictums(List<DetectorResult> detectorResults) {
        double dictum = 0;
        double totalWeight = 0;
        
        Collection<Double> weights = detectorWeights.values();

        for (Double weight : weights) {
            totalWeight += weight;
        }

        for (DetectorResult plagiCOJDetectorResult : detectorResults) {
            double ponderation = detectorWeights.get(plagiCOJDetectorResult.getOrigin().getClass()) / totalWeight;
            dictum += plagiCOJDetectorResult.getDictum() * ponderation;
        }
        
        return dictum;
    }
   
}
