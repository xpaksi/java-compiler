import java.util.ArrayList;
import java.util.List;

class Bucket {

  public Bucket() {
  }

  public Bucket(String idName) {
    this.idName = idName;
    this.lexicLev = UNDEFINED;
    this.orderNum = UNDEFINED;
    this.idType = UNDEFINED;
    this.idKind = UNDEFINED;
    this.codeAddress = UNDEFINED;
    this.parameters = new ArrayList<Bucket>();
    this.nextBucket = null;
  }

  public void setIdName(String idName) {
    this.idName = idName;
  }

  public void setLexicLev(int lexicLev) {
    this.lexicLev = lexicLev;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public void setLLON(int lexicLev, int orderNum) {
    this.lexicLev = lexicLev;
    this.orderNum = orderNum;
  }

  public void setIdType(int idType) {
    this.idType = idType;
  }

  public void setIdKind(int idKind) {
    this.idKind = idKind;
  }

  public void setNextBucket() {
    nextBucket = null;
  }

  public void setNextBucket(Bucket next) {
    nextBucket = next;
  }

  public void setCodeAddress(int addr) {
    this.codeAddress = addr;
  }

  public String getIdName() {
    return idName;
  }

  public int getLexicLev() {
    return lexicLev;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public int getIdType() {
    return idType;
  }

  public int getIdKind() {
    return idKind;
  }

  public String getIdTypeStr() {
    String type = null;

    switch (idType) {
      case INTEGER:
        type = "integer";
        break;
      case BOOLEAN:
        type = "boolean";
        break;
      case UNDEFINED:
        type = "undefined";
        break;
    }

    return type;
  }

  public String getIdKindStr() {
    String kind = null;

    switch (idKind) {
      case SCALAR:
        kind = "scalar";
        break;
      case ARRAY:
        kind = "array";
        break;
      case PROCEDURE:
        kind = "procedure";
        break;
      case FUNCTION:
        kind = "function";
        break;
      case UNDEFINED:
        kind = "undefined";
        break;
    }

    return kind;
  }

  public Bucket getNextBucket() {
    return nextBucket;
  }

  public int getCodeAddress() {
    return codeAddress;
  }

  // Parameters related methods
  public void addParameter(Bucket param) {
    this.parameters.add(param);
  }

  public int getParamCount() {
    return paramCount;
  }

  public void setParamCount(int paramCount) {
    this.paramCount = paramCount;
  }

  public List<Bucket> getParameters() {
    return parameters;
  }

  public boolean hasSameSignature(List<Bucket> otherParams) {

    if (this.parameters.size() != otherParams.size()) {
      return false;
    }

    for (int i = 0; i < this.parameters.size(); i++) {
      int myType = this.parameters.get(i).getIdType();
      int otherType = otherParams.get(i).getIdType();

      if (myType != otherType) {
        return false;
      }
    }

    return true;
  }

  public static final int INTEGER = 0;
  public static final int BOOLEAN = 1;
  public static final int UNDEFINED = -1;

  public static final int SCALAR = 0;
  public static final int ARRAY = 1;
  public static final int PROCEDURE = 2;
  public static final int FUNCTION = 3;

  private String idName;
  private int orderNum;
  private int lexicLev;
  private int idType;
  private int idKind;
  private int codeAddress;
  private int paramCount;
  private List<Bucket> parameters = new ArrayList<Bucket>();
  private Bucket nextBucket;
}
