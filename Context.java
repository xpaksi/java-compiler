
/**
 * @class: Context
 * This class constructs Context object that has attributes :
 * 1. lexicalLevel    : current lexical level
 * 2. orderNumber     : current order number
 * 3. symbolHash      : hash table of symbols
 * 4. symbolStack     : stack to keep symbol's name
 * 4. typeStack       : stack to keep symbol's type
 * 4. printSymbols    : choice of printing symbols
 * 4. errorCount      : error counter of context checking
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Context {

	public Context() {
		lexicalLevel = -1;
		orderNumber = 0;
		symbolHash = new Hash(HASH_SIZE);
		symbolStack = new Stack<String>();
		typeStack = new Stack<Integer>();
		paramStack = new Stack<List<Bucket>>();
		argCountStack = new Stack<Integer>();
		argTypesStack = new Stack<List<Integer>>();
		calleeStack = new Stack<Bucket>();
		printSymbols = false;
		errorCount = 0;
	}

	/**
	 * This method chooses which action to be taken
	 *
	 * @input : ruleNo(type:int)
	 * @output: -(type:void)
	 */
	public void C(int ruleNo) {
		// System.out.println("C" + ruleNo);
		String calleeName;
		Bucket calleeBucket;
		List<Bucket> parameterList;

		switch (ruleNo) {
			case 0:
				lexicalLevel++;
				orderNumber = 0;
				break;
			case 1:
				if (printSymbols)
					symbolHash.print(lexicalLevel);
				break;
			case 2:
				symbolHash.delete(lexicalLevel);
				lexicalLevel--;
				break;
			case 3:
			if (symbolHash.isExist(currentStr, lexicalLevel)) {
				System.err.println(
						"Variable declared at line " + currentLine + ": " + currentStr);
					errorCount++;
					System.err.println(
							"\nProcess terminated.\nAt least " +
									(errorCount + parser.yylex.num_error) +
									" error(s) detected.");
					System.exit(1);
				} else {
					Bucket bucket = new Bucket(currentStr);
					bucket.setLexicLev(lexicalLevel);
					symbolHash.insert(bucket);
				}
				symbolStack.push(currentStr);
				break;
			case 4:
				symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
				break;
			case 5:
				symbolHash.find(currentStr).setIdType(typeStack.peek());
				break;
			case 6:
			if (!symbolHash.isExist(currentStr)) {
				System.err.println(
						"Variable undeclared at line " + currentLine + ": " + currentStr);
					errorCount++;
					System.err.println(
							"\nProcess terminated.\nAt least " +
									(errorCount + parser.yylex.num_error) +
									" error(s) detected.");
					System.exit(1);
				} else {
					symbolStack.push(currentStr);
				}
				break;
			case 7:
				symbolStack.pop();
				if (!calleeStack.isEmpty()) {
					calleeStack.pop();
				}
				break;
			case 8:
				typeStack.push(symbolHash.find(currentStr).getIdType());
				break;
			case 9:
				typeStack.push(Bucket.INTEGER);
				break;
			case 10:
				typeStack.push(Bucket.BOOLEAN);
				break;
			case 11:
				typeStack.pop();
				break;
			case 12:
			switch (typeStack.peek()) {
				case Bucket.BOOLEAN:
					System.err.println(
							"Type of integer expected at line " +
									currentLine +
									": " +
									currentStr);
					errorCount++;
					break;
				case Bucket.UNDEFINED:
					System.err.println(
							"Undefined type at line " + currentLine + ": " + currentStr);
					errorCount++;
					break;
			}
				break;
		case 13:
			switch (typeStack.peek()) {
				case Bucket.INTEGER:
					System.err.println(
							"Type of boolean expected at line " +
									currentLine +
									": " +
									currentStr);
					errorCount++;
					break;
				case Bucket.UNDEFINED:
					System.err.println(
							"Undefined type at line " + currentLine + ": " + currentStr);
					errorCount++;
					break;
			}
				break;
			case 14:
			// C14: Check type match
			int poppedType = typeStack.pop();
			if (poppedType != typeStack.peek()) {
				System.err.println(
						"Unmatched type at line " + currentLine + ": " + currentStr);
				errorCount++;
			}
				typeStack.push(poppedType);
				break;
			case 15:
			// C15: Check integer type for comparison
			int comparedType = typeStack.pop();
			if ((comparedType != Bucket.INTEGER) && typeStack.peek() != Bucket.INTEGER) {
				System.err.println(
						"Unmatched type at line " + currentLine + ": " + currentStr);
				errorCount++;
			}
				typeStack.push(comparedType);
				break;
			case 16:
			// C16: Check assignment type match
			int variableType = symbolHash.find(symbolStack.peek()).getIdType();
			if (variableType != typeStack.peek()) {
				System.err.println(
						"Unmatched type at line " + currentLine + ": " + currentStr);
				errorCount++;
			}
				break;
			case 17:
			// C17: Check variable is integer type
			int varType = symbolHash.find(symbolStack.peek()).getIdType();
			if (varType != Bucket.INTEGER) {
				System.err.println(
						"Type of integer expected at line " +
								currentLine +
								": " +
								currentStr);
				errorCount++;
			}
				break;
			case 18:
				symbolHash.find(currentStr).setIdKind(Bucket.SCALAR);
				orderNumber++;
				break;
			case 19:
				symbolHash.find(currentStr).setIdKind(Bucket.ARRAY);
				orderNumber += 3;
				break;
		case 20:
			switch (symbolHash.find(symbolStack.peek()).getIdKind()) {
				case Bucket.UNDEFINED:
					System.err.println(
							"Variable not fully defined at line " +
									currentLine +
									": " +
									currentStr);
					errorCount++;
					break;
				case Bucket.ARRAY:
					System.err.println(
							"Scalar variable expected at line " +
									currentLine +
									": " +
									currentStr);
					errorCount++;
					break;
			}
				break;
			case 21:
				switch (symbolHash.find(symbolStack.peek()).getIdKind()) {
					case Bucket.UNDEFINED:
						System.out.println(
								"Variable not fully defined at line " +
										currentLine +
										": " +
										currentStr);
						errorCount++;
						break;
					case Bucket.SCALAR:
						System.out.println(
								"Array variable expected at line " +
										currentLine +
										": " +
										currentStr);
						errorCount++;
						break;
				}
				break;
			case 22:
				symbolHash.find(symbolStack.peek()).setLLON(lexicalLevel, orderNumber);
				break;
			case 23:
				symbolHash.find(symbolStack.peek()).setIdType(typeStack.peek());
				break;
			case 24:
			// C24: Insert procedure into symbol table (defer duplicate check to C35 for
			// overloading)
			{
				Bucket procedureBucket = new Bucket(currentStr);
				procedureBucket.setIdKind(Bucket.PROCEDURE);
				procedureBucket.setLexicLev(lexicalLevel);
				symbolHash.insert(procedureBucket);
				symbolStack.push(currentStr);
				paramStack.push(new ArrayList<Bucket>());
			}
				break;
			case 25:
				// C25: Insert parameter into symbol table
				if (!paramStack.isEmpty()) {
					String paramName = symbolStack.peek();
					Bucket param = symbolHash.find(paramName);
					if (param != null) {
						paramStack.peek().add(param);
					}
				}
				break;
			case 26:
			// C26: Insert function into symbol table (defer duplicate check to C35 for
			{
				Bucket functionBucket = new Bucket(currentStr);
				functionBucket.setIdKind(Bucket.FUNCTION);
				functionBucket.setLexicLev(lexicalLevel);
				symbolHash.insert(functionBucket);
				symbolStack.push(currentStr);
				paramStack.push(new ArrayList<Bucket>());
			}
				break;
			case 27:
				symbolHash.delete(lexicalLevel);
				lexicalLevel--;
				break;
			case 28:
			if (symbolHash.find(currentStr).getIdKind() != Bucket.PROCEDURE) {
				System.err.println(
						"Procedure expected at line " + currentLine + ": " + currentStr);
				errorCount++;
			}
				break;
			case 29:
				// C29: For no-args calls, find matching 0-parameter function/procedure
				if (!symbolStack.isEmpty()) {
					String callee = symbolStack.peek();
					Bucket calleeBkt = symbolHash.find(callee);
					if (calleeBkt != null) {
						int kind = calleeBkt.getIdKind();
						if (kind == Bucket.FUNCTION || kind == Bucket.PROCEDURE) {
							Bucket match = symbolHash.findBySignature(
									callee,
									new ArrayList<Integer>());
							if (match == null) {
								System.out.println(
										"No matching function/procedure with 0 arguments at line " +
												currentLine +
												": " +
												callee);
								errorCount++;
								calleeStack.push(calleeBkt);
							} else {
								calleeStack.push(match);
							}
						} else {
							calleeStack.push(calleeBkt);
						}
					}
				}
				break;
			case 30:
				// C30: Initialize for function/procedure call with arguments
				argCountStack.push(0);
				argTypesStack.push(new ArrayList<Integer>());
				if (!symbolStack.isEmpty()) {
					calleeStack.push(symbolHash.find(symbolStack.peek()));
				}
				break;
			case 31:
				if (!argTypesStack.isEmpty() && !typeStack.isEmpty()) {
					argTypesStack.peek().add(typeStack.peek());
				}
				break;
			case 32:
				// C32: After all arguments parsed, find matching overload
				if (symbolStack.isEmpty() ||
						argCountStack.isEmpty() ||
						argTypesStack.isEmpty())
					break;
				calleeName = symbolStack.peek();
				List<Integer> argTypes = argTypesStack.pop();
				int argumentCount = argCountStack.pop();

			calleeBucket = symbolHash.findBySignature(calleeName, argTypes);
			if (calleeBucket == null) {
				System.err.println(
						"No matching function/procedure for given arguments at line " +
								currentLine +
								": " +
								calleeName);
				errorCount++;
			} else {
					if (!calleeStack.isEmpty()) {
						calleeStack.pop();
						calleeStack.push(calleeBucket);
					}

					int expectedParamCount = calleeBucket.getParamCount();
					if (argumentCount != expectedParamCount) {
						System.out.println(
								"Argument count mismatch at line " +
										currentLine +
										": " +
										currentStr);
						errorCount++;
					}
				}
				break;
			case 33:
			if (symbolHash.find(currentStr).getIdKind() != Bucket.FUNCTION) {
				System.err.println(
						"Function expected at line " + currentLine + ": " + currentStr);
				errorCount++;
			}
				break;
			case 34:
				if (!argCountStack.isEmpty()) {
					int currentArgCount = argCountStack.pop();
					argCountStack.push(currentArgCount + 1);
				}
				break;
			case 35:
				if (symbolStack.isEmpty())
					break;
				calleeName = symbolStack.peek();
				calleeBucket = symbolHash.find(calleeName);
				if (calleeBucket != null && !paramStack.isEmpty()) {
					parameterList = paramStack.pop();
					calleeBucket.getParameters().clear();
					for (Bucket param : parameterList)
						calleeBucket.addParameter(param);
					calleeBucket.setParamCount(parameterList.size());
					int paramCount = parameterList.size();
					int kind = calleeBucket.getIdKind();
					int baseOffset = (kind == Bucket.FUNCTION) ? 3 : 2;

					for (int i = 0; i < paramCount; i++) {
						Bucket param = parameterList.get(i);
						int negativeOffset = -(paramCount - i + baseOffset);
						param.setLLON(param.getLexicLev(), negativeOffset);
					}

					int declLevel = calleeBucket.getLexicLev();
				if (symbolHash.isExist(
						calleeName,
						declLevel,
						parameterList,
						kind,
						calleeBucket)) {
					String kindStr = (kind == Bucket.FUNCTION)
							? "Function"
							: "Procedure";
					System.err.println(
							kindStr +
									" with same signature already declared at line " +
									currentLine +
									": " +
									calleeName);
					errorCount++;
						System.err.println(
								"\nProcess terminated.\nAt least " +
										(errorCount + parser.yylex.num_error) +
										" error(s) detected.");
						System.exit(1);
					}
				}
				if (!argCountStack.isEmpty())
					argCountStack.pop();
				if (calleeBucket != null) {
					calleeStack.push(calleeBucket);
				}
				break;
			case 36:
			int functionReturnType = symbolHash
					.find(symbolStack.peek())
					.getIdType();
			int expressionType = typeStack.peek();
			if (functionReturnType != expressionType) {
				System.err.println(
						"Unmatched function return type at line " +
								currentLine +
								": " +
								currentStr);
				errorCount++;
			}
				break;
			case 37:
			int identifierKind = symbolHash.find(currentStr).getIdKind();
			if (identifierKind == Bucket.FUNCTION) {
			} else {
				switch (symbolHash.find(symbolStack.peek()).getIdKind()) {
					case Bucket.UNDEFINED:
						System.err.println(
								"Variable not fully defined at line " +
										currentLine +
										": " +
										currentStr);
						errorCount++;
						break;
					case Bucket.ARRAY:
						System.err.println(
								"Scalar variable expected at line " +
										currentLine +
										": " +
										currentStr);
						errorCount++;
						break;
				}
			}
				break;
		}
	}

	/**
	 * This method sets the current token and line
	 *
	 * @input : str(type:int), line(type:int)
	 * @output: -(type:void)
	 */
	public void setCurrent(String str, int line) {
		currentStr = str;
		currentLine = line;
	}

	/**
	 * This method sets symbol printing option
	 *
	 * @input : bool(type:boolean)
	 * @output: -(type:void)
	 */
	public void setPrint(boolean bool) {
		printSymbols = bool;
	}

	private final int HASH_SIZE = 211;

	public static int lexicalLevel;
	public static int orderNumber;
	public static Hash symbolHash;
	private Stack<String> symbolStack;
	private Stack<Integer> typeStack;
	private Stack<List<Bucket>> paramStack;
	private Stack<Integer> argCountStack;
	private Stack<List<Integer>> argTypesStack;
	public static String currentStr;
	public static int currentLine;
	public static Stack<Bucket> calleeStack;
	private boolean printSymbols;
	public int errorCount;

}
