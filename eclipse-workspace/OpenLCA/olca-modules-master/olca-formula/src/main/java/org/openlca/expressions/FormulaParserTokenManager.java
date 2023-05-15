/* Generated By:JavaCC: Do not edit this line. FormulaParserTokenManager.java */
package org.openlca.expressions;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.openlca.expressions.functions.*;

/** Token Manager. */
public class FormulaParserTokenManager implements FormulaParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x301000L) != 0L)
         {
            jjmatchedKind = 28;
            return 32;
         }
         if ((active0 & 0x6000L) != 0L)
            return 11;
         return -1;
      case 1:
         if ((active0 & 0x301000L) != 0L)
         {
            jjmatchedKind = 28;
            jjmatchedPos = 1;
            return 32;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 13:
         jjmatchedKind = 2;
         return jjMoveStringLiteralDfa1_0(0x20L);
      case 40:
         return jjStopAtPos(0, 29);
      case 41:
         return jjStopAtPos(0, 30);
      case 42:
         return jjStopAtPos(0, 10);
      case 43:
         return jjStopAtPos(0, 8);
      case 45:
         return jjStopAtPos(0, 9);
      case 47:
         return jjStopAtPos(0, 11);
      case 59:
         return jjStopAtPos(0, 31);
      case 60:
         jjmatchedKind = 13;
         return jjMoveStringLiteralDfa1_0(0x4000L);
      case 62:
         jjmatchedKind = 17;
         return jjMoveStringLiteralDfa1_0(0x40000L);
      case 94:
         return jjStopAtPos(0, 19);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x100000L);
      case 120:
         return jjMoveStringLiteralDfa1_0(0x200000L);
      default :
         return jjMoveNfa_0(1, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 10:
         if ((active0 & 0x20L) != 0L)
            return jjStopAtPos(1, 5);
         break;
      case 61:
         if ((active0 & 0x4000L) != 0L)
            return jjStopAtPos(1, 14);
         else if ((active0 & 0x40000L) != 0L)
            return jjStopAtPos(1, 18);
         break;
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x300000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 100:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(2, 20, 32);
         break;
      case 114:
         if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(2, 21, 32);
         break;
      case 118:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(2, 12, 32);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 32;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 25)
                        kind = 25;
                     jjCheckNAddStates(0, 4);
                  }
                  else if (curChar == 36)
                  {
                     if (kind > 28)
                        kind = 28;
                     jjCheckNAddTwoStates(18, 19);
                  }
                  else if (curChar == 46)
                     jjCheckNAdd(14);
                  else if (curChar == 60)
                     jjstateSet[jjnewStateCnt++] = 11;
                  else if (curChar == 33)
                     jjstateSet[jjnewStateCnt++] = 9;
                  else if (curChar == 61)
                     jjstateSet[jjnewStateCnt++] = 7;
                  else if (curChar == 38)
                  {
                     if (kind > 6)
                        kind = 6;
                  }
                  if (curChar == 61)
                  {
                     if (kind > 15)
                        kind = 15;
                  }
                  else if (curChar == 38)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 32:
                  if ((0x3ff001000000000L & l) != 0L)
                  {
                     if (kind > 28)
                        kind = 28;
                     jjCheckNAdd(19);
                  }
                  if (curChar == 36)
                  {
                     if (kind > 28)
                        kind = 28;
                     jjCheckNAddTwoStates(18, 19);
                  }
                  break;
               case 0:
                  if (curChar == 38 && kind > 6)
                     kind = 6;
                  break;
               case 2:
                  if (curChar == 38 && kind > 6)
                     kind = 6;
                  break;
               case 6:
               case 7:
                  if (curChar == 61 && kind > 15)
                     kind = 15;
                  break;
               case 8:
                  if (curChar == 61)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if (curChar == 61 && kind > 16)
                     kind = 16;
                  break;
               case 10:
                  if (curChar == 33)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               case 11:
                  if (curChar == 62)
                     kind = 16;
                  break;
               case 12:
                  if (curChar == 60)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 13:
                  if (curChar == 46)
                     jjCheckNAdd(14);
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAddTwoStates(14, 15);
                  break;
               case 16:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(17);
                  break;
               case 17:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAdd(17);
                  break;
               case 18:
                  if (curChar != 36)
                     break;
                  if (kind > 28)
                     kind = 28;
                  jjCheckNAddTwoStates(18, 19);
                  break;
               case 19:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 28)
                     kind = 28;
                  jjCheckNAdd(19);
                  break;
               case 20:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAddStates(0, 4);
                  break;
               case 21:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAddStates(5, 7);
                  break;
               case 22:
                  if (curChar == 46)
                     jjCheckNAdd(23);
                  break;
               case 23:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAddTwoStates(23, 24);
                  break;
               case 25:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(26);
                  break;
               case 26:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAdd(26);
                  break;
               case 27:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(27, 28);
                  break;
               case 28:
                  if (curChar != 46)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjstateSet[jjnewStateCnt++] = 29;
                  break;
               case 30:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(31);
                  break;
               case 31:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAdd(31);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 28)
                        kind = 28;
                     jjCheckNAddTwoStates(18, 19);
                  }
                  else if (curChar == 124)
                  {
                     if (kind > 7)
                        kind = 7;
                  }
                  if (curChar == 124)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 32:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 28)
                        kind = 28;
                     jjCheckNAdd(19);
                  }
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 28)
                        kind = 28;
                     jjCheckNAddTwoStates(18, 19);
                  }
                  break;
               case 3:
                  if (curChar == 124 && kind > 7)
                     kind = 7;
                  break;
               case 4:
                  if (curChar == 124)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 5:
                  if (curChar == 124 && kind > 7)
                     kind = 7;
                  break;
               case 15:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(8, 9);
                  break;
               case 18:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 28)
                     kind = 28;
                  jjCheckNAddTwoStates(18, 19);
                  break;
               case 19:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 28)
                     kind = 28;
                  jjCheckNAdd(19);
                  break;
               case 24:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(10, 11);
                  break;
               case 29:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(12, 13);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 32 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   21, 22, 24, 27, 28, 21, 22, 24, 16, 17, 25, 26, 30, 31, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, "\53", "\55", "\52", "\57", 
"\144\151\166", "\74", "\74\75", null, null, "\76", "\76\75", "\136", "\155\157\144", 
"\170\157\162", null, null, null, null, null, null, null, "\50", "\51", "\73", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0xf23fffc1L, 
};
static final long[] jjtoSkip = {
   0x3eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[32];
private final int[] jjstateSet = new int[64];
protected char curChar;
/** Constructor. */
public FormulaParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public FormulaParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 32; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100000600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}