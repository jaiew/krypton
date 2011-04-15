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
package com.thoughtworks.krypton.driver.cocoa;

import org.rococoa.NSClass;
import org.rococoa.NSObject;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSPoint;

public interface NSEvent extends NSObject {

	public static final Class CLASS = Rococoa.createClass("NSEvent", Class.class);

	public abstract class Class implements NSClass {
		public abstract NSEvent keyEventWithType_location_modifierFlags_timestamp_windowNumber_context_characters_charactersIgnoringModifiers_isARepeat_keyCode(
				int type, NSPoint nsPoint, int mask, double d, int i, NSObject context, String string, String string2, boolean b,
				short keyCode);

		public abstract NSEvent mouseEventWithType_location_modifierFlags_timestamp_windowNumber_context_eventNumber_clickCount_pressure(int type,
				NSPoint nsPoint, int mouseenteredmask, double d, int windowNumber, NSObject context, int i, int clickCount, float pressure);

	}

	// public int AlphaShiftKeyMask = 1 << 16;
	public int ShiftKeyMask = 1 << 17;
	public int ControlKeyMask = 1 << 18;
	public int AlternateKeyMask = 1 << 19;
	public int CommandKeyMask = 1 << 20;
	// public int NumericPadKeyMask = 1 << 21;
	// public int HelpKeyMask = 1 << 22;
	// public int FunctionKeyMask = 1 << 23;

	// special keys

	public int UpArrowFunctionKey = 0xF700;
	public int DownArrowFunctionKey = 0xF701;
	public int LeftArrowFunctionKey = 0xF702;
	public int RightArrowFunctionKey = 0xF703;
	// public int F1FunctionKey = 0xF704;
	// public int F2FunctionKey = 0xF705;
	// public int F3FunctionKey = 0xF706;
	// public int F4FunctionKey = 0xF707;
	// public int F5FunctionKey = 0xF708;
	// public int F6FunctionKey = 0xF709;
	// public int F7FunctionKey = 0xF70A;
	// public int F8FunctionKey = 0xF70B;
	// public int F9FunctionKey = 0xF70C;
	// public int F10FunctionKey = 0xF70D;
	// public int F11FunctionKey = 0xF70E;
	// public int F12FunctionKey = 0xF70F;
	// public int F13FunctionKey = 0xF710;
	// public int F14FunctionKey = 0xF711;
	// public int F15FunctionKey = 0xF712;
	// public int F16FunctionKey = 0xF713;
	// public int F17FunctionKey = 0xF714;
	// public int F18FunctionKey = 0xF715;
	// public int F19FunctionKey = 0xF716;
	// public int F20FunctionKey = 0xF717;
	// public int F21FunctionKey = 0xF718;
	// public int F22FunctionKey = 0xF719;
	// public int F23FunctionKey = 0xF71A;
	// public int F24FunctionKey = 0xF71B;
	// public int F25FunctionKey = 0xF71C;
	// public int F26FunctionKey = 0xF71D;
	// public int F27FunctionKey = 0xF71E;
	// public int F28FunctionKey = 0xF71F;
	// public int F29FunctionKey = 0xF720;
	// public int F30FunctionKey = 0xF721;
	// public int F31FunctionKey = 0xF722;
	// public int F32FunctionKey = 0xF723;
	// public int F33FunctionKey = 0xF724;
	// public int F34FunctionKey = 0xF725;
	// public int F35FunctionKey = 0xF726;
	// public int InsertFunctionKey = 0xF727;
	// public int DeleteFunctionKey = 0xF728;
	public int HomeFunctionKey = 0xF729;
	// public int BeginFunctionKey = 0xF72A;
	public int EndFunctionKey = 0xF72B;
	public int PageUpFunctionKey = 0xF72C;
	public int PageDownFunctionKey = 0xF72D;
	// public int PrintScreenFunctionKey = 0xF72E;
	// public int ScrollLockFunctionKey = 0xF72F;
	// public int PauseFunctionKey = 0xF730;
	// public int SysReqFunctionKey = 0xF731;
	// public int BreakFunctionKey = 0xF732;
	// public int ResetFunctionKey = 0xF733;
	// public int StopFunctionKey = 0xF734;
	// public int MenuFunctionKey = 0xF735;
	// public int UserFunctionKey = 0xF736;
	// public int SystemFunctionKey = 0xF737;
	// public int PrintFunctionKey = 0xF738;
	// public int ClearLineFunctionKey = 0xF739;
	// public int ClearDisplayFunctionKey = 0xF73A;
	// public int InsertLineFunctionKey = 0xF73B;
	// public int DeleteLineFunctionKey = 0xF73C;
	// public int InsertCharFunctionKey = 0xF73D;
	// public int DeleteCharFunctionKey = 0xF73E;
	// public int PrevFunctionKey = 0xF73F;
	// public int NextFunctionKey = 0xF740;
	// public int SelectFunctionKey = 0xF741;
	// public int ExecuteFunctionKey = 0xF742;
	// public int UndoFunctionKey = 0xF743;
	// public int RedoFunctionKey = 0xF744;
	// public int FindFunctionKey = 0xF745;
	// public int HelpFunctionKey = 0xF746;
	// public int ModeSwitchFunctionKey = 0xF747;

