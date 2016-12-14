/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.Token;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.detectors.Detector;
import cu.uci.plagicoj.detectors.DetectorMatch;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.utils.Utils;

/**
 *
 * @author Leandro
 */
public class StringTilingFJDetector implements Detector {

    private SourceCode source;
    private SourceCode destination;
    private int minimumMatchLength;
    private ConcurrentLinkedDeque<DetectorMatchImpl> matches;
    List<Token> sourceCodeTokens;
    List<Token> destinationCodeTokens;
    boolean marksSource[];
    boolean marksDestination[];
    private AtomicInteger maxMatch;

    public StringTilingFJDetector(SourceCode source, SourceCode destination) {
        this(source, destination, 3);
    }

    public StringTilingFJDetector(SourceCode source, SourceCode destination, int minimumMatchLength) {
        this.source = source;
        this.destination = destination;
        this.minimumMatchLength = minimumMatchLength;
        maxMatch = new AtomicInteger();
        matches = new ConcurrentLinkedDeque<>();
    }

    public DetectorResult detectPlagiarism() {

        try {

            ArrayList<DetectorMatch> tiles = new ArrayList<DetectorMatch>();

            sourceCodeTokens = Utils.getTokens(source);
            destinationCodeTokens = Utils.getTokens(destination);

            int lenghtSource = sourceCodeTokens.size();
            int lenghtDestination = destinationCodeTokens.size();

            // String Tiling Algorithm start
            marksSource = new boolean[lenghtSource];
            marksDestination = new boolean[lenghtDestination];

            
            do {
                maxMatch.set(getMinimumMatchLength());
                matches.clear();

                StringTillingSubRutine stringTillingSubRutine = new StringTillingSubRutine(0, lenghtSource);
                ForkJoinPool pool = new ForkJoinPool();    
                pool.invoke(stringTillingSubRutine);
                pool.shutdown();
                pool.awaitTermination(1, TimeUnit.MINUTES);

                if (!matches.isEmpty()) {
                    List<DetectorMatchImpl> nonFilteredMatches = new ArrayList(matches);
                    Collections.sort(nonFilteredMatches);
                    
                    matches.clear();

                    Iterator<DetectorMatchImpl> it = nonFilteredMatches.iterator();

                    matches.add(it.next());

                    int length = matches.peekLast().getLength();

                    for (; it.hasNext();) {
                        DetectorMatchImpl matchAnt = matches.peekLast();
                        DetectorMatchImpl match = it.next();
                        if (matchAnt.getSourceIndex() + length <= match.getSourceIndex() && matchAnt.getDestinationIndex() + length <= match.getDestinationIndex()) {
                            matches.add(match);
                        }
                    }


                    while (!matches.isEmpty()) {
                        DetectorMatchImpl match = matches.pop();
                        for (int j = 0; j < match.getLength(); j++) {
                            marksSource[match.getSourceIndex() + j] = true;
                            marksDestination[match.getDestinationIndex()
                                    + j] = true;
                        }
                        tiles.add(match);
                    }
                }

            } while (maxMatch.get() > getMinimumMatchLength());
            
            int sumLength = 0;
            for (int i = 0; i < tiles.size(); i++) {
                sumLength += tiles.get(i).getLength();
            }

            return new PlagiCOJDetectorResultImpl((double) (2 * sumLength)
                    / (double) (lenghtSource + lenghtDestination), tiles,
                    source, destination);

        } catch (Exception ex) {
            Logger.getLogger(StringTilingFJDetector.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return null;
    }

    public SourceCode getSource() {
        return source;
    }

    public void setSource(SourceCode source) {
        this.source = source;
    }

    public SourceCode getDestination() {
        return destination;
    }

    public void setDestination(SourceCode destination) {
        this.destination = destination;
    }

    public int getMinimumMatchLength() {
        return minimumMatchLength;
    }

    public void setMinimumMatchLength(int minimumMatchLength) {
        this.minimumMatchLength = minimumMatchLength;
    }

    private class StringTillingSubRutine extends RecursiveAction {

        private static final long serialVersionUID = 1L;
        public final static int TASK_LEN = 60;
        private int start;
        private int end;
        private int lenghtDestination;
        private int lenghtSource;
        private Token[] sourceTokens;
        private Token[] destinationTokens;

        public StringTillingSubRutine(int start, int end) {
            this.start = start;
            this.end = end;
            this.lenghtDestination = destinationCodeTokens.size();
            this.lenghtSource = sourceCodeTokens.size();
            this.sourceTokens = sourceCodeTokens.toArray(new Token[0]);
            this.destinationTokens = destinationCodeTokens.toArray(new Token[0]);


        }

        private void detect(int start, int end) {

            for (int i = start; i < end; i++) {
                if (marksSource[i]) {
                    continue;
                }
                for (int j = 0; j < lenghtDestination; j++) {
                    if (marksDestination[j]) {
                        continue;
                    }
                    int length = 0;

                    while (i + length < lenghtSource
                            && j + length < lenghtDestination
                            && sourceTokens[i + length].getType()
                            == destinationTokens[j + length].getType()
                            && !marksSource[i + length]
                            && !marksDestination[j + length]) {
                        ++length;
                    }
                    if (maxMatch.get() <= length) {
                        if (maxMatch.get() < length) {
                            maxMatch.set(length);
                            matches.clear();
                        }

                        matches.add(DetectorMatchImpl.create(i, j, length));
                    }

                }
            }

        }

        @Override
        protected void compute() {
            int length = end - start;            
            if (length < TASK_LEN) {
                detect(start, end);
            } else {

                int middle = (start + end) / 2;
                StringTillingSubRutine subRutine1 = new StringTillingSubRutine(start, middle);
                StringTillingSubRutine subRutine2 = new StringTillingSubRutine(middle, end);               
                invokeAll(subRutine1, subRutine2);
            }
        }
    }
}
