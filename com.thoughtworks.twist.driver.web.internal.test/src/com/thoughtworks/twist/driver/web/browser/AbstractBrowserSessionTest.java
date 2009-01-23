/****************************************************************************
 * Copyright 2008-2011 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Initial Contributors:
 *   Håkan Råberg
 *   Manish Chakravarty
 *   Pavan K S
 ***************************************************************************/
package com.thoughtworks.twist.driver.web.browser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;
import org.w3c.dom.Element;

public abstract class AbstractBrowserSessionTest extends AbstractBaseBrowserSession {
    @Test
    public void shouldExecuteJavascriptAndReturnResult() throws Exception {
        render("<html/>");
        assertEquals("Hello", session.execute("'Hello'"));
    }

    @Test
    public void shouldExecuteJavascriptAndReturnEvaluatedResult() throws Exception {
        render("<html/>");
        assertEquals("2", session.execute("1 + 1"));
    }

    @Test
    public void shouldExecuteJavascriptAndThrowException() throws Exception {
        render("<html/>");
        String reference = "Hello";
        try {
            session.execute(reference);
            fail();
        } catch (JavascriptException e) {
            assertEquals(referenceErrorMessage(reference), e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenInjectingNonExistingJavascript() throws Exception {
        render("<html/>");
        try {
            inject("test-does-not-exist.js");
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void shouldInjectJavascriptFromResource() throws Exception {
        render("<html/>");
        inject("test-hello-world.js");

        assertEquals("world", session.execute("hello()"));
    }

    @Test
    public void shouldReadSimpleDomAsXml() throws Exception {
        render("<html/>");

        assertEquals("html", session.dom().getDocumentElement().getTagName());
    }

//    @Test
//    public void shouldReadDomAsXml() throws Exception {
//        String html = readResource("test-dom.html");
//
//        render(html);
//
//        assertXmlEquals(parse(html), session.dom());
//    }

    @Test
    public void shouldReturnBoundingRectangleForElement() throws Exception {
        render(readResource("test-bounding-rectangle.html"));

        Element element = (Element) session.dom().getElementsByTagName("div").item(0);
        assertNotNull(element);

        Rectangle rectangle = session.boundingRectangle(element);
        assertEquals(new Rectangle(100, 200, 300, 400), rectangle);
    }

    @Test
    public void shouldReturnCenterForElement() throws Exception {
        render(readResource("test-bounding-rectangle.html"));

        Element element = (Element) session.dom().getElementsByTagName("div").item(0);
        assertNotNull(element);

        Point center = session.center(element);
        assertEquals(new Point(250, 400), center);
    }

    @Test
    public void shouldUseCheckedPropertyFalseForNonExistingCheckedAttributeForCheckbox() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"checkbox\"/></body></html>");
        Element checkbox = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), checkbox.getAttribute("checked"));
    }

    @Test
    public void shouldUseCheckedPropertyTrueForExisitingAttributeForCheckbox() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"checkbox\" checked/></body></html>");
        Element checkbox = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), checkbox.getAttribute("checked"));
    }

    @Test
    public void shouldUseCheckedPropertyAfterChangeForCheckboxButLoadedHtmlShouldBeSame() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"checkbox\" checked=\"true\"/></body></html>");
        session.execute(session.domExpression(session.dom().getElementById("1")) + ".checked = false");
        Element checkbox = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), checkbox.getAttribute("checked"));
//        assertFalse(session.domAsString().contains("false"));
    }

    @Test
    public void shouldUseValueEmptyStringForNonExistingValueAttributeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\"/></body></html>");
        Element textfield = session.dom().getElementById("1");
        assertEquals("", textfield.getAttribute("value"));
        assertTrue(textfield.hasAttribute("value"));
    }

    @Test
    public void shouldUseValueForExistingValueAttributeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" value=\"hello\"/></body></html>");
        Element textfield = session.dom().getElementById("1");
        assertEquals("hello", textfield.getAttribute("value"));
    }

    @Test
    public void shouldUseValuePropertyAfterChangeForTextFieldButLoadedHtmlShouldBeSame() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" value=\"hello\"/></body></html>");
        session.execute(session.domExpression(session.dom().getElementById("1")) + ".value = 'world'");
        Element textfield = session.dom().getElementById("1");
        assertEquals("world", textfield.getAttribute("value"));