	public int LeftMouseDown = 1;
	public int LeftMouseUp = 2;
	public int RightMouseDown = 3;
	public int RightMouseUp = 4;
	public int MouseMoved = 5;
	public int LeftMouseDragged = 6;
	// public int RightMouseDragged = 7;
	public int MouseEntered = 8;
	// public int MouseExited = 9;
	public int KeyDown = 10;
	public int KeyUp = 11;
	// public int FlagsChanged = 12;
	// public int AppKitDefined = 13;
	// public int SystemDefined = 14;
	// public int ApplicationDefined = 15;
	// public int Periodic = 16;
	// public int CursorUpdate = 17;
	// public int ScrollWheel = 22;
	// public int TabletPoint = 23;
	// public int TabletProximity = 24;
	// public int OtherMouseDown = 25;
	// public int OtherMouseUp = 26;
	// public int OtherMouseDragged = 27
	// public int EventTypeGesture = 29;
	// public int EventTypeMagnify = 30;
	// public int EventTypeSwipe = 31;
	// public int EventTypeRotate = 18;
	// public int EventTypeBeginGesture = 19;
	// public int EventTypeEndGesture = 20;

	// masks
	// public int LeftMouseDownMask = 1 << LeftMouseDown;
	// public int LeftMouseUpMask = 1 << LeftMouseUp;
	// public int RightMouseDownMask = 1 << RightMouseDown;
	// public int RightMouseUpMask = 1 << RightMouseUp;
	// public int MouseMovedMask = 1 << MouseMoved;
	// public int LeftMouseDraggedMask = 1 << LeftMouseDragged;
	// public int RightMouseDraggedMask = 1 << RightMouseDragged;
	public int MouseEnteredMask = 1 << MouseEntered;

	// public int MouseExitedMask = 1 << MouseExited;
	// public int KeyDownMask = 1 << KeyDown;
	// public int KeyUpMask = 1 << KeyUp;
	// public int FlagsChangedMask = 1 << FlagsChanged;
	// public int AppKitDefinedMask = 1 << AppKitDefined;
	// public int SystemDefinedMask = 1 << SystemDefined;
	// public int ApplicationDefinedMask = 1 << ApplicationDefined;
	// public int PeriodicMask = 1 << Periodic;
	// public int CursorUpdateMask = 1 << CursorUpdate;
	// public int ScrollWheelMask = 1 << ScrollWheel;
	// public int TabletPointMask = 1 << TabletPoint;
	// public int TabletProximityMask = 1 << TabletProximity;
	// public int OtherMouseDownMask = 1 << OtherMouseDown;
	// public int OtherMouseUpMask = 1 << OtherMouseUp;
	// public int OtherMouseDraggedMask = 1 << OtherMouseDragged;
	// public int EventMaskGesture = 1 << EventTypeGesture;
	// public int EventMaskMagnify = 1 << EventTypeMagnify;
	// public int EventMaskSwipe = 1U << EventTypeSwipe;
	// public int EventMaskRotate = 1 << EventTypeRotate;
	// public int EventMaskBeginGesture = 1 << EventTypeBeginGesture;
	// public int EventMaskEndGesture = 1 << EventTypeEndGesture;
	// public int AnyEventMask = 0xffffffffU;

}
