/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors.impl;

import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;
import cu.uci.plagicoj.detectors.Detector;
import java.util.List;
import org.antlr.runtime.Token;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.detectors.DetectorMatch;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.detectors.SourceCodeDetectorResult;

/**
 *
 * @author Leandro
 */
public class SourceCodeDetectorResultImpl extends DetectorResultImpl implements SourceCodeDetectorResult {
    
    protected SourceCode sourceSourceCode;
    protected SourceCode destinationSourceCode;
    protected List<DetectorMatch> matches;
    protected List<Token> sourceTokens;
    protected List<Token> destinationTokens;
    
    public static SourceCodeDetectorResultImpl create(SourceCode sourceSourceCode, SourceCode destinationSourceCode, Detector origin,double dictum, List<DetectorMatch> matches, List<Token> sourceTokens, List<Token> destinationTokens) {
        return new SourceCodeDetectorResultImpl(sourceSourceCode, destinationSourceCode,origin, dictum, matches, sourceTokens, destinationTokens);
    }
    
    private SourceCodeDetectorResultImpl(SourceCode sourceSourceCode,
     SourceCode destinationSourceCode,Detector origin,
     double dictum,
     List<DetectorMatch> matches,
     List<Token> sourceTokens,
     List<Token> destinationTokens){
        super(origin,dictum);
        
        this.sourceSourceCode= sourceSourceCode;
        this.destinationSourceCode = destinationSourceCode;        
        this.matches = matches;
        this.sourceTokens = sourceTokens;
        this.destinationTokens = destinationTokens;
    }

//    public String getDictumStringFormat() {
//        char dictum[] = String.valueOf(getDictum() * 100).toCharArray();
//        StringBuilder dictumF = new StringBuilder("");
//        for (int i = 0; i < dictum.length; ++i) {
//            if (dictum[i] == '.' || dictum[i] == ',') {
//                break;
//            }
//
//            dictumF.append(dictum[i]);
//        }
//
//        return dictumF.append("%").toString();
//    }

    
//
//    public List<DetectorMatchFormat> getPlagiCOJDetectorMatchesFormat() {
//
//        List<DetectorMatchFormat> answer = new ArrayList<DetectorMatchFormat>();
//        
//        try {
//            int i = 0, j = 0, i2, j2, k2, i1 = 0, j1 = 0, k1 = 0;
//            int l1 = getSourceTokens().size();
//            int l2 = getDestinationTokens().size();
//            List<DetectorMatch> blocks = this.matches;
//            Collections.sort(blocks);
//
//            List<DetectorMatch> non_adjacent = new ArrayList<DetectorMatch>();
//            non_adjacent.add(new DetectorMatchImpl(0, 0, 10));
//            for (DetectorMatch idx : blocks) {
//                DetectorMatch block = idx;                
//                i2 = block.getSourceIndex();
//                j2 = block.getDestinationIndex();
//                k2 = block.getLength();
//                if (i1 + k1 == i2 && j1 + k1 == j2) {
//                    k1 += k2;
//                } else {
//                    if (k1 != 0) {
//                        non_adjacent.add(new DetectorMatchImpl(i1, j1, k1));
//                    }
//                    i1 = i2;
//                    j1 = j2;
//                    k1 = k2;
//                }
//            }
//
//            if (k1 != 0) {
//                non_adjacent.add(new DetectorMatchImpl(i1, j1, k1));
//            }
//            non_adjacent.add(new DetectorMatchImpl(l1, l2, 0));
//            blocks = non_adjacent;
//
//            for (DetectorMatch block : blocks) {
//                int ai = block.getSourceIndex();
//                int bj = block.getDestinationIndex();
//                int size = block.getLength();
//                DiffTag tag = null;
//                if (i < ai && j < bj) {
//                    tag = DiffTag.replace;
//                } else if (i < ai) {
//                    tag = DiffTag.delete;
//                } else if (j < bj) {
//                    tag = DiffTag.insert;
//                }
//                if (tag != null) {
//                    answer.add(new DetectorMatchFormat(tag, i, ai, j, bj));
//                }
//                i = ai + size;
//                j = bj + size;
//                if (size != 0) {
//                    answer.add(new DetectorMatchFormat(DiffTag.equal, ai, i, bj, j));
//                }
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(SourceCodeDetectorResultImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return answer;
//    }


//    /**
//     * @return the sourceTokens
//     */
//    public List<Token> getSourceTokens() {
//        if (sourceTokens == null){
//            try {
//                sourceTokens = Utils.getTokens(source);
//            } catch (Exception ex) {
//                Logger.getLogger(SourceCodeDetectorResultImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return sourceTokens;
//    }
//
//    /**
//     * @param sourceTokens the sourceTokens to set
//     */
//    public void setSourceTokens(List<Token> sourceTokens) {
//        this.sourceTokens = sourceTokens;
//    }

//    /**
//     * @return the destinationTokens
//     */
//    public List<Token> getDestinationTokens() {
//        if (destinationTokens == null){
//            try {
//                destinationTokens = Utils.getTokens(destination);
//            } catch (Exception ex) {
//                Logger.getLogger(SourceCodeDetectorResultImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return destinationTokens;
//    }
//
//    /**
//     * @param destinationTokens the destinationTokens to set
//     */
//    public void setDestinationTokens(List<Token> destinationTokens) {
//        this.destinationTokens = destinationTokens;
//    }
    
    @Override
    public SourceCode getSourceSourceCode() {
        return sourceSourceCode;
    }

    @Override
    public SourceCode getDestinationSourceCode() {
        return destinationSourceCode;
    }

    @Override
    public List<DetectorMatch> getMatches() {
        return matches;
    }

    @Override
    public List<Token> getSourceTokens() {
        return sourceTokens;
    }

    @Override
    public List<Token> getDestinationTokens() {
        return destinationTokens;
    }
}
