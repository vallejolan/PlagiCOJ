/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors.impl;

import java.util.ArrayList;
import java.util.List;
import cu.uci.plagicoj.entities.ProblemClassification;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.detectors.Detector;
import cu.uci.plagicoj.detectors.DetectorMatch;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.factories.PlagiCOJFactory;

public class ProblemProfileBasedDetector implements Detector {

    private final SourceCode sourceSubmission;
    private final SourceCode destinationSubmission;
    private final List<ProblemClassification> sourceProblemClassification;
    private final List<ProblemClassification> destinationProblemClassification;
    private List<DetectorMatch> plagiCOJDetectorMatches;
    
    
    public ProblemProfileBasedDetector(SourceCode sourceSubmission, SourceCode destinationSubmission, List<ProblemClassification> sourceProblemClassification, List<ProblemClassification> destinationProblemClassification) {
        this.sourceSubmission = sourceSubmission;
        this.destinationSubmission = destinationSubmission;
        this.sourceProblemClassification = sourceProblemClassification;
        this.destinationProblemClassification = destinationProblemClassification;
        
    }
    
    @Override
    public DetectorResult detectPlagiarism() {
        int lengthSourceCode = sourceSubmission.getCode().length();
        int lengthDestinationCode = destinationSubmission.getCode().length();
        
        plagiCOJDetectorMatches = new ArrayList<>();        
        
        int sumLengthProblemClassification = 0;

        for (int i = 0; i < sourceProblemClassification.size(); i++) {
            ProblemClassification sourcePClassification = sourceProblemClassification.get(i);

            for (int j = 0; j < destinationProblemClassification.size(); j++) {
                ProblemClassification destinationPClassification = destinationProblemClassification.get(j);
                if (sourcePClassification.compareTo(destinationPClassification) == 0) {
                    if (sourcePClassification.getIdClassification() != 0) {
                        sumLengthProblemClassification += sourcePClassification.getEstimatedCodeLength();
                    }
                    plagiCOJDetectorMatches.add(PlagiCOJFactory.createDetectorMatch(i, j, 1));
                }
            }
        }
        //@Lan - razón entre lo no común y el total
        double dictum = ((double)Math.min(Math.abs((double) (lengthSourceCode + lengthDestinationCode - 2 * sumLengthProblemClassification)),10000)/10000.0);
        
        return PlagiCOJFactory.createDetectorResult(this,dictum);
    }
}
