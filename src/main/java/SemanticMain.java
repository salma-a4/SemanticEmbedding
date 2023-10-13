import org.apache.commons.lang3.time.StopWatch;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SemanticMain {
    public List<String> listVocabulary = new ArrayList<>();  //List that contains all the vocabularies loaded from the csv file.
    public List<double[]> listVectors = new ArrayList<>(); //Associated vectors from the csv file.
    public List<Glove> listGlove = new ArrayList<>();
    public final List<String> STOPWORDS;

    public SemanticMain() throws IOException {
        STOPWORDS = Toolkit.loadStopWords();
        Toolkit.loadGLOVE();
    }


    public static void main(String[] args) throws IOException {
        StopWatch mySW = new StopWatch();
        mySW.start();
        SemanticMain mySM = new SemanticMain();
        mySM.listVocabulary = Toolkit.getListVocabulary();
        mySM.listVectors = Toolkit.getlistVectors();
        mySM.listGlove = mySM.CreateGloveList();

        List<CosSimilarityPair> listWN = mySM.WordsNearest("computer");
        Toolkit.PrintSemantic(listWN, 5);

        listWN = mySM.WordsNearest("phd");
        Toolkit.PrintSemantic(listWN, 5);

        List<CosSimilarityPair> listLA = mySM.LogicalAnalogies("china", "uk", "london", 5);
        Toolkit.PrintSemantic("china", "uk", "london", listLA);

        listLA = mySM.LogicalAnalogies("woman", "man", "king", 5);
        Toolkit.PrintSemantic("woman", "man", "king", listLA);

        listLA = mySM.LogicalAnalogies("banana", "apple", "red", 3);
        Toolkit.PrintSemantic("banana", "apple", "red", listLA);
        mySW.stop();

        if (mySW.getTime() > 2000)
            System.out.println("It takes too long to execute your code!\nIt should take less than 2 second to run.");
        else
            System.out.println("Well done!\nElapsed time in milliseconds: " + mySW.getTime());
    }

    public List<Glove> CreateGloveList() {
        List<Glove> listResult = new ArrayList<>();
        //TODO Task 6.1
        for (int x = 0; x < listVocabulary.size(); x++){
            if(!STOPWORDS.contains(listVocabulary.get(x))){
                Vector vector = new Vector(listVectors.get(x));
                Glove gloVe = new Glove(listVocabulary.get(x), vector);
                listResult.add(gloVe);
            }
        }
        listGlove = listResult;
        return listResult;
    }

    public List<CosSimilarityPair> WordsNearest(String _word) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.2
        boolean wordExists = false;
        int errIndex = 0;
        Vector vector1 = null;
        Vector vector2 = null;
        for (int x = 0; x < listGlove.size(); x++){
            if (_word.equals(listGlove.get(x).getVocabulary())){
                wordExists = true;
                vector1 = new Vector(listGlove.get(x).getVector().getAllElements());
            }
            if ("error".equals(listGlove.get(x).getVocabulary()))
                errIndex = x;
        }
        if (!wordExists){
            _word = "error";
            vector1 = new Vector(listGlove.get(errIndex).getVector().getAllElements());
        }
        for (int x = 0; x < listGlove.size(); x++){
            if (!_word.equals(listGlove.get(x).getVocabulary())){
                vector2 = new Vector(listGlove.get(x).getVector().getAllElements());
                CosSimilarityPair csp = new CosSimilarityPair(_word, listGlove.get(x).getVocabulary(), vector1.cosineSimilarity(vector2));
                listCosineSimilarity.add(csp);
            }
        }
        listCosineSimilarity = HeapSort.doHeapSort(listCosineSimilarity);
        return listCosineSimilarity;
    }

    public List<CosSimilarityPair> WordsNearest(Vector _vector) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.3
        Vector vector2 = null;
        for (int x = 0; x < listGlove.size(); x++){
            if(!_vector.equals(listGlove.get(x).getVector())){
                vector2 = new Vector(listGlove.get(x).getVector().getAllElements());
                CosSimilarityPair csp = new CosSimilarityPair(_vector, listGlove.get(x).getVocabulary(), _vector.cosineSimilarity(vector2));
                listCosineSimilarity.add(csp);
            }
        }
        listCosineSimilarity = HeapSort.doHeapSort(listCosineSimilarity);
        return listCosineSimilarity;
    }

    /**
     * Method to calculate the logical analogies by using references.
     * <p>
     * Example: uk is to london as china is to XXXX.
     *       _firISRef  _firTORef _secISRef
     * In the above example, "uk" is the first IS reference; "london" is the first TO reference
     * and "china" is the second IS reference. Moreover, "XXXX" is the vocabulary(ies) we'd like
     * to get from this method.
     * <p>
     * If _top <= 0, then returns an empty listResult.
     * If the vocabulary list does not include _secISRef or _firISRef or _firTORef, then returns an empty listResult.
     *
     * @param _secISRef The second IS reference
     * @param _firISRef The first IS reference
     * @param _firTORef The first TO reference
     * @param _top      How many vocabularies to include.
     */
    public List<CosSimilarityPair> LogicalAnalogies(String _secISRef, String _firISRef, String _firTORef, int _top) {
        List<CosSimilarityPair> listResult = new ArrayList<>();
        //TODO Task 6.4
        Vector firIsRefVec = null;
        Vector firToRefVec = null;
        Vector secIsRefVec = null;
        for (int x = 0; x < listGlove.size(); x++){
            if (_firISRef.equals(listGlove.get(x).getVocabulary()))
                firIsRefVec = new Vector(listGlove.get(x).getVector().getAllElements());
            else if (_firTORef.equals(listGlove.get(x).getVocabulary()))
                firToRefVec = new Vector(listGlove.get(x).getVector().getAllElements());
            else if (_secISRef.equals(listGlove.get(x).getVocabulary()))
                secIsRefVec = new Vector(listGlove.get(x).getVector().getAllElements());
        }
        if (firIsRefVec == null || firToRefVec == null || secIsRefVec == null || _top <= 0)
            return listResult;
        Vector secToRefVec = (secIsRefVec.subtraction(firIsRefVec)).add(firToRefVec);
        List<CosSimilarityPair> tempList = WordsNearest(secToRefVec);
        for(int x = 0; x < _top; x++){
            if ((!_firISRef.equals(tempList.get(x).getWord2())) && (!_firTORef.equals(tempList.get(x).getWord2())) && (!_secISRef.equals(tempList.get(x).getWord2()))){
                listResult.add(tempList.get(x));
            }
            else{
                tempList.remove(x);
                x--;
            }
        }
        return listResult;
    }
}