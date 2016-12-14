package cu.uci.plagicoj.detectors.impl;

import cu.uci.plagicoj.utils.DiffTag;


/**
 *
 * @author Leandro
 */
public class DetectorMatchFormat {

    private int sourceCodeStart;
    private int sourceCodeEnd;
    private int destinationCodeStart;
    private int destinationCodeEnd;
    private DiffTag plagiCOJDiffTag;

    public DetectorMatchFormat( DiffTag plagiCOJDiffTag,int sourceCodeStart, int sourceCodeEnd, int destinationCodeStart, int destinationCodeEnd) {
        this.plagiCOJDiffTag = plagiCOJDiffTag;
        this.sourceCodeStart = sourceCodeStart;
        this.sourceCodeEnd = sourceCodeEnd;
        this.destinationCodeStart = destinationCodeStart;
        this.destinationCodeEnd = destinationCodeEnd;
        
    }

    /**
     * @return the sourceCodeStart
     */
    public int getSourceCodeStart() {
        return sourceCodeStart;
    }

    /**
     * @param sourceCodeStart the sourceCodeStart to set
     */
    public void setSourceCodeStart(int sourceCodeStart) {
        this.sourceCodeStart = sourceCodeStart;
    }

    /**
     * @return the sourceCodeEnd
     */
    public int getSourceCodeEnd() {
        return sourceCodeEnd;
    }

    /**
     * @param sourceCodeEnd the sourceCodeEnd to set
     */
    public void setSourceCodeEnd(int sourceCodeEnd) {
        this.sourceCodeEnd = sourceCodeEnd;
    }

    /**
     * @return the destinationCodeStart
     */
    public int getDestinationCodeStart() {
        return destinationCodeStart;
    }

    /**
     * @param destinationCodeStart the destinationCodeStart to set
     */
    public void setDestinationCodeStart(int destinationCodeStart) {
        this.destinationCodeStart = destinationCodeStart;
    }

    /**
     * @return the destinationCodeEnd
     */
    public int getDestinationCodeEnd() {
        return destinationCodeEnd;
    }

    /**
     * @param destinationCodeEnd the destinationCodeEnd to set
     */
    public void setDestinationCodeEnd(int destinationCodeEnd) {
        this.destinationCodeEnd = destinationCodeEnd;
    }

    /**
     * @return the plagiCOJDiffTag
     */
    public DiffTag getDiffTag() {
        return plagiCOJDiffTag;
    }

    /**
     * @param plagiCOJDiffTag the plagiCOJDiffTag to set
     */
    public void setDiffTag(DiffTag plagiCOJDiffTag) {
        this.plagiCOJDiffTag = plagiCOJDiffTag;
    }
}
