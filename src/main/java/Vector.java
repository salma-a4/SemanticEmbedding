import java.util.Arrays;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 1.1
        doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        //TODO Task 1.2
        double elementAtIndex;
        try{
            elementAtIndex = doubElements[_index];
        }
        catch(Exception e){
            elementAtIndex = -1;
        }
        return elementAtIndex;
    }

    public void setElementatIndex(double _value, int _index) {
        //TODO Task 1.3
        try{
        doubElements[_index] = _value;
        }
        catch (Exception e){
            doubElements[doubElements.length - 1] = _value;
        }
    }

    public double[] getAllElements() {
        //TODO Task 1.4
        return doubElements;
    }

    public int getVectorSize() {
        //TODO Task 1.5
        return doubElements.length;
    }

    public Vector reSize(int _size) {
        //TODO Task 1.6
        Vector vector = null;
        if (_size == getVectorSize() || _size <= 0){
            vector = new Vector(doubElements);
            return vector;
        }
        if (_size < getVectorSize())
            vector = new Vector(Arrays.copyOfRange(doubElements, 0, _size));
        else{
            int oldSize = doubElements.length;
            double[] newList = new double[_size];
            for(int x = 0; x < _size; x++){
                try{
                    newList[x] = doubElements[x];
                }
                catch (Exception e){
                    newList[x] = -1;
                }
            }
            vector = new Vector(newList);
        }
        return vector;
    }

    public Vector add(Vector _v) {
        //TODO Task 1.7
        Vector vector = new Vector(doubElements);
        if (_v.getVectorSize() > vector.getVectorSize())
            vector = vector.reSize(_v.getVectorSize());
        else
            _v = _v.reSize(vector.getVectorSize());
        for(int x = 0; x < vector.getVectorSize(); x++){
            vector.doubElements[x] = vector.doubElements[x] + _v.doubElements[x];
        }
        doubElements = vector.doubElements;
        return vector;
    }

    public Vector subtraction(Vector _v) {
        //TODO Task 1.8
        Vector vector = new Vector(doubElements);
        if (_v.getVectorSize() > vector.getVectorSize())
            vector = vector.reSize(_v.getVectorSize());
        else
            _v = _v.reSize(vector.getVectorSize());
        for(int x = 0; x < _v.getVectorSize(); x++){
            vector.doubElements[x] = vector.doubElements[x] - _v.doubElements[x];
        }
        return vector;
    }

    public double dotProduct(Vector _v) {
        //TODO Task 1.9
        Vector vector = new Vector(doubElements);
        if (_v.getVectorSize() > vector.getVectorSize())
            vector = vector.reSize(_v.getVectorSize());
        else
            _v = _v.reSize(vector.getVectorSize());
        double dotProduct = 0;
        for(int x = 0; x < _v.getVectorSize(); x++){
            dotProduct = dotProduct + (vector.doubElements[x] * _v.doubElements[x]);
        }
        return dotProduct;
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 1.10
        Vector vector = new Vector(doubElements);
        if (_v.getVectorSize() > vector.getVectorSize())
            vector = vector.reSize(_v.getVectorSize());
        else
            _v = _v.reSize(vector.getVectorSize());
        double dotProduct = vector.dotProduct(_v);
        double cosineSimilarity;
        double rootSquare1 = 0;
        double rootSquare2 = 0;
        for (int x = 0; x < vector.getVectorSize(); x++){
            rootSquare1 = rootSquare1 + Math.pow(vector.doubElements[x], 2);
        }
        rootSquare1 = Math.pow(rootSquare1, 0.5);
        for (int x = 0; x < _v.getVectorSize(); x++){
            rootSquare2 = rootSquare2 + Math.pow(_v.doubElements[x], 2);
        }
        rootSquare2 = Math.pow(rootSquare2, 0.5);
        cosineSimilarity = dotProduct/ (rootSquare1 * rootSquare2);
        return cosineSimilarity;
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;
        //TODO Task 1.11
        if (getVectorSize() != v.getVectorSize())
            boolEquals = false;
        else {
            for (int x = 0; x < getVectorSize(); x++){
                if (doubElements[x] != v.doubElements[x]){
                    boolEquals = false;
                    return boolEquals;
                }
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
