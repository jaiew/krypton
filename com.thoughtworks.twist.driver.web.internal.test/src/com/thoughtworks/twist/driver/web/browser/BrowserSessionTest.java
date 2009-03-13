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

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;

public class BrowserSessionTest extends AbstractBaseBrowserSession {
    @Test
    public void shouldExecuteJavascriptAndReturnResult() throws Exception {
        assertEquals("Hello", session.evaluate("'Hello'"));
    }

    @Test
    public void shouldExecuteJavascriptAndReturnEvaluatedResult() throws Exception {
        assertEquals("2", session.evaluate("1 + 1"));
    }

    @Test
    public void shouldExecuteJavascriptAndThrowException() throws Exception {
        String reference = "Hello";
        try {
            session.evaluate(reference);
            fail();
        } catch (JavascriptException e) {
            assertEquals(referenceErrorMessage(reference), e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenInjectingNonExistingJavascript() throws Exception {
        try {
            inject("test-does-not-exist.js");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void shouldInjectJavascriptFromResource() throws Exception {
        render("<html/>");
        inject("test-hello-world.js");

        assertEquals("world", session.evaluate("hello()"));
    }

    @Test
    public void shouldReadSimpleDomAsXml() throws Exception {
        render("<html/>");

        assertEquals("html", session.dom().getDocumentElement().getTagName());
    }

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
    public void shouldUseIdPropertyAfterChangeForElement() throws Exception {
        render("<html><head/><body><div id=\"1\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".id = '2'");
        assertNull(session.dom().getElementById("1"));
        assertNotNull(session.dom().getElementById("2"));
    }

    @Test
    public void shouldUseNamePropertyAfterChangeForInputElement() throws Exception {
        render("<html><head/><body><input name=\"my-name\" id=\"1\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".name = 'my-new-name'");
        assertEquals("my-new-name", session.dom().getElementById("1").getAttribute("name"));
    }

    @Test
    public void shouldUseCheckedPropertyFalseForNonExistingCheckedAttributeForCheckbox() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"checkbox\"/></body></html>");
        Element checkbox = session.dom().getElementById("1");
        assertEquals("checkbox", checkbox.getAttribute("type"));
        assertEquals(Boolean.FALSE.toString(), checkbox.getAttribute("checked"));
    }

    @Test
    public void shouldUseCheckedPropertyTrueForExisitingAttributeForCheckbox() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"checkbox\" checked/></body></html>");
        Element checkbox = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), checkbox.getAttribute("checked"));
    }

    @Test
    public void shouldUseCheckedPropertyAfterChangeForCheckbox() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"checkbox\" checked=\"true\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".checked = false");
        Element checkbox = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), checkbox.getAttribute("checked"));
    }

    @Test
    public void shouldUseCheckedPropertyFalseForNonExistingCheckedAttributeForRadioButton() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"radio\"/></body></html>");
        Element radio = session.dom().getElementById("1");
        assertEquals("radio", radio.getAttribute("type"));
        assertEquals(Boolean.FALSE.toString(), radio.getAttribute("checked"));
    }

    @Test
    public void shouldUseCheckedPropertyTrueForExisitingAttributeForRadioButton() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"radio\" checked/></body></html>");
        Element radio = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), radio.getAttribute("checked"));
    }

    @Test
    public void shouldUseCheckedPropertyAfterChangeForRadioButton() throws Exception {
        render("<html><head/><body><form><input name=\"theRadio\" id=\"1\" type=\"radio\" checked=\"true\"/><input id=\"2\" name=\"theRadio\" type=\"radio\"/></form></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("2")) + ".checked = true");
        Element radio = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), radio.getAttribute("checked"));
        radio = session.dom().getElementById("2");
        assertEquals(Boolean.TRUE.toString(), radio.getAttribute("checked"));
    }

    @Test
    public void shouldUseValueEmptyStringForNonExistingValueAttributeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\"/></body></html>");
        Element textfield = session.dom().getElementById("1");
        assertEquals("text", textfield.getAttribute("type"));
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
    public void shouldUseValuePropertyAfterChangeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" value=\"hello\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".value = 'world'");
        Element textfield = session.dom().getElementById("1");
        assertEquals("world", textfield.getAttribute("value"));
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
    public void shouldUseValuePropertyAfterChangeForTextArea() throws Exception {
        render("<html><head/><body><textarea id=\"1\">hello</textarea></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".value = 'world'");
        Element textarea = session.dom().getElementById("1");
        assertEquals("world", textarea.getAttribute("value"));
        assertEquals("world", textarea.getTextContent());
    }

    @Test
    public void shouldUseSelectedPropertyTrueForNonExistingSelectedAttributeForSingleOption() throws Exception {
        render("<html><head/><body><select><option id=\"1\"/></select></body></html>");
        Element option = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), option.getAttribute("selected"));
    }

    @Test
    public void shouldUseMultiplePropertyFalseForSingleSelect() throws Exception {
        render("<html><head/><body><select id=\"1\"><option/></select></body></html>");
        Element select = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), select.getAttribute("multiple"));
    }

    @Test
    public void shouldUseMultiplePropertyTrueAfterChangedForSingleSelect() throws Exception {
        render("<html><head/><body><select id=\"1\"><option/></select></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".multiple = true");
        Element select = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), select.getAttribute("multiple"));
    }

    @Test
    public void shouldUseMultiplePropertyTrueForMultipleSelect() throws Exception {
        render("<html><head/><body><select id=\"1\" multiple><option/></select></body></html>");
        Element select = session.dom().getElementById("1");
        assertEquals(Boolean.TRUE.toString(), select.getAttribute("multiple"));
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
    public void shouldUseSelectedPropertyAfterChangeForOption() throws Exception {
        render("<html><head/><body><select><option id=\"0\"/><option id=\"1\" selected=\"true\"/></select></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("0")) + ".selected = true");
        Element option = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), option.getAttribute("selected"));
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
    public void shouldUseDisabledPropertyAfterChangeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" disabled=\"true\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".disabled = false");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), textfield.getAttribute("disabled"));
    }

    @Test
    public void shouldUseFalseForNonExistingReadonlyAttributeForTextField() throws Exception {
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
    public void shouldUseReadonlyPropertyAfterChangeForTextField() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" readonly=\"true\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".readOnly = false");
        Element textfield = session.dom().getElementById("1");
        assertEquals(Boolean.FALSE.toString(), textfield.getAttribute("readonly"));
    }

    @Test
    public void shouldUseForPropertyAfterHtmlForChangeForLabel() throws Exception {
        render("<html><head/><body><label id=\"1\" for=\"2\">My Label</label><input id=\"2\" type=\"text\" readonly=\"true\"/><input id=\"3\" type=\"text\" readonly=\"true\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".htmlFor = '3'");
        Element label = session.dom().getElementById("1");
        assertEquals("3", label.getAttribute("for"));
    }

    @Test
    public void shouldUseClassPropertyAfterClassNameChangeForElement() throws Exception {
        render("<html><head/><body><input id=\"1\" type=\"text\" class=\"text-box\"/></body></html>");
        executeAndWaitForIdle(session.domExpression(session.dom().getElementById("1")) + ".className = 'my-text-box'");
        Element label = session.dom().getElementById("1");
        assertEquals("my-text-box", label.getAttribute("class"));
    }

    @Test
    public void shouldParseAndEscapeXmlText() throws Exception {
        render("<html><head/><body id=\"1\">& &amp; &#38; < &lt; &#60; > &gt; &#62; \" &quot; &#34; ' &apos; &#39;</body></html>");
        Element body = session.dom().getElementById("1");
        assertEquals("& & & < < < > > > \" \" \" ' ' '", session.getText(body));
    }

    @Test @Ignore("Not entirely sure about this one. Nekohtml won't unescape, so.")
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
        assertEquals("window.frames[0].document", session.getDocumentExpression());
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

    @Test @Ignore("Seems like nekohtml handles this.")
    public void shouldRemoveIllegalAttributes() throws Exception {
        render("<html><head/><body id=\"1\" -illegal=\"\"></body></html>");
        Element body = session.dom().getElementById("1");
        assertFalse(body.hasAttribute("-illegal"));
    }
}
