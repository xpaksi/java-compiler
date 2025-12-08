import java.util.List;

class Hash {

  public Hash(int capacity) {
    item = new Bucket[capacity];
    this.capacity = capacity;
  }

  public int getCapacity() {
    return capacity;
  }

  public void insert(Bucket newBucket) {
    if (newBucket.getIdName() != null) {
      int index = hashFunction(newBucket.getIdName());

      if (item[index] != null) newBucket.setNextBucket(item[index]);

      item[index] = newBucket;
    } else System.err.println("Symbol insertion failed.");
  }

  public Bucket delete(String idName) {
    int index = hashFunction(idName);

    if (item[index] != null) item[index] = item[index].getNextBucket();

    return item[index];
  }

  public void print(int lexicLevel) {
    Bucket temp;

    for (int i = 0; i < capacity; i++) {
      temp = item[i];

      if (temp != null) {
        while (temp.getLexicLev() == lexicLevel) {
          System.out.println();
          System.out.println("Identifier   : " + temp.getIdName());
          System.out.println("Lexical Level: " + temp.getLexicLev());
          System.out.println("Order Number : " + temp.getOrderNum());
          System.out.println("Type         : " + temp.getIdTypeStr());
          System.out.println("Kind         : " + temp.getIdKindStr());

          temp = temp.getNextBucket();
          if (temp == null) break;
        }
      }
    }
    System.out.println();
  }

  public void delete(int lexicLevel) {
    for (int i = 0; i < capacity; i++) {
      if (item[i] != null) {
        while (item[i].getLexicLev() == lexicLevel) {
          item[i] = item[i].getNextBucket();
          if (item[i] == null) break;
        }
      }
    }
  }

  public Bucket find(String idName) {
    int index = hashFunction(idName);
    Bucket temp = item[index];

    if (temp != null) {
      while (!(temp.getIdName()).equals(idName)) temp = temp.getNextBucket();
    }

    return temp;
  }

  public Bucket findBySignature(String idName, List<Integer> argTypes) {
    int index = hashFunction(idName);
    Bucket temp = item[index];

    while (temp != null) {
      if (temp.getIdName().equals(idName)) {
        int kind = temp.getIdKind();
        if (kind == Bucket.FUNCTION || kind == Bucket.PROCEDURE) {
          List<Bucket> params = temp.getParameters();
          if (params.size() == argTypes.size()) {
            boolean match = true;
            for (int i = 0; i < params.size(); i++) {
              if (params.get(i).getIdType() != argTypes.get(i)) {
                match = false;
                break;
              }
            }
            if (match) {
              return temp;
            }
          }
        }
      }
      temp = temp.getNextBucket();
    }
    return null;
  }

  public boolean isExist(String idName, int lexicLev) {
    boolean found = false;
    int index = hashFunction(idName);
    Bucket temp = item[index];

    if (temp != null) {
      while (!found) {
        if (
          (temp.getIdName()).equals(idName) && temp.getLexicLev() == lexicLev
        ) found = true;

        temp = temp.getNextBucket();
        if (temp == null) return found;
      }
    }

    return found;
  }

  public boolean isExist(String idName) {
    boolean found = false;
    int index = hashFunction(idName);
    Bucket temp = item[index];

    if (temp != null) {
      while (!found) {
        if ((temp.getIdName()).equals(idName)) found = true;

        temp = temp.getNextBucket();
        if (temp == null) return found;
      }
    }

    return found;
  }

  public boolean isExist(
    String idName,
    int lexicLev,
    List<Bucket> params,
    int idKind
  ) {
    return isExist(idName, lexicLev, params, idKind, null);
  }

  public boolean isExist(
    String idName,
    int lexicLev,
    List<Bucket> params,
    int idKind,
    Bucket exclude
  ) {
    int index = hashFunction(idName);
    Bucket temp = item[index];

    while (temp != null) {
      if (temp != exclude) {
        if (temp.getIdName().equals(idName) && temp.getLexicLev() == lexicLev) {
          boolean existingIsFunc = (temp.getIdKind() == Bucket.FUNCTION ||
            temp.getIdKind() == Bucket.PROCEDURE);
          boolean newIsFunc = (idKind == Bucket.FUNCTION ||
            idKind == Bucket.PROCEDURE);

          if (existingIsFunc && newIsFunc) {
            if (temp.hasSameSignature(params)) {
              return true;
            }
          } else {
            return true;
          }
        }
      }

      temp = temp.getNextBucket();
    }
    return false;
  }

  private int hashFunction(String str) {
    int sum = 0;

    for (int i = 0; i < str.length(); i++) sum += str.charAt(i);

    sum %= capacity;

    return sum;
  }

  private Bucket[] item;
  private int capacity;
}
