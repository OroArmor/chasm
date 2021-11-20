package org.quiltmc.chasm.asm.writer;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.RecordComponentVisitor;
import org.quiltmc.chasm.NodeConstants;
import org.quiltmc.chasm.tree.ListNode;
import org.quiltmc.chasm.tree.MapNode;
import org.quiltmc.chasm.tree.Node;
import org.quiltmc.chasm.tree.ValueNode;

@SuppressWarnings("unchecked")
public class ChasmRecordComponentWriter {
    private final MapNode componentNode;

    public ChasmRecordComponentWriter(MapNode componentNode) {
        this.componentNode = componentNode;
    }

    private void visitAttributes(RecordComponentVisitor componentVisitor) {
        for (Node n : (ListNode) componentNode.get(NodeConstants.ATTRIBUTES)) {
            componentVisitor.visitAttribute(((ValueNode<Attribute>) n).getValue());
        }
    }

    private void visitAnnotations(RecordComponentVisitor componentVisitor) {
        for (Node n : (ListNode) componentNode.get(NodeConstants.ANNOTATIONS)) {
            ChasmAnnotationWriter writer = new ChasmAnnotationWriter(n);
            writer.visitAnnotation(componentVisitor::visitAnnotation, componentVisitor::visitTypeAnnotation);
        }
    }

    public void visitRecordComponent(ClassVisitor visitor) {
        String name = componentNode.get(NodeConstants.NAME).getAsString();
        String descriptor = componentNode.get(NodeConstants.DESCRIPTOR).getAsString();
        String signature = componentNode.get(NodeConstants.SIGNATURE).getAsString();

        RecordComponentVisitor componentVisitor = visitor.visitRecordComponent(name, descriptor, signature);

        // visitAnnotation/visitTypeAnnotation
        visitAnnotations(componentVisitor);

        // visitAttribute
        visitAttributes(componentVisitor);

        // visitEnd
        componentVisitor.visitEnd();
    }
}
