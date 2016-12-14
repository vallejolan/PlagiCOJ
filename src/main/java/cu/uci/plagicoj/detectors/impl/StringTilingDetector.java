/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors.impl;

import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.Token;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.detectors.Detector;
import cu.uci.plagicoj.detectors.DetectorMatch;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.detectors.SourceCodeDetector;
import cu.uci.plagicoj.factories.PlagiCOJFactory;
import cu.uci.plagicoj.utils.Utils;

/**
 *
 * @author Leandro
 */
public class StringTilingDetector implements Detector, SourceCodeDetector {

    private SourceCode source;
    private SourceCode destination;
    private int minimumMatchLength;

    public StringTilingDetector(SourceCode source, SourceCode destination) {
        this(source, destination, 3);
    }

    public StringTilingDetector(SourceCode source, SourceCode destination, int minimumMatchLength) {
        this.source = source;
        this.destination = destination;
        this.minimumMatchLength = minimumMatchLength;
    }

    @Override
    public DetectorResult detectPlagiarism(){
            ArrayList<DetectorMatch> tiles = new ArrayList<>();

            List<Token> sourceCodeTokens = Utils.getTokens(source);
            List<Token> destinationCodeTokens = Utils.getTokens(destination);

            int lenghtSource = sourceCodeTokens.size();
            int lenghtDestination = destinationCodeTokens.size();

            // String Tiling Algorithm start
            boolean marksSource[] = new boolean[lenghtSource];
            boolean marksDestination[] = new boolean[lenghtDestination];
            int maxMatch;

            do {
                maxMatch =this.minimumMatchLength;
                ArrayList<DetectorMatchImpl> matches = new ArrayList<>();

                for (int i = 0; i < lenghtSource; i++) {
                    for (int j = 0; j < lenghtDestination; j++) {
                        int length = 0;
                        while (i + length < lenghtSource
                                && j + length < lenghtDestination
                                && sourceCodeTokens.get(i + length).getType()
                                == destinationCodeTokens.get(j + length).getType()
                                && !marksSource[i + length]
                                && !marksDestination[j + length]) {
                            ++length;
                        }
                        if (length == maxMatch
                                && (matches.isEmpty() || (matches.get(
                                        matches.size() - 1).getSourceIndex()
                                + length <= i && matches.get(
                                        matches.size() - 1)
                                .getDestinationIndex()
                                + length <= j))) {
                            matches.add(DetectorMatchImpl.create(i, j, length));

                        } else if (maxMatch < length) {
                            maxMatch = length;
                            matches.clear();
                            matches.add(DetectorMatchImpl.create(i, j, length));
                        }

                    }
                }

                for (int i = 0; i < matches.size(); i++) {
                    for (int j = 0; j < matches.get(i).getLength(); j++) {
                        marksSource[matches.get(i).getSourceIndex() + j] = true;
                        marksDestination[matches.get(i).getDestinationIndex()
                                + j] = true;
                    }
                    tiles.add(matches.get(i));
                }
            } while (maxMatch > this.minimumMatchLength);
            int sumLength = 0;
            for (int i = 0; i < tiles.size(); i++) {
                sumLength += tiles.get(i).getLength();
            }

            double dictum = (double) (2 * sumLength)/ (double) (lenghtSource + lenghtDestination);
            
            return PlagiCOJFactory.createSourceCodeDetectorResult(source, destination,this, dictum, tiles, sourceCodeTokens, destinationCodeTokens);
    }
    
    @Override
    public String toString(){
        return "String Tiling Detector";
    }
}
