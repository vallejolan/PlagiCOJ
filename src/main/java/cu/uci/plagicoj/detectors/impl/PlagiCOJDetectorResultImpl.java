/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.Token;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.detectors.Detector;
import cu.uci.plagicoj.detectors.DetectorMatch;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.utils.DiffTag;
import cu.uci.plagicoj.utils.Utils;

/**
 *
 * @author Leandro
 */
public class PlagiCOJDetectorResultImpl implements DetectorResult {

    
    private SourceCode source;
    private SourceCode destination;
    private double dictum;
    private List<DetectorMatch> matches;
    private List<Token> sourceTokens;
    private List<Token> destinationTokens;
    

    public String getDictumStringFormat() {
        char dictum[] = String.valueOf(getDictum() * 100).toCharArray();
        StringBuilder dictumF = new StringBuilder("");
        for (int i = 0; i < dictum.length; ++i) {
            if (dictum[i] == '.' || dictum[i] == ',') {
                break;
            }

            dictumF.append(dictum[i]);
        }

        return dictumF.append("%").toString();
    }

    public double getDictum() {

        return dictum;
    }

    /**
     * @param dictum the dictum to set
     */
    public void setDictum(double dictum) {
        this.dictum = dictum;
    }

    public PlagiCOJDetectorResultImpl() {
    }

    public PlagiCOJDetectorResultImpl(double dictum) {
        this.dictum = dictum;
    }

    public PlagiCOJDetectorResultImpl(double dictum, ArrayList<DetectorMatch> matches) {
        this.dictum = dictum;
        this.matches = matches;
    }

   
    public PlagiCOJDetectorResultImpl(double dictum, List<DetectorMatch> matches, SourceCode source, SourceCode destination) {
        this.dictum = dictum;
        this.matches = matches;
        this.source = source;
        this.destination = destination;
    }

    public List<DetectorMatchFormat> getPlagiCOJDetectorMatchesFormat() {

        List<DetectorMatchFormat> answer = new ArrayList<DetectorMatchFormat>();
        
        try {
            int i = 0, j = 0, i2, j2, k2, i1 = 0, j1 = 0, k1 = 0;
            int l1 = getSourceTokens().size();
            int l2 = getDestinationTokens().size();
            List<DetectorMatch> blocks = this.matches;
            Collections.sort(blocks);

            List<DetectorMatch> non_adjacent = new ArrayList<DetectorMatch>();
            non_adjacent.add(DetectorMatchImpl.create(0, 0, 10));
            for (DetectorMatch idx : blocks) {
                DetectorMatch block = idx;                
                i2 = block.getSourceIndex();
                j2 = block.getDestinationIndex();
                k2 = block.getLength();
                if (i1 + k1 == i2 && j1 + k1 == j2) {
                    k1 += k2;
                } else {
                    if (k1 != 0) {
                        non_adjacent.add(DetectorMatchImpl.create(i1, j1, k1));
                    }
                    i1 = i2;
                    j1 = j2;
                    k1 = k2;
                }
            }

            if (k1 != 0) {
                non_adjacent.add(DetectorMatchImpl.create(i1, j1, k1));
            }
            non_adjacent.add(DetectorMatchImpl.create(l1, l2, 0));
            blocks = non_adjacent;

            for (DetectorMatch block : blocks) {
                int ai = block.getSourceIndex();
                int bj = block.getDestinationIndex();
                int size = block.getLength();
                DiffTag tag = null;
                if (i < ai && j < bj) {
                    tag = DiffTag.replace;
                } else if (i < ai) {
                    tag = DiffTag.delete;
                } else if (j < bj) {
                    tag = DiffTag.insert;
                }
                if (tag != null) {
                    answer.add(new DetectorMatchFormat(tag, i, ai, j, bj));
                }
                i = ai + size;
                j = bj + size;
                if (size != 0) {
                    answer.add(new DetectorMatchFormat(DiffTag.equal, ai, i, bj, j));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PlagiCOJDetectorResultImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return answer;
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

    
    
    /**
     * @return the matches
     */
    public List<DetectorMatch> getMatches() {
        return matches;
    }

    /**
     * @param matches the matches to set
     */
    public void setMatches(List<DetectorMatch> matches) {
        this.matches = matches;
    }

    public int max(int a, int b) {
        return Math.max(a, b);
    }

    /**
     * @return the sourceTokens
     */
    public List<Token> getSourceTokens() {
        if (sourceTokens == null){
            try {
                sourceTokens = Utils.getTokens(source);
            } catch (Exception ex) {
                Logger.getLogger(PlagiCOJDetectorResultImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sourceTokens;
    }

    /**
     * @param sourceTokens the sourceTokens to set
     */
    public void setSourceTokens(List<Token> sourceTokens) {
        this.sourceTokens = sourceTokens;
    }

    /**
     * @return the destinationTokens
     */
    public List<Token> getDestinationTokens() {
        if (destinationTokens == null){
            try {
                destinationTokens = Utils.getTokens(destination);
            } catch (Exception ex) {
                Logger.getLogger(PlagiCOJDetectorResultImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return destinationTokens;
    }

    /**
     * @param destinationTokens the destinationTokens to set
     */
    public void setDestinationTokens(List<Token> destinationTokens) {
        this.destinationTokens = destinationTokens;
    }

    @Override
    public Detector getOrigin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