//        assertFalse(session.domAsString().contains("world"));
    }

    @Test
    public void shouldUseValueEmptyStringForNonExistingValueAttributeForTextArea() throws Exception {
        render("<html><head/><body><textarea id=\"1\"></textarea></body></html>");
        Element textarea = session.dom().getElementById("1");
        assertEquals("", textarea.getAttribute("value"));
        assertTrue(textarea.hasAttribute("value"));
    }

    @Test
    public void shouldUseValueForExistingValueAttributeForTextArea() throws Exception {
        render("<html><head/><body><textarea id=\"1\">hello</textarea></body></html>");
        Element textarea = session.dom().getElementById("1");
        assertEquals("hello", textarea.getAttribute("value"));
        assertEquals("hello", textarea.getTextContent());
    }

    @Test
    public void shouldUseValuePropertyAfterChangeForTextAreaButLoadedHtmlShouldBeSame() throws Exception {
        render("<html><head/><body><textarea id=\"1\">hello</textarea></body></html>");
        session.execute(session.domExpression(session.dom().getElementById("1")) + ".value = 'world'");
        Element textarea = session.dom().getElementById("1");
        assertEquals("world", textarea.getAttribute("value"));
        assertEquals("world", textarea.getTextContent());
//        assertFalse(session.domAsString().contains("world"));
    }

    @Test
    public void shouldUseSelectedPropertyTrueForNonExistingSelectedAttributeForSingleOption() throws Exception {
        render("<html><head/><body><select><option id=\"1\"/></select></body></html>");
        Element option = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), option.getAttribute("selected"));
    }

    @Test
    public void shouldUseSelectedPropertyFalseForNonExistingSelectedAttributeForOption() throws Exception {
        render("<html><head/><body><select><option/><option id=\"1\"/></select></body></html>");
        Element option = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), option.getAttribute("selected"));
    }

    @Test
    public void shouldUseSelectedPropertyTrueForExisitingAttributeForOption() throws Exception {
        render("<html><head/><body><select><option/><option id=\"1\" selected/></select></body></html>");
        Element option = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), option.getAttribute("selected"));
    }

    @Test
    public void shouldUseSelectedPropertyAfterChangeForOptionButLoadedHtmlShouldBeSame() throws Exception {
        render("<html><head/><body><select><option id=\"0\"/><option id=\"1\" selected=\"true\"/></select></body></html>");
        session.execute(session.domExpression(session.dom().getElementById("0")) + ".selected = true");
        Element option = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), option.getAttribute("selected"));
//        assertFalse(session.domAsString().contains("false"));
    }

    @Test
    public void shouldUseFalseForNonExistingDisabledAttributeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\"/></body></html>");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), textfield.getAttribute("disabled"));
    }

    @Test
    public void shouldUseExistingDisabledAttributeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" disabled=\"true\"/></body></html>");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), textfield.getAttribute("disabled"));
    }

    @Test
    public void shouldUseDisabledPropertyAfterChangeForTextFieldButLoadedHtmlShouldBeSame() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" disabled=\"true\"/></body></html>");
        session.execute(session.domExpression(session.dom().getElementById("1")) + ".disabled = false");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), textfield.getAttribute("disabled"));
//        assertFalse(session.domAsString().contains("false"));
    }

    @Test
    public void shouldUseNFalseyForNonExistingReadonlyAttributeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\"/></body></html>");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), textfield.getAttribute("readonly"));
    }

    @Test
    public void shouldUseExistingReadonlyAttributeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" readonly=\"true\"/></body></html>");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), textfield.getAttribute("readonly"));
    }

    @Test
    public void shouldUseReadonlyPropertyAfterChangeForTextFieldButLoadedHtmlShouldBeSame() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" readonly=\"true\"/></body></html>");
        session.execute(session.domExpression(session.dom().getElementById("1")) + ".readOnly = false");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), textfield.getAttribute("readonly"));
