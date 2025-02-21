package org.quiltmc.chasm.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
import org.quiltmc.chasm.NodeConstants;
import org.quiltmc.chasm.tree.LinkedHashMapNode;
import org.quiltmc.chasm.tree.LinkedListNode;
import org.quiltmc.chasm.tree.ListNode;
import org.quiltmc.chasm.tree.MapNode;
import org.quiltmc.chasm.tree.ValueNode;

public class ChasmRecordComponentVisitor extends RecordComponentVisitor {
    private final ListNode annotations = new LinkedListNode();
    private final ListNode attributes = new LinkedListNode();

    public ChasmRecordComponentVisitor(int api, MapNode recordComponentNode) {
        super(api);

        recordComponentNode.put(NodeConstants.ANNOTATIONS, annotations);
        recordComponentNode.put(NodeConstants.ATTRIBUTES, attributes);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        MapNode annotation = new LinkedHashMapNode();
        ListNode values = new LinkedListNode();
        annotation.put(NodeConstants.DESCRIPTOR, new ValueNode<>(descriptor));
        annotation.put(NodeConstants.VISIBLE, new ValueNode<>(visible));
        annotation.put(NodeConstants.VALUES, new ValueNode<>(values));
        annotations.add(annotation);

        return new ChasmAnnotationVisitor(api, values);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        MapNode annotation = new LinkedHashMapNode();
        ListNode values = new LinkedListNode();
        annotation.put(NodeConstants.DESCRIPTOR, new ValueNode<>(descriptor));
        annotation.put(NodeConstants.VISIBLE, new ValueNode<>(visible));
        annotation.put(NodeConstants.VALUES, new ValueNode<>(values));
        annotation.put(NodeConstants.TYPE_REF, new ValueNode<>(typeRef));
        annotation.put(NodeConstants.TYPE_PATH, new ValueNode<>(typePath.toString()));
        annotations.add(annotation);

        return new ChasmAnnotationVisitor(api, values);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        attributes.add(new ValueNode<>(attribute));
    }

    @Override
    public void visitEnd() {
        // Nothing to do here
    }
}
