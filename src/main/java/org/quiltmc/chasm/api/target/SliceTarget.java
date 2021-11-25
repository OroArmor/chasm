package org.quiltmc.chasm.api.target;

import org.quiltmc.chasm.api.tree.LinkedListNode;
import org.quiltmc.chasm.api.tree.ListNode;
import org.quiltmc.chasm.api.tree.Node;
import org.quiltmc.chasm.internal.metadata.PathMetadata;

public class SliceTarget implements Target {
    private final PathMetadata path;
    // NOTE! "Virtual Index". Divide by two for actual list index
    private int startIndex;
    private int endIndex;

    public SliceTarget(ListNode listNode, int start, int end) {
        this.path = listNode.getMetadata().get(PathMetadata.class);
        assert this.path != null;
        this.startIndex = start;
        this.endIndex = end;
    }

    public PathMetadata getPath() {
        return path;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public boolean contains(Target other) {
        if (other instanceof NodeTarget) {
            NodeTarget nodeTarget = (NodeTarget) other;
            PathMetadata targetPath = nodeTarget.getPath();
            if (this.path.startsWith(targetPath)) {
                return false;
            }
            if (nodeTarget.getPath().startsWith(this.path)) {
                PathMetadata.Entry index = targetPath.get(this.path.size());
                if (index.isInteger()) {
                    int intIndex = index.asInteger();
                    return startIndex / 2 <= intIndex && intIndex < endIndex / 2;
                } else {
                    throw new RuntimeException("Unexpected index type");
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean overlaps(Target other) {
        if (other instanceof NodeTarget) {
            return false;
        } else if (other instanceof SliceTarget) {
            SliceTarget sliceTarget = (SliceTarget) other;
            
            if (!this.path.equals(sliceTarget.path)) {
                return false;
            }

            if (this.startIndex > sliceTarget.startIndex) {
                return other.overlaps(this);
            }

            if (this.startIndex == sliceTarget.startIndex) {
                return false;
            }

            return sliceTarget.startIndex < this.endIndex && this.endIndex < sliceTarget.endIndex;
        } else {
            throw new RuntimeException("Unexpected target type");
        }
    }

    @Override
    public Node resolve(Node root) {
        Node parent = path.resolve(root);
        if (parent instanceof ListNode) {
            ListNode slice = new LinkedListNode();
            int realStart = startIndex / 2;
            int realEnd = endIndex / 2;
            for (int i = realStart; i < realEnd; i++) {
                slice.add(((ListNode) parent).get(i));
            }
            return slice;
        } else {
            throw new UnsupportedOperationException("Invalid slice into non-list");
        }
    }
}