//        assertFalse(session.domAsString().contains("false"));
    }

    @Test
    public void shouldParseAndEscapeXmlText() throws Exception {
        render("<html><head/><body id=\"1\">& &amp; &#38; < &lt; &#60; > &gt; &#62; \" &quot; &#34; ' &apos; &#39;</body></html>");
        Element body = session.dom().getElementById("1");
        assertEquals("& & & < < < > > > \" \" \" ' ' '", session.getText(body));
    }

    @Test
    public void shouldParseAndEscapeXmlAttribute() throws Exception {
        render("<html><head/><body><a id=\"1\" href=\""
                + "http://localhost/my%20image.gif?send=yes&make-it-large='true'%26beautiful=false&amp;mine=%22true%22\">my link</a></body></html>");
        Element link = session.dom().getElementById("1");
        String encodedAttribute = link.getAttribute("href");
        String expectedFullyUnencodedAndXmlUnescapedUrl = "http://localhost/my image.gif?send=yes&make-it-large='true'&beautiful=false&mine=\"true\"";
        assertEquals(expectedFullyUnencodedAndXmlUnescapedUrl, encodedAttribute);
    }
    
    @Test
    public void shouldAccessElementInFrame() throws Exception {
        render(readResource("test-frames.html"));
        assertNull(session.dom().getElementById("1"));
        session.setWindowExpression("window.frames[0]");
        assertNotNull(session.dom().getElementById("1"));
        assertEquals("window.frames[0].document", session.getDocumentExpression());
    }

    @Test
    public void shouldAccessElementInIFrame() throws Exception {
        render(readResource("test-iframe.html"));
        assertNull(session.dom().getElementById("1"));
        session.setWindowExpression("window.frames[0]");
        assertNotNull(session.dom().getElementById("1"));
        assertEquals("window.frames[0].document", session.getDocumentExpression());
    }

    @Test
    public void shouldChangeBackToTopFrame() throws Exception {
        render(readResource("test-iframe.html"));
        session.setWindowExpression("window.frames[0]");
        assertNotNull(session.dom().getElementById("1"));
        session.setWindowExpression("window");
        assertNull(session.dom().getElementById("1"));
    }

    @Test
    public void shouldNormalizeExpressionForFrame() throws Exception {
        render(readResource("test-frames.html"));
        assertNull(session.dom().getElementById("1"));
        session.setWindowExpression("document.getElementsByTagName('frame')[0].contentWindow");
        assertNotNull(session.dom().getElementById("1"));
        assertEquals("window.frames[0].document", session.getDocumentExpression());
    }

    @Test
    public void shouldThrowExceptionWhenAccessingNonExistingFrame() throws Exception {
        render(readResource("test-iframe.html"));
        try {
            session.setWindowExpression("window.frames[1]");
            fail();
        } catch (JavascriptException e) {
        }
    }

    // These are invalid for now, may be resurrected.    
    
//    @Test
//    public void shouldTryToFixIllegalAttributes() throws Exception {
//        render("<html><head/><body id=\"1\" -illegal=\"\" another=very=\"strange\"></body></html>");
//        Element body = session.dom().getElementById("1");
//        assertFalse(body.hasAttribute("-illegal"));
//        assertEquals("very=\"strange\"", body.getAttribute("another"));
//    }
//    
//    @Test
//    public void shouldHandleEntitiesInAttributes() throws Exception {
//        render("<html><head/><body id=\"1\" onClick=\"return newprefs('promo_id=rec&amp;promo_action=hide')\" onmouseup=\"return newprefs_geo('promo_id=local&amp;promo_action=accept',this,2);\" /><html>");
//        Element body = session.dom().getElementById("1");
//        assertEquals("return newprefs('promo_id=rec&promo_action=hide')", body.getAttribute("onclick"));
//        assertEquals("return newprefs_geo('promo_id=local&promo_action=accept',this,2);", body.getAttribute("onmouseup"));
//    }
}
