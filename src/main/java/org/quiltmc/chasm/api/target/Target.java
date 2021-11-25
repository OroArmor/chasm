package org.quiltmc.chasm.api.target;

import org.quiltmc.chasm.api.tree.Node;

public interface Target {
    /**
     * True if this target fully contains the other.
     *
     * @param other The other target to check against.
     *
     * @return True if this target fully contains the other.
     */
    boolean contains(Target other);

    /**
     * True if the targets overlap, but neither fully contains the other.
     *
     * @param other The other target to check against.
     *
     * @return True if this target fully contains the other.
     */
    boolean overlaps(Target other);

    Node resolve(Node root);
}
