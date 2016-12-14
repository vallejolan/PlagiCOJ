/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors.impl;

import cu.uci.plagicoj.detectors.DetectorMatch;

/**
 *
 * @author Leandro
 */
public class DetectorMatchImpl implements DetectorMatch {

    public static DetectorMatchImpl create(int sourceIndex, int destinationIndex, int length) {
        return new DetectorMatchImpl(sourceIndex, destinationIndex, length);
    }

    private int sourceIndex;
    private int destinationIndex;
    private int length;

    public DetectorMatchImpl() {
    }

    private DetectorMatchImpl(int sourceIndex, int destinationIndex, int length) {
        this.sourceIndex = sourceIndex;
        this.destinationIndex = destinationIndex;
        this.length = length;
    }

    /**
     * @return the sourceIndex
     */
    @Override
    public int getSourceIndex() {
        return sourceIndex;
    }

    /**
     * @param sourceIndex the sourceIndex to set
     */
    @Override
    public void setSourceIndex(int sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    /**
     * @return the destinationIndex
     */
    @Override
    public int getDestinationIndex() {
        return destinationIndex;
    }

    /**
     * @param destinationIndex the destinationIndex to set
     */
    @Override
    public void setDestinationIndex(int destinationIndex) {
        this.destinationIndex = destinationIndex;
    }

    /**
     * @return the length
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int compareTo(DetectorMatch o) {
        if (sourceIndex != o.getSourceIndex()) {
            return sourceIndex - o.getSourceIndex();
        }
        return destinationIndex - o.getDestinationIndex();
    }
}
