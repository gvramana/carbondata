/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on Aug 13, 2013 6:12:20 PM
 * Time to generate: 00:13.116 seconds
 *
 */

package com.huawei.unibi.molap.restructure.schema.updatemember;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MoveNodeAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return MoveNode.class;
    }
    
    public void testConstructor() throws Throwable {
        MoveNode moveNode = new MoveNode(new IIOMetadataNode());
        assertEquals("moveNode.getDimensions().size()", 0, moveNode.getDimensions().size());
    }
    
    public void testConstructor1() throws Throwable {
        NodeList nodeList = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Mockingbird.ignoreConstructorExceptions(UpdateMemberNode.class);
        Node node = (Node) Mockingbird.getProxyObject(Node.class);
        Element element = (Element) Mockingbird.getProxyObject(Element.class);
        NodeList nodeList2 = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Node node2 = (Node) Mockingbird.getProxyObject(Node.class);
        Element element2 = (Element) Mockingbird.getProxyObject(Element.class);
        NodeList nodeList3 = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Element element3 = (Element) Mockingbird.getProxyObject(Element.class);
        Element element4 = (Element) Mockingbird.getProxyObject(Element.class);
        Iterator iterator = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        Element element5 = (Element) Mockingbird.getProxyObject(Element.class);
        NodeList nodeList4 = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Element element6 = (Element) Mockingbird.getProxyObject(Element.class);
        NodeList nodeList5 = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Element element7 = (Element) Mockingbird.getProxyObject(Element.class);
        Element element8 = (Element) Mockingbird.getProxyObject(Element.class);
        NodeList nodeList6 = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Element element9 = (Element) Mockingbird.getProxyObject(Element.class);
        NodeList nodeList7 = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Node node3 = (Node) Mockingbird.getProxyObject(Node.class);
        Node node4 = (Node) Mockingbird.getProxyObject(Node.class);
        Mockingbird.enterRecordingMode();
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        Mockingbird.setReturnValue(nodeList.getLength(), 2);
        Mockingbird.setReturnValue(nodeList.item(0), node);
        Mockingbird.setReturnValue(node.getNodeType(), (short)0);
        Mockingbird.setReturnValue(nodeList.item(1), element);
        Mockingbird.setReturnValue(element.getNodeType(), (short)1);
        Mockingbird.setReturnValue(element.getElementsByTagName("Dimension"), nodeList2);
        Mockingbird.setReturnValue(nodeList2.getLength(), 2);
        Mockingbird.setReturnValue(nodeList2.item(0), node2);
        Mockingbird.setReturnValue(node2.getNodeType(), (short)0);
        Mockingbird.setReturnValue(nodeList2.item(1), element2);
        Mockingbird.setReturnValue(element2.getNodeType(), (short)1);
        Mockingbird.setReturnValue(element2.getAttribute("name"), "");
        ArrayList arrayList2 = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList2);
        Mockingbird.setReturnValue(false, element2, "getElementsByTagName", "(java.lang.String)org.w3c.dom.NodeList", nodeList3, 1);
        Mockingbird.setReturnValue(nodeList3.getLength(), 2);
        Mockingbird.setReturnValue(nodeList3.item(0), element3);
        Mockingbird.setReturnValue(element3.getNodeType(), (short)1);
        Mockingbird.setReturnValue(arrayList2.add(element3), false);
        Mockingbird.setReturnValue(nodeList3.item(1), element4);
        Mockingbird.setReturnValue(element4.getNodeType(), (short)1);
        Mockingbird.setReturnValue(arrayList2.add(element4), false);
        Mockingbird.setReturnValue(arrayList2.iterator(), iterator);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), element5);
        Dimension dimension = (Dimension) Mockingbird.getProxyObject(Dimension.class);
        Mockingbird.replaceObjectForRecording(Dimension.class, "<init>(java.lang.String)", dimension);
        Mockingbird.setReturnValue(false, element5, "getElementsByTagName", "(java.lang.String)org.w3c.dom.NodeList", nodeList4, 1);
        Mockingbird.setReturnValue(nodeList4.getLength(), 1);
        Mockingbird.setReturnValue(nodeList4.item(0), element6);
        Mockingbird.setReturnValue(element6.getNodeType(), (short)1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(UpdateMemberNode.class), "getLevels", "(org.w3c.dom.Element)java.util.List", null, 1);
        Mockingbird.setReturnValue(false, element5, "getElementsByTagName", "(java.lang.String)org.w3c.dom.NodeList", nodeList5, 1);
        Mockingbird.setReturnValue(nodeList5.getLength(), 1);
        Mockingbird.setReturnValue(nodeList5.item(0), element7);
        Mockingbird.setReturnValue(element7.getNodeType(), (short)1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(UpdateMemberNode.class), "getLevels", "(org.w3c.dom.Element)java.util.List", null, 1);
        Mockingbird.setReturnValue(arrayList.add(dimension), false);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), element8);
        Dimension dimension2 = (Dimension) Mockingbird.getProxyObject(Dimension.class);
        Mockingbird.replaceObjectForRecording(Dimension.class, "<init>(java.lang.String)", dimension2);
        Mockingbird.setReturnValue(false, element8, "getElementsByTagName", "(java.lang.String)org.w3c.dom.NodeList", nodeList6, 1);
        Mockingbird.setReturnValue(nodeList6.getLength(), 1);
        Mockingbird.setReturnValue(nodeList6.item(0), element9);
        Mockingbird.setReturnValue(element9.getNodeType(), (short)1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(UpdateMemberNode.class), "getLevels", "(org.w3c.dom.Element)java.util.List", null, 1);
        Mockingbird.setReturnValue(false, element8, "getElementsByTagName", "(java.lang.String)org.w3c.dom.NodeList", nodeList7, 1);
        Mockingbird.setReturnValue(nodeList7.getLength(), 2);
        Mockingbird.setReturnValue(nodeList7.item(0), node3);
        Mockingbird.setReturnValue(node3.getNodeType(), (short)0);
        Mockingbird.setReturnValue(nodeList7.item(1), node4);
        Mockingbird.setReturnValue(node4.getNodeType(), (short)0);
        Mockingbird.setReturnValue(arrayList.add(dimension2), false);
        Mockingbird.setReturnValue(iterator.hasNext(), false);
        Mockingbird.enterTestMode();
        MoveNode moveNode = new MoveNode(nodeList);
        assertNotNull("moveNode.getDimensions()", moveNode.getDimensions());
        assertInvocationCount(element5, "getElementsByTagName", 2);
        assertInvocationCount(element8, "getElementsByTagName", 2);
    }
    
    public void testGetDimensions() throws Throwable {
        ArrayList result = (ArrayList) new MoveNode(new IIOMetadataNode("testMoveNodeParam1")).getDimensions();
        assertEquals("result.size()", 0, result.size());
    }
    
    public void testGetMoveLevels() throws Throwable {
        MoveNode moveNode = new MoveNode(new IIOMetadataNode());
        Element element = (Element) Mockingbird.getProxyObject(Element.class);
        Mockingbird.enterRecordingMode();
        NodeList nodeList = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Mockingbird.setReturnValue(false, element, "getElementsByTagName", "(java.lang.String)org.w3c.dom.NodeList", new Object[] {"testString"}, nodeList, 1);
        Mockingbird.setReturnValue(false, nodeList, "getLength", "()int", new Object[] {}, new Integer(0), 1);
        Mockingbird.enterTestMode(MoveNode.class);
        Object result = callPrivateMethod("com.huawei.unibi.molap.restructure.schema.updatemember.MoveNode", "getMoveLevels", new Class[] {Element.class, String.class}, moveNode, new Object[] {element, "testMoveNodeFromOrTo"});
        assertNull("result", result);
    }
    
    public void testConstructorThrowsNullPointerException() throws Throwable {
        try {
            new MoveNode(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(MoveNode.class, ex);
        }
    }
    
    public void testGetMoveLevelsThrowsNullPointerException() throws Throwable {
        Element iIOMetadataNode = new IIOMetadataNode();
        MoveNode moveNode = new MoveNode(new IIOMetadataNode("testMoveNodeParam1"));
        try {
            callPrivateMethod("com.huawei.unibi.molap.restructure.schema.updatemember.MoveNode", "getMoveLevels", new Class[] {Element.class, String.class}, moveNode, new Object[] {iIOMetadataNode, "testMoveNodeFromOrTo"});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(IIOMetadataNode.class, ex);
            assertNull("(IIOMetadataNode) iIOMetadataNode.getNodeName()", iIOMetadataNode.getNodeName());
        }
    }
    
    public void testGetMoveLevelsThrowsNullPointerException1() throws Throwable {
        MoveNode moveNode = new MoveNode(new IIOMetadataNode("testMoveNodeParam1"));
        Element element = (Element) Mockingbird.getProxyObject(Element.class);
        Mockingbird.enterRecordingMode();
        NodeList nodeList = (NodeList) Mockingbird.getProxyObject(NodeList.class);
        Mockingbird.setReturnValue(false, element, "getElementsByTagName", "(java.lang.String)org.w3c.dom.NodeList", new Object[] {null}, nodeList, 1);
        Mockingbird.setException(false, nodeList, "getLength", "()int", new Object[] {}, (Throwable) Mockingbird.getProxyObject(NullPointerException.class), 1);
        Mockingbird.enterTestMode(MoveNode.class);
        try {
            callPrivateMethod("com.huawei.unibi.molap.restructure.schema.updatemember.MoveNode", "getMoveLevels", new Class[] {Element.class, String.class}, moveNode, new Object[] {element, ""});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertTrue("Test call resulted in expected outcome", true);
        }
    }
}

